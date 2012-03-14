package it.superbudget.gui.jspinner;

import java.util.Calendar;

public class WeekendsSpinnerModel extends DailySpinnerModel
{

	public WeekendsSpinnerModel(boolean nullValueAllowed)
	{
		super(nullValueAllowed);
	}

	@Override
	public Object getNextValue()
	{
		if (getCalendar() == null && isNullValueAllowed())
		{
			setCalendar(Calendar.getInstance());
		}
		else
		{
			Calendar calendar = getCalendar();
			calendar.add(Calendar.DAY_OF_MONTH, 3);
			while (calendar.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek())
			{
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			setCalendar(calendar);
		}
		this.invokeChangeListener();
		return getCalendar();
	}

	@Override
	public Object getPreviousValue()
	{
		if (getCalendar() == null && isNullValueAllowed())
		{
			setCalendar(Calendar.getInstance());
		}
		else
		{
			Calendar calendar = getCalendar();
			// calendar.add(Calendar.DAY_OF_MONTH, 3);
			while (calendar.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek())
			{
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			setCalendar(calendar);
		}
		this.invokeChangeListener();
		return getCalendar();

	}
}
