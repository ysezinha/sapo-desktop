import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedImageAdapter;

import org.jdesktop.swingworker.SwingWorker;

/*
 * FerramentasDesenho.java
 *
 * Created on 18 de Junho de 2005, 17:28
 */

/**
 *
 * @author Anderson Zanardi de Freitas
 */
public class FerramentasDesenho extends LeJAIPanel{
    
    int X1,X2,Y1,Y2,W,H;
    public String texto = "";
/*
 *Desenho tipo
 *0 = linha
 *1 = retangulo
 *2 = circulo
 *3 = texto
 */    
    /** Creates a new instance of FerramentasDesenho */
    public FerramentasDesenho(SAPO obj) {
        super(obj);
    }
    boolean keyBackSpace; 
    public void keyPressed(KeyEvent e){
        int txtLength = texto.length();
        int keyCode = e.getKeyCode();
        keyBackSpace = keyCode==KeyEvent.VK_BACK_SPACE;
        if((keyBackSpace)&&(txtLength>0))
            texto = texto.substring(0, txtLength-1);
    }
    
    public void keyTyped(KeyEvent e){
        if(!keyBackSpace)
            texto=texto+e.getKeyChar();
        sapo.jif[sapo.numImg].jaiP.setString(X1,X2,sapo.corDesenho, sapo.fonteAtual, texto);
    }
 
    public void mousePressed(MouseEvent e) {
        X1=e.getX();
        X2=e.getY();
        switch (sapo.desenhoTipo) {
            case 3: {
            	/*if ((e.getButton()==MouseEvent.BUTTON1) || (e.getButton()==MouseEvent.BUTTON2)){
                    javax.swing.JOptionPane.showMessageDialog(sapo, "Use o botão direito do mouse");
                }*/
                if(e.getButton()==MouseEvent.BUTTON3){
                    texto = javax.swing.JOptionPane.showInputDialog(sapo, "Entre um texto");
                }
                sapo.jif[sapo.numImg].jaiP.requestFocusInWindow();
                sapo.jif[sapo.numImg].jaiP.setString(X1,X2,sapo.corDesenho, sapo.fonteAtual, texto);
            };break;
            default: texto=""; break;
        }
        sapo.repaint();
    } //mousePressed
   
    public void mouseDragged(MouseEvent e) {
        Y1=e.getX();
        Y2=e.getY();
        W=Math.abs(Y1-X1);
        H=Math.abs(Y2-X2);
        switch (sapo.desenhoTipo) {
            case 0:  sapo.jif[sapo.numImg].jaiP.setLine(X1,X2,Y1,Y2,sapo.corDesenho, sapo.espLinha);break;
            case 1:  sapo.jif[sapo.numImg].jaiP.setRect(X1,X2,W,H,sapo.corDesenho, sapo.espLinha); break;
            case 2:  sapo.jif[sapo.numImg].jaiP.setCircle(X1,X2,W,H,sapo.corDesenho, sapo.espLinha); break;
            case 3:  {
                X1=Y1;
                X2=Y2;
                sapo.jif[sapo.numImg].jaiP.setString(Y1,Y2,sapo.corDesenho, sapo.fonteAtual, texto);
            }break;
            case 4:  sapo.jif[sapo.numImg].jaiP.setRectFill(X1,X2,W,H,sapo.corDesenho, sapo.espLinha);break;
        }
    } //mouseDragged
    
    public void mouseReleased(MouseEvent e) {
    	sapo.repaint();
    }
    
    public void desacoplar(int num) {
        sapo.tbrDesenho.resetButtons();
        super.desacoplar(num);
    }
    
    public void aceitaAlteracao() {
    	final MsgPane waitMsg = new MsgPane();
    	SwingWorker aWorker = new SwingWorker() {
    		public Object doInBackground() {
    			fazAlteracao();
    			sapo.clearGlassPane(sapo);
    			sapo.saveImage(sapo.jif[sapo.numImg].file.getPath());
    			sapo.repaint();
    			return null;
    		}
    	};
    	sapo.setGlassPane(waitMsg);
    	waitMsg.setText("Aplicando alterações ...");
    	sapo.getGlassPane().setVisible(true);
    	aWorker.execute();
    }
    
    protected void fazAlteracao() {
    	float sc = sapo.jif[sapo.numImg].zoom / 100.0F;
    	Font font = new Font("Dialog", Font.BOLD, 12);
        PlanarImage pimg = new RenderedImageAdapter(sapo.jif[sapo.numImg].getRenderedImage());
        BufferedImage bimg = pimg.getAsBufferedImage();
        Graphics2D g2D = bimg.createGraphics();
        g2D.setColor(sapo.corDesenho);
        g2D.setStroke(new BasicStroke(Math.round(sapo.espLinha/sc)));
        if (sapo.fonteAtual != null)
        	font  = new Font(sapo.fonteAtual.getName(), sapo.fonteAtual.getStyle(), Math.round(sapo.fonteAtual.getSize()/sc));
        g2D.setFont(font);
        switch (sapo.desenhoTipo) {
            case 0:  g2D.drawLine(Math.round(X1/sc),Math.round(X2/sc),Math.round(Y1/sc),Math.round(Y2/sc)); break;
            case 1:  g2D.drawRect(Math.round(X1/sc),Math.round(X2/sc),Math.round(W/sc),Math.round(H/sc)); break;
            case 2:  g2D.drawOval(Math.round(X1/sc),Math.round(X2/sc),Math.round(W/sc),Math.round(H/sc)); break;
            case 3:  if (texto != null) g2D.drawString(texto,Math.round(Y1/sc),Math.round(Y2/sc)); break;
            case 4:  g2D.fillRect(Math.round(X1/sc),Math.round(X2/sc),Math.round(W/sc),Math.round(H/sc)); break;
        }
        texto = "";
        sapo.jif[sapo.numImg].setRenderedImage(bimg);
        pimg = null;
        bimg = null;
        sapo.jif[sapo.numImg].jaiP.setPaint(false, false, false, false);
        sapo.jif[sapo.numImg].jaiP.resetMousePosition();
        sapo.showInternalFrameWithImage(sapo.numImg);
        sapo.release();
    }
    
}
