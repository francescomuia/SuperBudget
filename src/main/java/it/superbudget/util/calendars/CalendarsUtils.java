package it.superbudget.util.calendars;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarsUtils
{
	public static java.sql.Date getSqlInitCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlFinishCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlInitCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlInitYear(int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlFinishCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlFinishYear(int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static Date getInitMonth(int month)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static Date getFinishMonth(int month)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static java.sql.Date getSqlInitMonth(int month)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlInitMonth(int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlFinishMonth(int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static java.sql.Date getSqlFinishMonth(int month)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public static Date getInitCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		return CalendarsUtils.getInitMonth(calendar.get(Calendar.MONTH));
	}

	public static Date getFinishCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		return CalendarsUtils.getFinishMonth(calendar.get(Calendar.MONTH));
	}

	public static Date getInitCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		return calendar.getTime();
	}

	public static Date getFinishCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		return calendar.getTime();
	}

	public static List<String> getMonthNames()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		List<String> monthNames = new ArrayList<String>(12);
		for (int count = 0; count < 12; count++)
		{
			String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			monthNames.add(name.substring(0, 1).toUpperCase() + name.substring(1));
			calendar.add(Calendar.MONTH, 1);
		}
		return monthNames;
	}

	public static String getCurrentMonthLabel()
	{
		Calendar calendar = Calendar.getInstance();
		String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static String getCurrentMonthLabel(Date date)
	{
		if (date == null)
		{
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static int getCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static int getCurrentYear(Date date)
	{
		if (date == null)
		{
			return 0;
		}
		else
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar.get(Calendar.YEAR);
		}
	}

	public static String getCurrentQuartelyMonthLabel()
	{
		return getCurrentQuartelyMonthLabel(new Date());
	}

	public static String getCurrentQuartelyMonthLabel(Date date)
	{
		if (date == null)
		{
			return "";
		}
		else
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int month = calendar.get(Calendar.MONTH);
			if (month > 0 && month <= 3)
			{
				calendar.set(Calendar.MONDAY, Calendar.JANUARY);
				String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				return name.substring(0, 1).toUpperCase() + name.substring(1);
			}
			else if (month > 3 && month <= 6)
			{
				calendar.set(Calendar.MONDAY, Calendar.APRIL);
				String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				return name.substring(0, 1).toUpperCase() + name.substring(1);
			}
			else if (month > 6 && month <= 9)
			{
				calendar.set(Calendar.MONDAY, Calendar.JULY);
				String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				return name.substring(0, 1).toUpperCase() + name.substring(1);
			}
			else if (month > 9 && month <= 12)
			{
				calendar.set(Calendar.MONDAY, Calendar.OCTOBER);
				String name = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				return name.substring(0, 1).toUpperCase() + name.substring(1);
			}
		}
		return null;
	}

	public static Calendar getCurrentWeekends()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		return calendar;
	}

	public static Calendar getInitYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		return calendar;
	}

	public static Calendar getFinishYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		return calendar;
	}
}
