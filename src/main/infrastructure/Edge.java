package main.infrastructure;

import java.util.Objects;

public class Edge
{
	Vertex vertex1, vertex2;
	public double length;
	public double pheromoneValue = 0;
	
	public Edge(Vertex vertex1, Vertex vertex2)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		calculateLength();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this) return true;
		
		if(!(o instanceof Edge)) return false;
		
		Edge e = (Edge) o;
		
		return	(e.vertex1.equals(this.vertex1) && e.vertex2.equals(this.vertex2))
				||	(e.vertex1.equals(this.vertex2) && e.vertex2.equals(this.vertex1));
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(vertex1, vertex2, length);
	}
	
	private void calculateLength()
	{
		double 
			fromX = vertex1.X,	fromY = vertex1.Y,
			toX 	= vertex2.X,	toY 	= vertex2.Y;
		
		this.length = Math.sqrt((fromX - toX)*(fromX - toX) + (fromY - toY)*(fromY - toY));
	}
	
}
