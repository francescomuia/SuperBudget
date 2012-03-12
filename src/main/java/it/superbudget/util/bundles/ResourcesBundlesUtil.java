package it.superbudget.util.bundles;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ResourcesBundlesUtil
{
	public static String LABEL_BUNDLE_FILE_NAME = "bundles.LabelsBundle";

	public static String VERSION_FILE_NAME = "/version.properties";

	public static String VERSION_PROPERTY_NAME = "version";

	private static Logger getLogger()
	{
		return Logger.getLogger(ResourcesBundlesUtil.class);
	}

	public static ResourceBundle getLabelsBundles()
	{
		return ResourceBundle.getBundle(LABEL_BUNDLE_FILE_NAME, Locale.getDefault());
	}

	public static String getApplicationVersion()
	{
		Properties props = new Properties();
		try
		{
			props.load(ResourcesBundlesUtil.class.getResourceAsStream(VERSION_FILE_NAME));
			return props.getProperty(VERSION_PROPERTY_NAME);
		}
		catch (IOException e)
		{
			getLogger().error("Failed to load Version Properties", e);
		}
		return null;
	}

	public static void dump(ResourceBundle bundle)
	{
		Logger logger = getLogger();
		Iterator<String> keys = bundle.keySet().iterator();
		while (keys.hasNext())
		{
			String key = keys.next();
			logger.debug(key + " = " + bundle.getString(key));
		}
	}
}
