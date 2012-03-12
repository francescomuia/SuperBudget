package it.superbudget.util.calendars;

import java.util.Calendar;

public class CalendarsUtils
{
	public static java.sql.Date getInitCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getFinishCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getInitCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getFinishCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

}
