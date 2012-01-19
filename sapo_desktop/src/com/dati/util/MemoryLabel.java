package com.dati.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.Timer;

public class MemoryLabel extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8170145156588072790L;
	public long m_freeMemory;
    public long m_totalMemory;

    public MemoryLabel() {
        setAlignmentX(0.5f);
        setAlignmentY(0.5f);
        setOpaque(false);
        setBorder(BorderFactory.createLoweredBevelBorder()/* .createEmptyBorder(1, 4, 1, 2)*/);
    }

    public MemoryLabel(int delay) {
        this();
        computeText();
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                computeText();
                repaint();
            }
        });
        timer.setCoalesce(false);
        timer.setRepeats(true);
        timer.setInitialDelay(0);
        timer.setDelay(delay);
        timer.start();
    }

    public MemoryLabel(long freeMemory, long totalMemory) {
        this();
        computeText(freeMemory, totalMemory);
    }

    public void computeText() {
        Runtime runtime = Runtime.getRuntime();
        computeText((long) (runtime.freeMemory() / 1024.0), (long) (runtime.totalMemory() / 1024.0));
    }

    public void computeText(long freeMemory, long totalMemory) {
        m_freeMemory = freeMemory;
        m_totalMemory = totalMemory;
        int percent = (int) (((double) m_freeMemory / m_totalMemory) * 100.0);
        setText(" (" + Long.toString(m_freeMemory) + "KB " + "Livres" + 
        		" / " + Long.toString(m_totalMemory) + "KB " + "Alocados" + 
        		")   " + Integer.toString(percent) + "% " + "Livre");
    }

    public void paintComponent(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        int w = (int) (((double) m_freeMemory / m_totalMemory) * getWidth());
        gg.setColor(SystemColor.activeCaptionBorder);
        gg.drawRect(1,2,getWidth()-1,getHeight()-2);
        gg.setColor(SystemColor.white);
        gg.fillRect(getWidth()-w, 2, getWidth()-1, getHeight()-2);
        gg.setColor(SystemColor.lightGray);
        gg.fillRect(1, 2, getWidth()-w-1, getHeight()-2);
        gg.setColor(SystemColor.gray);
        super.paintComponent(g);
        gg.setPaintMode();
    }
}