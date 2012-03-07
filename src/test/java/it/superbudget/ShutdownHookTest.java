package it.superbudget;

import javax.persistence.EntityManagerFactory;

import junit.framework.TestCase;

public class ShutdownHookTest extends TestCase
{
	private EntityManagerFactory emf;

	protected void setUp() throws Exception
	{
		// emf = new org.eclipse.persistence.jpa.PersistenceProvider().createEntityManagerFactory("SuperBudget-test", null);
		// emf.createEntityManager();
	}

	public void testShutdown()
	{
		// fail(SuperBudget.class.getResource("/log4j.xml").toString());
		// EntityManager em = emf.createEntityManager();
		// assertNotNull(em);

	}

}
