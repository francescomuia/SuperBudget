package it.superbudget.util.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class InternetConnectionUtils
{
	public static boolean isInternetReachable(String urlString)
	{
		try
		{
			// make a URL to a known source
			URL url = new URL(urlString);

			// open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

			// trying to retrieve data from the source. If there
			// is no connection, this line will fail
			Object objData = urlConnect.getContent();

		}
		catch (UnknownHostException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}
}
