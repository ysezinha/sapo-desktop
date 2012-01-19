import java.awt.event.MouseEvent;

/*
 * Created on 02/11/2004
 *
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

class Desenha extends LeJAIPanel {
    
    /**
     * @param sapo
     */
    Desenha(SAPO obj) {
            super(obj);
    }

    public int X1,X2,Y1,Y2=0;
    
    public void zeraTudo() {
        X1=X2=Y1=Y2=0;
    }
    
    public void mousePressed(MouseEvent e) {
        X1=e.getX();
        Y1=e.getY();
    }//mousePressed
    
    public void mouseReleased(MouseEvent e) {
        X2=e.getX();
        Y2=e.getY();
    } //mouseReleased
}