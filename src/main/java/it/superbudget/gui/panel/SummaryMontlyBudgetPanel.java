package it.superbudget.gui.panel;

import it.superbudget.persistence.dao.BudgetDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.fonts.FontUtils;
import it.superbudget.util.messages.MessagesUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SummaryMontlyBudgetPanel extends JSplitPane implements ChangeListener
{
	private final ResourceBundle labels;

	private final JLabel lblBudgetName;

	private final JLabel labelEntrateMensili;

	private final JLabel labelSpeseMensili;

	private final JLabel lblSaldoParziale;

	private final JComponent lblSaldo;

	private final JLabel labelSaldo;

	private final DefaultPieDataset dataset;

	private final JFreeChart jfc;

	private final ChartPanel chartPanel;

	private static final String BUDGET_PANEL_LABEL_CALCULATE_ERROR = "BUDGET.PANEL.LABEL.CALCULATE.ERROR";

	private static final String BUDGET_PANEL_LABEL_MONTH = "BUDGET.PANEL.LABEL.MONTH";

	private static final String BUDGET_PANEL_LABEL_BUDGET = "BUDGET.PANEL.LABEL.BUDGET";

	private static final String BUDGET_PANEL_LABEL_ENTRATE_MENSILI = "BUDGET.PANEL.LABEL.ENTRATE.MENSILI";

	private static final String BUDGET_PANEL_LABEL_SPESE_MENSILI = "BUDGET.PANEL.LABEL.SPESE.MENSILI";

	private static final String BUDGET_PANEL_LABEL_SALDO = "BUDGET.PANEL.LABEL.SALDO";

	private final Logger logger = Logger.getLogger(SummaryMontlyBudgetPanel.class);

	private Budget budget;

	private final JLabel lblMonth;

	private final JLabel lblMonthValue;

	private int month;

	private int year;

	/**
	 * Create the panel.
	 */
	public SummaryMontlyBudgetPanel(Budget budget)
	{
		Calendar calendar = Calendar.getInstance();
		this.month = calendar.get(Calendar.MONTH);
		this.year = calendar.get(Calendar.YEAR);
		this.budget = budget;
		this.labels = ResourcesBundlesUtil.getLabelsBundles();
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);

		JPanel panelMontlyBudget = new JPanel();
		this.setLeftComponent(panelMontlyBudget);
		panelMontlyBudget.setBackground(Color.WHITE);
		panelMontlyBudget.setLayout(new FormLayout(new ColumnSpec[]
		{ FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[]
				{ FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblBudget = new JLabel(labels.getString(BUDGET_PANEL_LABEL_BUDGET));
		lblBudget.setFont(FontUtils.getFontForLabelInsert());
		lblBudget.setForeground(FontUtils.getFontColorForLabelInsert());
		panelMontlyBudget.add(lblBudget, "2, 2");

		lblBudgetName = new JLabel();
		lblBudgetName.setFont(FontUtils.getFontForLabel());
		panelMontlyBudget.add(lblBudgetName, "4, 2");

		lblMonth = FontUtils.getJlabelInsert(labels.getString(BUDGET_PANEL_LABEL_MONTH));
		panelMontlyBudget.add(lblMonth, "2, 4");

		lblMonthValue = new JLabel();
		lblMonthValue.setFont(FontUtils.getFontForLabel());
		panelMontlyBudget.add(lblMonthValue, "4, 4");
		JLabel lblEntrateMontly = new JLabel(labels.getString(BUDGET_PANEL_LABEL_ENTRATE_MENSILI));
		lblEntrateMontly.setFont(FontUtils.getFontForLabelInsert());
		lblEntrateMontly.setForeground(FontUtils.getFontColorForLabelInsert());
		panelMontlyBudget.add(lblEntrateMontly, "2, 6");

		labelEntrateMensili = new JLabel();
		labelEntrateMensili.setFont(FontUtils.getFontForLabel());
		panelMontlyBudget.add(labelEntrateMensili, "4, 6");

		JLabel lblExpenseMontly = new JLabel(labels.getString(BUDGET_PANEL_LABEL_SPESE_MENSILI));
		lblExpenseMontly.setFont(FontUtils.getFontForLabelInsert());
		lblExpenseMontly.setForeground(FontUtils.getFontColorForLabelInsert());
		panelMontlyBudget.add(lblExpenseMontly, "2, 8");

		labelSpeseMensili = new JLabel();
		labelSpeseMensili.setFont(FontUtils.getFontForLabel());
		panelMontlyBudget.add(labelSpeseMensili, "4, 8");

		lblSaldoParziale = new JLabel();
		lblSaldoParziale.setFont(FontUtils.getFontForLabel());
		panelMontlyBudget.add(lblSaldoParziale, "4, 10");

		lblSaldo = new JLabel(labels.getString(BUDGET_PANEL_LABEL_SALDO));
		lblSaldo.setFont(FontUtils.getFontForLabelInsert());
		lblSaldo.setForeground(FontUtils.getFontColorForLabelInsert());
		panelMontlyBudget.add(lblSaldo, "2, 14");

		labelSaldo = new JLabel();

		labelSaldo.setFont(FontUtils.getFontForLabel());
		panelMontlyBudget.add(labelSaldo, "4, 14");

		JPanel panelDetails = new JPanel();
		panelDetails.setBackground(Color.WHITE);
		this.setRightComponent(panelDetails);
		panelDetails.setLayout(new BorderLayout(0, 0));
		this.dataset = new DefaultPieDataset();
		this.jfc = ChartFactory.createPieChart("Budget Mensile", dataset, true, true, false);
		chartPanel = new ChartPanel(jfc);
		chartPanel.setPreferredSize(new Dimension(400, 400));
		panelDetails.add(chartPanel);
	}

	public void calculate()
	{
		dataset.clear();
		lblBudgetName.setText(budget.getName());
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		lblMonthValue.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
		NumberFormat format = NumberFormat.getCurrencyInstance();
		labelEntrateMensili.setText(this.calculateEntrateMensili());
		labelSpeseMensili.setText(this.calculateSpeseMensili());
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
			dataset.setValue("Income", format.parse(labelEntrateMensili.getText()));
			dataset.setValue("Expense", format.parse(labelSpeseMensili.getText()));
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
			Number number = format.parse(this.labelEntrateMensili.getText());
			BigDecimal entrate = new BigDecimal(number.doubleValue());
			number = format.parse(this.labelSpeseMensili.getText());
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

	private String calculateSpeseMensili()
	{
		BudgetDao budgetDao = new BudgetDao();
		BigDecimal yearlyIncome = budgetDao.getMontlyExpense(this.budget, this.month, this.year);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(3);
		return format.format(yearlyIncome);
	}

	private String calculateEntrateMensili()
	{
		BudgetDao budgetDao = new BudgetDao();
		BigDecimal yearlyIncome = budgetDao.getMontlyIncome(this.budget, this.month, this.year);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(3);
		return format.format(yearlyIncome);
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
		MontlyBudgetPanel budgetPanel = (MontlyBudgetPanel) e.getSource();
		JSlider slider = budgetPanel.getSlider();
		this.month = slider.getValue();
		this.year = budgetPanel.getYear();
		this.calculate();
	}
}
