package it.superbudget.gui.jspinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DailySpinnerModel implements SpinnerModel
{
	private List<ChangeListener> changeListeners;

	private Calendar calendar;

	private boolean nullValueAllowed;

	public DailySpinnerModel(boolean nullValueAllowed)
	{
		this.changeListeners = new ArrayList<ChangeListener>();
		calendar = Calendar.getInstance();
		this.nullValueAllowed = nullValueAllowed;
	}

	@Override
	public Object getValue()
	{
		return calendar;
	}

	protected void invokeChangeListener()
	{
		for (ChangeListener changeListener : this.changeListeners)
		{
			ChangeEvent event = new ChangeEvent(this);
			changeListener.stateChanged(event);
		}
	}

	@Override
	public void setValue(Object value)
	{
		this.calendar = (Calendar) value;
		this.invokeChangeListener();

	}

	@Override
	public Object getNextValue()
	{
		if (calendar == null && nullValueAllowed)
		{
			calendar = Calendar.getInstance();
		}
		else if (nullValueAllowed)
		{
			if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getMaximum(Calendar.DAY_OF_MONTH)
					&& calendar.get(Calendar.MONTH) == calendar.getMaximum(Calendar.MONTH))
			{
				calendar = null;
			}
			else
			{
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		else
		{
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		this.invokeChangeListener();
		return calendar;
	}

	@Override
	public Object getPreviousValue()
	{
		if (calendar == null && nullValueAllowed)
		{
			calendar = Calendar.getInstance();
		}
		else if (nullValueAllowed)
		{
			if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getMinimum(Calendar.DAY_OF_MONTH)
					&& calendar.get(Calendar.MONTH) == calendar.getMinimum(Calendar.MONTH))
			{
				calendar = null;
			}
			else
			{
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		}
		else
		{
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}

		this.invokeChangeListener();
		return calendar;
	}

	@Override
	public void addChangeListener(ChangeListener l)
	{
		changeListeners.add(l);
	}

	@Override
	public void removeChangeListener(ChangeListener l)
	{
		changeListeners.remove(l);
	}

	public Calendar getCalendar()
	{
		return calendar;
	}

	public void setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
	}

	public boolean isNullValueAllowed()
	{
		return nullValueAllowed;
	}

	public void setNullValueAllowed(boolean nullValueAllowed)
	{
		this.nullValueAllowed = nullValueAllowed;
	}

}
