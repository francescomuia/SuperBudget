package it.superbudget.persistence.entities;

import it.superbudget.persistence.PersistenceManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class BudgetEntryView implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long budgetEntryViewId;

	private Long budgetEntryId;

	private BigDecimal value;

	private Category category;

	private SubCategory subCategory;

	@Temporal(TemporalType.DATE)
	private Date dateFrom;

	private Date dateTo;

	@ManyToOne
	private Budget budget;

	private Integer recurrenceValue;

	private String note;

	private String type;

	@ManyToOne
	private Recurrence recurrence;

	public BudgetEntryView(ResultSet resultSet) throws SQLException
	{
		EntityManager em = PersistenceManager.getInstance().getEntityManager();

		budgetEntryViewId = resultSet.getLong(1);
		this.budget = em.find(Budget.class, resultSet.getLong(2));
		this.budgetEntryId = resultSet.getLong(3);
		this.category = em.find(Category.class, resultSet.getLong(4));
		this.dateFrom = resultSet.getDate(5);
		this.dateTo = resultSet.getDate(6);
		this.note = resultSet.getString(7);
		this.recurrence = em.find(Recurrence.class, resultSet.getLong(8));
		this.subCategory = em.find(SubCategory.class, resultSet.getLong(9));
		this.type = resultSet.getString(10);
		// this.recurrenceValue = resultSet.getInt(6);
		this.value = resultSet.getBigDecimal(11);
	}

	public Long getBudgetEntryViewId()
	{
		return budgetEntryViewId;
	}

	public void setBudgetEntryViewId(Long budgetEntryViewId)
	{
		this.budgetEntryViewId = budgetEntryViewId;
	}

	public Long getBudgetEntryId()
	{
		return budgetEntryId;
	}

	public void setBudgetEntryId(Long budgetEntryId)
	{
		this.budgetEntryId = budgetEntryId;
	}

	public BigDecimal getValue()
	{
		return value;
	}

	public void setValue(BigDecimal value)
	{
		this.value = value;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public SubCategory getSubCategory()
	{
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory)
	{
		this.subCategory = subCategory;
	}

	public Date getDateFrom()
	{
		return dateFrom;
	}

	public void setDateFrom(Date date)
	{
		this.dateFrom = date;
	}

	public Budget getBudget()
	{
		return budget;
	}

	public void setBudget(Budget budget)
	{
		this.budget = budget;
	}

	public Integer getRecurrenceValue()
	{
		return recurrenceValue;
	}

	public void setRecurrenceValue(Integer recurrenceValue)
	{
		this.recurrenceValue = recurrenceValue;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public Recurrence getRecurrence()
	{
		return recurrence;
	}

	public void setRecurrence(Recurrence recurrence)
	{
		this.recurrence = recurrence;
	}

	public Date getDateTo()
	{
		return dateTo;
	}

	public void setDateTo(Date dateTo)
	{
		this.dateTo = dateTo;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
