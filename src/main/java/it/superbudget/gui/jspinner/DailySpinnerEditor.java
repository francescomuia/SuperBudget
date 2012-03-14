package it.superbudget.gui.jspinner;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DailySpinnerEditor extends JPanel implements ChangeListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private SimpleDateFormat df;

	private JTextField textField;

	private SpinnerModel model;

	public DailySpinnerEditor(JSpinner spinner, String format, boolean nullable)
	{
		spinner.addChangeListener(this);
		this.model = spinner.getModel();
		textField = new JTextField();
		textField.setEditable(false);
		df = new SimpleDateFormat(format);
		this.setLayout(new BorderLayout());
		this.add(textField);
		if (nullable)
		{
			JLabel cancelButton = new JLabel(new ImageIcon(WeeklySpinnerEditor.class.getResource("/images/delete12.png")));
			cancelButton.setVerticalAlignment(SwingConstants.TOP);
			cancelButton.addMouseListener(this);
			this.add(cancelButton, BorderLayout.WEST);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		JSpinner spinner = (JSpinner) e.getSource();
		if (spinner.getValue() == null)
		{
			this.textField.setText(null);
		}
		else
		{
			this.textField.setText(df.format(((Calendar) spinner.getValue()).getTime()));
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
