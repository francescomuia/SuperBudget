package it.superbudget;

import it.superbudget.util.bundles.ResourcesBundlesUtil;

import java.util.ResourceBundle;

import junit.framework.TestCase;

public class ResourcesBundlesUtilTest extends TestCase
{

	public void testGetLabelsBundles()
	{
		ResourceBundle bundle = ResourcesBundlesUtil.getLabelsBundles();
		assertNotNull(bundle);
	}

	public void testGetApplicationVersion()
	{
		String version = ResourcesBundlesUtil.getApplicationVersion();
		assertNotNull(version);
		assertFalse("PROVOLA", version.trim().isEmpty());
	}

}
