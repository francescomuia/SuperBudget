package it.superbudget.persistence.dao;

import it.superbudget.persistence.PersistenceManager;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;

import java.util.List;

import javax.persistence.Query;

public class BudgetEntryDao
{

	@SuppressWarnings("unchecked")
	public List<BudgetEntry> getIncomes(Budget budget)
	{
		Query query = PersistenceManager.getInstance().getEntityManager().createNamedQuery("getBudgetIncome");
		query.setParameter("budget", budget);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<BudgetEntry> getExpenses(Budget budget)
	{
		Query query = PersistenceManager.getInstance().getEntityManager().createNamedQuery("getBudgetExpenses");
		query.setParameter("budget", budget);
		return query.getResultList();
	}

}
