import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

/**
 * 
 */

public class SmoothLabel extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2740189516606880413L;

	public SmoothLabel() {}

    public SmoothLabel(String text)
    {
        super(text);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
    }
}