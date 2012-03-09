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

@NamedQuery(name = "getBudgetIncome", query = "Select t From Revenues t where t.budget =:budget order by t.date"),
		@NamedQuery(name = "getBudgetExpenses", query = "Select t From Revenues t where t.budget =:budget order by t.date") })
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
	private Date date;

	@ManyToOne
	private Budget budget;

	private boolean recursive;

	private Occurs occurs;

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

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
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
}
