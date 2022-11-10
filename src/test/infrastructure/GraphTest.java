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
		
		A.createEdgeToVertex(B); A.createEdgeToVertex(C); A.createEdgeToVertex(D); A.createEdgeToVertex(E);
		B.createEdgeToVertex(A); B.createEdgeToVertex(C); B.createEdgeToVertex(D); B.createEdgeToVertex(E);
		C.createEdgeToVertex(A); C.createEdgeToVertex(B); C.createEdgeToVertex(D); C.createEdgeToVertex(E);
		D.createEdgeToVertex(A); D.createEdgeToVertex(B); D.createEdgeToVertex(C); D.createEdgeToVertex(E);
		E.createEdgeToVertex(A); E.createEdgeToVertex(B); E.createEdgeToVertex(C); E.createEdgeToVertex(D);
		
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
