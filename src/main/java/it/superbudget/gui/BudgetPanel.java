package it.superbudget.gui;

import it.superbudget.gui.panel.SummaryYearlyBudgetPanel;
import it.superbudget.gui.panel.YearlyBudgetPanel;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.util.bundles.ResourcesBundlesUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

public class BudgetPanel extends JPanel
{

	/**
	 * =Budget : =Entrate Annuali : =Spese Annuali :
	 */
	private static final long serialVersionUID = 1L;

	private static final String BUDGET_PANEL_TOOLBAR_NUOVA_ENTRATA = "BUDGET.PANEL.TOOLBAR.NUOVA.ENTRATA";

	private static final String BUDGET_PANEL_TOOLBAR_NUOVA_SPESA = "BUDGET.PANEL.TOOLBAR.NUOVA.SPESA";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_SPESA_TOOLTIP_DISABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.SPESA.TOOLTIP.DISABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_ENTRATA_TOOLTIP_DISABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.ENTRATA.TOOLTIP.DISABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_SPESA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.SPESA.TOOLTIP.ENABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_ENTRATA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.ENTRATA.TOOLTIP.ENABLED";

	private Budget budget;

	private ResourceBundle labels;

	private Logger logger = Logger.getLogger(BudgetPanel.class);

	private JSplitPane splitPane;

	private JFrame owner;

	private JButton btnNewButton;

	private JButton btnNuovaSpesa;

	private YearlyBudgetPanel yearlyBudgetPanel;

	private SummaryYearlyBudgetPanel summaryYearlyBudgetPanel;

	/**
	 * Create the panel.
	 */
	public BudgetPanel(JFrame owner, Budget budget)
	{
		this.owner = owner;
		setBackground(UIManager.getColor("Panel.background"));
		this.budget = budget;
		this.labels = ResourcesBundlesUtil.getLabelsBundles();
		setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setBackground(Color.WHITE);
		add(splitPane, BorderLayout.CENTER);

		JPanel surround = new JPanel();
		surround.setLayout(new BoxLayout(surround, BoxLayout.PAGE_AXIS));
		splitPane.setLeftComponent(surround);

		summaryYearlyBudgetPanel = new SummaryYearlyBudgetPanel(budget);
		surround.add(summaryYearlyBudgetPanel);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		splitPane.setRightComponent(tabbedPane);
		yearlyBudgetPanel = new YearlyBudgetPanel(owner, budget);
		yearlyBudgetPanel.addYearChangeListener(summaryYearlyBudgetPanel);
		tabbedPane.addTab("Anno", null, yearlyBudgetPanel, null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel, null);
		panel.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);

		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);

		btnNewButton = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_NUOVA_ENTRATA), new ImageIcon(
				BudgetPanel.class.getResource("/images/euro.png")));
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showNewIcomesDialog();
			}
		});
		Font font = new Font(btnNewButton.getFont().getFontName(), Font.BOLD, btnNewButton.getFont().getSize());
		btnNewButton.setFont(font);
		toolBar.add(btnNewButton);

		btnNuovaSpesa = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_NUOVA_SPESA), new ImageIcon(
				BudgetPanel.class.getResource("/images/expense.png")));
		btnNuovaSpesa.setFont(font);
		btnNuovaSpesa.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				showNewExpenseDialog();
			}
		});
		toolBar.add(btnNuovaSpesa);

		if (this.budget != null)
		{
			summaryYearlyBudgetPanel.calculate();
			yearlyBudgetPanel.populateTableIncome();
			yearlyBudgetPanel.populateTableExpense();
		}
		this.checkToolbarButton();
	}

	private void checkToolbarButton()
	{
		if (budget != null)
		{
			btnNewButton.setEnabled(true);
			btnNewButton.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_ENTRATA_TOOLTIP_ENABLED));
			btnNuovaSpesa.setEnabled(true);
			btnNuovaSpesa.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_SPESA_TOOLTIP_ENABLED));
		}
		else
		{
			btnNewButton.setEnabled(false);
			btnNewButton.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_ENTRATA_TOOLTIP_DISABLED));
			btnNuovaSpesa.setEnabled(false);
			btnNuovaSpesa.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_SPESA_TOOLTIP_DISABLED));
		}
	}

	protected void showNewIcomesDialog()
	{
		BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, null, true);
		budgetEntryDialog.setVisible(true);
		if (budgetEntryDialog.isUpdateViews())
		{
			yearlyBudgetPanel.populateTableIncome();
			summaryYearlyBudgetPanel.calculate();
			this.getSplitPane().resetToPreferredSizes();
			this.summaryYearlyBudgetPanel.resetToPreferredSizes();
		}
	}

	protected void showNewExpenseDialog()
	{
		BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, null, false);
		budgetEntryDialog.setVisible(true);
		if (budgetEntryDialog.isUpdateViews())
		{
			yearlyBudgetPanel.populateTableExpense();
			summaryYearlyBudgetPanel.calculate();
			this.getSplitPane().resetToPreferredSizes();
			this.summaryYearlyBudgetPanel.resetToPreferredSizes();
		}
	}

	public void budgetChanged(Budget newBudget)
	{
		this.budget = newBudget;
		yearlyBudgetPanel.setBudget(newBudget);
		summaryYearlyBudgetPanel.setBudget(newBudget);
		if (this.budget != null)
		{
			summaryYearlyBudgetPanel.calculate();
			yearlyBudgetPanel.populateTableIncome();
			yearlyBudgetPanel.populateTableExpense();
			this.checkToolbarButton();
			this.getSplitPane().resetToPreferredSizes();
			this.summaryYearlyBudgetPanel.resetToPreferredSizes();
		}
	}

	public JSplitPane getSplitPane()
	{
		return splitPane;
	}

}
