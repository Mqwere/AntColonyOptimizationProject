package main.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static main.AntColonyOptimizationProject.*;

public class AntColony
{	
	private final Graph graph;
	
	private final double weightOfEdgeLength;
	private final double weightOfPheromoneValue;
	
	private final double pheromoneNominator;
	
	private final double pheromonePercentileEvaporationRate;
	
	private final int numberOfAnts;
	private ArrayList<Ant> ants = new ArrayList<>();
		
	private ArrayList<Edge> edgesAwaitingPheromoneUpdate = new ArrayList<>();
	
	private ArrayList<Vertex> currentBestTrail;
	private double bestTrailLength;
	
	
	private AntColony(AntColony.Builder builder)
	{
		this.graph = builder.graph;
		
		this.weightOfEdgeLength = builder.weightOfEdgeLength;
		this.weightOfPheromoneValue = builder.weightOfPheromoneValue;
		
		this.pheromoneNominator = builder.pheromoneNominator;
		
		this.pheromonePercentileEvaporationRate = builder.pheromoneEvaporationRatio;
		
		this.numberOfAnts = builder.numberOfAnts;
		
		for(int i = 0; i < numberOfAnts; i++)
		{
			ants.add(new Ant(this));
		}
	}
	
	private void requestPheromoneUpdateForEdge(Edge edge)
	{
		edgesAwaitingPheromoneUpdate.add(edge);
	}
	
	public void performANumberOfFullPasses(int numberOfPasses)
	{
		if(numberOfPasses <= 0) return;
		logln(">>> Starting TSP optimisation (%d passes, %d ants, %d vertices).", numberOfPasses, numberOfAnts, graph.getKeys().size());
		IntStream.range(0, numberOfPasses).forEach( i -> {
			logln(">> Beginning pass %d/%d.", i+1, numberOfPasses);
			performOneFullPass();
		});
		if(!SHOULD_LOG) System.out.printf("%s, length: %f", trailToString(currentBestTrail), bestTrailLength);
	}
	
	public ArrayList<Vertex> performOneFullPass()
	{
		int passLength = graph.getKeys().size();
		setAntsUp();
		
		IntStream.range(0, passLength).forEach( i -> {
			logln("> Beginning iteration %d of the current pass.", i+1);
			moveAnts();
			updatePheromones();
			updateBestSolution();
		});
		
		return currentBestTrail;
	}
	
	private void setAntsUp()
	{
		logln("Beginning ants setup sequence.");
		Set<Character> graphKeys = graph.getKeys();
		int numberOfKeys = graphKeys.size();
		Character[] keys = new Character[numberOfKeys];
		graphKeys.toArray(keys);
		
		Random rand = new Random();
		
		ants.forEach
		(
			(a) -> 
			{
				Character key;
				a.beginTrailAt
				(
					graph.get
					(
						key = keys[rand.nextInt(numberOfKeys)]
					)
				);
				logln("Ant-%d beginning at %s.", a.ID, key);
			}
		);
		logln("Ants setup sequence completed.");
	}
	
	private void moveAnts()
	{
		logln("Beginning ants movement sequence.");
		
		ants.forEach(Ant::moveToNextVertex);
		
		logln("Ants movement sequence completed.");
	}
	
	private void updatePheromones()
	{
		logln("Beginning pheromone evaporation.");
		
		graph.getEdges()
			.stream()
			.forEach
			(
				(edge) -> 
				{
					edge.pheromoneValue *= (1 - pheromonePercentileEvaporationRate);
				}
			);
		
		logln("Pheromone evaporation completed. Now applying pheromone updates.");
		
		edgesAwaitingPheromoneUpdate
			.stream()
			.forEach
			(
				(edge) -> 
				{
					edge.pheromoneValue += (pheromoneNominator / edge.length);
				}
			);

		edgesAwaitingPheromoneUpdate.clear();
		
		logln("Pheromone update completed.");
	}
	
	private void updateBestSolution()
	{
		logln("Now updating best solution.");
		currentBestTrail = null;
		for(Ant a: ants)
		{
			if(currentBestTrail == null || a.getCurrentTrailLength() < bestTrailLength) 
			{
				currentBestTrail = a.getClonedTrail();
				bestTrailLength = a.getCurrentTrailLength();
			}
		}
		currentBestTrail = graph.makeListStartFromFirstVertex(currentBestTrail);
		logln("Best solution update completed. Now the best solution is:\n%s, length: %f", trailToString(currentBestTrail), bestTrailLength);
	}
	
	public static String trailToString(ArrayList<Vertex> input)
	{
		if(input == null) return "N/A";
		
		StringBuilder output = new StringBuilder("<<");
		
		input.stream().forEach
		(
			v -> output.append(" ").append(v).append(" >")
		);
		
		output.append(">");
		
		return output.toString();
	}
	
	private static class Ant
	{
		private static int antCounter = 1;
		
		private ArrayList<Vertex> trail;
		
		private AntColony myColony;
		
		private Vertex firstVertex;
		private Vertex nextVertex;
		
		private final int ID;
		
