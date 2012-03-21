package it.superbudget.gui.jtable;

import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public abstract class BudgetEntryTable extends JTable
{

	public BudgetEntryTable(TableModel dm)
	{
		super(dm);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	private static final long serialVersionUID = 1L;

	public BudgetEntry getSelectedBudgetEntry()
	{
		if (this.getSelectedRow() != -1)
		{
			return this.getModel().getBudgetEntry(this.getSelectedRow());
		}
		return null;
	}

	@Override
	public BudgetEntryTableModel getModel()
	{
		return (BudgetEntryTableModel) super.getModel();
	}

	public abstract void populate(Budget budget, Object... params);

	public void packColumns(int margin)
	{
		for (int c = 0; c < this.getColumnCount(); c++)
		{
			packColumn(c, margin);
		}
	}

	// Sets the preferred width of the visible column specified by vColIndex. The column
	// will be just wide enough to show the column head and the widest cell in the column.
	// margin pixels are added to the left and right
	// (resulting in an additional width of 2*margin pixels).
	public void packColumn(int vColIndex, int margin)
	{
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) this.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null)
		{
			renderer = this.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(this, col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		// Get maximum width of column data
		for (int r = 0; r < this.getRowCount(); r++)
		{
			renderer = this.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(this, this.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		// Add margin
		width += 2 * margin;

		// Set the width
		col.setPreferredWidth(width);
	}

}
