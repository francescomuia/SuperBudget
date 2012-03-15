package it.superbudget;

import java.util.List;

import com.github.api.v2.schema.Tree;
import com.github.api.v2.services.GitHubServiceFactory;
import com.github.api.v2.services.ObjectService;

public class GitHubVersionChecker
{
	private static final String user = "francescomuia";

	private static final String project = "SuperBudget";

	private static final String releaseDirSha = "caf8280ca49173f6dc078929a8cec870fbadc88d";

	private static final String libDirSha = "d8ea1a935cb05196dc6309c4bc51eded1b12f517";

	private static final String downloaderVersionFile = "/downloaderVer.properties";

	private static final String versionKey = "version";
	
	public static boolean downloaderIsUpToDate()
	{
		
	}

	public static boolean isUpToDate(String version)
	{
		if (version.contains("SNAPSHOT"))
		{
			return false;
		}
		else
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
	}
}
