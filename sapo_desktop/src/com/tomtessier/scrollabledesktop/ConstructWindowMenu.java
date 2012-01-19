package com.tomtessier.scrollabledesktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 * This class constructs the "Window" menu items for use by
 * {@link com.tomtessier.scrollabledesktop.DesktopMenu DesktopMenu}.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  11-Aug-2001
 */


public class ConstructWindowMenu implements ActionListener {

      private DesktopMediator desktopMediator;


    /**
     * creates the ConstructWindowMenu object.
     *
     * @param sourceMenu the source menu to apply the menu items
     * @param desktopMediator a reference to the DesktopMediator
     * @param tileMode the current tile mode (tile or cascade)
     */
      public ConstructWindowMenu(JMenu sourceMenu, 
                               DesktopMediator desktopMediator,
                               boolean tileMode) {
            this.desktopMediator = desktopMediator;
            constructMenuItems(sourceMenu, tileMode);
      }

    /**
     * constructs the actual menu items.
     *
     * @param sourceMenu the source menu to apply the menu items
     * @param tileMode the current tile mode
     *
     * alterado em 17/02/2004 - Edison Puig Maldonado
     */
      private void constructMenuItems(JMenu sourceMenu, boolean tileMode) {
		
        BaseMenuItem tile = new BaseMenuItem(this, "Lado a Lado", KeyEvent.VK_T, -1); // Puig
        tile.setBackground(java.awt.SystemColor.menu); // Puig
        tile.setFont(sourceMenu.getFont()); // Puig
        sourceMenu.add(tile);
        BaseMenuItem cascade = new BaseMenuItem(this, "Em Cascata", KeyEvent.VK_C, -1); // Puig
        cascade.setBackground(java.awt.SystemColor.menu); // Puig
        cascade.setFont(sourceMenu.getFont()); // Puig
        sourceMenu.add(cascade);
        sourceMenu.addSeparator();

        JMenu autoMenu = new JMenu("Auto");
        autoMenu.setBackground(java.awt.SystemColor.menu); // Puig
        autoMenu.setFont(sourceMenu.getFont()); // Puig
        autoMenu.setMnemonic(KeyEvent.VK_U); // Puig
        ButtonGroup autoMenuGroup = new ButtonGroup();
        JRadioButtonMenuItem radioItem = new BaseRadioButtonMenuItem(this, 
                    " Lado a Lado", KeyEvent.VK_T, -1, tileMode);
        radioItem.setBackground(java.awt.SystemColor.menu); // Puig
        radioItem.setFont(sourceMenu.getFont());// Puig
        autoMenu.add(radioItem);
        autoMenuGroup.add(radioItem);

        radioItem = new BaseRadioButtonMenuItem(this, 
                    " Cascata", KeyEvent.VK_C, -1, !tileMode);
        radioItem.setBackground(java.awt.SystemColor.menu); // Puig
        radioItem.setFont(sourceMenu.getFont()); // Puig
        autoMenu.add(radioItem);
        autoMenuGroup.add(radioItem);

        sourceMenu.add(autoMenu);
        sourceMenu.addSeparator();

        BaseMenuItem close = new BaseMenuItem(this, "Fechar", KeyEvent.VK_S, KeyEvent.VK_Z); // Puig
        close.setBackground(java.awt.SystemColor.menu); // Puig
        close.setFont(sourceMenu.getFont()); // Puig
        sourceMenu.add(close);
        sourceMenu.addSeparator();

      }


      /**
        * propogates actionPerformed menu event to the DesktopMediator reference
        *
        * @param e the ActionEvent to propogate
        */
      public void actionPerformed(ActionEvent e) {
            desktopMediator.actionPerformed(e);
      }

}