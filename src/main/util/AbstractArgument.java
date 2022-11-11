package main.util;

import java.util.regex.Pattern;

public abstract class AbstractArgument
{
	protected Character shortForm = null;
	protected String longForm = null;
	
	private Pattern shortFormPattern;
	private Pattern longFormPattern;
	
	public AbstractArgument withShortForm(Character shortForm)
	{
		this.shortForm = shortForm;
		this.shortFormPattern = Pattern.compile(String.format("^-\\w*%s\\w*", shortForm));
		return this;
	}
	
	public AbstractArgument withLongForm(String longForm)
	{
		this.longForm = longForm;
		this.longFormPattern = Pattern.compile("--" + longForm);
		return this;
	}
	
	public abstract void detect(String[] input);

	protected boolean stringMatchesShortForm( String input )
	{
		return shortForm == null
				? false 
				: shortFormPattern.matcher(input).find();
	}
	
	protected boolean stringMatchesLongForm( String input )
	{
		return longForm == null
				? false 
				: longFormPattern.matcher(input).find();
	}
	
	public String toHelpString()
	{
		return String.format(
			"[ %s%s%s ]",
			shortForm != null? "-"+shortForm:"",
			shortForm != null && longForm != null? " / ":"",
			longForm != null? "--"+longForm:""
		);
	}
	
}
