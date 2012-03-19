package it.superbudget;

import java.util.List;

import com.github.api.v2.schema.Blob;
import com.github.api.v2.schema.Tree;
import com.github.api.v2.services.GitHubServiceFactory;
import com.github.api.v2.services.ObjectService;

public class GitHubVersionChecker
{
	private static final String user = "francescomuia";

	private static final String project = "SuperBudget";

	private static final String releaseDirSha = "caf8280ca49173f6dc078929a8cec870fbadc88d";

	private static final String downloadDirSha = "34b8e138a844f7af6382ecfec55cb788c130a302";

	private static final String versionKey = "version";

	public static ApplicationZipFile getLatestZip()
	{
		GitHubServiceFactory factory = GitHubServiceFactory.newInstance();
		ObjectService objectService = factory.createObjectService();
		Blob blob = getBlobForZipFile();
		// InputStream inputStream = objectService.getObjectContent(user, project, blob.getSha());
		return new ApplicationZipFile(blob, null);

	}

	public static void test()
	{
		GitHubServiceFactory factory = GitHubServiceFactory.newInstance();
		ObjectService objectService = factory.createObjectService();
		// SimpleEntry<String, String> entry = getShaForZipFile();
		List<Blob> blobList = objectService.getBlobs(user, project, downloadDirSha);
		for (Blob blob2 : blobList)
		{
			System.out.println(blob2.getSize());
			double blobSize = (blob2.getSize() / 1024);
			blobSize = blobSize / 1024;
			System.out.println(blobSize);
		}

	}

	private static Blob getBlobForZipFile()
	{
		GitHubServiceFactory factory = GitHubServiceFactory.newInstance();
		ObjectService objectService = factory.createObjectService();
		List<Blob> blobList = objectService.getBlobs(user, project, downloadDirSha);

		String versionString = "";
		String name = null;
		Double ver = new Double(0.0);
		Blob myBlob = null;
		for (Blob blob : blobList)
		{
			name = blob.getName();
			if (name.contains("-SNAPSHOT"))
			{
				name = name.replace("-SNAPSHOT", "");
			}
			versionString = name.substring((name.lastIndexOf(project + "-") + (project + "-").length()), name.lastIndexOf(".zip"));
			Double tempVer = new Double(versionString);
			if (tempVer.compareTo(ver) > 0)
			{
				ver = tempVer;
				myBlob = blob;
			}
		}
		return myBlob;
	}

	public static boolean isUpToDate(String version)
	{

		GitHubServiceFactory factory = GitHubServiceFactory.newInstance();
		ObjectService objectService = factory.createObjectService();
		List<Tree> trees = objectService.getTree(user, "SuperBudget", releaseDirSha);
		String versionString = "";
		Double ver = new Double(0.0);
		for (Tree tree : trees)
		{
			// SuperBudget/0.1/SuperBudget-0.1.jar
			String name = tree.getName();
			if (name.endsWith(".jar") && !name.contains("javadoc") && !name.contains("sources"))
			{
				versionString = name.substring((name.lastIndexOf(project + "-") + (project + "-").length()), name.lastIndexOf(".jar"));
				Double tempVer = new Double(versionString);
				if (tempVer.compareTo(ver) > 0)
				{
					ver = tempVer;
				}
			}
		}
		if (ver.compareTo(new Double(0.0)) == 0)
		{
			throw new RuntimeException("Nessuna versione trovata");
		}
		Double applicationVersion = new Double(version);
		return ver.compareTo(applicationVersion) > 0;
	}

	public static void main(String[] args)
	{
		GitHubVersionChecker.test();
	}
}
