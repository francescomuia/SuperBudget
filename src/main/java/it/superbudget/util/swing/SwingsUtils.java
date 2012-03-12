package it.superbudget.util.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;

public class SwingsUtils
{
	public static Frame getFrameOwner(Component c)
	{
		Container parent = c.getParent();
		while (parent != null && !(parent instanceof Frame))
		{
			System.out.println("PARENT " + parent);
			parent = parent.getParent();
		}
		if (parent != null)
		{
			return (Frame) parent;
		}
		else
		{
			return null;
		}
	}
}
