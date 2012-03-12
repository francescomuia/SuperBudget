package it.superbudget.gui;

import it.superbudget.persistence.dao.BudgetDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.util.bundles.ResourcesBundlesUtil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.apache.log4j.Logger;

public class SuperBudgetApp
{
	private final Logger logger = Logger.getLogger(SuperBudgetApp.class);

	private static final String FRAME_TITLE_PROPERTY = "SUPERBUDGETAPP.FRAME.TITLE";

	private static final String NAME_MENU_BUDGET_PROPERTY = "SUPERBUDGETAPP.MENU.BUDGET";

	private static final String NAME_MENU_BUDGET_NUOVO_PROPERTY = "SUPERBUDGETAPP.MENU.BUDGET.NUOVO";

	private static final String NAME_MENU_BUDGET_MODIFICA_PROPERTY = "SUPERBUDGETAPP.MENU.BUDGET.MODIFICA";

	private static final String NAME_MENU_BUDGET_DISPLAY_PROPERTY = "SUPERBUDGETAPP.MENU.BUDGET.DISPLAY";

	private static final String NAME_MENU_REPORT_PROPERTY = "SUPERBUDGETAPP.MENU.REPORTS";

	private static final String IMAGE_MENU_BUDGET_ICON = "/images/budget.png";

	private static final String IMAGE_MENU_BUDGET_NUOVO_ICON = "/images/nuovo.png";

	private static final String IMAGE_MENU_BUDGET_MODIFICA_ICON = "/images/modify.png";

	private static final String IMAGE_MENU_BUDGET_DISPLAY_ICON = "/images/visualizza.png";

	private JFrame frame;

	private final ButtonGroup budgetButtonGroup = new ButtonGroup();

	private JMenu mnAvaiable;

	private Budget selectedBudget;

	private BudgetPanel budgetPanel;

	/**
	 * Create the application.
	 */
	public SuperBudgetApp()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		ResourceBundle labels = ResourcesBundlesUtil.getLabelsBundles();
		String title = labels.getString(FRAME_TITLE_PROPERTY) + " - " + ResourcesBundlesUtil.getApplicationVersion();
		frame = new JFrame(title);
		try
		{
			frame.setIconImage(ImageIO.read(SuperBudgetApp.class.getResourceAsStream("/images/logo.png")));
		}
		catch (IOException e1)
		{
			logger.error("Could not load icon image for frame", e1);

		}
		frame.setSize(1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnBudget = new JMenu(labels.getString(NAME_MENU_BUDGET_PROPERTY));
		mnBudget.setIcon(new ImageIcon(SuperBudgetApp.class.getResource(IMAGE_MENU_BUDGET_ICON)));
		menuBar.add(mnBudget);

		JMenuItem mntmNuovo = new JMenuItem(labels.getString(NAME_MENU_BUDGET_NUOVO_PROPERTY));
		mntmNuovo.setIcon(new ImageIcon(SuperBudgetApp.class.getResource(IMAGE_MENU_BUDGET_NUOVO_ICON)));
		mntmNuovo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showNewBudgetDialog(false);
			}
		});

		mnAvaiable = new JMenu(labels.getString(NAME_MENU_BUDGET_PROPERTY));
		mnBudget.add(mnAvaiable);

		this.createBudgetMenu();
		budgetPanel = new BudgetPanel(this.frame, getSelectedBudget());
		this.frame.add(budgetPanel);
		mnBudget.add(mntmNuovo);

		JMenuItem mntmModifica = new JMenuItem(labels.getString(NAME_MENU_BUDGET_MODIFICA_PROPERTY));
		mntmModifica.setIcon(new ImageIcon(SuperBudgetApp.class.getResource(IMAGE_MENU_BUDGET_MODIFICA_ICON)));
		mntmModifica.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				showNewBudgetDialog(true);
			}
		});
		mnBudget.add(mntmModifica);

		JMenuItem mntmVisualizza = new JMenuItem(labels.getString(NAME_MENU_BUDGET_DISPLAY_PROPERTY));
		mntmVisualizza.setIcon(new ImageIcon(SuperBudgetApp.class.getResource(IMAGE_MENU_BUDGET_DISPLAY_ICON)));
		mntmVisualizza.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				showCurrentBudget();
			}
		});
		mnBudget.add(mntmVisualizza);
		JMenu mnReport = new JMenu(labels.getString(NAME_MENU_REPORT_PROPERTY));
		menuBar.add(mnReport);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		frame.setLocation(x, y);
	}

	protected void showCurrentBudget()
	{

	}

	private void createBudgetMenu()
	{
		mnAvaiable.removeAll();
		budgetButtonGroup.clearSelection();
		Enumeration<AbstractButton> enums = budgetButtonGroup.getElements();
		while (enums.hasMoreElements())
		{
			AbstractButton abstractButton = enums.nextElement();
			budgetButtonGroup.remove(abstractButton);
		}
		List<Budget> budgets = new BudgetDao().findAll();
		for (Budget budget : budgets)
		{
			BudgetItemsAction action = new BudgetItemsAction(budget.getName(), budget);
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
			item.setSelected(budget.isDefaultBudget());
			if (budget.isDefaultBudget())
			{
				this.selectedBudget = budget;
			}
			budgetButtonGroup.add(item);
			mnAvaiable.add(item);
		}
	}

	protected void showNewBudgetDialog(boolean modify)
	{
		JDialog budgetDialog = null;
		if (modify)
		{
			budgetDialog = new BudgetDialog(this.frame, this.getSelectedBudget());
		}
		else
		{

			budgetDialog = new BudgetDialog(this.frame, this.budgetButtonGroup.getButtonCount() > 0);
		}
		budgetDialog.setVisible(true);
		this.createBudgetMenu();
		this.budgetPanel.budgetChanged(getSelectedBudget());
	}

	/**
	 * Metodo che intercetta l'azione di selezione di un nuovo budget
	 * 
	 * @param budget
	 */
	public void budgetSelected(Budget budget)
	{
		this.setSelectedBudget(budget);
	}

	public void display()
	{
		this.frame.setVisible(true);
	}

	public Budget getSelectedBudget()
	{
		return selectedBudget;
	}

	public void setSelectedBudget(Budget selectedBudget)
	{
		this.selectedBudget = selectedBudget;
	}

	class BudgetItemsAction extends AbstractAction
	{

		private static final long serialVersionUID = 1L;

		private Budget budget;

		public BudgetItemsAction(String name, Budget budget)
		{
			super(name);
			this.budget = budget;
		}

		public void actionPerformed(ActionEvent e)
		{
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
			if (item.isSelected())
			{
				budgetSelected(budget);
			}
		}

	}

}
