package it.superbudget.util.messages;

import it.superbudget.util.bundles.ResourcesBundlesUtil;
import it.superbudget.util.dialog.ExceptionDialog;
import it.superbudget.util.icons.IconsUtils;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessagesUtils
{
	public static final String ERROR_MESSAGE_TITLE = "ERROR.MESSAGE.TITLE";

	public static void showErrorMessage(Component parent, String message)
	{
		JOptionPane.showMessageDialog(parent, message, ResourcesBundlesUtil.getLabelsBundles().getString(ERROR_MESSAGE_TITLE),
				JOptionPane.ERROR_MESSAGE, IconsUtils.getErrorMessageIcon());
	}

	public static void showExceptionMessage(Exception e)
	{
		ExceptionDialog dialog = new ExceptionDialog(e);
		dialog.setVisible(true);
	}
}
