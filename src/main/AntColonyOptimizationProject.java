package main;

import main.infrastructure.Graph;

public class AntColonyOptimizationProject
{
	private static final int idxNumber = 6799;
	
	public static void main(String[] args)
	{
		Graph.createGraphFromIdxNumber(idxNumber);
	}
	
	public static void printArray(Object[] array)
	{
		String printedString = "{";
		
		for(Object o: array) 
		{
			printedString += " " + o;
		}
		
		printedString += " }";
		System.out.println(printedString);
	}
	
}
