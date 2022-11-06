package main.infrastructure;

import java.util.HashMap;

public class Vertex
{
	public final int Y, X;
	public HashMap<Vertex, Edge> outgoingEdges = new HashMap<>();
	
	public Vertex(int Y, int X)
	{
		this.Y = Y;
		this.X = X;		
	}
	
	public void createEdgeToVertex(Vertex destination)
	{
		if(destination.equals(this)) return;
		
		outgoingEdges.putIfAbsent(destination, new Edge(this, destination) );
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this) return true;
		
		if(!(o instanceof Vertex)) return false;
		
		Vertex v = (Vertex) o;
		
		return v.X == this.X && v.Y == this.Y;
	}
	
	public static void createAllEdgesBetweenVertices(Vertex... vertices)
	{
		if(vertices.length <= 1) return;
		
		for(int i = 0; i < vertices.length-1; i++)
		{
			Vertex source = vertices[i];
			for(int z = i + 1; z < vertices.length; z++)
			{
				Vertex destination = vertices[z];
				source.createEdgeToVertex(destination);
				destination.createEdgeToVertex(source);
			}
		}
		
	}

}
