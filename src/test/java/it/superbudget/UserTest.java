package it.superbudget;

import java.io.InputStream;
import java.sql.Connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import org.eclipse.persistence.jpa.JpaEntityManager;

public class UserTest extends TestCase
{
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("SuperBudget-test");

	public void setUp() throws Exception
	{
		InputStream testData = User.class.getResourceAsStream("/user.db.xml");

		JpaEntityManager em = (JpaEntityManager) emf.createEntityManager();
		Connection connection = em.unwrap(java.sql.Connection.class);
		System.out.println("CONNECTION " + connection);
		connection.setAutoCommit(true);
		System.out.println("CONNECTION " + connection.getAutoCommit());
		DbUnitDataLoader loader = new DbUnitDataLoader(testData, connection);

		loader.populateTestData();
	}

	public void testFindAll()
	{
		System.out.println("testFindAll");
		User.setEntityManager(emf.createEntityManager());
		System.out.println("set EntityManager");
		User user = User.find(1);
		System.out.println("user find" + user);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("John Doe", user.getName());
	}
}
