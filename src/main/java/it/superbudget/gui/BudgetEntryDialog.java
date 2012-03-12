package it.superbudget.gui;

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
import it.superbudget.util.db.CalendarFormatter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

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

	private final static String INCOME_DIALOG_LABELS_CATEGORY = "INCOME.DIALOG.LABELS.CATEGORY";

	private final static String INCOME_DIALOG_LABELS_SUB_CATEGORY = "INCOME.DIALOG.LABELS.SUB.CATEGORY";

	private final static String INCOME_DIALOG_LABELS_OCCURS = "INCOME.DIALOG.LABELS.OCCURS";

	private final static String INCOME_DIALOG_LABELS_OCCURS_HOW = "INCOME.DIALOG.LABELS.OCCURS.HOW";

	private final static String INCOME_DIALOG_LABELS_NOTE = "INCOME.DIALOG.LABELS.NOTE";

	private final static String INCOME_DIALOG_LABELS_OCCURS_VALUE = "INCOME.DIALOG.LABELS.OCCURS.VALUE";

	/*
	 * INCOME.DIALOG.LABELS.NOTE=Nota INCOME.DIALOG.LABELS.OCCURS.VALUE=Ogni
	 */
	private JComboBox comboBoxCategoria;

	private JComboBox comboBoxSubCategoria;

	private JFormattedTextField formattedTextFieldValue;

	private JCheckBox checkBoxOccurs;

	private JComboBox comboBoxRecurrence;

	private JLabel lblQuando;

	private JPanel panel;

	private JSpinner jSpinnerEach;

	private JLabel lblEach;

	private JDatePicker datePicker;

	private JTextArea textAreaNote;

	private Budget budget;

	private boolean updateViews;

	public BudgetEntryDialog(JFrame owner, Budget budget, BudgetEntry entry, boolean income)
	{
		super(owner);
		this.setModal(true);
		this.budgetEntry = entry;
		this.income = income;
		this.budget = budget;
		String title = "";
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
		panel = new JPanel();
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[]
		{ FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[]
		{ FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC, }));
		JLabel lblValore = new JLabel(labels.getString(INCOME_DIALOG_LABELS_VALUE));
		lblValore.setFont(FontUtils.getFontForLabelInsert());
		lblValore.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblValore, "2, 2, right, default");
		formattedTextFieldValue = new JFormattedTextField(decimalFormat);
		panel.add(formattedTextFieldValue, "4, 2, left, default");
		formattedTextFieldValue.setColumns(10);
		formattedTextFieldValue.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		JLabel lblData = new JLabel(labels.getString(INCOME_DIALOG_LABELS_DATE));
		lblData.setFont(FontUtils.getFontForLabelInsert());
		lblData.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblData, "2, 4, right, default");
		MaskFormatter formatter = null;
		try
		{
			formatter = new MaskFormatter("##/##/####");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		formatter.setPlaceholderCharacter('_');
		datePicker = JDateComponentFactory.createJDatePicker(new UtilDateModel(), new CalendarFormatter(new SimpleDateFormat("dd/MM/yyyy")));
		panel.add((Component) datePicker, "4, 4, left, default");
		JLabel lblCategoria = new JLabel(labels.getString(INCOME_DIALOG_LABELS_CATEGORY));
		lblCategoria.setFont(FontUtils.getFontForLabelInsert());
		lblCategoria.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblCategoria, "2, 6, right, default");
		comboBoxCategoria = new JComboBox();

		comboBoxCategoria.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				categoriaChangedEvent();
			}
		});
		panel.add(comboBoxCategoria, "4, 6, left, default");
		JLabel lblSottoCategoria = new JLabel(labels.getString(INCOME_DIALOG_LABELS_SUB_CATEGORY));
		lblSottoCategoria.setFont(FontUtils.getFontForLabelInsert());
		lblSottoCategoria.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblSottoCategoria, "2, 8, right, default");
		comboBoxSubCategoria = new JComboBox();
		panel.add(comboBoxSubCategoria, "4, 8, left, default");

		JLabel lblRicorre = new JLabel(labels.getString(INCOME_DIALOG_LABELS_OCCURS));
		lblRicorre.setFont(FontUtils.getFontForLabelInsert());
		lblRicorre.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblRicorre, "2, 10, right, default");

		checkBoxOccurs = new JCheckBox();
		checkBoxOccurs.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				checkBoxOccursClicked();
			}
		});
		panel.add(checkBoxOccurs, "4, 10, left, default");

		lblQuando = new JLabel(labels.getString(INCOME_DIALOG_LABELS_OCCURS_HOW));
		lblQuando.setFont(FontUtils.getFontForLabelInsert());
		lblQuando.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblQuando, "2, 12, right, default");
		lblQuando.setVisible(false);

		comboBoxRecurrence = new JComboBox();
		panel.add(comboBoxRecurrence, "4, 12, fill, default");
		RecurrenceDao recurrenceDao = new RecurrenceDao();
		List<Recurrence> recurrences = recurrenceDao.findAll();
		DefaultComboBoxModel model = (DefaultComboBoxModel) comboBoxRecurrence.getModel();
		model.addElement(null);
		for (Recurrence recurrence : recurrences)
		{
			model.addElement(recurrence);
		}
		comboBoxRecurrence.setVisible(false);

		JLabel lblNote = new JLabel(labels.getString(INCOME_DIALOG_LABELS_NOTE));
		lblNote.setFont(FontUtils.getFontForLabelInsert());
		lblNote.setForeground(FontUtils.getFontColorForLabelInsert());
		panel.add(lblNote, "2, 16, right, default");

		textAreaNote = new JTextArea();
		textAreaNote.setRows(5);
		textAreaNote.setColumns(10);

		JScrollPane scrollPane = new JScrollPane(textAreaNote);
		panel.add(scrollPane, "4, 16, fill, fill");
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

	protected void occursChangedEvent()
	{
		/*
		 * JLabel lblTmp = new JLabel("Tmp"); panel.add(lblTmp, "2, 14, right, default"); INCOME_DIALOG_LABELS_OCCURS_VALUE txtTmp = new JTextField();
		 * txtTmp.setText("Tmp"); panel.add(txtTmp, "4, 14, fill, default"); txtTmp.setColumns(10);
		 */
		if (this.getComboBoxOccurs().getSelectedItem() != null)
		{
			// OccursDao occursDao = new OccursDao();
			// Occurs occurs = (Occurs) this.getComboBoxOccurs().getSelectedItem();
			// lblEach = new JLabel(labels.getString(INCOME_DIALOG_LABELS_OCCURS_VALUE));
			// panel.add(lblEach, "2, 14, right, default");
			//
			// jSpinnerEach = new JSpinner();
			// jSpinnerEach.setModel(new SpinnerNumberModel(new Integer(1), 1, null, new Integer(1)));
			// panel.add(jSpinnerEach, "4, 14, fill, default");
			// pack();

		}
		else
		{
			if (lblEach != null)
			{

				lblEach.setVisible(false);
				jSpinnerEach.setVisible(false);
				pack();
			}
		}
	}

	protected void checkBoxOccursClicked()
	{
		if (this.getCheckBoxOccurs().isSelected())
		{
			this.lblQuando.setVisible(true);
			this.comboBoxRecurrence.setVisible(true);
			this.pack();
		}
		else
		{
			this.lblQuando.setVisible(false);
			this.comboBoxRecurrence.setVisible(false);
		}
	}

	protected void saveIncome()
	{
		// TODO Auto-generated method stub
		BudgetEntryDao budgetEntryDao = new BudgetEntryDao();
		try
		{
			BigDecimal value = new BigDecimal(this.formattedTextFieldValue.getValue().toString());
			Date date = (Date) this.datePicker.getModel().getValue();
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
			Recurrence recurrence = new RecurrenceDao().getNoneRecurrence();
			if (this.comboBoxRecurrence.getSelectedIndex() != -1)
			{
				recurrence = (Recurrence) this.comboBoxRecurrence.getSelectedItem();
			}
			BudgetEntry budgetEntry = null;
			if (income)
			{
				budgetEntry = new Income(value, category, subCategory, date, this.budget, null, note, recurrence);
			}
			else
			{
				budgetEntry = new Expenses(value, category, subCategory, date, this.budget, null, note, recurrence);
			}
			budgetEntryDao.save(budgetEntry);
			this.updateViews = true;
			dispose();
		}
		catch (RuntimeException e)
		{
			logger.error("Errore duranta il salvataggio budgetEntry", e);
			MessagesUtils.showExceptionMessage(e);
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
		if (this.budgetEntry == null)
		{
			((UtilDateModel) this.datePicker.getModel()).setValue(new Date());
		}
		else
		{
			this.getFormattedTextFieldValue().setValue(this.budgetEntry.getValue());
			((UtilDateModel) this.datePicker.getModel()).setValue(budgetEntry.getDate());
			this.getComboBoxCategoria().setSelectedItem(this.budgetEntry.getCategory());
			this.getComboBoxSubCategoria().setSelectedItem(this.budgetEntry.getSubCategory());
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

	public JCheckBox getCheckBoxOccurs()
	{
		return checkBoxOccurs;
	}

	public JComboBox getComboBoxOccurs()
	{
		return comboBoxRecurrence;
	}

	public JLabel getLblQuando()
	{
		return lblQuando;
	}

	public JPanel getPanel()
	{
		return panel;
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
