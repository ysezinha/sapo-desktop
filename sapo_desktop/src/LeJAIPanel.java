import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Created on 02/11/2004
 *
 */

/**
 *
 * @author  Edison Puig Maldonado
 */ 

class LeJAIPanel implements MouseListener, MouseMotionListener, KeyListener {
    protected final SAPO sapo;
    protected boolean adicionaPonto = true;
	/**
	 * @param sapo
	 */
    LeJAIPanel(SAPO sapo) {
		this.sapo = sapo;
	}
    public void acoplar(int num) {
        sapo.jif[num].jaiP.addMouseListener(this);
        sapo.jif[num].jaiP.addMouseMotionListener(this);
        sapo.jif[num].jaiP.addKeyListener(this);
        sapo.jif[num].jaiP.setAdiciona(true);
        adicionaPonto = true;
    }
    public void desacoplar(int num) {
        sapo.jif[num].jaiP.removeMouseListener(this);
        sapo.jif[num].jaiP.removeMouseMotionListener(this);
        sapo.jif[num].jaiP.removeKeyListener(this);
        sapo.jif[num].jaiP.setPaint(false, false, false, false);
        adicionaPonto = false;
    }
    
    public void reacoplar(int num) {
    	desacoplar(num);
    	acoplar(num);
    }
    
    public void desacoplarMantendo(int num) {
        sapo.jif[num].jaiP.setAdiciona(false);
        adicionaPonto = false;
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited  (MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e ) {}
}