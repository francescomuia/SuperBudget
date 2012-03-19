package it.superbudget.exceptions;

public class DownloadException extends Exception
{

	public DownloadException(String message, Exception e)
	{
		super(message, e);
	}

}
