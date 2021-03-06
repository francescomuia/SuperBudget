package it.superbudget.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries(value =
{

		@NamedQuery(name = "getBudgetIncome", query = "Select t From Income t where t.budget =:budget order by t.dateFrom"),
		@NamedQuery(name = "getBudgetExpenses", query = "Select t From Income t where t.budget =:budget order by t.dateFrom"),
		@NamedQuery(name = "existBudgetEntry", query = "Select t From BudgetEntry t where t.budgetEntryId = :budgetEntryId and t.dateFrom = :dateFrom and t.dateTo = :dateTo and t.budget= :budget") })
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("B")
public abstract class BudgetEntry implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long budgetEntryId;

	private BigDecimal value;

	private Category category;

	private SubCategory subCategory;

	@Temporal(TemporalType.DATE)
	private Date dateFrom;

	@Temporal(TemporalType.DATE)
	private Date dateTo;

	@ManyToOne
	private Budget budget;

	private Integer recurrenceValue;

	private String note;

	@ManyToOne
	private Recurrence recurrence;

	public BudgetEntry()
	{

	}

	public BudgetEntry(BigDecimal value, Category category, SubCategory subCategory, Date dateFrom, Date dateTo, Budget budget,
			Integer recurrenceValue, String note, Recurrence recurrence)
	{
		this.value = value;
		this.category = category;
		this.subCategory = subCategory;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.budget = budget;
		this.recurrenceValue = recurrenceValue;
		this.note = note;
		this.recurrence = recurrence;
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

	public Date getDateFrom()
	{
		return dateFrom;
	}

	public void setDateFrom(Date date)
	{
		this.dateFrom = date;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((budgetEntryId == null) ? 0 : budgetEntryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BudgetEntry other = (BudgetEntry) obj;
		if (budgetEntryId == null)
		{
			if (other.budgetEntryId != null)
				return false;
		}
		else if (!budgetEntryId.equals(other.budgetEntryId))
			return false;
		return true;
	}

	public Budget getBudget()
	{
		return budget;
	}

	public void setBudget(Budget budget)
	{
		this.budget = budget;
	}

	public SubCategory getSubCategory()
	{
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory)
	{
		this.subCategory = subCategory;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public Integer getRecurrenceValue()
	{
		return recurrenceValue;
	}

	public void setRecurrenceValue(Integer recurrenceValue)
	{
		this.recurrenceValue = recurrenceValue;
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

}
