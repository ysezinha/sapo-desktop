package com.dati.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

public class FormUtilities {

	/** Posiciona um componente no centro do form */
	public static void centerInForm(Component form, Component w) {
		Dimension dForm = form.getSize();
		Dimension dWindow = w.getSize();
		Point pForm = form.getLocationOnScreen();
		if (dForm.width==0) return;
		int left = (int)pForm.getX() + dForm.width/2-dWindow.width/2;
		int top  = (int)pForm.getY() + dForm.height/2-dWindow.height/2;
		if (top<0) top = 0;
		w.setLocation(left, top);
	}
	
}
