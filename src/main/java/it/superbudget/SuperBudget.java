package it.superbudget;

import it.superbudget.gui.SplashScreen;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class for the application
 * 
 * @author Francesco Muià
 * 
 */
public class SuperBudget
{

	public static void main(String[] args) throws Exception
	{
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		SwingUtilities.invokeLater(new Runnable()
		{

			public void run()
			{
				SplashScreen splashScreen = new SplashScreen();
				splashScreen.setVisible(true);
				splashScreen.start();

			}
		});

	}
}
