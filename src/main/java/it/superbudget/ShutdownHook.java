package it.superbudget;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe che spegne il database
 * 
 * @author Francesco Muià
 * 
 */
public class ShutdownHook extends Thread
{
	private static final String project = "SuperBudget";

	public static ThreadLocal<Boolean> restartApp = new ThreadLocal<Boolean>();

	public File getApplicationFileName(File dir)
	{
		File[] files = dir.listFiles();
		Double ver = new Double(0.0);
		File currentFile = null;
		for (File file : files)
		{
			String name = file.getName();

			if (name.endsWith(".jar") && !name.contains("javadoc") && !name.contains("sources"))
			{

				String versionString = name.substring((name.lastIndexOf(project + "-") + (project + "-").length()), name.lastIndexOf(".jar"));
				versionString = versionString.replace("-SNAPSHOT", "");
				Double tempVer = new Double(versionString);
				if (tempVer.compareTo(ver) > 0)
				{
					ver = tempVer;
					currentFile = file;
				}
			}

		}
		return currentFile;
	}

	public void restartApplication(File currentJar) throws IOException
	{
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
	}

	@Override
	public void run()
	{
		try
		{
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		}
		catch (SQLException e1)
		{
		}
		if (restartApp.get())
		{
			try
			{

				// Runtime runtime = Runtime.getRuntime();
				String path = ShutdownHook.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				File applicationDir = new File(decodedPath).getParentFile();
				this.restartApplication(this.getApplicationFileName(applicationDir));
			}
			catch (UnsupportedEncodingException e)
			{
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
