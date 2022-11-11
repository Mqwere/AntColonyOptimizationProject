package main.util;

public class AntColonyBuilderLite
{
	public static final String DELIMITER = ";";
	
	public double weightOfEdgeLength = 5D;
	public double weightOfPheromoneValue = 2D;
	public double pheromoneNominator = 1D;
	public double pheromoneEvaporationRatio = 0.5D;
	
	public int numberOfAnts = 5;
	public int numberOfFullPasses = 3;
	
	public AntColonyBuilderLite() {}
	
	public AntColonyBuilderLite(String input)
	{
		String[] inputSplit = input.split(DELIMITER);
		
		int i = 0;
		try { weightOfEdgeLength			= Double.parseDouble(inputSplit[i++]); } catch(Exception e) {}
		try { weightOfPheromoneValue 		= Double.parseDouble(inputSplit[i++]); } catch(Exception e) {}
		try { pheromoneNominator 			= Double.parseDouble(inputSplit[i++]); } catch(Exception e) {}
		try { pheromoneEvaporationRatio 	= Double.parseDouble(inputSplit[i++]); } catch(Exception e) {}
		
		try { numberOfAnts 					= Integer.parseInt(inputSplit[i++]); } catch(Exception e) {}
		try { numberOfFullPasses			= Integer.parseInt(inputSplit[i++]); } catch(Exception e) {}
	}
	
	
}
