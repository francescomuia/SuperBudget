package it.superbudget.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries(value =
{
		@NamedQuery(name = "findAllRecurrences", query = "Select t from Recurrence t order by t.recurrenceId"),
		@NamedQuery(name = "getRecurrenceForBudgetEntryDialog", query = "Select t from Recurrence t where t.recurrenceId <> 1 order by t.recurrenceId") })
@Entity
public class Recurrence implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	private Long recurrenceId;

	private String name;

	private int calendarField;

	private int calendarIncrement;

	public Long getRecurrenceId()
	{
		return recurrenceId;
	}

	public void setRecurrenceId(Long recurrenceId)
	{
		this.recurrenceId = recurrenceId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((recurrenceId == null) ? 0 : recurrenceId.hashCode());
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
		Recurrence other = (Recurrence) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (recurrenceId == null)
		{
			if (other.recurrenceId != null)
				return false;
		}
		else if (!recurrenceId.equals(other.recurrenceId))
			return false;
		return true;
	}

	public String toString()
	{
		return name;
	}

	public int getCalendarField()
	{
		return calendarField;
	}

	public void setCalendarField(int calendarField)
	{
		this.calendarField = calendarField;
	}

	public int getCalendarIncrement()
	{
		return calendarIncrement;
	}

	public void setCalendarIncrement(int calendarIncrement)
	{
		this.calendarIncrement = calendarIncrement;
	}
}
