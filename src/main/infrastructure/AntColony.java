package main.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class AntColony
{
	private static final double MIN_DECISION_VALUE = 0.5D;
	
	private final Graph graph;
	
	private final double weightOfEdgeLength;
	private final double weightOfPheromoneValue;
	
	private final double pheromoneNominator;
	
	private final double pheromonePercentileEvaporationRate;
	
	private final int numberOfAnts;
	private ArrayList<Ant> ants = new ArrayList<>();
	
	private final int numberOfFullPasses;
	
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
		
		this.numberOfFullPasses = builder.numberOfFullPasses;
		
		for(int i = 0; i < numberOfAnts; i++)
		{
			ants.add(new Ant(this));
		}
		
		setAntsUp();
	}
	
	public void requestPheromoneUpdateForEdge(Edge edge)
	{
		edgesAwaitingPheromoneUpdate.add(edge);
	}
	
	private void setAntsUp()
	{
		Set<Character> graphKeys = graph.getKeys();
		int numberOfKeys = graphKeys.size();
		Character[] keys = new Character[numberOfKeys];
		graphKeys.toArray(keys);
		
		Random rand = new Random();
		
		ants.forEach
		(
			(a) -> a.beginTrailAt
			(
				graph.get
				(
					keys[rand.nextInt(numberOfKeys)]
				)
			)
		);
	}
	
	private void moveAnts()
	{
		ants.forEach(Ant::moveToNextVertex);
	}
	
	private void updatePheromones()
	{
		graph.getEdges()
			.stream()
			.forEach
			(
				(edge) -> 
				{
					edge.pheromoneValue *= (1 - pheromonePercentileEvaporationRate);
				}
			);
		
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
	}
	
	private void updateBestSolution()
	{
		currentBestTrail = null;
		for(Ant a: ants)
		{
			if(currentBestTrail == null || a.getCurrentTrailLength() < bestTrailLength) 
			{
				currentBestTrail = a.getClonedTrail();
				bestTrailLength = a.getCurrentTrailLength();
			}
		}
	}
	
	public static class Ant
	{
		private final double weightOfEdgeLength;
		private final double weightOfPheromoneValue;
		
		private ArrayList<Vertex> trail;
		
		private AntColony myColony;
		
		private Vertex firstVertex;
		private Vertex nextVertex;
		
		public Ant(AntColony colony)
		{
			this.myColony = colony;
			
			this.weightOfEdgeLength = colony.weightOfEdgeLength;
			this.weightOfPheromoneValue = colony.weightOfPheromoneValue;
						
			this.trail = new ArrayList<>();
		}
		
		public double getCurrentTrailLength()
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
		
		public void decideOnNextMove()
		{
			Vertex currentVertex = nextVertex;
			
			HashMap<Vertex, Double> decisionMap = createDecisionMap( currentVertex );
						
			Double decisionValue = getSumOfDecisionValues( decisionMap ) * new Random().nextDouble();
			
			Optional<Vertex> output = getPotentialOutputValue( decisionMap, decisionValue );

			nextVertex = output.orElse(firstVertex);
		}
				
		public void beginTrailAt(Vertex vertex)
		{	
			this.trail = new ArrayList<>();
			nextVertex = vertex;
			moveToNextVertex();
			decideOnNextMove();
		}
		
		public ArrayList<Vertex> getClonedTrail()
		{
			ArrayList<Vertex> clone = new ArrayList<>();
			
			for(Vertex v: trail)
				clone.add(v);
			
			return clone;
		}

		private void moveToNextVertex()
		{
			if(firstVertex == null) firstVertex = nextVertex;
			addToTrail( nextVertex );
			decideOnNextMove();
		}

		private void addToTrail( Vertex vertex )
		{
			if(trail.size() > 0)
			{
				myColony.requestPheromoneUpdateForEdge(trail.get(trail.size()-1).outgoingEdges.get(vertex));
			}
			
			trail.add( vertex );
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
								(MIN_DECISION_VALUE + Math.pow(edge.pheromoneValue, weightOfPheromoneValue)) 
								/ Math.pow(edge.length, weightOfEdgeLength);
						
						decisionMap.compute(ver, (key, value) -> value == null ? edgeValue : value + edgeValue );
					}
				);
		}
		
	}
	
	public static class Builder
	{
		private Graph graph;
		
		private double weightOfEdgeLength = 2D;
		private double weightOfPheromoneValue = 5D;
		private double pheromoneNominator = 1D;
		private double pheromoneEvaporationRatio = 0.5D;
		
		private int numberOfAnts = 5;

		private int numberOfFullPasses = 1;
		
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

		public Builder withNumberOfFullPasses(int numberOfFullPasses)
		{
			this.numberOfFullPasses = numberOfFullPasses;
			return this;
		}
		public AntColony build()
		{
			return new AntColony(this);
		}
	}
}