package it.superbudget.gui.jtable;

import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.persistence.entities.BudgetEntryView;
import it.superbudget.persistence.entities.Expenses;
import it.superbudget.persistence.entities.Income;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class BudgetEntryTableModel extends DefaultTableModel
{
	private static final long serialVersionUID = 1L;

	Class<?>[] columnTypes = new Class<?>[]
	{ Integer.class, Date.class, Date.class, String.class, String.class, String.class, BigDecimal.class, String.class };

	boolean[] columnEditables = new boolean[]
	{ false, false, false, false, false, false, false, false };

	private List<BudgetEntryView> budgetEntries;

	public BudgetEntryTableModel(String[] columNames)
	{
		super(new Object[][]
		{}, columNames);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return columnTypes[columnIndex];
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return columnEditables[column];
	}

	public void populate(List<BudgetEntryView> budgetEntries)
	{
		this.budgetEntries = budgetEntries;
		for (BudgetEntryView budgetEntry : budgetEntries)
		{
			this.addRow(new Object[]
			{ budgetEntry.getBudgetEntryViewId(), budgetEntry.getDateFrom(), budgetEntry.getDateTo(), budgetEntry.getCategory(),
					budgetEntry.getSubCategory() != null ? budgetEntry.getSubCategory() : null, budgetEntry.getNote(), budgetEntry.getValue(),
					budgetEntry.getRecurrence() });
		}
	}

	public BudgetEntry getBudgetEntry(int row)
	{
		BudgetEntryView budgetEntryView = this.budgetEntries.get(row);
		if ("I".equals(budgetEntryView.getType()))
		{
			BudgetEntry budgetEntry = new Income(budgetEntryView.getValue(), budgetEntryView.getCategory(), budgetEntryView.getSubCategory(),
					budgetEntryView.getDateFrom(), budgetEntryView.getDateTo(), budgetEntryView.getBudget(), budgetEntryView.getRecurrenceValue(),
					budgetEntryView.getNote(), budgetEntryView.getRecurrence());
			budgetEntry.setBudgetEntryId(budgetEntryView.getBudgetEntryId());
			return budgetEntry;
		}
		else
		{
			BudgetEntry budgetEntry = new Expenses(budgetEntryView.getValue(), budgetEntryView.getCategory(), budgetEntryView.getSubCategory(),
					budgetEntryView.getDateFrom(), budgetEntryView.getDateTo(), budgetEntryView.getBudget(), budgetEntryView.getRecurrenceValue(),
					budgetEntryView.getNote(), budgetEntryView.getRecurrence());
			budgetEntry.setBudgetEntryId(budgetEntryView.getBudgetEntryId());
			return budgetEntry;
		}
	}
}
