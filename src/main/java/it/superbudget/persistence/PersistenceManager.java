package it.superbudget.persistence;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PersistenceManager
{
	private static PersistenceManager instance;

	private final EntityManagerFactory emf;

	public final static String PERSISTENCE_UNIT_NAME = "SuperBudget";

	private static final String DATABASE_DRIVER_KEY = "javax.persistence.jdbc.driver";

	private static final String DATABASE_URL_KEY = "javax.persistence.jdbc.url";

	private static final String DATABASE_DRIVER_DEVELOPMENT = "org.apache.derby.jdbc.ClientDriver";

	private static final String DATABASE_URL_DEVELOPMENT = "jdbc:derby://localhost:1527/C:/Users/muia/workspace/programmi/superbudget/database";

	private PersistenceManager()
	{
		Properties properties = new Properties();
		try
		{
			properties.load(PersistenceManager.class.getResourceAsStream("/META-INF/connection.properties"));
		}
		catch (IOException e)
		{
			throw new RuntimeException("connection.properties not found");
		}
		if (properties.get(DATABASE_DRIVER_KEY).toString().startsWith("${"))
		{
			properties.setProperty(DATABASE_DRIVER_KEY, DATABASE_DRIVER_DEVELOPMENT);
		}
		if (properties.get(DATABASE_URL_KEY).toString().startsWith("${"))
		{
			properties.setProperty(DATABASE_URL_KEY, DATABASE_URL_DEVELOPMENT);
		}
		emf = new org.eclipse.persistence.jpa.PersistenceProvider().createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
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
