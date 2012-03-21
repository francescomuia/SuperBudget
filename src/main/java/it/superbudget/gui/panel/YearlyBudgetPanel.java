package it.superbudget.gui.panel;

import it.superbudget.gui.BudgetEntryDialog;
import it.superbudget.gui.BudgetPanel;
import it.superbudget.gui.jtable.BigDecimalCellRenderer;
import it.superbudget.gui.jtable.BudgetEntryTableModel;
import it.superbudget.gui.jtable.DateCellRenderer;
import it.superbudget.gui.jtable.YearlyExpensesEntryTable;
import it.superbudget.gui.jtable.YearlyIncomesEntryTable;
import it.superbudget.persistence.dao.BudgetEntryDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.fonts.FontUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class YearlyBudgetPanel extends JPanel implements ChangeListener
{

	private final YearlyIncomesEntryTable tableYearlyIncome;

	private final ResourceBundle labels;

	private final YearlyExpensesEntryTable tableYearlyExpense;

	private static final String BUDGET_PANEL_TABLE_INCOME = "BUDGET.PANEL.TABLE.INCOME";

	private static final String BUDGET_PANEL_TABLE_HEADER_ENTRATE_ANNUALI = "BUDGET.PANEL.TABLE.HEADER.ENTRATE.ANNUALI";

	private static final String BUDGET_PANEL_TABLE_HEADER_SPESE_ANNUALI = "BUDGET.PANEL.TABLE.HEADER.SPESE.ANNUALI";

	private static final String BUDGET_PANEL_TOOLBAR_MODIFICA = "BUDGET.PANEL.TOOLBAR.MODIFICA";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_MODIFICA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.MODIFICA.TOOLTIP.ENABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_DELETE_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.DELETE.TOOLTIP.ENABLED";

	private static final String BUDGET_PANEL_TOOLBAR_DELETE = "BUDGET.PANEL.TOOLBAR.DELETE";

	private final JFrame owner;

	private Budget budget;

	private final JSlider slider;

	private final List<ChangeListener> yearChangeListeners;

	private SummaryYearlyBudgetPanel summaryYearlyBudgetPanel;

	private final JSplitPane splitPane;

	/**
	 * Create the panel.
	 */
	public YearlyBudgetPanel(JFrame owner, Budget budget)
	{
		this.splitPane = new JSplitPane();
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.add(splitPane, BorderLayout.CENTER);
		JPanel surround = new JPanel();
		surround.setLayout(new BoxLayout(surround, BoxLayout.PAGE_AXIS));
		splitPane.setLeftComponent(surround);

		summaryYearlyBudgetPanel = new SummaryYearlyBudgetPanel(budget);
		surround.add(summaryYearlyBudgetPanel);

		yearChangeListeners = new ArrayList<ChangeListener>();
		this.owner = owner;
		this.budget = budget;
		this.labels = ResourcesBundlesUtil.getLabelsBundles();
		// this.setBackground(Color.WHITE);
		// setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		// add(panel);
		panel.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panelSurroundIncome = new JPanel();
		panel.add(panelSurroundIncome);
		panelSurroundIncome.setLayout(new BorderLayout(0, 0));

		JPanel panelIncome = new JPanel();
		panelSurroundIncome.add(panelIncome);
		panelIncome.setBackground(Color.WHITE);

		tableYearlyIncome = new YearlyIncomesEntryTable(new BudgetEntryTableModel(labels.getString(BUDGET_PANEL_TABLE_INCOME).split(",")));
		tableYearlyIncome.setDefaultRenderer(Date.class, new DateCellRenderer());
		tableYearlyIncome.setDefaultRenderer(BigDecimal.class, new BigDecimalCellRenderer());
		tableYearlyIncome.setBackground(Color.WHITE);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableYearlyIncome.getModel());
		tableYearlyIncome.setRowSorter(sorter);
		tableYearlyIncome.getRowSorter().toggleSortOrder(2);
		// tableYearlyIncome.getColumnModel().getColumn(3).setPreferredWidth(150);
		panelIncome.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPaneTableIncome = new JScrollPane(tableYearlyIncome);
		scrollPaneTableIncome.getViewport().setBackground(Color.white);
		panelIncome.add(scrollPaneTableIncome);

		JLabel lblEntrateAnnualiTableHeader = new JLabel(labels.getString(BUDGET_PANEL_TABLE_HEADER_ENTRATE_ANNUALI));
		lblEntrateAnnualiTableHeader.setFont(FontUtils.getFontForLabelInsert());
		lblEntrateAnnualiTableHeader.setForeground(FontUtils.getFontColorForLabelInsert());
		panelIncome.add(lblEntrateAnnualiTableHeader, BorderLayout.NORTH);

		JToolBar toolBarIncomes = new JToolBar();
		panelSurroundIncome.add(toolBarIncomes, BorderLayout.NORTH);
		JButton btnModificaIncome = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_MODIFICA), new ImageIcon(
				BudgetPanel.class.getResource("/images/modify.png")));
		Font font = new Font(btnModificaIncome.getFont().getFontName(), Font.BOLD, btnModificaIncome.getFont().getSize());
		btnModificaIncome.setFont(font);
		btnModificaIncome.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_MODIFICA_TOOLTIP_ENABLED));
		btnModificaIncome.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				showEditIncomeDialog();
			}
		});
		toolBarIncomes.add(btnModificaIncome);
		btnModificaIncome.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_MODIFICA_TOOLTIP_ENABLED));

		JButton btnDeleteIncome = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_DELETE), new ImageIcon(
				YearlyBudgetPanel.class.getResource("/images/delete.png")));
		btnDeleteIncome.setFont(font);
		btnDeleteIncome.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_DELETE_TOOLTIP_ENABLED));
		btnDeleteIncome.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				deleteIncome();
			}
		});
		toolBarIncomes.add(btnDeleteIncome);

		JPanel panelSurroundExpense = new JPanel();
		panel.add(panelSurroundExpense);
		panelSurroundExpense.setLayout(new BorderLayout(0, 0));

		JPanel panelExpenses = new JPanel();
		panelSurroundExpense.add(panelExpenses);
		panelExpenses.setBackground(Color.WHITE);
		panelExpenses.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPaneTableExpense = new JScrollPane();
		scrollPaneTableExpense.getViewport().setBackground(Color.WHITE);
		panelExpenses.add(scrollPaneTableExpense, BorderLayout.CENTER);

		tableYearlyExpense = new YearlyExpensesEntryTable(new BudgetEntryTableModel(labels.getString(BUDGET_PANEL_TABLE_INCOME).split(",")));
		tableYearlyExpense.setDefaultRenderer(Date.class, new DateCellRenderer());
		tableYearlyExpense.setDefaultRenderer(BigDecimal.class, new BigDecimalCellRenderer());

		sorter = new TableRowSorter<TableModel>(tableYearlyExpense.getModel());
		tableYearlyExpense.setRowSorter(sorter);
		tableYearlyExpense.getRowSorter().toggleSortOrder(2);
		scrollPaneTableExpense.setViewportView(tableYearlyExpense);

		JLabel lblSpeseAnnualiTableHeader = new JLabel(labels.getString(BUDGET_PANEL_TABLE_HEADER_SPESE_ANNUALI));
		panelExpenses.add(lblSpeseAnnualiTableHeader, BorderLayout.NORTH);

		lblSpeseAnnualiTableHeader.setFont(FontUtils.getFontForLabelInsert());
		lblSpeseAnnualiTableHeader.setForeground(FontUtils.getFontColorForLabelInsert());

		JToolBar toolBarExpense = new JToolBar();
		panelSurroundExpense.add(toolBarExpense, BorderLayout.NORTH);

		JButton buttonModificaExpense = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_MODIFICA), new ImageIcon(
				BudgetPanel.class.getResource("/images/modify.png")));
		buttonModificaExpense.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showEditExpenseDialog();
			}
		});
		buttonModificaExpense.setFont(font);
		toolBarExpense.add(buttonModificaExpense);

		slider = new JSlider();
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		slider.setMinimum(currentYear - 5);
		slider.setMaximum(currentYear + 5);
		slider.setValue(currentYear);
		slider.addChangeListener(this);
		add(slider, BorderLayout.NORTH);
		this.addYearChangeListener(summaryYearlyBudgetPanel);

	}

	protected void deleteIncome()
	{
		if (this.tableYearlyIncome.getSelectedBudgetEntry() != null)
		{
			BudgetEntry income = this.tableYearlyIncome.getSelectedBudgetEntry();
			BudgetEntryDao budgetEntryDao = new BudgetEntryDao();
			if (budgetEntryDao.existBudgetEntry(income))
			{
				int choose = JOptionPane.showConfirmDialog(this, "Conferma elimizione ?", "Super Budget : Conferma elimizione",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (JOptionPane.YES_OPTION == choose)
				{
					budgetEntryDao.delete(income);
					this.populateTableIncome();
					this.summaryYearlyBudgetPanel.calculate();
					this.resetToPreferredSizes();
				}
			}
			else
			{
				int choose = JOptionPane.showOptionDialog(this,
						"<html>L'entrata selezionata è stata generata da una ricorrezza.<br/> Come si vuole procedere ?</html>",
						"Super Budget : Conferma elimizione", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
						UIManager.getIcon("OptionPane.warningIcon"), new String[]
						{ "Elimina solo la riga", "Elimina tutta la ricorrenza" }, "Elimina tutta la ricorrenza");
				switch (choose)
				{
				case 0:
					BudgetEntry dad = budgetEntryDao.findBudgetEntry(income.getBudgetEntryId());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(income.getDateFrom());
					if (dad.getDateTo() != null)
					{
						Calendar dateTo = Calendar.getInstance();
						dateTo.setTime(dad.getDateTo());
						if (dateTo.after(calendar))
						{
							calendar.add(income.getRecurrence().getCalendarField(), -income.getRecurrence().getCalendarIncrement());
							Date oldDadDateTo = dad.getDateTo();
							dad.setDateTo(calendar.getTime());
							budgetEntryDao.save(dad);
							calendar.add(income.getRecurrence().getCalendarField(), income.getRecurrence().getCalendarIncrement());
							calendar.add(income.getRecurrence().getCalendarField(), income.getRecurrence().getCalendarIncrement());
							dad.setDateFrom(calendar.getTime());
							dad.setDateTo(oldDadDateTo);
							dad.setBudgetEntryId(null);
							budgetEntryDao.save(dad);
						}
						else
						{
							calendar.add(income.getRecurrence().getCalendarField(), -income.getRecurrence().getCalendarIncrement());
							dad.setDateTo(calendar.getTime());
							budgetEntryDao.save(dad);
						}
						this.populateTableIncome();
						this.summaryYearlyBudgetPanel.calculate();
						this.resetToPreferredSizes();
					}
					else
					{
						calendar.add(income.getRecurrence().getCalendarField(), -income.getRecurrence().getCalendarIncrement());
						Date oldDadDateTo = dad.getDateTo();
						dad.setDateTo(calendar.getTime());
						budgetEntryDao.save(dad);
						calendar.add(income.getRecurrence().getCalendarField(), income.getRecurrence().getCalendarIncrement());
						calendar.add(income.getRecurrence().getCalendarField(), income.getRecurrence().getCalendarIncrement());
						dad.setDateFrom(calendar.getTime());
						dad.setDateTo(oldDadDateTo);
						dad.setBudgetEntryId(null);
						budgetEntryDao.save(dad);
						this.populateTableIncome();
						this.summaryYearlyBudgetPanel.calculate();
						this.resetToPreferredSizes();
					}
					break;
				case 1:
					budgetEntryDao.delete(income);
					this.populateTableIncome();
					this.summaryYearlyBudgetPanel.calculate();
					this.resetToPreferredSizes();
					break;
				}
			}
		}

	}

	protected void showEditIncomeDialog()
	{
		if (this.tableYearlyIncome.getSelectedBudgetEntry() != null)
		{
			BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, tableYearlyIncome.getSelectedBudgetEntry(), true);
			budgetEntryDialog.setVisible(true);
			if (budgetEntryDialog.isUpdateViews())
			{
				this.populateTableIncome();
				this.summaryYearlyBudgetPanel.calculate();
				this.resetToPreferredSizes();
			}
		}
	}

	protected void showEditExpenseDialog()
	{
		if (this.tableYearlyExpense.getSelectedBudgetEntry() != null)
		{
			BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, tableYearlyExpense.getSelectedBudgetEntry(), false);
			budgetEntryDialog.setVisible(true);
			if (budgetEntryDialog.isUpdateViews())
			{
				this.populateTableExpense();
				this.summaryYearlyBudgetPanel.calculate();
				this.resetToPreferredSizes();
			}
		}
	}

	public void populateTableExpense()
	{
		this.tableYearlyExpense.populate(budget, this.slider.getValue());
	}

	public void populateTableIncome()
	{
		this.tableYearlyIncome.populate(budget, this.slider.getValue());
	}

	public Budget getBudget()
	{
		return budget;
	}

	public void setBudget(Budget budget)
	{
		this.budget = budget;
		this.summaryYearlyBudgetPanel.setBudget(budget);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting())
		{
			int value = source.getValue();
			if (value == source.getMaximum() || value == source.getMinimum())
			{
				source.setMinimum(value - 5);
				source.setMaximum(value + 5);
			}
			this.populateTableExpense();
			this.populateTableIncome();
			for (ChangeListener changeListener : this.yearChangeListeners)
			{
				changeListener.stateChanged(e);
			}
		}

	}

	public void addYearChangeListener(ChangeListener changeListener)
	{
		this.yearChangeListeners.add(changeListener);
	}

	public void resetToPreferredSizes()
	{
		this.splitPane.resetToPreferredSizes();
		this.summaryYearlyBudgetPanel.resetToPreferredSizes();
	}

	public SummaryYearlyBudgetPanel getSummaryYearlyBudgetPanel()
	{
		return summaryYearlyBudgetPanel;
	}

	public void setSummaryYearlyBudgetPanel(SummaryYearlyBudgetPanel summaryYearlyBudgetPanel)
	{
		this.summaryYearlyBudgetPanel = summaryYearlyBudgetPanel;
	}
}
