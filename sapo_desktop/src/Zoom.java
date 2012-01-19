/*
 * Zoom.java
 *
 * Created on 16 de Novembro de 2004, 18:11 by Anderson Zanardi de Freitas
 */
import java.awt.event.MouseEvent;
import java.awt.Point;

/**
 *
 * @author  Anderson Zanardi de Freitas
 */
public class Zoom extends LeJAIPanel{
    
    Point p1 = new Point(0,0);
    Point p2 = new Point(0,0);
    
    /** Creates a new instance of Zoom */

    public Zoom(SAPO obj) {
        super(obj);
    }
    
    public void mousePressed(MouseEvent e) {
        //p1 = e.getPoint();
    }
    
    public void mouseReleased(MouseEvent e) {
        //p2 = e.getPoint();
        //p2.x = Math.abs(p2.x - p1.x);
        //p2.y = Math.abs(p2.y - p1.y);
        //sapo.setZoom(p2.x);
    }
    
     public void mouseDragged(MouseEvent e) {
         //p2 = e.getPoint();
         //p2.x = p2.x - p1.x;
         //p2.y = p2.y - p1.y;
         //sapo.jaiP[sapo.numImg].setZoomArea(p1, p2);
     }
     
     public void mouseClicked(MouseEvent e) {
        //
     }
    
     
}
