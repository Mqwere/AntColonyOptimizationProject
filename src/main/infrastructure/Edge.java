package main.infrastructure;

public class Edge
{
	Vertex vertex1, vertex2;
	double pheromoneValue = 0;
	
	public Edge(Vertex vertex1, Vertex vertex2)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this) return true;
		
		if(!(o instanceof Edge)) return false;
		
		Edge e = (Edge) o;
		
		return (e.vertex1.equals(this.vertex1) && e.vertex2.equals(this.vertex2))
			|| (e.vertex1.equals(this.vertex2) && e.vertex2.equals(this.vertex1));
	}
}
