package main.infrastructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Vertex
{
	public final double Y, X;
	public HashMap<Vertex, Edge> outgoingEdges = new HashMap<>();
	
	public String identifier = "";
	
	public Vertex(double Y, double X)
	{
		this.Y = Y;
		this.X = X;		
	}
	
	public Vertex(double Y, double X, String identifier)
	{
		this.Y = Y;
		this.X = X;		
		this.identifier = identifier;
	}
	
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	
	public Edge createEdgeToVertex(Vertex destination)
	{
		Edge output;
		outgoingEdges.putIfAbsent(destination, output = new Edge(this, destination) );
		return output;
	}
	
	public void setEdgeToVertex(Vertex destination, Edge edge)
	{
		outgoingEdges.put(destination, edge );
	}
	
	public void createEdgesToAndFromVertex(Vertex destination)
	{
		setEdgeToVertex(destination, destination.createEdgeToVertex(this)); 
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash( Y, X );
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this) return true;
		
		if(!(o instanceof Vertex)) return false;
		
		Vertex v = (Vertex) o;
		
		return v.X == this.X && v.Y == this.Y;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (%.1f %.1f)", identifier, Y, X);
	}

	public static HashSet<Edge> createAllEdgesBetweenVertices(Vertex... vertices)
	{
		HashSet<Edge> output = new HashSet<>();
		if(vertices.length <= 1) return output;
		
		for(int i = 0; i < vertices.length-1; i++)
		{
			Vertex source = vertices[i];
			for(int z = i + 1; z < vertices.length; z++)
			{
				Vertex destination = vertices[z];
				
				Edge edge = source.createEdgeToVertex(destination);
				destination.setEdgeToVertex(source, edge);
				
				output.add(edge);
			}
		}
		
		return output;
	}

}
