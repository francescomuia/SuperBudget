package it.superbudget.gui;

import it.superbudget.gui.panel.MontlyBudgetPanel;
import it.superbudget.gui.panel.YearlyBudgetPanel;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.util.bundles.ResourcesBundlesUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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

	private final ResourceBundle labels;

	private final Logger logger = Logger.getLogger(BudgetPanel.class);

	// private JSplitPane splitPane;

	private final JFrame owner;

	private final JButton btnNewButton;

	private final JButton btnNuovaSpesa;

	private final YearlyBudgetPanel yearlyBudgetPanel;

	private final MontlyBudgetPanel montlyBudgetPanel;

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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		this.add(tabbedPane);
		yearlyBudgetPanel = new YearlyBudgetPanel(owner, budget);

		tabbedPane.addTab("Vis. Annuale", null, yearlyBudgetPanel, null);

		this.montlyBudgetPanel = new MontlyBudgetPanel(owner, budget);
		tabbedPane.addTab("Vis. Mensile", null, montlyBudgetPanel, null);
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);

		btnNewButton = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_NUOVA_ENTRATA), new ImageIcon(
				BudgetPanel.class.getResource("/images/euro.png")));
		btnNewButton.addActionListener(new ActionListener()
		{
			@Override
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
			this.updateTabs();
		}
		this.checkToolbarButton();
	}

	private void updateTabs()
	{
		this.yearlyBudgetPanel.getSummaryYearlyBudgetPanel().calculate();
		yearlyBudgetPanel.populateTableIncome();
		yearlyBudgetPanel.populateTableExpense();

		this.montlyBudgetPanel.getSummaryMontlyBudgetPanel().calculate();
		montlyBudgetPanel.populateTableIncome();
		montlyBudgetPanel.populateTableExpense();
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
			this.updateTabs();
		}
	}

	protected void showNewExpenseDialog()
	{
		BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, null, false);
		budgetEntryDialog.setVisible(true);
		if (budgetEntryDialog.isUpdateViews())
		{
			this.updateTabs();
		}
	}

	public void budgetChanged(Budget newBudget)
	{
		this.budget = newBudget;
		yearlyBudgetPanel.setBudget(newBudget);
		if (this.budget != null)
		{
			this.updateTabs();
			this.checkToolbarButton();
			this.yearlyBudgetPanel.resetToPreferredSizes();
			this.montlyBudgetPanel.resetToPreferredSizes();
		}
	}
}
