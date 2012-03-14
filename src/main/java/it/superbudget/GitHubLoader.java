package it.superbudget;

import java.util.List;

import com.github.api.v2.schema.Repository;
import com.github.api.v2.schema.Tree;
import com.github.api.v2.services.GitHubServiceFactory;
import com.github.api.v2.services.ObjectService;
import com.github.api.v2.services.RepositoryService;

public class GitHubLoader
{
	public static void main(String[] args)
	{
		GitHubServiceFactory factory = GitHubServiceFactory.newInstance();
		RepositoryService repositoryService = factory.createRepositoryService();
		// repositoryService.setAuthentication(new LoginPasswordAuthentication("francescoMuia83@gmail.com", "tiamobibi83"));
		Repository repository = repositoryService.getRepository("francescomuia", "SuperBudget");
		System.out.println(repository);
		ObjectService objectService = factory.createObjectService();

		List<Tree> trees = objectService.getTree("francescomuia", "SuperBudget", "caf8280ca49173f6dc078929a8cec870fbadc88d");
		for (Tree tree : trees)
		{
			System.out.println(tree);
		}
	}
}
