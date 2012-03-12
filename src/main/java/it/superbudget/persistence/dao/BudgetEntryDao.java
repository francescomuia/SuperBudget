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

public class BudgetEntryDao
{

	public List<BudgetEntryView> getIncomes(Budget budget)
	{
		return this.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.INCOME, CalendarsUtils.getInitCurrentYear(),
				CalendarsUtils.getFinishCurrentYear());
	}

	public List<BudgetEntryView> getExpenses(Budget budget)
	{
		return this.getBudgetEntry(budget.getBudgetId(), BudgetEntryTypes.EXPENSE, CalendarsUtils.getInitCurrentYear(),
				CalendarsUtils.getFinishCurrentYear());
	}

	public List<BudgetEntryView> getBudgetEntry(Long budgetId, BudgetEntryTypes type, Date startDate, Date endDate)
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();
		try
		{
			em.getTransaction().begin();
			Connection connection = em.unwrap(Connection.class);
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
}
