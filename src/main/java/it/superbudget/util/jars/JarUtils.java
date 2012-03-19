package it.superbudget.util.jars;

import java.io.File;

public class JarUtils
{
	private static final String project = "SuperBudget";

	public static String getLatestApplicationJarVersion(File applicationDir)
	{
		File[] files = applicationDir.listFiles();
		Double ver = new Double(0.0);
		String currentFile = null;
		for (File file : files)
		{
			String name = file.getName();
			Double tempVer = JarUtils.getJarVersion(name);
			if (tempVer != null && tempVer.compareTo(ver) > 0)
			{
				ver = tempVer;
				currentFile = name;
			}
		}
		return currentFile;
	}

	public static Double getJarVersion(String name)
	{
		if (name.endsWith(".jar") && !name.contains("javadoc") && !name.contains("sources"))
		{

			String versionString = name.substring((name.lastIndexOf(project + "-") + (project + "-").length()), name.lastIndexOf(".jar"));
			versionString = versionString.replace("-SNAPSHOT", "");
			Double tempVer = new Double(versionString);
			return tempVer;
		}
		return null;
	}

	public static void moveOldApplicationJarVersion(File[] files, String currentFile, File applicationDir)
	{
		for (File file : files)
		{
			String name = file.getName();
			String versionString = currentFile.substring((currentFile.lastIndexOf(project + "-") + (project + "-").length()),
					currentFile.lastIndexOf(".jar"));
			if (name.endsWith(".jar") && !name.contains("javadoc") && !name.contains("sources") && !name.contains(versionString))
			{
				File oldJarVersion = new File(applicationDir.getPath() + "/old");
				oldJarVersion.mkdir();
				File moveFile = new File(oldJarVersion, file.getName());
				file.renameTo(moveFile);
			}
		}
	}
}
