package main;

import main.infrastructure.Vertex;

public class AntColonyOptimizationProject
{
	private static final int idxNumber = 6799;
	
	@SuppressWarnings("unused")
	private static Vertex
		A = idxNumber % 2 == 0 ? new Vertex( 2, 3 ) : new Vertex( 1, 2 ),
		B = idxNumber % 2 == 0 ? new Vertex( 5, 1 ) : new Vertex( 3, 1 ),
		C = idxNumber % 2 == 0 ? new Vertex( 4, 7 ) : new Vertex( 3, 6 ),
		D = idxNumber % 2 == 0 ? new Vertex( 7, 7 ) : new Vertex( 6, 7 ),
		E = idxNumber % 2 == 0 ? new Vertex( 7, 3 ) : new Vertex( 5, 2 );

	public static void main(String[] args)
	{
		Vertex.createAllEdgesBetweenVertices( A, B, C, D, E );
		
		Vertex startingVertex = 
		switch(idxNumber % 10) 
		{
			case	0, 1 -> A;
			case	2, 3 -> B;
			case	4, 5 -> C;
			case	6, 7 -> D;
			default		 -> E;
		};

	}
	
}
