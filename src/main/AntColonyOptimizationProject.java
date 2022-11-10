package main;

import java.util.ArrayList;

import main.infrastructure.AntColony;
import main.infrastructure.Graph;
import main.infrastructure.Vertex;

public class AntColonyOptimizationProject
{
	private static final int INDEX_NUMBER = 6799;
	public static final double MIN_DECISION_VALUE = 0.1D;
	
	private static final boolean SHOULD_LOG = true;
	
	public static void main(String[] args)
	{
		Graph graph = Graph.createGraphFromIdxNumber(INDEX_NUMBER);
		
		AntColony colony =  new AntColony
										.Builder(graph)
											.withWeightOfEdgeLength(5D)
											.withWeightOfPheromoneValue(1D)
											.withPheromoneNominator(3D)
											.withPheromoneEvaporationRatio(0.5D)
											.withNumberOfAnts(2)
										.build();
		
		ArrayList<Vertex> output = colony.performANumberOfFullPasses(2);
		System.out.println(
			AntColony.trailToString(output)
		);
	}
	
	public static void log(Object message, Object... args)
	{
		if(!SHOULD_LOG) return;
		String logContent = args.length==0? message.toString() : String.format(message.toString(), args);
		System.out.print(logContent);
	}
	
	public static void logln(Object message, Object... args) 
	{
		log(message.toString()+"\n", args);
	}
	
}
