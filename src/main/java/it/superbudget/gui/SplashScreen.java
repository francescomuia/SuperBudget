package it.superbudget.gui;

import it.superbudget.SuperBudget;
import it.superbudget.persistence.PersistenceManager;
import it.superbudget.util.messages.MessagesUtils;

import java.awt.BorderLayout;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class SplashScreen extends JDialog
{
	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar;

	private JLabel lblProgress;

	private SwingWorker<Void, SimpleEntry<String, Integer>> worker;

	public SplashScreen()
	{
		setUndecorated(true);
		setModal(false);
		initializeComponents();
		pack();
		this.setLocationRelativeTo(null);
		worker = new SplashScreenTask();
	}

	private void initializeComponents()
	{
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(SplashScreen.class.getResource("/images/logo.png")));
		getContentPane().add(label, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		lblProgress = new JLabel(" ");
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblProgress, BorderLayout.NORTH);

		progressBar = new JProgressBar();
		panel.add(progressBar, BorderLayout.CENTER);
	}

	class SplashScreenTask extends SwingWorker<Void, SimpleEntry<String, Integer>>
	{
		private SuperBudgetApp application;

		public void enableLoggingSystem()
		{
			DOMConfigurator.configure(SuperBudget.class.getResource("/log4j.xml"));
			Logger logger = Logger.getLogger(SuperBudget.class);
			logger.info("Logging started");
		}

		public void startPersistenceContext()
		{
			PersistenceManager.getInstance().getEntityManager();
		}

		public void anagraficsCheck()
		{
			// InputStream stream = SplashScreen.class.getResourceAsStream("/db/recurrence.db.xml");
			// EntityManager em = PersistenceManager.getInstance().getEntityManager();
			// try
			// {
			// em.getTransaction().begin();
			// Connection connection = em.unwrap(Connection.class);
			// DbUnitDataLoader loader = new DbUnitDataLoader(stream, connection);
			// loader.populateData();
			// connection.commit();
			// em.getTransaction().commit();
			// }
			// catch (Exception e)
			// {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// stream = SplashScreen.class.getResourceAsStream("/db/category.db.xml");
			// em = PersistenceManager.getInstance().getEntityManager();
			//
			// try
			// {
			// em.getTransaction().begin();
			// Connection connection = em.unwrap(Connection.class);
			// DbUnitDataLoader loader = new DbUnitDataLoader(stream, connection);
			// loader.populateData();
			// connection.commit();
			// em.getTransaction().commit();
			// }
			// catch (Exception e)
			// {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground() throws Exception
		{
			// First Enable logger
			try
			{
				publish(new SimpleEntry<String, Integer>("Starting Logging System...", new Integer(0)));
				this.enableLoggingSystem();
				publish(new SimpleEntry<String, Integer>("Logging System Started", new Integer(10)));
				publish(new SimpleEntry<String, Integer>("Starting Persistence Context...", new Integer(10)));
				startPersistenceContext();
				publish(new SimpleEntry<String, Integer>("Persistence Context Started", new Integer(30)));

				publish(new SimpleEntry<String, Integer>("Initialize Anagrafics", new Integer(50)));
				this.anagraficsCheck();
				publish(new SimpleEntry<String, Integer>("Anagrafics Created", new Integer(70)));
				publish(new SimpleEntry<String, Integer>("Initialize Application", new Integer(70)));
				application = new SuperBudgetApp();
				publish(new SimpleEntry<String, Integer>("Application Initialized", new Integer(100)));
			}
			catch (Exception e)
			{
				Logger logger = Logger.getLogger(SplashScreen.class);
				logger.error(e);
				MessagesUtils.showExceptionMessage(e);
				throw e;
			}
			return null;
		}

		@Override
		protected void process(List<SimpleEntry<String, Integer>> chunks)
		{
			for (SimpleEntry<String, Integer> simpleEntry : chunks)
			{
				getLblProgress().setText(simpleEntry.getKey());
				getProgressBar().setValue(simpleEntry.getValue());
			}
		}

		@Override
		protected void done()
		{
			super.done();
			dispose();
			if (application != null)
			{
				SwingUtilities.invokeLater(new Runnable()
				{

					public void run()
					{

						application.display();
					}
				});
			}
			else
			{
				System.exit(-1);
			}

		}
	}

	public JProgressBar getProgressBar()
	{
		return progressBar;
	}

	public JLabel getLblProgress()
	{
		return lblProgress;
	}

	public void start()
	{
		worker.execute();
	}
}
