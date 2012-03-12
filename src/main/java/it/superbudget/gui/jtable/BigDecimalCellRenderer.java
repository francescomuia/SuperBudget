package it.superbudget.gui.jtable;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class BigDecimalCellRenderer extends DefaultTableCellRenderer
{
	private final NumberFormat numberFormat;

	public BigDecimalCellRenderer()
	{
		numberFormat = NumberFormat.getCurrencyInstance();
		numberFormat.setMaximumFractionDigits(3);
		numberFormat.setMinimumFractionDigits(3);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof BigDecimal)
		{
			String number = numberFormat.format(value);
			this.setText(number);
		}

		return this;
	}
}
