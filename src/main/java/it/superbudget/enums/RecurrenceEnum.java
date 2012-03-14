package it.superbudget.enums;

import it.superbudget.persistence.entities.Recurrence;

public enum RecurrenceEnum
{
	NONE, YEARLY, MONTLY, DAILY, WEEKLY, BIWEEKLY, QUARTERLY, WEEKENDS;

	public boolean equals(Recurrence recurrence)
	{
		if (recurrence != null)
		{
			int ordinal = this.ordinal();
			return (++ordinal) == recurrence.getRecurrenceId();
		}
		return false;
	}
}
