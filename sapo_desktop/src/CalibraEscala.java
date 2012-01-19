
import java.awt.event.MouseEvent;

/*
 * Created on 02/11/2004
 *
 */

/**
 *
 * @author  Edison Puig Maldonado 
 */

class CalibraEscala extends LeJAIPanel {
    
    /**
     * @param sapo
     */
    CalibraEscala(SAPO obj) {
        super(obj);
    }

    public void mousePressed(MouseEvent e) {
        int xp = e.getX();
        int yp = e.getY();
        sapo.jpCalEscala.jpCalEscalaX.jlCEXX1.setText(Integer.toString(xp));
        sapo.jpCalEscala.jpCalEscalaX.jlCEXY1.setText(Integer.toString(yp));
        sapo.jpCalEscala.jpCalEscalaY.jlCEYX1.setText(Integer.toString(xp));
        sapo.jpCalEscala.jpCalEscalaY.jlCEYY1.setText(Integer.toString(yp));
        sapo.jif[sapo.numImg].jaiP.mousePressedEscala(xp, yp);
    }
    
    public void mouseReleased(MouseEvent e) {
    	sapo.jpCalEscala.jpCalEscalaX.jlCEXX2.setText(Integer.toString(e.getX()));
    	sapo.jpCalEscala.jpCalEscalaX.jlCEXY2.setText(Integer.toString(e.getY()));
    	sapo.jpCalEscala.jpCalEscalaY.jlCEYX2.setText(Integer.toString(e.getX()));
    	sapo.jpCalEscala.jpCalEscalaY.jlCEYY2.setText(Integer.toString(e.getY()));
    }
}