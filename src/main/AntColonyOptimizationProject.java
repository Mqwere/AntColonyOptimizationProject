package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import main.infrastructure.AntColony;
import main.infrastructure.Graph;
import main.util.ArgumentsHandler;

public class AntColonyOptimizationProject
{
	private static final int DEFAULT_INDEX_NUMBER = 6799;
	
	public static final double MIN_DECISION_VALUE = 0.1D;

	private static File loggingFile;
	private static final String LOG_DIRECTORY = "./logs";
	
	public static ArgumentsHandler handler;
	
	public static void main(String[] args)
	{
		//args = new String[] {"-v"};
		
		handler = new ArgumentsHandler(args);
		
		if(handler.isHelpRequired())
		{
			checklessLog(handler.toHelpString());
			return;
		}
		
		Graph graph = getGraphUsingArgumentsReceived();
		
		AntColony colony =  new AntColony
										.Builder(graph)
											.withLiteBuilder(handler.getColonyProperties())
										.build();
		
		colony.performANumberOfFullPasses(handler.getColonyProperties().numberOfFullPasses);
		
		logDump();
	}
	
	public static Graph getGraphUsingArgumentsReceived()
	{
		if(handler.shouldUseIndexNumber())
		{
			log("Creating a graph using index \"%s\".", handler.getIndexNumberToUse());
			return Graph.createGraphFromIdxNumber(Integer.parseInt(handler.getIndexNumberToUse()));
		}
		else
		if(handler.shouldUseFileInput())
		{
			try
			{
				BufferedReader reader = new BufferedReader( new FileReader( handler.getFilePathToUse( ) ) );

				log("Creating a graph using file from \"%s\".", handler.getFilePathToUse());
				
				String fileContent = "", line;
				
				while ((line = reader.readLine()) != null) 
				{
					fileContent += " " + line;
				}
				reader.close();
//				byte[] encoded = Files.readAllBytes(Paths.get(handler.getFilePathToUse()));
//				String fileContent = new String(encoded, Charset.defaultCharset());// = readFile("test.txt", Charset.defaultCharset());
				return Graph.parse(fileContent);
			} 
			catch (Exception e)
			{
				checklessLog("Error occured while parsing file to a usable graph. Falling back to the default graph.\nException caught:\n" + e);
			}
		}
		
		log("Creating a graph using the default index \"%s\"", DEFAULT_INDEX_NUMBER);
		
		return Graph.createGraphFromIdxNumber(DEFAULT_INDEX_NUMBER);
	}
	
	public static void checklessLog(Object message, Object...args)
	{
		String logContent = createLogContentFromMessageAndArgs( message, args ) + "\n";
		fileLogData += logContent;
		System.out.print(logContent);
	}
		
	public static void log(Object message, Object... args) 
	{
		if(handler.shouldBeVerbose())
			checklessLog( message, args );
		else fileLogData += createLogContentFromMessageAndArgs( message, args ) + "\n";
	}

	private static String createLogContentFromMessageAndArgs( Object message, Object... args )
	{
		return args.length==0? message.toString() : String.format(message.toString(), args);
	}
	
	private static String fileLogData = "";
	private static void logDump()
	{
		if(loggingFile == null)
		{
			if(!createLoggingFile()) checklessLog("Could not create logging file.");
		}		
		
		PrintWriter fileWriter;
		try
		{
			fileWriter = new PrintWriter(loggingFile.getPath());
			fileWriter.print(fileLogData);
			fileWriter.close();
			checklessLog("Logs created at \"%s\"", loggingFile.getPath());
		} 
		catch (FileNotFoundException e)
		{
			checklessLog(e);
		}
	}
	private static boolean createLoggingFile()
	{
		try
		{
			ZonedDateTime now = Instant.now().atZone(ZoneOffset.ofHours(2));
			File directory = new File(LOG_DIRECTORY);
			if(!directory.exists()) directory.mkdirs();
			String nowString = 
				String.format(
					"%d%02d%02d%02d%02d%02d", 
					now.getYear(), 
					now.getMonthValue(), 
					now.getDayOfMonth(),
					now.getHour(),
					now.getMinute(),
					now.getSecond());
			loggingFile = new File("./logs/" + nowString + ".log");
			return loggingFile.createNewFile();
		} 
		catch (IOException e)
		{
			checklessLog(e);
			return false;
		}
	}
	
}
