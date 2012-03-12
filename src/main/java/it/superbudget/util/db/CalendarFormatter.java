package it.superbudget.util.db;

import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class CalendarFormatter extends AbstractFormatter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Format format;

	public CalendarFormatter(Format format)
	{
		this.format = format;
	}

	@Override
	public Object stringToValue(String text) throws ParseException
	{
		if (text != null)
		{
			return format.parseObject(text);
		}
		return null;
	}

	@Override
	public String valueToString(Object value) throws ParseException
	{
		if (value != null)
		{
			if (value instanceof Calendar)
			{
				Calendar calendar = (Calendar) value;
				return format.format(calendar.getTime());
			}
			else
			{
				throw new ClassCastException(value.getClass() + " is not instance of java.util.Calendar");
			}
		}
		return null;
	}

}
