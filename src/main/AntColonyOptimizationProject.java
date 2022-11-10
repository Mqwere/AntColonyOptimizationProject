package main;

import main.infrastructure.AntColony;
import main.infrastructure.Graph;

public class AntColonyOptimizationProject
{
	private static final int INDEX_NUMBER = 6799;
	public static final double MIN_DECISION_VALUE = 0.1D;
	
	public static final boolean SHOULD_LOG = false;
	
	public static void main(String[] args)
	{
		Graph graph = Graph.createGraphFromIdxNumber(INDEX_NUMBER);
		
		AntColony colony =  new AntColony
										.Builder(graph)
											.withWeightOfEdgeLength(10D)
											.withWeightOfPheromoneValue(1D)
											.withPheromoneNominator(1D)
											.withPheromoneEvaporationRatio(0.5D)
											.withNumberOfAnts(2)
										.build();
		
		colony.performANumberOfFullPasses(1);
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
