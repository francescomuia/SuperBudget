package it.superbudget.persistence.dao;

import it.superbudget.enums.BudgetEntryTypes;
import it.superbudget.persistence.PersistenceManager;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntryView;
import it.superbudget.util.calendars.CalendarsUtils;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BudgetDao
{
	public void save(Budget budget)
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();
		em.getTransaction().begin();
		if (budget.isDefaultBudget())
		{
			Query query = em.createNamedQuery("replaceDefaultBudget");
			query.executeUpdate();
			em.flush();
		}
		em.merge(budget);
		em.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Budget> findAll()
	{
		Query query = PersistenceManager.getInstance().getEntityManager().createNamedQuery("findAllBudget");
		return query.getResultList();
	}

	public BigDecimal getYearlyIncome(Budget budget)
	{
		BudgetEntryDao budgetEntryDao = new BudgetEntryDao();
		List<BudgetEntryView> budgetEntryViews = budgetEntryDao.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.INCOME,
				CalendarsUtils.getInitCurrentYear(), CalendarsUtils.getFinishCurrentYear());
		BigDecimal sums = new BigDecimal(0.0);
		for (BudgetEntryView budgetEntryView : budgetEntryViews)
		{
			sums = sums.add(budgetEntryView.getValue());
		}
		return sums;
	}

	public BigDecimal getYearlyExpense(Budget budget)
	{
		BudgetEntryDao budgetEntryDao = new BudgetEntryDao();
		List<BudgetEntryView> budgetEntryViews = budgetEntryDao.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.EXPENSE,
				CalendarsUtils.getInitCurrentYear(), CalendarsUtils.getFinishCurrentYear());
		BigDecimal sums = new BigDecimal(0.0);
		for (BudgetEntryView budgetEntryView : budgetEntryViews)
		{
			sums = sums.add(budgetEntryView.getValue());
		}
		return sums;
	}

	public static void main(String[] args)
	{
		Budget budget = new Budget();
		budget.setBudgetId(1L);
		System.out.println(new BudgetDao().getYearlyExpense(budget));
	}
}
