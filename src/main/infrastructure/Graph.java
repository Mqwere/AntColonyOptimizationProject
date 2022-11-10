package main.infrastructure;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Graph
{
	private HashMap<Character, Vertex> vertices = new HashMap<>();
	private HashSet<Edge> edges;
	private Vertex startingVertex = null;
	
	public Graph()
	{}
	
	public Graph(Vertex... vertices)
	{
		Arrays
			.stream(vertices)
			.forEach(
				(v) ->
				{
					this.vertices.put(getNextLetterForVertexName(), v);
				}
			);
	}
	
	public void add(Character name, Vertex vertex)
	{
		vertices.put(name, vertex);
	}
	
	public Vertex get(Character name)
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
	
	public Set<Character> getKeys()
	{
		return vertices.keySet();
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

	public static Graph createGraphFromIdxNumber(int idxNumber)
	{
		Graph output = new Graph(
			idxNumber % 2 == 0 ? new Vertex( 2, 3 ) : new Vertex( 1, 2 ), //A
			idxNumber % 2 == 0 ? new Vertex( 5, 1 ) : new Vertex( 3, 1 ), //B
			idxNumber % 2 == 0 ? new Vertex( 4, 7 ) : new Vertex( 3, 6 ), //C
			idxNumber % 2 == 0 ? new Vertex( 7, 7 ) : new Vertex( 6, 7 ), //D
			idxNumber % 2 == 0 ? new Vertex( 7, 3 ) : new Vertex( 5, 2 )  //E
		);
		
		output.setStartingVertex(
			output.get(
				switch(idxNumber % 10) 
				{
					case	0, 1 	-> 'A';
					case	2, 3 	-> 'B';
					case	4, 5 	-> 'C';
					case	6, 7 	-> 'D';
					default		-> 'E';
				}
			)
		);
		
		output.createAllEdgesBetweenMappedVertices();
		
		return output;
	}
	
	private int vertexNameNum = 0;
	private char getNextLetterForVertexName()
	{
		return (char)('A' + vertexNameNum++);
	}
}
