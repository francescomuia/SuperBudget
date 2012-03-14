package it.superbudget.gui;

import it.superbudget.gui.jtable.BigDecimalCellRenderer;
import it.superbudget.gui.jtable.DateCellRenderer;
import it.superbudget.persistence.dao.BudgetDao;
import it.superbudget.persistence.dao.BudgetEntryDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.persistence.entities.BudgetEntryView;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.fonts.FontUtils;
import it.superbudget.util.messages.MessagesUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BudgetPanel extends JPanel
{

	/**
	 * =Budget : =Entrate Annuali : =Spese Annuali :
	 */
	private static final long serialVersionUID = 1L;

	private static final String BUDGET_PANEL_LABEL_BUDGET = "BUDGET.PANEL.LABEL.BUDGET";

	private static final String BUDGET_PANEL_LABEL_ENTRATE_ANNUALI = "BUDGET.PANEL.LABEL.ENTRATE.ANNUALI";

	private static final String BUDGET_PANEL_LABEL_SPESE_ANNUALI = "BUDGET.PANEL.LABEL.SPESE.ANNUALI";

	private static final String BUDGET_PANEL_LABEL_SALDO = "BUDGET.PANEL.LABEL.SALDO";

	private static final String BUDGET_PANEL_LABEL_CALCULATE_ERROR = "BUDGET.PANEL.LABEL.CALCULATE.ERROR";

	private static final String BUDGET_PANEL_TABLE_INCOME = "BUDGET.PANEL.TABLE.INCOME";

	private static final String BUDGET_PANEL_TABLE_HEADER_ENTRATE_ANNUALI = "BUDGET.PANEL.TABLE.HEADER.ENTRATE.ANNUALI";

	private static final String BUDGET_PANEL_TABLE_HEADER_SPESE_ANNUALI = "BUDGET.PANEL.TABLE.HEADER.SPESE.ANNUALI";

	private static final String BUDGET_PANEL_TOOLBAR_NUOVA_ENTRATA = "BUDGET.PANEL.TOOLBAR.NUOVA.ENTRATA";

	private static final String BUDGET_PANEL_TOOLBAR_NUOVA_SPESA = "BUDGET.PANEL.TOOLBAR.NUOVA.SPESA";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_SPESA_TOOLTIP_DISABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.SPESA.TOOLTIP.DISABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_ENTRATA_TOOLTIP_DISABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.ENTRATA.TOOLTIP.DISABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_SPESA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.SPESA.TOOLTIP.ENABLED";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_NUOVA_ENTRATA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.NUOVA.ENTRATA.TOOLTIP.ENABLED";

	private static final String BUDGET_PANEL_TOOLBAR_MODIFICA = "BUDGET.PANEL.TOOLBAR.MODIFICA";

	private static final String BUDGET_PANEL_TOOLBAR_BUTTON_MODIFICA_TOOLTIP_ENABLED = "BUDGET.PANEL.TOOLBAR.BUTTON.MODIFICA.TOOLTIP.ENABLED";

	private Budget budget;

	private JLabel lblBudgetName;

	private JLabel labelEntrateAnnuali;

	private JLabel labelSpeseAnnuali;

	private JLabel lblSaldoParziale;

	private JLabel labelSaldo;

	private ResourceBundle labels;

	private Logger logger = Logger.getLogger(BudgetPanel.class);

	private JTable tableYearlyIncome;

	private JTable tableYearlyExpense;

	private JSplitPane splitPane;

	private JSplitPane splitPaneVertical;

	private JFrame owner;

	private JButton btnNewButton;

	private JButton btnNuovaSpesa;

	private JLabel lblSaldo;

	private JFreeChart jfc;

	private ChartPanel chartPanel;

	private DefaultPieDataset dataset;

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

		splitPaneVertical = new JSplitPane();
		splitPaneVertical.setOrientation(JSplitPane.VERTICAL_SPLIT);
		surround.add(splitPaneVertical);
		JPanel panelYearlyBudget = new JPanel();
		splitPaneVertical.setLeftComponent(panelYearlyBudget);
		panelYearlyBudget.setBackground(Color.WHITE);
		panelYearlyBudget.setLayout(new FormLayout(new ColumnSpec[]
		{ FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[]
				{ FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblBudget = new JLabel(labels.getString(BUDGET_PANEL_LABEL_BUDGET));
		lblBudget.setFont(FontUtils.getFontForLabelInsert());
		lblBudget.setForeground(FontUtils.getFontColorForLabelInsert());
		panelYearlyBudget.add(lblBudget, "2, 2");

		lblBudgetName = new JLabel();
		lblBudgetName.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(lblBudgetName, "4, 2");

		JLabel lblEntrateAnnuali = new JLabel(labels.getString(BUDGET_PANEL_LABEL_ENTRATE_ANNUALI));
		lblEntrateAnnuali.setFont(FontUtils.getFontForLabelInsert());
		lblEntrateAnnuali.setForeground(FontUtils.getFontColorForLabelInsert());
		panelYearlyBudget.add(lblEntrateAnnuali, "2, 6");

		labelEntrateAnnuali = new JLabel();
		labelEntrateAnnuali.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(labelEntrateAnnuali, "4, 6");

		JLabel lblSpeseAnnuali = new JLabel(labels.getString(BUDGET_PANEL_LABEL_SPESE_ANNUALI));
		lblSpeseAnnuali.setFont(FontUtils.getFontForLabelInsert());
		lblSpeseAnnuali.setForeground(FontUtils.getFontColorForLabelInsert());
		panelYearlyBudget.add(lblSpeseAnnuali, "2, 8");

		labelSpeseAnnuali = new JLabel();
		labelSpeseAnnuali.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(labelSpeseAnnuali, "4, 8");

		lblSaldoParziale = new JLabel();
		lblSaldoParziale.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(lblSaldoParziale, "4, 10");

		lblSaldo = new JLabel(labels.getString(BUDGET_PANEL_LABEL_SALDO));
		lblSaldo.setFont(FontUtils.getFontForLabelInsert());
		lblSaldo.setForeground(FontUtils.getFontColorForLabelInsert());
		panelYearlyBudget.add(lblSaldo, "2, 14");

		labelSaldo = new JLabel();

		labelSaldo.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(labelSaldo, "4, 14");

		JPanel panelDetails = new JPanel();
		panelDetails.setBackground(Color.WHITE);
		splitPaneVertical.setRightComponent(panelDetails);
		panelDetails.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		splitPane.setRightComponent(tabbedPane);

		JPanel panelAnno = new JPanel();
		panelAnno.setBackground(Color.WHITE);
		tabbedPane.addTab("Anno", null, panelAnno, null);
		panelAnno.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel_3 = new JPanel();
		panelAnno.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JPanel panelIncome = new JPanel();
		panel_3.add(panelIncome);
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
			{ Integer.class, Integer.class, Date.class, String.class, String.class, String.class, BigDecimal.class, String.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[]
			{ false, false, false, false, false, false, false, false };

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
		panel_3.add(toolBarIncomes, BorderLayout.NORTH);

		JPanel panel_4 = new JPanel();
		panelAnno.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		JPanel panelExpenses = new JPanel();
		panel_4.add(panelExpenses);
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
			{ Integer.class, Integer.class, Date.class, String.class, String.class, String.class, BigDecimal.class, String.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[]
			{ false, false, false, false, false, false, false, false };

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
		panel_4.add(toolBarExpense, BorderLayout.NORTH);

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

		JButton btnModificaIncome = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_MODIFICA), new ImageIcon(
				BudgetPanel.class.getResource("/images/modify.png")));
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

		JButton buttonModificaExpense = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_MODIFICA), new ImageIcon(
				BudgetPanel.class.getResource("/images/modify.png")));
		btnModificaIncome.setToolTipText(labels.getString(BUDGET_PANEL_TOOLBAR_BUTTON_MODIFICA_TOOLTIP_ENABLED));
		toolBarExpense.add(buttonModificaExpense);

		this.dataset = new DefaultPieDataset();
		this.jfc = ChartFactory.createPieChart3D("Budget Annuale", dataset, true, true, false);
		chartPanel = new ChartPanel(jfc);
		chartPanel.setPreferredSize(new Dimension(400, 400));
		panelDetails.add(chartPanel);
		if (this.budget != null)
		{
			this.calculate();
			this.populateTableIncome();
			this.populateTableExpense();
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

	protected void showNewIcomesDialog()
	{
		BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, null, true);
		budgetEntryDialog.setVisible(true);
		if (budgetEntryDialog.isUpdateViews())
		{
			this.populateTableIncome();
			this.calculate();
			this.getSplitPane().resetToPreferredSizes();
			this.getSplitPaneVertical().resetToPreferredSizes();
		}
	}

	protected void showNewExpenseDialog()
	{
		BudgetEntryDialog budgetEntryDialog = new BudgetEntryDialog(owner, budget, null, false);
		budgetEntryDialog.setVisible(true);
		if (budgetEntryDialog.isUpdateViews())
		{
			this.populateTableExpense();
			this.calculate();
			this.getSplitPane().resetToPreferredSizes();
			this.getSplitPaneVertical().resetToPreferredSizes();
		}
	}

	public void populateTableExpense()
	{
		DefaultTableModel model = (DefaultTableModel) this.tableYearlyExpense.getModel();
		model.setRowCount(0);
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntryView> budgets = budgetDao.getExpenses(budget);
		for (BudgetEntryView budgetEntry : budgets)
		{
			model.addRow(new Object[]
			{ budgetEntry.getBudgetEntryViewId(), budgetEntry.getBudgetEntryId(), budgetEntry.getDate(), budgetEntry.getCategory().getName(),
					budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory().getName() : null, budgetEntry.getNote(),
					budgetEntry.getValue(), budgetEntry.getRecurrence() });
		}
	}

	public void populateTableIncome()
	{
		DefaultTableModel model = (DefaultTableModel) this.tableYearlyIncome.getModel();
		model.setRowCount(0);
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntryView> budgets = budgetDao.getIncomes(budget);
		for (BudgetEntryView budgetEntry : budgets)
		{
			model.addRow(new Object[]
			{ budgetEntry.getBudgetEntryViewId(), budgetEntry.getBudgetEntryId(), budgetEntry.getDate(), budgetEntry.getCategory().getName(),
					budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory().getName() : null, budgetEntry.getNote(),
					budgetEntry.getValue(), budgetEntry.getRecurrence() });
		}
	}

	private void calculate()
	{
		dataset.clear();
		lblBudgetName.setText(budget.getName());
		NumberFormat format = NumberFormat.getCurrencyInstance();
		labelEntrateAnnuali.setText(this.calculateEntrateAnnuali());
		labelSpeseAnnuali.setText(this.calculateSpeseAnnuali());
		BigDecimal saldo = this.calculateSaldoParziale();
		format.setMaximumFractionDigits(3);
		lblSaldoParziale.setText(format.format(saldo));
		if (saldo.signum() == -1)
		{
			lblSaldoParziale.setForeground(Color.RED);
		}
		else
		{
			lblSaldoParziale.setForeground(Color.green);
		}
		saldo = budget.getSaldo().add(saldo);
		labelSaldo.setText(format.format(saldo));
		if (budget.getSaldo().signum() == -1)
		{
			labelSaldo.setForeground(Color.red);
		}
		else
		{
			labelSaldo.setForeground(Color.green);
		}
		try
		{
			dataset.setValue("Income", format.parse(labelEntrateAnnuali.getText()));
			dataset.setValue("Expense", format.parse(labelSpeseAnnuali.getText()));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BigDecimal calculateSaldoParziale()
	{
		try
		{
			NumberFormat format = NumberFormat.getCurrencyInstance();
			Number number = format.parse(this.labelEntrateAnnuali.getText());
			BigDecimal entrate = new BigDecimal(number.doubleValue());
			number = format.parse(this.labelSpeseAnnuali.getText());
			BigDecimal spese = new BigDecimal(number.doubleValue());

			BigDecimal saldo = entrate.subtract(spese);
			return saldo;
		}
		catch (ParseException e)
		{
			logger.error("Calculate Saldo Parziale generate an error", e);
			MessagesUtils.showErrorMessage(this, labels.getString(BUDGET_PANEL_LABEL_CALCULATE_ERROR));
			return new BigDecimal(0.0);
		}
	}

	private String calculateSpeseAnnuali()
	{
		BudgetDao budgetDao = new BudgetDao();
		BigDecimal yearlyIncome = budgetDao.getYearlyExpense(this.budget);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(3);
		return format.format(yearlyIncome);
	}

	private String calculateEntrateAnnuali()
	{
		BudgetDao budgetDao = new BudgetDao();
		BigDecimal yearlyIncome = budgetDao.getYearlyIncome(this.budget);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(3);
		return format.format(yearlyIncome);
	}

	public void budgetChanged(Budget newBudget)
	{
		this.budget = newBudget;
		if (this.budget != null)
		{
			this.calculate();
			this.populateTableIncome();
			this.populateTableExpense();
			this.checkToolbarButton();
			this.getSplitPane().resetToPreferredSizes();
			this.getSplitPaneVertical().resetToPreferredSizes();
		}
	}

	public JSplitPane getSplitPane()
	{
		return splitPane;
	}

	public JSplitPane getSplitPaneVertical()
	{
		return splitPaneVertical;
	}
}
