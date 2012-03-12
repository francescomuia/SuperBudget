package it.superbudget.gui;

import it.superbudget.persistence.dao.BudgetDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.fonts.FontUtils;
import it.superbudget.util.icons.IconsUtils;
import it.superbudget.util.messages.MessagesUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BudgetDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private final String BUDGET_DIALOG_TITLE_NEW = "BUDGET.DIALOG.TITLE.NEW";

	private final String BUDGET_DIALOG_TITLE_OLD = "BUDGET.DIALOG.TITLE.OLD";

	private final String BUTTON_SAVE = "BUTTON.SAVE";

	private final String BUTTON_ANNULLA = "BUTTON.ANNULLA";

	private final String BUDGET_DIALOG_LABEL_NAME = "BUDGET.DIALOG.LABEL.NAME";

	private final String BUDGET_DIALOG_LABEL_BUDGET = "BUDGET.DIALOG.LABEL.BUDGET";

	private final String BUDGET_DIALOG_LABEL_DEFAULT_BUDGET = "BUDGET.DIALOG.LABEL.DEFAULT.BUDGET";

	private final String BUDGET_DIALOG_LABEL_ERROR_NAME = "BUDGET.DIALOG.LABEL.ERROR.NAME";

	private final String BUDGET_DIALOG_LABEL_ERROR_BUDGET = "BUDGET.DIALOG.LABEL.ERROR.BUDGET";

	private final String BUDGET_DIALOG_LABEL_MESSAGE_SAVE_ERROR = "BUDGET.DIALOG.LABEL.MESSAGE.SAVE.ERROR";

	private JTextField textFieldName;

	private JFormattedTextField formattedTextFieldSaldo;

	private Budget budget;

	private ResourceBundle labels;

	private JLabel lblErrorName;

	private JLabel lblErrorSaldo;

	private JCheckBox checkBoxDefault;

	/**
	 * Create the dialog.
	 * 
	 */
	public BudgetDialog(JFrame parent, boolean haveOtherBudget)
	{
		super(parent);
		labels = ResourcesBundlesUtil.getLabelsBundles();
		budget = new Budget();
		initialize(labels.getString(BUDGET_DIALOG_TITLE_NEW), haveOtherBudget);
	}

	/**
	 * Create the dialog.
	 * 
	 * 
	 */
	public BudgetDialog(JFrame parent, Budget budget)
	{
		super(parent);
		this.budget = budget;
		labels = ResourcesBundlesUtil.getLabelsBundles();
		initialize(labels.getString(BUDGET_DIALOG_TITLE_OLD), true);
	}

	private void initialize(String title, boolean haveOtherBudget)
	{
		this.setTitle(title);
		this.setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[]
		{ FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[]
				{ FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.GLUE_ROWSPEC, }));

		JLabel lblNome = new JLabel(labels.getString(BUDGET_DIALOG_LABEL_NAME));
		lblNome.setForeground(FontUtils.getFontColorForLabelInsert());
		lblNome.setFont(FontUtils.getFontForLabelInsert());
		contentPanel.add(lblNome, "2, 2");

		textFieldName = new JTextField();
		lblNome.setLabelFor(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.setText(budget.getName());
		contentPanel.add(textFieldName, "6, 2, left, default");

		lblErrorName = new JLabel();
		lblErrorName.setFont(FontUtils.getFontForLabelError());
		lblErrorName.setForeground(FontUtils.getFontColorForLabelError());
		lblErrorName.setIcon(IconsUtils.getErrorLabelIcon());
		lblErrorName.setVisible(false);
		contentPanel.add(lblErrorName, "8, 2");

		JLabel lblSaldoIniziale = new JLabel(labels.getString(BUDGET_DIALOG_LABEL_BUDGET));
		lblSaldoIniziale.setForeground(FontUtils.getFontColorForLabelInsert());
		lblSaldoIniziale.setFont(FontUtils.getFontForLabelInsert());
		contentPanel.add(lblSaldoIniziale, "2, 4");
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
		decimalFormat.setMaximumFractionDigits(3);
		formattedTextFieldSaldo = new JFormattedTextField(decimalFormat);
		formattedTextFieldSaldo.setColumns(10);
		formattedTextFieldSaldo.setValue(budget.getSaldo());
		formattedTextFieldSaldo.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		contentPanel.add(formattedTextFieldSaldo, "6, 4, left, default");
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton(labels.getString(BUTTON_SAVE));
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveBudget();
			}
		});
		okButton.setIcon(IconsUtils.getSaveButtonIcon());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton(labels.getString(BUTTON_ANNULLA));
		cancelButton.setIcon(IconsUtils.getUndoButtonIcon());
		buttonPane.add(cancelButton);
		contentPanel.setBackground(Color.white);

		lblErrorSaldo = new JLabel();
		lblErrorSaldo.setFont(FontUtils.getFontForLabelError());
		lblErrorSaldo.setForeground(FontUtils.getFontColorForLabelError());
		lblErrorSaldo.setIcon(IconsUtils.getErrorLabelIcon());
		lblErrorSaldo.setVisible(false);
		contentPanel.add(lblErrorSaldo, "8, 4");

		JLabel lblDefault = new JLabel(labels.getString(BUDGET_DIALOG_LABEL_DEFAULT_BUDGET));
		lblDefault.setFont(FontUtils.getFontForLabelInsert());
		lblDefault.setForeground(FontUtils.getFontColorForLabelInsert());
		contentPanel.add(lblDefault, "2, 6");

		checkBoxDefault = new JCheckBox();
		if (haveOtherBudget)
		{
			checkBoxDefault.setSelected(budget.isDefaultBudget());
		}
		else
		{
			checkBoxDefault.setSelected(true);
			checkBoxDefault.setEnabled(false);
		}
		contentPanel.add(checkBoxDefault, "6, 6, center, default");
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}

	protected void saveBudget()
	{
		boolean error = false;
		if (!(this.textFieldName.getText() != null && !this.textFieldName.getText().trim().isEmpty()))
		{
			getLblErrorName().setText(labels.getString(BUDGET_DIALOG_LABEL_ERROR_NAME));
			getLblErrorName().setVisible(true);
			error = true;
		}
		if (!(formattedTextFieldSaldo.getValue() != null && !formattedTextFieldSaldo.toString().trim().isEmpty()))
		{
			this.getLblErrorSaldo().setText(labels.getString(BUDGET_DIALOG_LABEL_ERROR_BUDGET));
			this.getLblErrorSaldo().setVisible(true);
			error = true;
		}
		if (error)
		{
			this.pack();
		}
		else
		{
			try
			{
				Budget budget = new Budget(this.textFieldName.getText().trim(), new BigDecimal(this.formattedTextFieldSaldo.getValue().toString()),
						this.checkBoxDefault.isSelected());
				new BudgetDao().save(budget);
				dispose();
			}
			catch (RuntimeException e)
			{
				MessagesUtils.showErrorMessage(this, labels.getString(BUDGET_DIALOG_LABEL_MESSAGE_SAVE_ERROR));
				Logger logger = Logger.getLogger(BudgetDialog.class);
				logger.error(labels.getString(BUDGET_DIALOG_LABEL_MESSAGE_SAVE_ERROR), e);
			}
		}
	}

	public JLabel getLblErrorName()
	{
		return lblErrorName;
	}

	public JLabel getLblErrorSaldo()
	{
		return lblErrorSaldo;
	}
}
