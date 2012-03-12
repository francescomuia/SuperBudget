package it.superbudget.persistence.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("I")
public class Income extends BudgetEntry
{

	public Income(BigDecimal value, Category category, SubCategory subCategory, Date date, Budget budget, Integer recurrenceValue, String note,
			Recurrence recurrence)
	{
		super(value, category, subCategory, date, budget, recurrenceValue, note, recurrence);
		// TODO Auto-generated constructor stub
	}

	public Income()
	{
		super();
	}

	private static final long serialVersionUID = 1L;

}
