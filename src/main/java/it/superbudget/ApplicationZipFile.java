package it.superbudget;

import java.io.InputStream;

import com.github.api.v2.schema.Blob;

public class ApplicationZipFile
{
	private Blob blob;

	private InputStream inputStream;

	public ApplicationZipFile(Blob blob, InputStream in)
	{
		this.blob = blob;
		this.inputStream = in;
	}

	public Blob getBlob()
	{
		return blob;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}

}