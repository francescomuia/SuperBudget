package it.superbudget.gui;

import it.superbudget.enums.RecurrenceEnum;
import it.superbudget.gui.jspinner.DailySpinnerEditor;
import it.superbudget.gui.jspinner.DailySpinnerModel;
import it.superbudget.gui.jspinner.WeekendsSpinnerModel;
import it.superbudget.gui.jspinner.WeeklySpinnerEditor;
import it.superbudget.gui.jspinner.WeeklySpinnerModel;
import it.superbudget.persistence.dao.BudgetEntryDao;
import it.superbudget.persistence.dao.CategoryDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.persistence.entities.Category;
import it.superbudget.persistence.entities.Expenses;
import it.superbudget.persistence.entities.Income;
import it.superbudget.persistence.entities.Recurrence;
import it.superbudget.persistence.entities.SubCategory;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.calendars.CalendarFormatter;
import it.superbudget.util.calendars.CalendarsUtils;
import it.superbudget.util.fonts.FontUtils;
import it.superbudget.util.icons.IconsUtils;
import it.superbudget.util.messages.MessagesUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BudgetEntryDialog extends JDialog
{

	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(BudgetEntryDialog.class);

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private final BudgetEntry budgetEntry;

	private final boolean income;

	private final ResourceBundle labels = ResourcesBundlesUtil.getLabelsBundles();

	private final String BUTTON_SAVE = "BUTTON.SAVE";

	private final String BUTTON_ANNULLA = "BUTTON.ANNULLA";

	private final static String INCOME_DIALOG_TITLE = "INCOME.DIALOG.TITLE";

	private final static String INCOME_DIALOG_TITLE_NEW = "INCOME.DIALOG.TITLE.NEW";

	private final static String INCOME_DIALOG_TITLE_MODIFY = "INCOME.DIALOG.TITLE.MODIFY";

	private final static String EXPENSE_DIALOG_TITLE = "EXPENSE.DIALOG.TITLE";

	private final static String EXPENSE_DIALOG_TITLE_NEW = "EXPENSE.DIALOG.TITLE.NEW";

	private final static String EXPENSE_DIALOG_TITLE_MODIFY = "EXPENSE.DIALOG.TITLE.MODIFY";

	private final static String INCOME_DIALOG_LABELS_VALUE = "INCOME.DIALOG.LABELS.VALUE";

	private final static String INCOME_DIALOG_LABELS_DATE = "INCOME.DIALOG.LABELS.DATE";

	private final static String INCOME_DIALOG_LABELS_DATE_FROM = "INCOME.DIALOG.LABELS.DATE.FROM";

	private final static String INCOME_DIALOG_LABELS_DATE_TO = "INCOME.DIALOG.LABELS.DATE.TO";

	private final static String INCOME_DIALOG_LABELS_CATEGORY = "INCOME.DIALOG.LABELS.CATEGORY";

	private final static String INCOME_DIALOG_LABELS_SUB_CATEGORY = "INCOME.DIALOG.LABELS.SUB.CATEGORY";

	private final static String INCOME_DIALOG_LABELS_OCCURS = "INCOME.DIALOG.LABELS.OCCURS";

	private final static String INCOME_DIALOG_LABELS_OCCURS_HOW = "INCOME.DIALOG.LABELS.OCCURS.HOW";

	private final static String INCOME_DIALOG_LABELS_NOTE = "INCOME.DIALOG.LABELS.NOTE";

	private final static String INCOME_DIALOG_LABELS_VALUE_ERROR = "INCOME.DIALOG.LABELS.VALUE.ERROR";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_ERROR = "INCOME.DIALOG.LABELS.RECURRENCE.ERROR";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_YEARLY_ERROR = "INCOME.DIALOG.LABELS.RECURRENCE.YEARLY.ERROR";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_DAILY_ERROR = "INCOME.DIALOG.LABELS.RECURRENCE.DAILY.ERROR";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_WEEKENDS_ERROR = "INCOME.DIALOG.LABELS.RECURRENCE.WEEKENDS.ERROR";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_WEEKLY_ERROR = "INCOME.DIALOG.LABELS.RECURRENCE.WEEKLY.ERROR";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_YEARLY = "INCOME.DIALOG.LABELS.RECURRENCE.YEARLY";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_MONTLY = "INCOME.DIALOG.LABELS.RECURRENCE.MONTLY";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_QUARTERLY = "INCOME.DIALOG.LABELS.RECURRENCE.QUARTERLY";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_WEEKLY = "INCOME.DIALOG.LABELS.RECURRENCE.WEEKLY";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_WEEKENDS = "INCOME.DIALOG.LABELS.RECURRENCE.WEEKENDS";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_BIWEEKLY = "INCOME.DIALOG.LABELS.RECURRENCE.BIWEEKLY";

	private final static String INCOME_DIALOG_LABELS_RECURRENCE_DAILY = "INCOME.DIALOG.LABELS.RECURRENCE.DAILY";

	/*
	 * INCOME.DIALOG.LABELS.DATE.FROM=Da INCOME.DIALOG.LABELS.DATE.TO=Al
	 */
	private JComboBox comboBoxCategoria;

	private JComboBox comboBoxSubCategoria;

	private JFormattedTextField formattedTextFieldValue;

	private JCheckBox checkBoxRecurrence;

	private JComboBox comboBoxRecurrence;

	private JLabel lblQuando;

	private JPanel panelFormContainer;

	private JDatePicker datePicker;

	private JTextArea textAreaNote;

	private Budget budget;

	private boolean updateViews;

	private JSpinner recurrenceYearlySpinnerFrom;

	private JSpinner recurrenceMontlySpinnerFrom;

	private JLabel lblRecurrenceYearly;

	private JLabel lblRecurrenceMontly;

	private JLabel lblData;

	private JLabel lblRecurrenceWeekly;

	private JSpinner recurrenceWeeklySpinnerFrom;

	private JLabel lblDal;

	private JLabel lblTo;

	private JSpinner recurrenceYearlySpinnerTo;

	private JSpinner recurrenceMontlySpinnerTo;

	private JSpinner recurrenceWeeklySpinnerTo;

	private JLabel lblRecurrenceDaily;

	private JSpinner recurrenceDailySpinnerFrom;

	private JSpinner recurrenceDailySpinnerTo;

	private JLabel lblRecurrenceBiWeekly;

	private JSpinner recurrenceBiWeeklySpinnerFrom;

	private JSpinner recurrenceBiWeeklySpinnerTo;

	private JLabel lblRecurrenceQuartely;

	private JSpinner recurrenceQuartelySpinnerFrom;

	private JSpinner recurrenceQuartelySpinnerTo;

	private JLabel lblRecurrenceWeekends;

	private JSpinner recurrenceWeekendsSpinnerFrom;

	private JSpinner recurrenceWeekendsSpinnerTo;

	public BudgetEntryDialog(JFrame owner, Budget budget, BudgetEntry entry, boolean income)
	{
		super(owner);

		this.setModal(true);
		this.budgetEntry = entry;
		this.income = income;
		this.budget = budget;
		String title = "";
		lblRecurrenceYearly = new JLabel(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_YEARLY));
		lblRecurrenceYearly.setFont(FontUtils.getFontForLabelInsert());
		lblRecurrenceYearly.setForeground(FontUtils.getFontColorForLabelInsert());
		Calendar calendar = Calendar.getInstance();
		recurrenceYearlySpinnerFrom = new JSpinner(new SpinnerNumberModel(calendar.get(Calendar.YEAR), 1, Integer.MAX_VALUE, 1));
		recurrenceYearlySpinnerFrom.setEditor(new JSpinner.NumberEditor(recurrenceYearlySpinnerFrom, "0"));

		recurrenceYearlySpinnerTo = new JSpinner(new SpinnerNumberModel(calendar.get(Calendar.YEAR), 0, Integer.MAX_VALUE, 1));
		recurrenceYearlySpinnerTo.setEditor(new JSpinner.NumberEditor(recurrenceYearlySpinnerTo, "0"));

		lblRecurrenceMontly = new JLabel(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_MONTLY));
		lblRecurrenceMontly.setFont(FontUtils.getFontForLabelInsert());
		lblRecurrenceMontly.setForeground(FontUtils.getFontColorForLabelInsert());
		recurrenceMontlySpinnerFrom = new JSpinner(new SpinnerListModel(CalendarsUtils.getMonthNames()));
		((JSpinner.DefaultEditor) recurrenceMontlySpinnerFrom.getEditor()).getTextField().setEditable(false);
		List<String> monthNames = CalendarsUtils.getMonthNames();
		monthNames.add(0, "");
		recurrenceMontlySpinnerTo = new JSpinner(new SpinnerListModel(monthNames));
		((JSpinner.DefaultEditor) recurrenceMontlySpinnerTo.getEditor()).getTextField().setEditable(false);

		lblRecurrenceQuartely = FontUtils.getJlabelInsert(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_QUARTERLY));
		List<String> tempMonthName = CalendarsUtils.getMonthNames();
		List<String> quartelyMonthName = new ArrayList<String>();
		for (int i = 0; i < 12; i = i + 3)
		{
			quartelyMonthName.add(tempMonthName.get(i));
		}
		recurrenceQuartelySpinnerFrom = new JSpinner(new SpinnerListModel(quartelyMonthName));
		((JSpinner.DefaultEditor) recurrenceQuartelySpinnerFrom.getEditor()).getTextField().setEditable(false);
		List<String> quartelyMonthNameWithNull = new ArrayList<String>(quartelyMonthName);
		quartelyMonthNameWithNull.add(0, "");
		recurrenceQuartelySpinnerTo = new JSpinner(new SpinnerListModel(quartelyMonthNameWithNull));
		((JSpinner.DefaultEditor) recurrenceQuartelySpinnerTo.getEditor()).getTextField().setEditable(false);

		lblRecurrenceDaily = FontUtils.getJlabelInsert(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_DAILY));
		recurrenceDailySpinnerFrom = new JSpinner(new DailySpinnerModel(false));
		recurrenceDailySpinnerFrom.setEditor(new DailySpinnerEditor(recurrenceDailySpinnerFrom, "dd/MM/yyyy", false));
		recurrenceDailySpinnerTo = new JSpinner(new DailySpinnerModel(true));
		recurrenceDailySpinnerTo.setEditor(new DailySpinnerEditor(recurrenceDailySpinnerTo, "dd/MM/yyyy", true));

		lblRecurrenceWeekends = FontUtils.getJlabelInsert(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_WEEKENDS));
		recurrenceWeekendsSpinnerFrom = new JSpinner(new WeekendsSpinnerModel(false));
		recurrenceWeekendsSpinnerFrom.setEditor(new DailySpinnerEditor(recurrenceWeekendsSpinnerFrom, "dd/MM/yyyy", false));
		recurrenceWeekendsSpinnerTo = new JSpinner(new WeekendsSpinnerModel(true));
		recurrenceWeekendsSpinnerTo.setEditor(new DailySpinnerEditor(recurrenceWeekendsSpinnerTo, "dd/MM/yyyy", true));

		lblRecurrenceWeekly = new JLabel(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_WEEKLY));
		lblRecurrenceWeekly.setFont(FontUtils.getFontForLabelInsert());
		lblRecurrenceWeekly.setForeground(FontUtils.getFontColorForLabelInsert());
		recurrenceWeeklySpinnerFrom = new JSpinner(new WeeklySpinnerModel(false));
		recurrenceWeeklySpinnerFrom.setEditor(new WeeklySpinnerEditor(recurrenceWeeklySpinnerFrom, false));
		recurrenceWeeklySpinnerTo = new JSpinner(new WeeklySpinnerModel(true));
		recurrenceWeeklySpinnerTo.setEditor(new WeeklySpinnerEditor(recurrenceWeeklySpinnerTo, true));

		lblRecurrenceBiWeekly = FontUtils.getJlabelInsert(labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_BIWEEKLY));
		recurrenceBiWeeklySpinnerFrom = new JSpinner(new WeeklySpinnerModel(false, 2));
		recurrenceBiWeeklySpinnerFrom.setEditor(new WeeklySpinnerEditor(recurrenceBiWeeklySpinnerFrom, false));
		recurrenceBiWeeklySpinnerTo = new JSpinner(new WeeklySpinnerModel(true, 2));
		recurrenceBiWeeklySpinnerTo.setEditor(new WeeklySpinnerEditor(recurrenceBiWeeklySpinnerTo, true));

		if (income && budgetEntry != null)
		{
			title = labels.getString(INCOME_DIALOG_TITLE_MODIFY) + " ";
		}
		else if (income)
		{
			title = labels.getString(INCOME_DIALOG_TITLE_NEW) + " ";
		}
		else if (!income && budgetEntry != null)
		{
			title = labels.getString(EXPENSE_DIALOG_TITLE_MODIFY) + " ";
		}
		else if (!income)
		{
			title = labels.getString(EXPENSE_DIALOG_TITLE_NEW) + " ";
		}
		if (income)
		{
			this.setTitle(title + labels.getString(INCOME_DIALOG_TITLE));
		}
		else
		{
			this.setTitle(title + labels.getString(EXPENSE_DIALOG_TITLE));
		}
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
		decimalFormat.setMaximumFractionDigits(3);
		decimalFormat.setMinimumFractionDigits(3);
		panelFormContainer = new JPanel();
		contentPanel.add(panelFormContainer, BorderLayout.CENTER);
		panelFormContainer.setLayout(new FormLayout(new ColumnSpec[]
		{ FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("center:15dlu"), ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[]
		{ FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblRicorre = new JLabel(labels.getString(INCOME_DIALOG_LABELS_OCCURS));
		lblRicorre.setFont(FontUtils.getFontForLabelInsert());
		lblRicorre.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblRicorre, "2, 2, right, default");

		checkBoxRecurrence = new JCheckBox();
		checkBoxRecurrence.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				checkBoxReccurenceClicked();
			}
		});
		panelFormContainer.add(checkBoxRecurrence, "4, 2, left, default");
		lblQuando = new JLabel(labels.getString(INCOME_DIALOG_LABELS_OCCURS_HOW));
		lblQuando.setFont(FontUtils.getFontForLabelInsert());
		lblQuando.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblQuando, "2, 4, right, default");
		lblQuando.setVisible(false);

		comboBoxRecurrence = new JComboBox();

		panelFormContainer.add(comboBoxRecurrence, "4, 4, fill, default");

		comboBoxRecurrence.setVisible(false);
		JLabel lblValore = new JLabel(labels.getString(INCOME_DIALOG_LABELS_VALUE));
		lblValore.setFont(FontUtils.getFontForLabelInsert());
		lblValore.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblValore, "2, 6, right, default");
		formattedTextFieldValue = new JFormattedTextField(decimalFormat);
		panelFormContainer.add(formattedTextFieldValue, "4, 6, left, default");
		formattedTextFieldValue.setColumns(10);
		formattedTextFieldValue.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		JLabel lblCategoria = new JLabel(labels.getString(INCOME_DIALOG_LABELS_CATEGORY));
		lblCategoria.setFont(FontUtils.getFontForLabelInsert());
		lblCategoria.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblCategoria, "2, 8, right, default");
		comboBoxCategoria = new JComboBox();

		comboBoxCategoria.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				categoriaChangedEvent();
			}
		});
		panelFormContainer.add(comboBoxCategoria, "4, 8, left, default");
		JLabel lblSottoCategoria = new JLabel(labels.getString(INCOME_DIALOG_LABELS_SUB_CATEGORY));
		lblSottoCategoria.setFont(FontUtils.getFontForLabelInsert());
		lblSottoCategoria.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblSottoCategoria, "2, 10, right, default");
		comboBoxSubCategoria = new JComboBox();
		panelFormContainer.add(comboBoxSubCategoria, "4, 10, left, default");

		lblData = new JLabel(labels.getString(INCOME_DIALOG_LABELS_DATE));
		lblData.setFont(FontUtils.getFontForLabelInsert());
		lblData.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblData, "2, 12, right, default");
		datePicker = JDateComponentFactory.createJDatePicker(new UtilDateModel(), new CalendarFormatter(new SimpleDateFormat("dd/MM/yyyy")));
		datePicker.setDoubleClickAction(true);

		panelFormContainer.add((Component) datePicker, "4, 12, left, default");

		lblDal = FontUtils.getJlabelInsert(labels.getString(INCOME_DIALOG_LABELS_DATE_FROM));
		lblTo = FontUtils.getJlabelInsert(labels.getString(INCOME_DIALOG_LABELS_DATE_TO));

		textAreaNote = new JTextArea();
		textAreaNote.setRows(5);
		textAreaNote.setColumns(10);
		JScrollPane scrollPane = new JScrollPane(textAreaNote);
		panelFormContainer.add(scrollPane, "4, 17, fill, fill");
		JLabel lblNote = new JLabel(labels.getString(INCOME_DIALOG_LABELS_NOTE));
		lblNote.setFont(FontUtils.getFontForLabelInsert());
		lblNote.setForeground(FontUtils.getFontColorForLabelInsert());
		panelFormContainer.add(lblNote, "2, 17, right, default");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton(labels.getString(BUTTON_SAVE));
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveIncome();
			}
		});
		okButton.setIcon(IconsUtils.getSaveButtonIcon());
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton(labels.getString(BUTTON_ANNULLA));
		cancelButton.setIcon(IconsUtils.getUndoButtonIcon());
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateViews = false;
				dispose();
			}
		});
		buttonPane.add(cancelButton);
		initializeComponent();
		this.pack();
		this.setLocationRelativeTo(owner);

	}

	protected void checkBoxReccurenceClicked()
	{
		if (this.getCheckBoxRecurrence().isSelected())
		{
			this.lblQuando.setVisible(true);
			this.comboBoxRecurrence.setVisible(true);
			if (this.budgetEntry != null)
			{
				this.comboBoxRecurrence.setSelectedItem(budgetEntry.getRecurrence());
			}
			else
			{
				this.comboBoxRecurrence.setSelectedIndex(-1);
			}
			this.recurrenceChangeEvent();
			this.pack();
		}
		else
		{
			this.lblQuando.setVisible(false);
			this.comboBoxRecurrence.setVisible(false);
			this.comboBoxRecurrence.setSelectedIndex(-1);
			removeRecurrenceComponent();
			panelFormContainer.add(lblData, "2, 12, right, default");
			panelFormContainer.add((Component) datePicker, "4, 12, left, default");
			this.pack();
		}
	}

	private void recurrenceChangeEvent()
	{
		Recurrence recurrence = (Recurrence) this.comboBoxRecurrence.getSelectedItem();
		this.removeRecurrenceComponent();
		// panelFormContainer.add(lblDal, "3, 14");
		// panelFormContainer.add(lblTo, "3, 16");
		if (RecurrenceEnum.YEARLY.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceYearly, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceYearlySpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceYearlySpinnerTo, "4, 16, fill, default");

			if (this.budgetEntry != null)
			{
				recurrenceYearlySpinnerFrom.setValue(CalendarsUtils.getCurrentYear(budgetEntry.getDateFrom()));
				recurrenceYearlySpinnerTo.setValue(CalendarsUtils.getCurrentYear(budgetEntry.getDateTo()));
			}
			else
			{
				recurrenceYearlySpinnerFrom.setValue(CalendarsUtils.getCurrentYear());
				recurrenceYearlySpinnerTo.setValue(0);
			}
		}
		else if (RecurrenceEnum.MONTLY.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceMontly, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceMontlySpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceMontlySpinnerTo, "4, 16, fill, default");
			if (budgetEntry != null)
			{
				recurrenceMontlySpinnerFrom.setValue(CalendarsUtils.getCurrentMonthLabel(budgetEntry.getDateFrom()));
				recurrenceMontlySpinnerTo.setValue(CalendarsUtils.getCurrentMonthLabel(budgetEntry.getDateTo()));
			}
			else
			{
				recurrenceMontlySpinnerFrom.setValue(CalendarsUtils.getCurrentMonthLabel());
				recurrenceMontlySpinnerTo.setValue("");
			}
		}
		else if (RecurrenceEnum.DAILY.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceDaily, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceDailySpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceDailySpinnerTo, "4, 16, fill, default");
			if (budgetEntry != null)
			{
				recurrenceDailySpinnerFrom.setValue(budgetEntry.getDateFrom());
				recurrenceDailySpinnerTo.setValue(budgetEntry.getDateTo());
			}
			else
			{
				recurrenceDailySpinnerFrom.setValue(Calendar.getInstance());
				recurrenceDailySpinnerTo.setValue(null);
			}
		}
		else if (RecurrenceEnum.WEEKENDS.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceWeekends, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceWeekendsSpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceWeekendsSpinnerTo, "4, 16, fill, default");
			if (budgetEntry != null)
			{
				recurrenceWeekendsSpinnerFrom.setValue(budgetEntry.getDateFrom());
				recurrenceWeekendsSpinnerTo.setValue(budgetEntry.getDateTo());
			}
			else
			{
				recurrenceWeekendsSpinnerFrom.setValue(CalendarsUtils.getCurrentWeekends());
				recurrenceWeekendsSpinnerTo.setValue(null);
			}
		}
		else if (RecurrenceEnum.WEEKLY.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceWeekly, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceWeeklySpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceWeeklySpinnerTo, "4, 16, fill, default");
			if (budgetEntry != null)
			{
				Calendar weekStart = Calendar.getInstance();
				weekStart.setTime(budgetEntry.getDateFrom());
				recurrenceWeeklySpinnerFrom.setValue(weekStart);
				if (budgetEntry.getDateTo() != null)
				{
					Calendar weekEnd = Calendar.getInstance();
					weekEnd.setTime(budgetEntry.getDateTo());
					recurrenceWeeklySpinnerTo.setValue(weekEnd);
				}
				else
				{
					recurrenceWeeklySpinnerTo.setValue(null);
				}
			}
			else
			{
				recurrenceWeeklySpinnerFrom.setValue(Calendar.getInstance());
				recurrenceWeeklySpinnerTo.setValue(null);
			}
		}
		else if (RecurrenceEnum.BIWEEKLY.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceBiWeekly, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceBiWeeklySpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceBiWeeklySpinnerTo, "4, 16, fill, default");
			if (budgetEntry != null)
			{
				Calendar weekStart = Calendar.getInstance();
				weekStart.setTime(budgetEntry.getDateFrom());
				recurrenceBiWeeklySpinnerFrom.setValue(weekStart);
				if (budgetEntry.getDateTo() != null)
				{
					Calendar weekEnd = Calendar.getInstance();
					weekEnd.setTime(budgetEntry.getDateTo());
					recurrenceBiWeeklySpinnerTo.setValue(weekEnd);
				}
				else
				{
					recurrenceBiWeeklySpinnerTo.setValue(null);
				}
			}
			else
			{
				recurrenceBiWeeklySpinnerFrom.setValue(Calendar.getInstance());
				recurrenceBiWeeklySpinnerTo.setValue(null);
			}
		}
		else if (RecurrenceEnum.QUARTERLY.equals(recurrence))
		{
			panelFormContainer.add(lblRecurrenceQuartely, "2, 12, right, default");
			panelFormContainer.add(lblDal, "3, 14");
			panelFormContainer.add(recurrenceQuartelySpinnerFrom, "4, 14, fill, default");
			panelFormContainer.add(lblTo, "3, 16");
			panelFormContainer.add(recurrenceQuartelySpinnerTo, "4, 16, fill, default");
			if (budgetEntry != null)
			{
				recurrenceQuartelySpinnerFrom.setValue(CalendarsUtils.getCurrentQuartelyMonthLabel(budgetEntry.getDateFrom()));
				recurrenceQuartelySpinnerTo.setValue(CalendarsUtils.getCurrentQuartelyMonthLabel(budgetEntry.getDateTo()));
			}
			else
			{
				recurrenceQuartelySpinnerFrom.setValue(CalendarsUtils.getCurrentQuartelyMonthLabel());
				recurrenceQuartelySpinnerTo.setValue("");
			}
		}
		this.repaint();
		pack();
	}

	private void removeRecurrenceComponent()
	{
		this.panelFormContainer.remove((Component) this.datePicker);
		this.panelFormContainer.remove(lblData);
		this.panelFormContainer.remove(lblRecurrenceYearly);
		this.panelFormContainer.remove(lblDal);
		this.panelFormContainer.remove(recurrenceYearlySpinnerFrom);
		this.panelFormContainer.remove(lblTo);
		this.panelFormContainer.remove(recurrenceYearlySpinnerTo);
		this.panelFormContainer.remove(lblRecurrenceMontly);
		this.panelFormContainer.remove(recurrenceMontlySpinnerFrom);
		this.panelFormContainer.remove(recurrenceMontlySpinnerTo);
		this.panelFormContainer.remove(lblRecurrenceDaily);
		this.panelFormContainer.remove(recurrenceDailySpinnerFrom);
		this.panelFormContainer.remove(recurrenceDailySpinnerTo);
		this.panelFormContainer.remove(lblRecurrenceWeekends);
		this.panelFormContainer.remove(recurrenceWeekendsSpinnerFrom);
		this.panelFormContainer.remove(recurrenceWeekendsSpinnerTo);
		this.panelFormContainer.remove(lblRecurrenceWeekly);
		this.panelFormContainer.remove(recurrenceWeeklySpinnerFrom);
		this.panelFormContainer.remove(recurrenceWeeklySpinnerTo);
		this.panelFormContainer.remove(lblRecurrenceBiWeekly);
		this.panelFormContainer.remove(recurrenceBiWeeklySpinnerFrom);
		this.panelFormContainer.remove(recurrenceBiWeeklySpinnerTo);
		this.panelFormContainer.remove(lblRecurrenceQuartely);
		this.panelFormContainer.remove(recurrenceQuartelySpinnerFrom);
		this.panelFormContainer.remove(recurrenceQuartelySpinnerTo);

	}

	protected void saveIncome()
	{
		if (this.formattedTextFieldValue.getValue() != null && !this.formattedTextFieldValue.getValue().toString().trim().isEmpty())
		{
			if ((this.checkBoxRecurrence.isSelected() && this.comboBoxRecurrence.getSelectedIndex() != -1) || !this.checkBoxRecurrence.isSelected())
			{

				BudgetEntryDao budgetEntryDao = new BudgetEntryDao();
				try
				{
					BigDecimal value = new BigDecimal(this.formattedTextFieldValue.getValue().toString());

					Category category = (Category) this.comboBoxCategoria.getSelectedItem();
					SubCategory subCategory = null;
					if (this.comboBoxSubCategoria != null)
					{
						subCategory = (SubCategory) this.comboBoxSubCategoria.getSelectedItem();
					}
					String note = null;
					if (this.textAreaNote.getText() != null && !this.textAreaNote.getText().trim().isEmpty())
					{
						note = this.textAreaNote.getText().trim();
					}
					Date dateFrom = null;
					Date dateTo = null;
					boolean fail = false;
					String failMessage = null;
					Recurrence recurrence = new RecurrenceDao().getNoneRecurrence();
					if (this.checkBoxRecurrence.isSelected() && this.comboBoxRecurrence.getSelectedIndex() != -1)
					{
						recurrence = (Recurrence) this.comboBoxRecurrence.getSelectedItem();
						if (RecurrenceEnum.YEARLY.equals(recurrence))
						{
							int yearFrom = (Integer) this.recurrenceYearlySpinnerFrom.getValue();
							int yearTo = (Integer) this.recurrenceYearlySpinnerTo.getValue();
							if (yearTo != 0 && yearTo < yearFrom)
							{
								fail = true;
								failMessage = labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_YEARLY_ERROR);
							}
							else
							{
								Calendar calendar = CalendarsUtils.getInitYear();
								calendar.set(Calendar.YEAR, yearFrom);
								dateFrom = calendar.getTime();
								if (yearTo != 0)
								{
									calendar = CalendarsUtils.getFinishYear();
									calendar.set(Calendar.YEAR, yearTo);
								}

							}
						}
						else if (RecurrenceEnum.MONTLY.equals(recurrence))
						{
							String monthFrom = this.recurrenceMontlySpinnerFrom.getValue().toString();
							String monthTo = this.recurrenceMontlySpinnerTo.getValue().toString();
							int indexFrom = -1;
							int indexTo = -1;
							int yearTo = -1;
							List<String> monthNames = CalendarsUtils.getMonthNames();
							indexFrom = monthNames.indexOf(monthFrom);
							if (!monthTo.isEmpty())
							{

								indexTo = monthNames.indexOf(monthTo);
								Calendar calendar = Calendar.getInstance();
								if (indexTo < indexFrom)
								{
									yearTo = calendar.get(Calendar.YEAR) + 1;
								}
							}
							Calendar calendar = Calendar.getInstance();
							calendar.set(Calendar.MONTH, indexFrom);
							calendar.set(Calendar.DAY_OF_MONTH, 1);
							dateFrom = calendar.getTime();
							if (indexTo != -1)
							{
								calendar.set(Calendar.MONTH, indexTo);
								if (yearTo != -1)
								{
									calendar.set(Calendar.YEAR, yearTo);
								}
								calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
								dateTo = calendar.getTime();
							}
						}
						else if (RecurrenceEnum.DAILY.equals(recurrence))
						{
							Calendar calendarFrom = (Calendar) this.recurrenceDailySpinnerFrom.getValue();
							Calendar calendarTo = (Calendar) this.recurrenceDailySpinnerTo.getValue();
							if (calendarTo != null && calendarFrom.after(calendarTo))
							{
								fail = true;
								failMessage = labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_DAILY_ERROR);
							}
							else
							{
								dateFrom = calendarFrom.getTime();
								dateTo = calendarTo == null ? null : calendarTo.getTime();
							}
						}
						else if (RecurrenceEnum.WEEKENDS.equals(recurrence))
						{
							Calendar calendarFrom = (Calendar) this.recurrenceWeekendsSpinnerFrom.getValue();
							Calendar calendarTo = (Calendar) this.recurrenceWeekendsSpinnerTo.getValue();
							if (calendarTo != null && calendarFrom.after(calendarTo))
							{
								fail = true;
								failMessage = labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_WEEKENDS_ERROR);
							}
							else
							{
								dateFrom = calendarFrom.getTime();
								dateTo = calendarTo == null ? null : calendarTo.getTime();
							}
						}
						else if (RecurrenceEnum.WEEKLY.equals(recurrence))
						{
							Calendar calendarFrom = (Calendar) this.recurrenceWeeklySpinnerFrom.getValue();
							Calendar calendarTo = (Calendar) this.recurrenceWeeklySpinnerTo.getValue();
							if (calendarTo != null && calendarFrom.after(calendarTo))
							{
								fail = true;
								failMessage = labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_WEEKLY_ERROR);
							}
							else
							{
								dateFrom = calendarFrom.getTime();
								dateTo = calendarTo == null ? null : calendarTo.getTime();
							}
						}
						else if (RecurrenceEnum.BIWEEKLY.equals(recurrence))
						{
							Calendar calendarFrom = (Calendar) this.recurrenceWeeklySpinnerFrom.getValue();
							Calendar calendarTo = (Calendar) this.recurrenceWeeklySpinnerTo.getValue();
							if (calendarTo != null && calendarFrom.after(calendarTo))
							{
								fail = true;
								failMessage = labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_WEEKLY_ERROR);
							}
							else
							{
								dateFrom = calendarFrom.getTime();
								dateTo = calendarTo == null ? null : calendarTo.getTime();
							}
						}
						else if (RecurrenceEnum.QUARTERLY.equals(recurrence))
						{
							String monthFrom = this.recurrenceMontlySpinnerFrom.getValue().toString();
							String monthTo = this.recurrenceMontlySpinnerTo.getValue().toString();
							int indexFrom = -1;
							int indexTo = -1;
							int yearTo = -1;
							if (!monthTo.isEmpty())
							{
								List<String> monthNames = CalendarsUtils.getMonthNames();
								indexFrom = monthNames.indexOf(monthFrom);
								indexTo = monthNames.indexOf(monthTo);
								Calendar calendar = Calendar.getInstance();
								if (indexTo < indexFrom)
								{
									yearTo = calendar.get(Calendar.YEAR) + 1;
								}
							}
							Calendar calendar = Calendar.getInstance();
							calendar.set(Calendar.MONTH, indexFrom);
							calendar.set(Calendar.DAY_OF_MONTH, 1);
							dateFrom = calendar.getTime();
							if (indexTo != -1)
							{
								calendar.set(Calendar.MONTH, indexTo);
								if (yearTo != -1)
								{
									calendar.set(Calendar.YEAR, yearTo);
								}
								calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
								dateTo = calendar.getTime();
							}
						}
					}
					else
					{
						dateFrom = ((UtilDateModel) datePicker.getModel()).getValue();
					}
					if (fail)
					{
						MessagesUtils.showErrorMessage(this, failMessage);
					}
					else
					{
						// Date date = (Date) this.datePicker.getModel().getValue();

						BudgetEntry budgetEntry = null;
						if (income)
						{
							budgetEntry = new Income(value, category, subCategory, dateFrom, dateTo, this.budget, null, note, recurrence);
						}
						else
						{
							budgetEntry = new Expenses(value, category, subCategory, dateFrom, dateTo, this.budget, null, note, recurrence);
						}
						budgetEntryDao.save(budgetEntry);
						this.updateViews = true;
						dispose();
					}
				}
				catch (RuntimeException e)
				{
					logger.error("Errore duranta il salvataggio budgetEntry", e);
					MessagesUtils.showExceptionMessage(e);
				}
			}
			else
			{
				MessagesUtils.showErrorMessage(this, labels.getString(INCOME_DIALOG_LABELS_RECURRENCE_ERROR));
			}
		}
		else
		{
			MessagesUtils.showErrorMessage(this, labels.getString(INCOME_DIALOG_LABELS_VALUE_ERROR));
		}
	}

	protected void categoriaChangedEvent()
	{
		if (this.comboBoxCategoria.getSelectedIndex() != -1)
		{
			Category category = (Category) comboBoxCategoria.getSelectedItem();
			DefaultComboBoxModel model = (DefaultComboBoxModel) this.comboBoxSubCategoria.getModel();
			model.removeAllElements();
			for (SubCategory subCategory : category.getSubCategories())
			{
				model.addElement(subCategory);
			}
		}

	}

	public void initializeComponent()
	{
		List<Category> categories = new CategoryDao().findAll();
		for (Category category : categories)
		{

			((DefaultComboBoxModel) this.comboBoxCategoria.getModel()).addElement(category);
		}
		List<SubCategory> subCategories = ((Category) this.comboBoxCategoria.getSelectedItem()).getSubCategories();
		for (SubCategory subCategory : subCategories)
		{
			((DefaultComboBoxModel) this.comboBoxSubCategoria.getModel()).addElement(subCategory);
		}
		((DefaultComboBoxModel) this.comboBoxSubCategoria.getModel()).insertElementAt(null, 0);
		this.comboBoxSubCategoria.setSelectedIndex(0);

		RecurrenceDao recurrenceDao = new RecurrenceDao();
		List<Recurrence> recurrences = recurrenceDao.getRecurrenceForBudgetEntryDialog();
		DefaultComboBoxModel model = (DefaultComboBoxModel) comboBoxRecurrence.getModel();
		for (Recurrence recurrence : recurrences)
		{
			model.addElement(recurrence);
		}
		comboBoxRecurrence.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				recurrenceChangeEvent();
			}
		});
		if (this.budgetEntry == null)
		{
			((UtilDateModel) this.datePicker.getModel()).setValue(new Date());
		}
		else
		{
			this.getFormattedTextFieldValue().setValue(this.budgetEntry.getValue());
			((UtilDateModel) this.datePicker.getModel()).setValue(budgetEntry.getDateFrom());
			this.getComboBoxCategoria().setSelectedItem(this.budgetEntry.getCategory());
			this.getComboBoxSubCategoria().setSelectedItem(this.budgetEntry.getSubCategory());
			Recurrence recurrence = budgetEntry.getRecurrence();
			if (!recurrenceDao.getNoneRecurrence().equals(recurrence))
			{
				this.checkBoxRecurrence.setSelected(true);
				this.checkBoxReccurenceClicked();
			}
			this.comboBoxRecurrence.setSelectedItem(recurrence);
		}
	}

	public JComboBox getComboBoxCategoria()
	{
		return comboBoxCategoria;
	}

	public JComboBox getComboBoxSubCategoria()
	{
		return comboBoxSubCategoria;
	}

	public JFormattedTextField getFormattedTextFieldValue()
	{
		return formattedTextFieldValue;
	}

	public JCheckBox getCheckBoxRecurrence()
	{
		return checkBoxRecurrence;
	}

	public JComboBox getComboBoxOccurs()
	{
		return comboBoxRecurrence;
	}

	public JLabel getLblQuando()
	{
		return lblQuando;
	}

	public JPanel getPanelFormContainer()
	{
		return panelFormContainer;
	}

	public JTextArea getTextAreaNote()
	{
		return textAreaNote;
	}

	public boolean isUpdateViews()
	{
		return updateViews;
	}
}