		private Ant(AntColony colony)
		{
			this.ID = antCounter++;
			
			this.myColony = colony;
						
			this.trail = new ArrayList<>();
		}
		
		private double getCurrentTrailLength()
		{
			double length = 0d;
			
			for(int i = 1; i < trail.size(); i++)
			{
				Vertex 
					source = trail.get(i-1), 
					destination = trail.get(i);
				
				length += source.outgoingEdges.get(destination).length;
			}
			
			return length;
		}
		
		private void decideOnNextMove()
		{
			Vertex currentVertex = nextVertex;
			
			HashMap<Vertex, Double> decisionMap = createDecisionMap( currentVertex );
						
			Double decisionValue = getSumOfDecisionValues( decisionMap ) * new Random().nextDouble();
			
			Optional<Vertex> output = getPotentialOutputValue( decisionMap, decisionValue );

			nextVertex = output.orElse(firstVertex);
		}
				
		private void beginTrailAt(Vertex vertex)
		{	
			this.trail = new ArrayList<>();
			nextVertex = vertex;
			firstVertex = vertex;
			moveToNextVertex();
		}
		
		private ArrayList<Vertex> getClonedTrail()
		{
			ArrayList<Vertex> clone = new ArrayList<>();
			
			for(Vertex v: trail)
				clone.add(v);
			
			return clone;
		}

		private void moveToNextVertex()
		{
			addToTrail( nextVertex );
			decideOnNextMove();
		}

		private void addToTrail( Vertex vertex )
		{
			Optional<Vertex> previousVertex = trail.size() > 0 ? Optional.of( trail.get( trail.size( ) - 1 ) ) : Optional.empty();
			
			if(previousVertex.isPresent())
			{
				myColony.requestPheromoneUpdateForEdge(previousVertex.get().outgoingEdges.get(vertex));
			}
			
			trail.add( vertex );
			
			if(previousVertex.isPresent()) logln("Ant-%d %s -> %s", ID, previousVertex.get().toString(), vertex.toString());
		}

		private Optional<Vertex> getPotentialOutputValue( HashMap<Vertex, Double> decisionMap, Double decisionValue )
		{
			int remainingEntries = decisionMap.size();
						
			for(Entry<Vertex, Double> entry: decisionMap.entrySet())
			{
				decisionValue -= entry.getValue();
				if(decisionValue <= 0 || --remainingEntries <= 0) 
				{
					return Optional.of( entry.getKey() );
				}
			}
			
			return Optional.empty();
		}

		private Double getSumOfDecisionValues( HashMap<Vertex, Double> decisionMap )
		{
			Double sumOfEdgeValues = 0D;
			
			for(Entry<Vertex, Double> entry: decisionMap.entrySet())
			{
				sumOfEdgeValues += entry.getValue();
			}
			return sumOfEdgeValues;
		}

		private HashMap<Vertex, Double> createDecisionMap(Vertex vertex)
		{
			HashMap<Vertex, Double> output = new HashMap<>();
			
			fillDecisionMap(vertex, output );
			
			return output;
		}
		
		private void fillDecisionMap( Vertex vertex, HashMap<Vertex, Double> decisionMap )
		{
			vertex.outgoingEdges.entrySet()
				.stream()
				.filter
				(
					(entry) -> !trail.contains(entry.getKey())
				)
				.forEach
				(
					(entry) ->
					{
						Vertex ver = entry.getKey();
						Edge edge = entry.getValue();
						Double edgeValue = 
								(MIN_DECISION_VALUE + Math.pow(edge.pheromoneValue, myColony.weightOfPheromoneValue)) 
								/ Math.pow(edge.length, myColony.weightOfEdgeLength);
						
						decisionMap.compute(ver, (key, value) -> value == null ? edgeValue : value + edgeValue );
					}
				);
		}
		
	}
	
	public static class Builder
	{
		private Graph graph;
		
		private double weightOfEdgeLength = 5D;
		private double weightOfPheromoneValue = 1D;
		private double pheromoneNominator = 1D;
		private double pheromoneEvaporationRatio = 0.5D;
		
		private int numberOfAnts = 5;
		
		public Builder(Graph graph) 
		{
			this.graph = graph;
		}
		
		public Builder withWeightOfEdgeLength(double weightOfEdgeLength)
		{
			this.weightOfEdgeLength = weightOfEdgeLength;
			return this;
		}
		
		public Builder withWeightOfPheromoneValue(double weightOfPheromoneValue)
		{
			this.weightOfPheromoneValue = weightOfPheromoneValue;
			return this;
		}
		
		public Builder withPheromoneNominator(double pheromoneNominator)
		{
			this.pheromoneNominator = pheromoneNominator;
			return this;
		}
		
		public Builder withPheromoneEvaporationRatio(double pheromoneEvaporationRatio)
		{
			this.pheromoneEvaporationRatio = pheromoneEvaporationRatio;
			return this;
		}

		public Builder withNumberOfAnts(int numberOfAnts)
		{
			this.numberOfAnts = numberOfAnts;
			return this;
		}

		public AntColony build()
		{
			return new AntColony(this);
		}
	}
}