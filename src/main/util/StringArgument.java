package main.util;

import java.util.Optional;

public class StringArgument extends AbstractArgument
{
	private String defaultValue = "";
	private Optional<String> value = Optional.empty();
	
	public StringArgument() { }
	
	public StringArgument(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	
	@Override
	public void detect(String[] input)
	{
		for(int i = 0; i < input.length - 1; i++)
		{
			String subinput = input[i];
			if( stringMatchesShortForm( subinput ) || stringMatchesLongForm( subinput ) )
			{
				value = Optional.of(input[i+1]);
				break;
			}
		}
	}
	
	public String get()
	{
		return value.orElse(defaultValue);
	}

}
