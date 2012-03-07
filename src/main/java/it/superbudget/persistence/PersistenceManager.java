package it.superbudget.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PersistenceManager
{
	private static PersistenceManager instance;

	private final EntityManagerFactory emf;

	public final static String PERSISTENCE_UNIT_NAME = "SuperBudget";

	private PersistenceManager()
	{
		emf = new org.eclipse.persistence.jpa.PersistenceProvider().createEntityManagerFactory(PERSISTENCE_UNIT_NAME, null);
	}

	public static PersistenceManager getInstance()
	{
		if (instance == null)
		{
			instance = new PersistenceManager();
		}
		return instance;
	}

	public EntityManager getEntityManager()
	{
		return emf.createEntityManager();
	}
}
