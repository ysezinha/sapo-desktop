package com.dati.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * EscapeDialog.java
 *
 * Created on 7 de Fevereiro de 2005, 18:27
 */

/**
 *
 * @author Edison Puig Maldonado
 */

public class EscapeDialog extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = -3326532146157149855L;

public EscapeDialog() {
    this((Frame)null, false);
  }
  public EscapeDialog(Frame owner) {
    this(owner, false);
  }
  public EscapeDialog(Frame owner, boolean modal) {
    this(owner, null, modal);
  }
  public EscapeDialog(Frame owner, String title) {
    this(owner, title, false);     
  }
  public EscapeDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
  }
  public EscapeDialog(Dialog owner) {
    this(owner, false);
  }
  public EscapeDialog(Dialog owner, boolean modal) {
    this(owner, null, modal);
  }
  public EscapeDialog(Dialog owner, String title) {
    this(owner, title, false);     
  }
  public EscapeDialog(Dialog owner, String title, boolean modal) {
    super(owner, title, modal);
  }
  
  protected JRootPane createRootPane() {
    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        close();
        setVisible(false);
      }
    };
    JRootPane rootPane = new JRootPane();
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    return rootPane;
  }
  
  public void processWindowEvent(WindowEvent we) {
      super.processWindowEvent(we);
  }
  
  public void close() {
      processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSED));
  }
}

