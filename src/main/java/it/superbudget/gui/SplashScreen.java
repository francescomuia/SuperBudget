package it.superbudget.gui;

import it.superbudget.ApplicationZipFile;
import it.superbudget.GitHubVersionChecker;
import it.superbudget.ShutdownHook;
import it.superbudget.SuperBudget;
import it.superbudget.exceptions.DownloadException;
import it.superbudget.persistence.PersistenceManager;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.jars.JarUtils;
import it.superbudget.util.messages.MessagesUtils;
import it.superbudget.util.net.InternetConnectionUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

	private JPanel panelLogo;

	private JLabel lblVersion;

	private static String GIT_HUB_DOWNLOAD_APP_URL = "http://github.com/api/v2/json/blob/show/francescomuia/SuperBudget/";

	private static final String SUPER_BUDGET_UPDATER_JAR_NAME = "SuperBudgetUpdater";

	private JPanel panelMessages;

	private JLabel lblMessage;

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

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		panel.add(progressBar, BorderLayout.CENTER);

		panelMessages = new JPanel();
		panel.add(panelMessages, BorderLayout.NORTH);
		panelMessages.setLayout(new GridLayout(0, 1, 0, 0));

		lblProgress = new JLabel("");
		panelMessages.add(lblProgress);
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);

		lblMessage = new JLabel("");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		panelMessages.add(lblMessage);

		panelLogo = new JPanel();
		getContentPane().add(panelLogo, BorderLayout.CENTER);
		panelLogo.setLayout(new BorderLayout(0, 0));
		JLabel label = new JLabel();

		panelLogo.add(label, BorderLayout.CENTER);
		label.setIcon(new ImageIcon(SplashScreen.class.getResource("/images/logo.png")));

		lblVersion = new JLabel("VER. " + ResourcesBundlesUtil.getApplicationVersion());
		lblVersion.setVerticalAlignment(SwingConstants.BOTTOM);
		lblVersion.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblVersion.setForeground(new Color(2, 151, 236));
		lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLogo.add(lblVersion, BorderLayout.SOUTH);
	}

	class SplashScreenTask extends SwingWorker<Void, SimpleEntry<String, Integer>>
	{
		private SuperBudgetApp application;

		private Logger logger = Logger.getLogger(SplashScreenTask.class);

		public void enableLoggingSystem()
		{
			DOMConfigurator.configure(SuperBudget.class.getResource("/log4j.xml"));
			Logger logger = Logger.getLogger(SuperBudget.class);
			logger.info("Logging started");
		}

		@SuppressWarnings("unchecked")
		public void dowloadAppFromUrl(String fAddress, File localFileName, String destinationDir) throws DownloadException
		{
			OutputStream outStream = null;
			URLConnection uCon = null;

			InputStream is = null;
			try
			{
				URL Url;
				byte[] buf;
				int ByteRead, ByteWritten = 0;
				Url = new URL(fAddress);
				outStream = new BufferedOutputStream(new FileOutputStream(localFileName));
				int tempFileSize = getProgressBar().getMaximum();
				double effectiveSizeMb = tempFileSize / 1024.00;
				double mbDowloaded = 0.0;
				effectiveSizeMb = effectiveSizeMb / 1024.00;
				uCon = Url.openConnection();
				is = uCon.getInputStream();
				buf = new byte[1024];
				while ((ByteRead = is.read(buf)) != -1)
				{
					outStream.write(buf, 0, ByteRead);
					ByteWritten += ByteRead;
					mbDowloaded = ByteWritten / 1024.00;
					mbDowloaded = mbDowloaded / 1024.00;
					NumberFormat numberFormat = NumberFormat.getInstance();
					numberFormat.setMaximumFractionDigits(2);
					publish(new SimpleEntry<String, Integer>("Dowloaded " + numberFormat.format(mbDowloaded) + " of "
							+ numberFormat.format(effectiveSizeMb), ByteWritten));
				}
			}
			catch (Exception e)
			{
				throw new DownloadException("Si è verificato un errore durante il download", e);
			}
			finally
			{
				try
				{
					is.close();
					outStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		@SuppressWarnings("unchecked")
		public void checkApplicationVersion()
		{

			try
			{
				this.moveOldApplicationJar();
			}
			catch (UnsupportedEncodingException e2)
			{
				logger.error("Errore durante lo spostamento del vecchio application jar", e2);
			}
			if (InternetConnectionUtils.isInternetReachable("www.google.it"))
			{
				boolean isUpToDate = false;
				try
				{
					// isUpToDate = GitHubVersionChecker.isUpToDate(ResourcesBundlesUtil.getApplicationVersion());
					isUpToDate = false;
					if (!isUpToDate)
					{
						int choose = JOptionPane.showConfirmDialog(rootPane,
								"<html>Esiste una nuova versione dell'applicativo.<br/>Procedere con l'aggiornamento ? </html>", "Super Budget",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (choose == JOptionPane.YES_OPTION)
						{
							try
							{
								ApplicationZipFile applicationZipFile = GitHubVersionChecker.getLatestZip();
								publish(new SimpleEntry<String, Integer>("Dowload Application", new Integer(0)));
								getProgressBar().setMaximum(applicationZipFile.getBlob().getSize());
								String applicationFileName = applicationZipFile.getBlob().getName();
								File tempFile = File.createTempFile(applicationFileName, ".zip");
								// File tempFile = new File("C:\\Users\\muia\\AppData\\Local\\Temp/SuperBudget-0.2-SNAPSHOT3722066682505420876.zip");
								dowloadAppFromUrl(GIT_HUB_DOWNLOAD_APP_URL + applicationZipFile.getBlob().getSha(), tempFile, tempFile.getParent());
								String path = SplashScreenTask.class.getProtectionDomain().getCodeSource().getLocation().getPath();
								String decodedPath = URLDecoder.decode(path, "UTF-8");
								File applicationDir = new File(decodedPath).getParentFile();
								ZipFile applicationZip = new ZipFile(tempFile);
								publish(new SimpleEntry<String, Integer>("Extract Application file", new Integer(0)));
								getProgressBar().setMaximum(applicationZip.size());
								Enumeration<? extends ZipEntry> enumeration = applicationZip.entries();
								int counter = 1;
								while (enumeration.hasMoreElements())
								{
									File entryFile = null;
									ZipEntry entry = enumeration.nextElement();
									if (!entry.isDirectory())
									{
										String name = entry.getName();
										String fileName = name.substring(name.lastIndexOf("/") + 1);
										int firstIndex = name.indexOf("/");
										int secondIndex = name.lastIndexOf("/");
										if (firstIndex != secondIndex)
										{
											String dir = name.substring(firstIndex, secondIndex);
											entryFile = new File(applicationDir, dir + File.separator + fileName);
										}
										else
										{
											entryFile = new File(applicationDir, File.separator + fileName);
										}
										String publishString = "<html>Extract file " + fileName + " N° " + counter + " of " + applicationZip.size();
										publish(new SimpleEntry<String, Integer>(publishString + "</html>", new Integer(counter)));
										counter++;
										if (!entryFile.exists() || !entryFile.getName().equals(fileName))
										{
											byte[] buf;
											int ByteRead, ByteWritten = 0;
											BufferedOutputStream outStream = null;
											InputStream is = null;
											try
											{
												outStream = new BufferedOutputStream(new FileOutputStream(entryFile));
												double mbDowloaded = 0.0;
												is = applicationZip.getInputStream(entry);
												buf = new byte[16 * 1024];
												while ((ByteRead = is.read(buf)) != -1)
												{
													outStream.write(buf, 0, ByteRead);
													ByteWritten += ByteRead;
													mbDowloaded = ByteWritten / 1024.00;
													NumberFormat numberFormat = NumberFormat.getInstance();
													numberFormat.setMaximumFractionDigits(2);
													getLblMessage().setText("Extracted " + numberFormat.format(mbDowloaded));
													// publishString += "<br/><center> Extracted " + numberFormat.format(mbDowloaded) +
													// "</center></html>";
													// System.out.println("Extracted " + numberFormat.format(mbDowloaded));
													// publish(new SimpleEntry<String, Integer>(publishString, counter));
												}
											}
											catch (IOException e)
											{
												e.printStackTrace();
											}
											finally
											{
												if (outStream != null)
												{
													try
													{
														outStream.flush();
														outStream.close();
													}
													catch (IOException e1)
													{
														e1.printStackTrace();
													}
												}
												if (is != null)
												{
													is.close();
												}
											}
										}
									}
									ShutdownHook.restartApp = true;
									JOptionPane.showMessageDialog(rootPane, "Aggiornamento avvenuto l'applicazione verrà riavviata");
									System.exit(1);
								}
							}
							catch (RuntimeException e)
							{
								logger.error("Impossibile ottere file zip", e);
								MessagesUtils.showExceptionMessage(new Exception("Impossibile ottere file dell'applicazione", e));
							}
							catch (IOException e)
							{
								logger.error("Errore durante la crezione del file temporaneo", e);
								MessagesUtils.showExceptionMessage(new Exception("Errore durante la crezione del file temporaneo", e));
							}
							catch (DownloadException e)
							{
								logger.error(e.getMessage(), e);
								MessagesUtils.showExceptionMessage(new Exception(e.getMessage(), e));
							}

						}
					}
				}
				catch (RuntimeException e)
				{

					logger.error("Tentativo di controllo versione fallito", e);
				}
			}
			else
			{
				logger.info("Nessuna connessione");
			}

		}

		private void moveOldApplicationJar() throws UnsupportedEncodingException
		{
			String path = SplashScreenTask.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			File dir = new File(decodedPath).getParentFile();
			String currentFile = JarUtils.getLatestApplicationJarVersion(dir);
			JarUtils.moveOldApplicationJarVersion(dir.listFiles(), currentFile, dir);
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
				publish(new SimpleEntry<String, Integer>("Check application Version", new Integer(10)));
				checkApplicationVersion();

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

	public JLabel getLblMessage()
	{
		return lblMessage;
	}
}
