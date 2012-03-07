package it.superbudget;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che spegne il database
 * 
 * @author Francesco Muià
 * 
 */
public class ShutdownHook extends Thread
{

	@Override
	public void run()
	{
		try
		{
			System.out.println("DERBY IS SHUTDOWN");
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		}
		catch (SQLException e)
		{
			// e.printStackTrace();
		}
	}

}
