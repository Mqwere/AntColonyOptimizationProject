package test.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import main.infrastructure.Graph;
import main.infrastructure.Vertex;

public class GraphTest
{
	@Test
	public void graphCreatedFromMyIdxNumberIsCorrect()
	{
		Graph expectedGraph = new Graph();
		
		Vertex 
			A = new Vertex(1,2),
			B = new Vertex(3,1),
			C = new Vertex(3,6),
			D = new Vertex(6,7),
			E = new Vertex(5,2);
		
		B.createEdgesToAndFromVertex(A); 
		
		C.createEdgesToAndFromVertex(A); 
		C.createEdgesToAndFromVertex(B);
		
		D.createEdgesToAndFromVertex(A); 
		D.createEdgesToAndFromVertex(B); 
		D.createEdgesToAndFromVertex(C);
		
		E.createEdgesToAndFromVertex(A); 
		E.createEdgesToAndFromVertex(B); 
		E.createEdgesToAndFromVertex(C); 
		E.createEdgesToAndFromVertex(D);
		
		expectedGraph.add('A', A);
		expectedGraph.add('B', B);
		expectedGraph.add('C', C);
		expectedGraph.add('D', D);
		expectedGraph.add('E', E);
		
		expectedGraph.setStartingVertex(E);
		
		Graph actualGraph = Graph.createGraphFromIdxNumber(6799);
		
		Assertions.assertTrue(expectedGraph.equals(actualGraph));
		
	}
	
}
