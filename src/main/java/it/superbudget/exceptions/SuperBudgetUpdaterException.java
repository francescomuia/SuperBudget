package it.superbudget.exceptions;

import java.io.IOException;

public class SuperBudgetUpdaterException extends Exception
{

	public SuperBudgetUpdaterException(String message, IOException e)
	{
		super(message, e);
	}

}
