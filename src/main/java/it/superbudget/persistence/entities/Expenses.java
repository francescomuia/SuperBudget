package it.superbudget.persistence.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("E")
public class Expenses extends BudgetEntry
{

	public Expenses(BigDecimal value, Category category, SubCategory subCategory, Date date, Budget budget, Integer recurrenceValue, String note,
			Recurrence recurrence)
	{
		super(value, category, subCategory, date, budget, recurrenceValue, note, recurrence);
		// TODO Auto-generated constructor stub
	}

	public Expenses()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
