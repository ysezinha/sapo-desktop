
import java.awt.event.MouseEvent;


/*
 * Created on 02/11/2004
 *
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

class CalibraVertical extends LeJAIPanel {
    
    CalibraVerticalPanel cvPanel;
    
    CalibraVertical(SAPO sapo) {
	super(sapo);
        this.cvPanel = sapo.cvPanel;
    }

    public void mousePressed(MouseEvent e) {
        int xp = e.getX();
        int yp = e.getY();
        cvPanel.jlCVX1.setText(Integer.toString(xp));
        cvPanel.jlCVY1.setText(Integer.toString(yp));
        sapo.jif[sapo.numImg].jaiP.mousePressedEscala(xp, yp);
    }//mousePressed
    
    public void mouseReleased(MouseEvent e) {
        int xp = e.getX();
        int yp = e.getY();
        cvPanel.jlCVX2.setText(Integer.toString(xp));
        cvPanel.jlCVY2.setText(Integer.toString(yp));
        int X1 = Integer.parseInt(cvPanel.jlCVX1.getText());
        int Y1 = Integer.parseInt(cvPanel.jlCVY1.getText());
        int X2 = Integer.parseInt(cvPanel.jlCVX2.getText());
        int Y2 = Integer.parseInt(cvPanel.jlCVY2.getText());
        double angulo = Math.atan2((X2-X1),(Y2-Y1)); 
        cvPanel.jlCVAng.setText("Ângulo = "+sapo.numFormat.format(Math.toDegrees(angulo))+" graus");
    } //mouseReleased
}