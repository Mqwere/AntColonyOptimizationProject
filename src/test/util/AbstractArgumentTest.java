package test.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import main.util.SimpleArgument;

public class AbstractArgumentTest
{
	@Test
	public void abstractArgumentAndExtensionsDetectShortForms()
	{
		String[] args = {"-i"};
		
		SimpleArgument testArgument = (SimpleArgument) new SimpleArgument().withShortForm('i');
		
		testArgument.detect(args);
		
		Assertions.assertTrue(testArgument.getValue());
		
	}
}
