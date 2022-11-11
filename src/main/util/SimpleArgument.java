package main.util;

public class SimpleArgument extends AbstractArgument
{
	private boolean value;
	
	public SimpleArgument()
	{
		this.value = false;
	}
	
	@Override
	public void detect(String[] input)
	{
		for(String subinput: input)
		{
			if( stringMatchesShortForm( subinput ) || stringMatchesLongForm( subinput ) )
			{
				this.value = true;
				break;
			}
		}
	}
	
	public boolean getValue()
	{
		return value;
	}
}
