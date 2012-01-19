/*
 * JAIPanel.java
 *
 * Criada em 14 de Fevereiro de 2004, 20:20
 */

/**
 *
 * @author  Edison Puig Maldonado
 * @author  Anderson Zanardi de Freitas
 */

package com.dati.image;
    import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.RenderedImage;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * Esta classe implementa um painel que é container
 * de imagens JAI (DisplayJAI), eventos de mouse e
 * configurações de rolagem.
 */
    
public class JAIPanel extends DisplayJAI implements Scrollable{

   /**
	 * 
	 */
	private static final long serialVersionUID = 6762319220083449870L;
	// Variáveis de instância
    protected int xClicked = 0;  // x coord of mouse click 
    protected int yClicked = 0;  // y coord of mouse click
    protected int xMoved   = 0;  // x coord of mouse move 
    protected int yMoved   = 0;  // y coord of mouse move
    protected int xDrag    = 0;  // x coord of mouse drag
    protected int yDrag    = 0;  // y coord of mouse drag
    protected int xPressed = 0;  // x coord of mouse Pressed
    protected int yPressed = 0;  // y coord of mouse Preseed
    protected boolean coords = true;
    
    /** Construtor padrão */
    public JAIPanel() {
        super();
        incluiListerners();
    }
    
    /** Construtor - necessita receber uma imagem RenderedImage */
    public JAIPanel(RenderedImage img) {
        super(img);
        inicializaImagem(img); 
        incluiListerners();           
    }
    
    public JAIPanel(boolean bCoords, RenderedImage img) {
        super(img);
        inicializaImagem(img);
        coords = bCoords;
        incluiListerners();        
    }
    
    protected void incluiListerners(){
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    
    protected void inicializaImagem(RenderedImage img) {
        this.setBackground(Color.white);
        int w = img.getWidth();
        int h = img.getHeight();
        this.setPreferredSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
    }
    
    protected void IncializaImagem(RenderedImage img) {
        this.setBackground(Color.white);
        int w = img.getWidth();
        int h = img.getHeight();
        this.setPreferredSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
    }
    
    /** define uma imagem RenderedImage para o JAIPanel */
    public void setImage(RenderedImage img) {
        set(img, 0, 0);
        this.setBackground(Color.white);
        int w = img.getWidth();
        int h = img.getHeight();
        this.setPreferredSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
    }
    
    protected void _paintComponent(Graphics g){
    super.paintComponent(g);  // paint background and borders
    }
    
    /** Armazena coordenadas do mouse ao ser clicado */
    public void mouseClicked(MouseEvent e) { } 

    /** Armazena coordenadas do mouse ao ser movido */
    public void mouseMoved(MouseEvent e) { }

    /** Armazena coordenadas do mouse ao arrastar */
    public void mouseDragged (MouseEvent e) { }
    
    /** ignorado */
    public void mouseEntered (MouseEvent e) { }
    
    /** ajusta coordenadas do mouse para 0,0 quando sai da área de exibição */
    public void mouseExited  (MouseEvent e) { } 
    
    /** Armazena as coordenadas iniciais das retas de definição de vertical e medidas */
    public void mousePressed (MouseEvent e) { } 
    
    /** Armazena as poordenados finais das retas de definição de vertical e medidas*/
    public void mouseReleased(MouseEvent e) { }

    /** retorna tamanho preferencial para rolagem */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    
    /** retorna incremento unitário para rolagem */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int aux = 1;
        if (orientation == SwingConstants.HORIZONTAL) aux = this.getWidth()/20;
        if (orientation == SwingConstants.VERTICAL)   aux = this.getHeight()/20;
        return aux;
    } 
    
    /** retorna incremento de bloco para rolagem */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        int aux = 1;
        if (orientation == SwingConstants.HORIZONTAL) aux = this.getWidth()/5;
        if (orientation == SwingConstants.VERTICAL)   aux = this.getHeight()/5;
        return aux;
    }
    
    /** ignorado */
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    /** ignorado */
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    
    public void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
    }
    

    
}
