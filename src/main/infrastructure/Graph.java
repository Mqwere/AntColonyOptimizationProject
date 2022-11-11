package main.infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static main.AntColonyOptimizationProject.log;

public class Graph
{
	private HashMap<String, Vertex> vertices = new HashMap<>();
	private HashSet<Edge> edges;
	private Vertex startingVertex = null;
	
	public Graph()
	{}
	
	public Graph(Vertex... vertices)
	{
		this(Arrays.stream(vertices));
	}
	
	public Graph( List<Vertex> vertices )
	{
		this(vertices.stream());
	}
	
	public Graph( Stream<Vertex> verticesStream )
	{
		verticesStream
			.forEach(
				(v) ->
				{
					String letter = v.identifier.length() == 0 ? getNextVertexName() : v.identifier ;
					v.setIdentifier(letter);
					
					this.vertices.put(letter, v);
				}
			);
	}

	public void add(String name, Vertex vertex)
	{
		vertices.put(name, vertex);
	}
	
	public Vertex get(String name)
	{
		return vertices.get(name);
	}
		
	public void setStartingVertex(Vertex vertex)
	{
		if(startingVertex == null)
			startingVertex = vertex;
	}
	
	public Vertex getStartingVertex()
	{
		if(startingVertex == null && vertices.size() > 0)
			startingVertex = vertices.values().stream().findAny().get();
		
		return startingVertex;
	}
	
	public void createAllEdgesBetweenMappedVertices()
	{
		Vertex[] tempVertices = new Vertex[vertices.size()];
		vertices.values().toArray(tempVertices);
		edges = Vertex.createAllEdgesBetweenVertices(tempVertices);
	}
	
	public HashSet<Edge> getEdges()
	{
		return edges;
	}
	
	public Set<String> getKeys()
	{
		return vertices.keySet();
	}
	
	public ArrayList<Vertex> makeListStartFromFirstVertex(ArrayList<Vertex> input)
	{
		if(!input.contains(startingVertex) || input.get(0).equals(startingVertex)) return input;
		
		int lastIdx = input.size()-1;
		
		boolean inputIsClosedLoop = input.size() > 1 && input.get(0).equals(input.get(lastIdx));
		if(inputIsClosedLoop) { input.remove(lastIdx); }
		
		ArrayList<Vertex> output = shuffleAroundToHaveFirstVertexBeFirst( input );
		
		if(inputIsClosedLoop) output.add(startingVertex);
		
		return output;
	}

	private ArrayList<Vertex> shuffleAroundToHaveFirstVertexBeFirst( ArrayList<Vertex> input )
	{
		ArrayList<Vertex> output = new ArrayList<>();
		int idx = 0;
		boolean mayCopy = false;
		
		while(output.size() != input.size())
		{
			int normalizedIdx = idx % input.size();
			Vertex currentVertex = input.get(normalizedIdx);
			if (mayCopy || 
				(mayCopy = currentVertex.equals(startingVertex)))
			{
				output.add(input.get(normalizedIdx));
			}
			idx++;
		}
		
		return output;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
				startingVertex, vertexNameNum, vertices
		);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Graph))
			return false;
		Graph other = (Graph) obj;
		return 
				Objects.equals(startingVertex, other.startingVertex) && 
				Objects.equals( vertices, other.vertices );
	}
	
	public static Graph parse(String input)
	{
		Pattern pattern = Pattern.compile("(-?[1-9]\\d*)(\\.\\d+)?");
		Matcher matcher = pattern.matcher(input);
		
		HashSet<Vertex> vertices = new HashSet<>();
		
		while (matcher.find())
		{
			String Y = matcher.group(), X;
			if(matcher.find())
			{
				X = matcher.group();
				
				try
				{
					vertices.add( new Vertex(Double.parseDouble(Y), Double.parseDouble(X)));
				}
				catch(NumberFormatException e)
				{
					log("VERTEX COULD NOT BE PARSED (%s, %s)", Y, X);
				}
			}
		}
		
		Graph output = new Graph(vertices.stream().toList());
		output.createAllEdgesBetweenMappedVertices();
		
		return output;
	}

	public static Graph createGraphFromIdxNumber(int idxNumber)
	{
		Graph output = new Graph(
			idxNumber % 2 == 0 ? new Vertex( 2, 3, "A" ) : new Vertex( 1, 2, "A" ),
			idxNumber % 2 == 0 ? new Vertex( 5, 1, "B" ) : new Vertex( 3, 1, "B" ),
			idxNumber % 2 == 0 ? new Vertex( 4, 7, "C" ) : new Vertex( 3, 6, "C" ),
			idxNumber % 2 == 0 ? new Vertex( 7, 7, "D" ) : new Vertex( 6, 7, "D" ),
			idxNumber % 2 == 0 ? new Vertex( 7, 3, "E" ) : new Vertex( 5, 2, "E" )
		);
		
		output.setStartingVertex(
			output.get(
				switch(idxNumber % 10) 
				{
					case	0, 1 	-> "A";
					case	2, 3 	-> "B";
					case	4, 5 	-> "C";
					case	6, 7 	-> "D";
					default		-> "E";
				}
			)
		);
		
		output.createAllEdgesBetweenMappedVertices();
		
		return output;
	}
	
	private int vertexNameNum = 1;
	private String getNextVertexName()
	{
		return "" + vertexNameNum++;
	}
}
