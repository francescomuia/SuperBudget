package it.superbudget.gui.jtable;

import it.superbudget.persistence.dao.BudgetEntryDao;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntryView;

import java.util.List;

import javax.swing.table.TableModel;

public class MontlyIncomesEntryTable extends BudgetEntryTable
{

	public MontlyIncomesEntryTable(TableModel dm)
	{
		super(dm);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void populate(Budget budget, Object... params)
	{

		this.getModel().setRowCount(0);
		BudgetEntryDao budgetDao = new BudgetEntryDao();
		List<BudgetEntryView> budgets = budgetDao.getMontlyIncomes(budget, (Integer) params[0], (Integer) params[1]);
		this.getModel().populate(budgets);
		this.packColumns(10);
	}
}
