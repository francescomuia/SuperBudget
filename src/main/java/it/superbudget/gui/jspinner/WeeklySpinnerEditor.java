package it.superbudget.gui.jspinner;

import it.superbudget.util.bundles.ResourcesBundlesUtil;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WeeklySpinnerEditor extends JPanel implements ChangeListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private ResourceBundle labels;

	private static final String WEEKLYSPINNER_EDITOR_WEEK = "WEEKLYSPINNER.EDITOR.WEEK";

	private static final String WEEKLYSPINNER_EDITOR_WEEK_SHORT = "WEEKLYSPINNER.EDITOR.WEEK.SHORT";

	private JTextField textField;

	private SpinnerModel model;

	public WeeklySpinnerEditor(JSpinner spinner, boolean nullable)
	{
		spinner.addChangeListener(this);
		this.model = spinner.getModel();
		textField = new JTextField();
		textField.setEditable(false);

		labels = ResourcesBundlesUtil.getLabelsBundles();

		this.setLayout(new BorderLayout());
		this.add(textField);
		if (nullable)
		{
			JLabel cancelButton = new JLabel(new ImageIcon(WeeklySpinnerEditor.class.getResource("/images/delete12.png")));
			cancelButton.setVerticalAlignment(SwingConstants.TOP);
			cancelButton.addMouseListener(this);
			this.add(cancelButton, BorderLayout.WEST);
		}

		if (spinner.getValue() != null)
		{
			this.createText((Calendar) spinner.getValue());
		}

	}

	private void createText(Calendar calendar)
	{
		String text = labels.getString(WEEKLYSPINNER_EDITOR_WEEK_SHORT) + " N° " + calendar.get(Calendar.WEEK_OF_MONTH);
		Calendar tempCalendar = (Calendar) calendar.clone();
		tempCalendar.setTime(calendar.getTime());
		tempCalendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		Date firstDayOfWeek = tempCalendar.getTime();
		// getLastDayOfWeek(tempCalendar);
		// tempCalendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 6);
		// Date lastDayOfWeek = tempCalendar.getTime();
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		text = " Dal " + df.format(firstDayOfWeek);
		this.textField.setText(text);
	}

	public static void getLastDayOfWeek(Calendar date)
	{
		int day_of_week = date.get(Calendar.DAY_OF_WEEK);
		if (day_of_week != 1)
		{
			day_of_week = date.get(Calendar.DAY_OF_WEEK) - 2;
			date.add(Calendar.DATE, -day_of_week);
			date.add(Calendar.DATE, 6);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		JSpinner spinner = (JSpinner) e.getSource();
		WeeklySpinnerModel model = (WeeklySpinnerModel) spinner.getModel();
		if (model.getValue() == null)
		{
			this.textField.setText(null);
		}
		else
		{
			this.createText((Calendar) model.getValue());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.textField.setText(null);
		this.model.setValue(null);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

}
