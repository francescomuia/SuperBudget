package it.superbudget.gui;

import it.superbudget.persistence.dao.BudgetDao;
import it.superbudget.persistence.dao.BudgetEntryDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.fonts.FontUtils;
import it.superbudget.util.messages.MessagesUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

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

	/**
	 * Create the panel.
	 */
	public BudgetPanel(Budget budget)
	{
		setBackground(UIManager.getColor("Panel.background"));
		this.budget = budget;
		this.labels = ResourcesBundlesUtil.getLabelsBundles();
		setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setBackground(Color.WHITE);
		splitPane.setDividerLocation(178);
		add(splitPane, BorderLayout.CENTER);

		JPanel surround = new JPanel();
		surround.setLayout(new BoxLayout(surround, BoxLayout.PAGE_AXIS));
		JPanel panelYearlyBudget = new JPanel();
		panelYearlyBudget.setBackground(Color.WHITE);

		surround.add(panelYearlyBudget);
		splitPane.setLeftComponent(surround);
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

		JLabel lblSaldo = new JLabel(labels.getString(BUDGET_PANEL_LABEL_SALDO));
		lblSaldo.setFont(FontUtils.getFontForLabelInsert());
		lblSaldo.setForeground(FontUtils.getFontColorForLabelInsert());
		panelYearlyBudget.add(lblSaldo, "2, 14");

		labelSaldo = new JLabel();

		labelSaldo.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(labelSaldo, "4, 14");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		splitPane.setRightComponent(tabbedPane);

		JPanel panelAnno = new JPanel();
		panelAnno.setBackground(Color.WHITE);
		tabbedPane.addTab("Anno", null, panelAnno, null);
		panelAnno.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panelIncome = new JPanel();
		panelIncome.setBackground(Color.WHITE);
		panelAnno.add(panelIncome);
		logger.debug("INCOME COLUMN NAMES " + labels.getString(BUDGET_PANEL_TABLE_INCOME).split(","));

		tableYearlyIncome = new JTable();
		tableYearlyIncome.setBackground(Color.WHITE);
		tableYearlyIncome.setModel(new DefaultTableModel(new Object[][]
		{}, labels.getString(BUDGET_PANEL_TABLE_INCOME).split(","))
		{
			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			Class<?>[] columnTypes = new Class<?>[]
			{ Date.class, String.class, String.class, String.class, BigDecimal.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[]
			{ false, false, false, false, false };

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
		tableYearlyIncome.getColumnModel().getColumn(3).setPreferredWidth(150);
		panelIncome.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPaneTableIncome = new JScrollPane(tableYearlyIncome);
		scrollPaneTableIncome.getViewport().setBackground(Color.white);
		panelIncome.add(scrollPaneTableIncome);

		JLabel lblEntrateAnnualiTableHeader = new JLabel(labels.getString(BUDGET_PANEL_TABLE_HEADER_ENTRATE_ANNUALI));
		lblEntrateAnnualiTableHeader.setFont(FontUtils.getFontForLabelInsert());
		lblEntrateAnnualiTableHeader.setForeground(FontUtils.getFontColorForLabelInsert());
		panelIncome.add(lblEntrateAnnualiTableHeader, BorderLayout.NORTH);

		JPanel panelExpenses = new JPanel();
		panelExpenses.setBackground(Color.WHITE);
		panelAnno.add(panelExpenses);
		panelExpenses.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPaneTableExpense = new JScrollPane();
		scrollPaneTableExpense.getViewport().setBackground(Color.WHITE);
		panelExpenses.add(scrollPaneTableExpense, BorderLayout.CENTER);

		tableYearlyExpense = new JTable();
		tableYearlyExpense.setModel(new DefaultTableModel(new Object[][]
		{}, labels.getString(BUDGET_PANEL_TABLE_INCOME).split(","))
		{
			/**
							 * 
							 */
			private static final long serialVersionUID = 1L;

			Class<?>[] columnTypes = new Class<?>[]
			{ Date.class, String.class, String.class, String.class, BigDecimal.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[]
			{ false, false, false, false, false };

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
		scrollPaneTableExpense.setViewportView(tableYearlyExpense);

		JLabel lblSpeseAnnualiTableHeader = new JLabel(labels.getString(BUDGET_PANEL_TABLE_HEADER_SPESE_ANNUALI));
		panelExpenses.add(lblSpeseAnnualiTableHeader, BorderLayout.NORTH);

		lblSpeseAnnualiTableHeader.setFont(FontUtils.getFontForLabelInsert());
		lblSpeseAnnualiTableHeader.setForeground(FontUtils.getFontColorForLabelInsert());

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

		JButton btnNewButton = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_NUOVA_ENTRATA), new ImageIcon(
				BudgetPanel.class.getResource("/images/euro.png")));
		Font font = new Font(btnNewButton.getFont().getFontName(), Font.BOLD, btnNewButton.getFont().getSize());
		btnNewButton.setFont(font);
		toolBar.add(btnNewButton);

		JButton btnNuovaSpesa = new JButton(labels.getString(BUDGET_PANEL_TOOLBAR_NUOVA_SPESA), new ImageIcon(
				BudgetPanel.class.getResource("/images/expense.png")));
		btnNuovaSpesa.setFont(font);
		toolBar.add(btnNuovaSpesa);
		if (this.budget != null)
		{
			this.calculate();
			this.populateTableIncome();
			this.populateTableExpense();
		}

	}

	private void populateTableExpense()
	{
		DefaultTableModel model = (DefaultTableModel) this.tableYearlyExpense.getModel();
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntry> budgets = budgetDao.getExpenses(budget);
		for (BudgetEntry budgetEntry : budgets)
		{
			model.addRow(new Object[]
			{ budgetEntry.getDate(), budgetEntry.getCategory().getName(),
					budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory().getName() : null, budgetEntry.getValue() });
		}
	}

	public void populateTableIncome()
	{
		DefaultTableModel model = (DefaultTableModel) this.tableYearlyIncome.getModel();
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntry> budgets = budgetDao.getIncomes(budget);
		for (BudgetEntry budgetEntry : budgets)
		{
			model.addRow(new Object[]
			{ budgetEntry.getDate(), budgetEntry.getCategory().getName(),
					budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory().getName() : null, budgetEntry.getValue() });
		}
	}

	private void calculate()
	{
		lblBudgetName.setText(budget.getName());
		labelEntrateAnnuali.setText(this.calculateEntrateAnnuali());
		labelSpeseAnnuali.setText(this.calculateSpeseAnnuali());
		BigDecimal saldo = this.calculateSaldoParziale();
		NumberFormat format = NumberFormat.getCurrencyInstance();
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
		labelSaldo.setText(format.format(budget.getSaldo()));
		if (budget.getSaldo().signum() == -1)
		{
			labelSaldo.setForeground(Color.red);
		}
		else
		{
			labelSaldo.setForeground(Color.green);
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
		}
	}

	public JSplitPane getSplitPane()
	{
		return splitPane;
	}
}
