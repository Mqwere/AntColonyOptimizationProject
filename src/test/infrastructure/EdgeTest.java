package test.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import main.infrastructure.Edge;
import main.infrastructure.Vertex;

public class EdgeTest
{
	@Test
	public void edgeCalculatesValidLengths()
	{
		Edge edge = new Edge(new Vertex(0,0), new Vertex(3,4));
		Assertions.assertEquals(5, edge.length);

		edge = new Edge(new Vertex(0,0), new Vertex(5,12));
		Assertions.assertEquals(13, edge.length);
		
		edge = new Edge(new Vertex(0,0), new Vertex(7,24));
		Assertions.assertEquals(25, edge.length);
	}
}
