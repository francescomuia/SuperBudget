package it.superbudget.util.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

public class FontUtils
{
	public static Font getFontForLabelInsert()
	{
		return new Font("SansSerif", Font.BOLD, 14);
	}

	public static Font getFontForLabel()
	{
		return new Font("SansSerif", Font.PLAIN, 14);
	}

	public static Color getFontColorForLabelInsert()
	{
		return SystemColor.textHighlight;
	}

	public static Font getFontForLabelError()
	{
		return new Font("SansSerif", Font.BOLD, 14);
	}

	public static Color getFontColorForLabelError()
	{
		return Color.RED;
	}

}
