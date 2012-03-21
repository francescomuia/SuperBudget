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

public class SummaryYearlyBudgetPanel extends JSplitPane implements ChangeListener
{
	private final ResourceBundle labels;

	private final JLabel lblBudgetName;

	private final JLabel labelEntrateAnnuali;

	private final JLabel labelSpeseAnnuali;

	private final JLabel lblSaldoParziale;

	private final JComponent lblSaldo;

	private final JLabel labelSaldo;

	private final DefaultPieDataset dataset;

	private final JFreeChart jfc;

	private final ChartPanel chartPanel;

	private static final String BUDGET_PANEL_LABEL_CALCULATE_ERROR = "BUDGET.PANEL.LABEL.CALCULATE.ERROR";

	private static final String BUDGET_PANEL_LABEL_YEAR = "BUDGET.PANEL.LABEL.YEAR";

	private static final String BUDGET_PANEL_LABEL_BUDGET = "BUDGET.PANEL.LABEL.BUDGET";

	private static final String BUDGET_PANEL_LABEL_ENTRATE_ANNUALI = "BUDGET.PANEL.LABEL.ENTRATE.ANNUALI";

	private static final String BUDGET_PANEL_LABEL_SPESE_ANNUALI = "BUDGET.PANEL.LABEL.SPESE.ANNUALI";

	private static final String BUDGET_PANEL_LABEL_SALDO = "BUDGET.PANEL.LABEL.SALDO";

	private final Logger logger = Logger.getLogger(SummaryYearlyBudgetPanel.class);

	private Budget budget;

	private final JLabel lblAnno;

	private final JLabel lblYearValue;

	private int year;

	/**
	 * Create the panel.
	 */
	public SummaryYearlyBudgetPanel(Budget budget)
	{
		Calendar calendar = Calendar.getInstance();
		this.year = calendar.get(Calendar.YEAR);
		this.budget = budget;
		this.labels = ResourcesBundlesUtil.getLabelsBundles();
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);

		JPanel panelYearlyBudget = new JPanel();
		this.setLeftComponent(panelYearlyBudget);
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

		lblAnno = FontUtils.getJlabelInsert(labels.getString(BUDGET_PANEL_LABEL_YEAR));
		panelYearlyBudget.add(lblAnno, "2, 4");

		lblYearValue = new JLabel();
		lblYearValue.setFont(FontUtils.getFontForLabel());
		panelYearlyBudget.add(lblYearValue, "4, 4");
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
		this.setRightComponent(panelDetails);
		panelDetails.setLayout(new BorderLayout(0, 0));
		this.dataset = new DefaultPieDataset();
		this.jfc = ChartFactory.createPieChart("Budget Annuale", dataset, true, true, false);
		chartPanel = new ChartPanel(jfc);
		chartPanel.setPreferredSize(new Dimension(400, 400));
		panelDetails.add(chartPanel);
	}

	public void calculate()
	{
		dataset.clear();
		lblBudgetName.setText(budget.getName());
		lblYearValue.setText(year + "");
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
		BigDecimal yearlyIncome = budgetDao.getYearlyExpense(this.budget, this.year);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(3);
		return format.format(yearlyIncome);
	}

	private String calculateEntrateAnnuali()
	{
		BudgetDao budgetDao = new BudgetDao();
		BigDecimal yearlyIncome = budgetDao.getYearlyIncome(this.budget, this.year);
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
		JSlider slider = (JSlider) e.getSource();
		this.year = slider.getValue();
		this.calculate();
	}
}
