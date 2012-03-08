package it.superbudget.persistence.dao;

import it.superbudget.persistence.PersistenceManager;
import it.superbudget.persistence.entities.Budget;

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
		return new BigDecimal(0.0);
	}

	public BigDecimal getYearlyExpense(Budget budget)
	{
		// TODO Auto-generated method stub
		return new BigDecimal(0.0);
	}

}
