package main.util;

import java.io.File;

public class Tools
{
	public static boolean canBeParsedToInteger(String input)
	{
		try
		{
			Integer.parseInt(input);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static boolean isAnExistingFile( String potentialFilePath )
	{
		return new File(potentialFilePath).exists();
	}
}
