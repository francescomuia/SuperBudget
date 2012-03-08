package it.superbudget.util.dialog;

import it.superbudget.util.fonts.FontUtils;
import it.superbudget.util.icons.IconsUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ExceptionDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			ExceptionDialog dialog = new ExceptionDialog(new Exception("Prova eccezione"));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExceptionDialog(Exception e)
	{

		getContentPane().setLayout(new BorderLayout());
		this.setModal(true);
		this.setMaximumSize(new Dimension(400, 400));
		this.setPreferredSize(new Dimension(400, 400));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		JLabel lblMessage = new JLabel("<html>Si \u00E8 verificato un errore imprevisto </html>");
		lblMessage.setFont(FontUtils.getFontForLabelError());
		lblMessage.setForeground(FontUtils.getFontColorForLabelError());
		lblMessage.setIcon(IconsUtils.getErrorLabelIcon());
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblMessage, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		JTextArea textArea = new JTextArea(ExceptionUtils.getStackTrace(e));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		this.pack();
		this.setLocationRelativeTo(null);
	}
}
