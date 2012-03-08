package it.superbudget.util.icons;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconsUtils
{
	public static final String SAVE_BUTTON_ICON_IMAGE = "/images/save.png";

	public static final String UNDO_BUTTON_ICON_IMAGE = "/images/undo.png";

	public static final String WARNING_BUTTON_ICON_IMAGE = "/images/warning.png";

	public static Icon getSaveButtonIcon()
	{
		return new ImageIcon(IconsUtils.class.getResource(SAVE_BUTTON_ICON_IMAGE));
	}

	public static Icon getUndoButtonIcon()
	{
		return new ImageIcon(IconsUtils.class.getResource(UNDO_BUTTON_ICON_IMAGE));
	}

	public static Icon getErrorLabelIcon()
	{
		return new ImageIcon(IconsUtils.class.getResource(WARNING_BUTTON_ICON_IMAGE));
	}

	public static Icon getErrorMessageIcon()
	{
		return new ImageIcon(IconsUtils.class.getResource(WARNING_BUTTON_ICON_IMAGE));
	}
}
