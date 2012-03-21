package it.superbudget.persistence.dao;

import it.superbudget.enums.BudgetEntryTypes;
import it.superbudget.persistence.PersistenceManager;
import it.superbudget.persistence.entities.Budget;
import it.superbudget.persistence.entities.BudgetEntry;
import it.superbudget.persistence.entities.BudgetEntryView;
import it.superbudget.util.calendars.CalendarsUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BudgetEntryDao
{

	public List<BudgetEntryView> getYearlyIncomes(Budget budget, int year)
	{
		return this.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.INCOME, CalendarsUtils.getSqlInitYear(year),
				CalendarsUtils.getSqlFinishYear(year));
	}

	public List<BudgetEntryView> getYearlyExpenses(Budget budget, int year)
	{
		return this.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.EXPENSE, CalendarsUtils.getSqlInitYear(year),
				CalendarsUtils.getSqlFinishYear(year));
	}

	public List<BudgetEntryView> getMontlyIncomes(Budget budget, int month, int year)
	{
		java.sql.Date init = CalendarsUtils.getSqlInitMonth(month, year);
		java.sql.Date finish = CalendarsUtils.getSqlFinishMonth(month, year);
		return this.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.INCOME, CalendarsUtils.getSqlInitMonth(month, year),
				CalendarsUtils.getSqlFinishMonth(month, year));
	}

	public List<BudgetEntryView> getMontlyExpenses(Budget budget, int month, Integer year)
	{
		return this.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.EXPENSE, CalendarsUtils.getSqlInitMonth(month, year),
				CalendarsUtils.getSqlFinishMonth(month, year));
	}

	public List<BudgetEntryView> getBudgetEntry(Long budgetId, BudgetEntryTypes type, Date startDate, Date endDate)
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();
		Connection connection = null;
		try
		{
			em.getTransaction().begin();
			connection = em.unwrap(Connection.class);
			CallableStatement callableStatement = connection.prepareCall("{call getBudgetEntry(?,?,?,?)}");
			callableStatement.setLong(1, budgetId);
			callableStatement.setString(2, type.getType());
			callableStatement.setDate(3, new java.sql.Date(startDate.getTime()));
			callableStatement.setDate(4, new java.sql.Date(endDate.getTime()));
			boolean hasResult = callableStatement.execute();
			List<BudgetEntryView> budgetEntryViews = new ArrayList<BudgetEntryView>();
			if (hasResult)
			{
				ResultSet resultSet = callableStatement.getResultSet();
				while (resultSet.next())
				{
					budgetEntryViews.add(new BudgetEntryView(resultSet));
				}
			}
			return budgetEntryViews;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			if (connection != null)
			{
				try
				{
					connection.close();
				}
				catch (SQLException e)
				{
				}
				em.getTransaction().rollback();
				em.close();
			}
		}
	}

	public void save(BudgetEntry budgetEntry)
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();
		em.getTransaction().begin();
		em.merge(budgetEntry);
		em.getTransaction().commit();
	}

	public static void main(String[] args)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		System.out.println(new BudgetEntryDao().getBudgetEntry(1L, BudgetEntryTypes.INCOME, new java.sql.Date(System.currentTimeMillis()),
				new java.sql.Date(c.getTimeInMillis())));
	}

	public BudgetEntry findBudgetEntry(Long budgetEntryId)
	{
		return PersistenceManager.getInstance().getEntityManager().find(BudgetEntry.class, budgetEntryId);
	}

	public boolean existBudgetEntry(BudgetEntry budgetEntry)
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();
		String queryString = "Select t From BudgetEntry t where t.budgetEntryId = :budgetEntryId and t.dateFrom = :dateFrom  and t.budget= :budget ";
		if (budgetEntry.getDateTo() == null)
		{
			queryString += "and t.dateTo is null";
		}
		else
		{
			queryString += "and t.dateTo = :dateTo";
		}
		Query query = em.createQuery(queryString);
		query.setParameter("budgetEntryId", budgetEntry.getBudgetEntryId());
		query.setParameter("dateFrom", budgetEntry.getDateFrom());
		if (budgetEntry.getDateTo() != null)
		{
			query.setParameter("dateTo", budgetEntry.getDateTo());
		}
		query.setParameter("budget", budgetEntry.getBudget());
		return !query.getResultList().isEmpty();
	}

	public void delete(BudgetEntry income)
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();
		em.getTransaction().begin();
		income = em.find(income.getClass(), income.getBudgetEntryId());
		em.remove(income);
		em.getTransaction().commit();
	}
}
