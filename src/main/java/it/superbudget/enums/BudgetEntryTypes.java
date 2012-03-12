package it.superbudget.enums;

public enum BudgetEntryTypes
{
	INCOME("I"), EXPENSE("E");

	private String type;

	BudgetEntryTypes(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
