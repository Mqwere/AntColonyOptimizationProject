package main.util;

import static main.util.AntColonyBuilderLite.DELIMITER;

public class ArgumentsHandler
{
	private final SimpleArgument isHelpRequired = (SimpleArgument) new SimpleArgument().withShortForm('h').withLongForm("help");
	private final SimpleArgument shouldBeVerbose = (SimpleArgument) new SimpleArgument().withShortForm('v').withLongForm("verbosity");
	private final SimpleArgument shouldUseIndexNumber = (SimpleArgument) new SimpleArgument().withShortForm('i').withLongForm("index");
	private final SimpleArgument shouldUseFileInput = (SimpleArgument) new SimpleArgument().withShortForm('f').withLongForm("file");
	private final SimpleArgument shouldUseAntColonyProperties = (SimpleArgument) new SimpleArgument().withShortForm('c').withLongForm("colony");
	
	private final StringArgument indexNumberToUse = (StringArgument) new StringArgument().withShortForm('i').withLongForm("index");
	private final StringArgument filePathToUse = (StringArgument) new StringArgument().withShortForm('f').withLongForm("file");
	private final StringArgument antColonyProperties = (StringArgument) new StringArgument().withShortForm('c').withLongForm("colony");
	
	public ArgumentsHandler(String[] args)
	{
		isHelpRequired.detect(args);
		
		if(!isHelpRequired())
		{
			shouldBeVerbose.detect(args);
			
			shouldUseIndexNumber.detect(args);
			if(shouldUseIndexNumber.getValue()) indexNumberToUse.detect(args);
			
			shouldUseFileInput.detect(args);
			if(!shouldUseIndexNumber() && shouldUseFileInput.getValue()) filePathToUse.detect(args);
			
			shouldUseAntColonyProperties.detect(args);
			if(shouldUseAntColonyProperties.getValue()) antColonyProperties.detect(args);
		}
	}
	
	public boolean isHelpRequired()
	{
		return isHelpRequired.getValue();
	}
	
	public boolean shouldBeVerbose()
	{
		return this.shouldBeVerbose.getValue();
	}
	
	public boolean shouldUseIndexNumber()
	{
		return shouldUseIndexNumber.getValue() && Tools.canBeParsedToInteger(getIndexNumberToUse());
	}
	
	public boolean indexNumberWasUnusable()
	{
		return shouldUseIndexNumber.getValue() && !Tools.canBeParsedToInteger(getIndexNumberToUse());
	}
	
	public boolean shouldUseFileInput()
	{
		return shouldUseFileInput.getValue() && Tools.isAnExistingFile(getFilePathToUse());
	}

	public boolean fileInputWasUnusable()
	{
		return shouldUseFileInput.getValue() && !Tools.isAnExistingFile(getFilePathToUse());
	}
	
	public boolean shouldUseAntColonyProperties()
	{
		return shouldUseAntColonyProperties.getValue();
	}	
	
	public String getIndexNumberToUse()
	{
		return indexNumberToUse.get();
	}
	
	public String getFilePathToUse()
	{
		return filePathToUse.get();
	}
	
	public AntColonyBuilderLite getColonyProperties()
	{
		return new AntColonyBuilderLite(antColonyProperties.get());
	}
	
	public String toHelpString()
	{
		String helpString;
		int bufferedLength = 30;
		return 
			new StringBuilder("Available arguments:\n")
				.append("\tusage: ").append(helpString = isHelpRequired.toHelpString()).append(getPaddingOfSize(bufferedLength - helpString.length())).append("prints this help message.\n")
				.append("\tusage: ").append(helpString = shouldBeVerbose.toHelpString()).append(getPaddingOfSize(bufferedLength - helpString.length())).append("enables verbosity.\n")
				.append("\n")
				.append("\tusage: ").append(helpString = (shouldUseIndexNumber.toHelpString() + " {integer}")).append(getPaddingOfSize(bufferedLength - helpString.length())).append("creates a graph from given student index.\n")
				.append("\tusage: ").append(helpString = (shouldUseFileInput.toHelpString() + " {filePath}")).append(getPaddingOfSize(bufferedLength - helpString.length())).append("creates a graph from given file path, if accessible.\n")
				.append("\n")
				.append("\tusage: ")
					.append(helpString = (shouldUseAntColonyProperties.toHelpString()))
					.append(" dw" + DELIMITER + "pw" + DELIMITER + "pn" + DELIMITER + "pe" + DELIMITER + "na" + DELIMITER +"np" )
					.append("\n\t").append(getPaddingOfSize(7))
					.append("assigns properties of the Ant Colony, where:\n")
					.append("\t").append(getPaddingOfSize(7))
					.append("dw - distance weight (Double [5]) [how much do ants value length of an edge],\n")
					.append("\t").append(getPaddingOfSize(7))
					.append("pw - pheromone weight (Double [2]) [how much do ants value pheromones on an edge],\n")
					.append("\t").append(getPaddingOfSize(7))
					.append("pn - pheromone nominator (Double [1]) [the bigger, the more pheromone is left every time],\n")
					.append("\t").append(getPaddingOfSize(7))
					.append("pe - pheromone evaporation ratio (Double [0.5]) [how fast does pheromone evaporate],\n")
					.append("\t").append(getPaddingOfSize(7))
					.append("na - number of ants (Integer [5]),\n")
					.append("\t").append(getPaddingOfSize(7))
					.append("np - number of passes (Integer [3]) [full journeys the ants will go through].")
				.toString();
	}
	
	private String getPaddingOfSize(int size)
	{
		StringBuilder output = new StringBuilder();
		output.setLength(size);
		return output.toString();
	}
	
	@Override
	public String toString()
	{
		return 
		new StringBuilder("Arguments:\n")
		
		.append("shouldBeVerbose: ").append(shouldBeVerbose.getValue()).append("\n")
		.append("shouldUseIndexNumber: ").append(shouldUseIndexNumber.getValue()).append("\n")
		.append("shouldUseFileInput: ").append(shouldUseFileInput.getValue()).append("\n")
		
		.append("indexNumberToUse: ").append(indexNumberToUse.get()).append("\n")
		.append("filePathToUse: ").append(filePathToUse.get()).append("\n")
		
		.append("indexNumberWasUnusable: ").append(indexNumberWasUnusable()).append("\n")
		.append("fileInputWasUnusable: ").append(fileInputWasUnusable()).append("\n")
		.toString();
	}
}