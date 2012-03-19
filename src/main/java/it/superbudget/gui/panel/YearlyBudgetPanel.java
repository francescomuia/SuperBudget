package it.superbudget.gui.panel;

import it.superbudget.gui.BudgetEntryDialog;
import it.superbudget.gui.BudgetPanel;
import it.superbudget.gui.jtable.BigDecimalCellRenderer;
import it.superbudget.gui.jtable.DateCellRenderer;
import it.superbudget.persistence.dao.BudgetEntryDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.persistence.entities.BudgetEntryView;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class YearlyBudgetPanel extends JPanel implements ChangeListener
{

	private JTable tableYearlyIncome;

	private ResourceBundle labels;

	private JTable tableYearlyExpense;

	private static final String BUDGET_PANEL_TABLE_INCOME = "BUDGET.PANEL.TABLE.INCOME";

	private static final String BUDGET_PANEL_TABLE_HEADER_ENTRATE_ANNUALI = "BUDGET.PANEL.TABLE.HEADER.ENTRATE.ANNUALI";

	private static final String BUDGET_PANEL_TABLE_HEADER_SPESE_ANNUALI = "BUDGET.PANEL.TABLE.HEADER.SPESE.ANNUALI";

	private static final String BUDGET_PANEL_TOOLBAR_MODIFICA = "BUDGET.PANEL.TOOLBAR.MODIFICA";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_MODIFICA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.MODIFICA.TOOLTIP.ENABLED";

	private JFrame owner;

	private Budget budget;

	private JSlider slider;

	private List<ChangeListener> yearChangeListeners;

	/**
	 * Create the panel.
	 */
	public YearlyBudgetPanel(JFrame owner, Budget budget)
	{
		yearChangeListeners = new ArrayList<ChangeListener>();
		this.owner = owner;
		this.budget = budget;
		this.labels = ResourcesBundlesUtil.getLabelsBundles();
		this.setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panelSurroundIncome = new JPanel();
		panel.add(panelSurroundIncome);
		panelSurroundIncome.setLayout(new BorderLayout(0, 0));

		JPanel panelIncome = new JPanel();
		panelSurroundIncome.add(panelIncome);
		panelIncome.setBackground(Color.WHITE);

		tableYearlyIncome = new JTable();
		tableYearlyIncome.setDefaultRenderer(Date.class, new DateCellRenderer());
		tableYearlyIncome.setDefaultRenderer(BigDecimal.class, new BigDecimalCellRenderer());
		tableYearlyIncome.setBackground(Color.WHITE);
		tableYearlyIncome.setModel(new DefaultTableModel(new Object[][]
		{}, labels.getString(BUDGET_PANEL_TABLE_INCOME).split(","))
		{
			/**
													 * 
													 */
			private static final long serialVersionUID = 1L;

			Class<?>[] columnTypes = new Class<?>[]
			{ Integer.class, Integer.class, Date.class, Date.class, String.class, String.class, String.class, BigDecimal.class, String.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[]
			{ false, false, false, false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableYearlyIncome.getModel());
		tableYearlyIncome.setRowSorter(sorter);
		tableYearlyIncome.getRowSorter().toggleSortOrder(2);
		tableYearlyIncome.getColumnModel().getColumn(3).setPreferredWidth(150);
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

		tableYearlyExpense = new JTable();
		tableYearlyExpense.setDefaultRenderer(Date.class, new DateCellRenderer());
		tableYearlyExpense.setDefaultRenderer(BigDecimal.class, new BigDecimalCellRenderer());
		tableYearlyExpense.setModel(new DefaultTableModel(new Object[][]
		{}, labels.getString(BUDGET_PANEL_TABLE_INCOME).split(","))
		{
			/**
																									 * 
																									 */
			private static final long serialVersionUID = 1L;

			Class<?>[] columnTypes = new Class<?>[]
			{ Integer.class, Integer.class, Date.class, Date.class, String.class, String.class, String.class, BigDecimal.class, String.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[]
			{ false, false, false, false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
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

	}

	protected void showEditIncomeDialog()
	{
		if (this.tableYearlyIncome.getSelectedRow() != -1)
		{
			BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, this.getSelectedIncome(), true);
			budgetEntryDialog.setVisible(true);
		}
	}

	private BudgetEntry getSelectedIncome()
	{
		int row = tableYearlyIncome.getSelectedRow();
		Long budgetEntryId = new Long(tableYearlyIncome.getModel().getValueAt(row, 1).toString());
		BudgetEntry budgetEntry = new BudgetEntryDao().findBudgetEntry(budgetEntryId);
		return budgetEntry;
	}

	public void populateTableExpense()
	{
		DefaultTableModel model = (DefaultTableModel) this.tableYearlyExpense.getModel();
		model.setRowCount(0);
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntryView> budgets = budgetDao.getExpenses(budget, this.slider.getValue());
		for (BudgetEntryView budgetEntry : budgets)
		{
			model.addRow(new Object[]
			{ budgetEntry.getBudgetEntryViewId(), budgetEntry.getBudgetEntryId(), budgetEntry.getDateFrom(), budgetEntry.getDateTo(),
					budgetEntry.getCategory().getName(), budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory().getName() : null,
					budgetEntry.getNote(), budgetEntry.getValue(), budgetEntry.getRecurrence() });
		}
	}

	public void populateTableIncome()
	{
		DefaultTableModel model = (DefaultTableModel) this.tableYearlyIncome.getModel();
		model.setRowCount(0);
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntryView> budgets = budgetDao.getIncomes(budget, slider.getValue());
		for (BudgetEntryView budgetEntry : budgets)
		{
			model.addRow(new Object[]
			{ budgetEntry.getBudgetEntryViewId(), budgetEntry.getBudgetEntryId(), budgetEntry.getDateFrom(), budgetEntry.getDateTo(),
					budgetEntry.getCategory().getName(), budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory().getName() : null,
					budgetEntry.getNote(), budgetEntry.getValue(), budgetEntry.getRecurrence() });
		}
	}

	public Budget getBudget()
	{
		return budget;
	}

	public void setBudget(Budget budget)
	{
		this.budget = budget;
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
}
