/*
 * JAIPanelSAPO.java
 *
 * Criada em 3 de abril de 2005, 22:00
 */

/**
 *
 * @author  Edison Puig Maldonado
 * @author  Anderson Zanardi de Freitas
 */

/**
 * Esta classe é responsável por mostrar as imagens,
 * os pontos e ângulos assinalados para uma imagem do Paciente
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import com.dati.image.JAIPanel;
import com.dati.image.DistanciaMedida;


public class JAIPanelSAPO extends JAIPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7677215185709711999L;
	public static final int ANGULO_INDEFINIDO = 0;
	public static final int ANGULO_HORIZONTAL = 1;
	public static final int ANGULO_VERTICAL = 2;
	public static final int ANGULO_TRES_PONTOS = 3;
	public static final int ANGULO_QUATRO_PONTOS = 4;
	public int anguloTipo;
	
	// Variáveis de instância
	protected ArrayList pontos = new ArrayList();
	protected ArrayList apresenta = new ArrayList();
	protected ArrayList distancias = new ArrayList();
	protected boolean line = false;
	protected boolean point = false;
	protected boolean rect = false;
	protected int anguloMedido;
	protected int numRetas;
	protected Point p1 = new Point();
	protected Point p2 = new Point();
	protected Point p3 = new Point();
	protected double alfa1, alfa2;
	protected double deltaAlfa;
	protected final int DELTA = 30;
	protected Point ptAngulos[] = new Point[5];
	protected boolean inverteAngulo = false; 
	protected Point ptZoomP1, ptZoomP2 = new Point(0,0);
	protected boolean zoom = false; 
	protected java.text.DecimalFormat numFormat = new java.text.DecimalFormat("####.#");    
	
	int pX1,pX2,pY1,pY2;
	Color corDesenho;
	int desenhoTipo=999;
	BasicStroke espLinha = new BasicStroke();
	String txtGraph;
	Font font;
	float zoomEscala;
	Point ptDistDraw[] = new Point[2];
	
	private boolean adicionaPonto = true;
	
	protected Point ptDistancia[] = new Point[2];
	protected boolean distanciaBoolean = false;
	protected boolean distanciaBlDraw = false;
	
	/** Construtor - necessita receber uma imagem RenderedImage */
	public JAIPanelSAPO(RenderedImage img) { 
		super(img);
		for(int i=0;i <5;i++) ptAngulos[i] = new Point(0,0);
	}
	
	public JAIPanelSAPO(boolean bCoords, RenderedImage img) {
		super(bCoords, img);
		for(int i=0;i <5;i++) ptAngulos[i] = new Point(0,0);
	}    
	
	private void desenhaPonto(Graphics g, Point p){
		g.drawOval(p.x-6, p.y-6, 12, 12);
		g.drawLine(p.x-10, p.y, p.x+10, p.y);
		g.drawLine(p.x, p.y-10, p.x, p.y+10);    
	}
	
	private void desenhaAngulo( Graphics g ) {
		if(ptAngulos[0].x == 0 && ptAngulos[0].y == 0) return; // angulo ainda não definido
		
		if ( anguloTipo == ANGULO_HORIZONTAL ) { 
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[1].x, ptAngulos[1].y);
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[0].x+DELTA, ptAngulos[0].y);
			g.drawArc(ptAngulos[0].x-Math.abs(DELTA), ptAngulos[0].y-Math.abs(DELTA), 2*Math.abs(DELTA), 2*Math.abs(DELTA), (int)alfa1, (int)(deltaAlfa));
			//g.drawString(String.valueOf(numFormat.format(deltaAlfa)), xDrag+5, yDrag+5);
			g.setColor(Color.BLUE);
			for(int i=0; i<2; i++) desenhaPonto(g,ptAngulos[i]);
		} 
		else if (anguloTipo == ANGULO_VERTICAL ) {
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[1].x, ptAngulos[1].y);
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[0].x, ptAngulos[0].y+DELTA);
			g.drawArc(ptAngulos[0].x-Math.abs(DELTA), ptAngulos[0].y-Math.abs(DELTA), 2*Math.abs(DELTA), 2*Math.abs(DELTA), (int)alfa1, (int)(deltaAlfa));
			//g.drawString(String.valueOf(numFormat.format(deltaAlfa)), xDrag+5, yDrag+5);
			g.setColor(Color.BLUE);
			for(int i=0; i<2; i++) desenhaPonto(g,ptAngulos[i]);
		} 
		else if ( anguloTipo == ANGULO_TRES_PONTOS ) { 
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[1].x, ptAngulos[1].y);
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[2].x, ptAngulos[2].y);
			g.drawArc(ptAngulos[0].x-Math.abs(DELTA), ptAngulos[0].y-Math.abs(DELTA), 2*Math.abs(DELTA), 2*Math.abs(DELTA), (int)alfa1, (int)(deltaAlfa));
			//g.drawString(String.valueOf(numFormat.format(Math.abs(deltaAlfa))), xDrag+10, yDrag+10);
			g.setColor(Color.BLUE);
			for(int i=0; i<3; i++) desenhaPonto(g,ptAngulos[i]);
		}
		else if (anguloTipo == ANGULO_QUATRO_PONTOS ) { 
			g.drawLine(ptAngulos[0].x, ptAngulos[0].y, ptAngulos[1].x, ptAngulos[1].y);
			g.drawLine(ptAngulos[2].x, ptAngulos[2].y, ptAngulos[3].x, ptAngulos[3].y);
			g.drawArc(ptAngulos[4].x-Math.abs(DELTA), ptAngulos[4].y-Math.abs(DELTA), 2*Math.abs(DELTA), 2*Math.abs(DELTA), (int)alfa1, (int)(deltaAlfa));
			//g.drawString(String.valueOf(numFormat.format(Math.abs(deltaAlfa))), xDrag+10, yDrag+10);
			g.setColor(Color.BLUE);
			for(int i=0; i<=4; i++){
				if(i==4) g.setColor(Color.WHITE);
				desenhaPonto(g,ptAngulos[i]);
			} //for
		}
	}
	
	/** paintComponent(Graphics g) - adiciona coordenadas do mouse ao gráfico */
	public void paintComponent(Graphics g) {
		_paintComponent(g);
//		g.setColor(Color.GRAY);
//		if (coords && (xMoved != 0) && (yMoved != 0)) 
//		g.drawString("x=" + xMoved   + ", y=" + yMoved, xMoved, yMoved);
		g.setColor(Color.RED);
		if (line)  g.drawLine(xPressed, yPressed, xDrag, yDrag);
		if (rect)  g.drawRect(xPressed, yPressed, xDrag-xPressed, yDrag-yPressed);
		if (point) {
			for ( int i = 0; i < pontos.size(); i++){
				Point pto = (Point)pontos.get(i);
				if (((Boolean)apresenta.get(i)).booleanValue() == true) desenhaPonto(g, pto);
			}
		}
		
		desenhaAngulo( g );
		
		if(zoom){
			g.setColor(Color.RED);
			g.drawRect(ptZoomP1.x, ptZoomP1.y, ptZoomP2.x, ptZoomP2.y);
		}
		Graphics2D g2 = (Graphics2D)g;
		switch(desenhoTipo){
		case 0: {
			g2.setColor(corDesenho);
			g2.setStroke(espLinha);
			g2.drawLine(pX1,pX2,pY1,pY2);
		} break;
		case 1: {
			g2.setColor(corDesenho);
			g2.setStroke(espLinha);
			g2.drawRect(pX1,pX2,pY1,pY2);
		} break;
		case 2: {
			g2.setColor(corDesenho);
			g2.setStroke(espLinha);
			g2.drawOval(pX1,pX2,pY1,pY2);
		} break;
		case 3: {
			g2.setColor(corDesenho);
			g2.setFont(font);
			if (txtGraph != null) g2.drawString(txtGraph, pX1, pX2);
			repaint();
		} break;
		case 4: {
			g2.setColor(corDesenho);
			g2.setStroke(espLinha);
			g2.fillRect(pX1,pX2,pY1,pY2);
		} break;
		}
		if(distanciaBlDraw){
			g2.setColor(corDesenho);
			g2.setStroke(espLinha);
			
			ptDistDraw = colocaZoom(ptDistancia);
			g2.drawLine(ptDistDraw[0].x,ptDistDraw[0].y,ptDistDraw[1].x,ptDistDraw[1].y);
			g.setColor(Color.BLUE);
			desenhaPonto(g, ptDistDraw[0]);
			desenhaPonto(g, ptDistDraw[1]);
		}
		if(distanciaBoolean){
			for(int i=0; i<distancias.size(); i++){
				boolean bl = ((DistanciaMedida)distancias.get(i)).apresentaDist;
				if(bl){
					Point [] pt = ((DistanciaMedida)distancias.get(i)).p;
					pt = colocaZoom(pt);
					g2.setColor(corDesenho);
					g2.setStroke(espLinha);
					g2.drawLine(pt[0].x,pt[0].y,pt[1].x,pt[1].y);
					g.setColor(Color.BLUE);
					desenhaPonto(g, pt[0]);
					desenhaPonto(g, pt[1]);
				}
			}
		}
	} //paintComponent
	
	public void setPaint (boolean argLine, boolean argPoint, boolean argRect, boolean argDistDraw) {
		line  = argLine;
		point = argPoint;
		rect  = argRect;
		distanciaBlDraw = argDistDraw;
	}
	
	public void resetMousePosition() {
		xPressed = 0;
		yPressed = 0;
		xDrag    = 0;
		yDrag    = 0;
		xClicked = 0;
		yClicked = 0;
		pontos.clear();
		apresenta.clear();
	}
	
	/** Armazena coordenadas do mouse ao ser clicado */
	public void mouseClicked(MouseEvent e) { } 
	
	/** Armazena coordenadas do mouse ao ser movido */
	public void mouseMoved(MouseEvent e) { }
	
	/** Armazena coordenadas do mouse ao arrastar */
	public void mouseDragged (MouseEvent e) {
		xDrag = e.getX(); // save the x coordinate of the drag
		yDrag = e.getY(); // save the y coordinate of the drag
//		dragged = true;
		this.repaint();
	}
	
	/** ignorado */
	public void mouseEntered (MouseEvent e) {}
	
	/** ajusta coordenadas do mouse para 0,0 quando sai da área de exibição */
	public void mouseExited  (MouseEvent e) {
		xMoved = 0;
		yMoved = 0;
//		repaint();
	} 
	
	/** Armazena as coordenadas iniciais das retas de definição de vertical e medidas */
	public void mousePressed (MouseEvent e) {
		xPressed = e.getX(); // save the x coordinate of the pressed
		yPressed = e.getY(); // save the y coordinate of the pressed
	}
	
	public void mousePressedEscala(int xp, int yp){
		xPressed = xp;
		yPressed = yp;
	}
	
	/** Armazena as poordenados finais das retas de definição de vertical e medidas*/
	public void mouseReleased(MouseEvent e) {
		if((e.getButton()!=MouseEvent.BUTTON3)&&(e.getButton()!=MouseEvent.BUTTON2))
			if (point && adicionaPonto) addPonto(e.getPoint());
		repaint();
	}    
	
	public void setAdiciona(boolean opt) {
		adicionaPonto = opt;
	}
	
	public void setRetas(int argRetas) {
		numRetas = argRetas;
	}
	
	private void calculaAngulos(){
		Point zero = new Point(0,0);
		if ( ptAngulos[0].equals(zero) && 
				ptAngulos[1].equals(zero) &&
				ptAngulos[2].equals(zero) &&
				ptAngulos[3].equals(zero) &&
				ptAngulos[4].equals(zero)){
			alfa1 = 0;
			deltaAlfa = 0;
		}
		else if ( anguloTipo == ANGULO_HORIZONTAL ) {
			alfa1 = 0;
			
			int deltaX = ptAngulos[1].x - ptAngulos[0].x;
			int deltaY = ptAngulos[1].y - ptAngulos[0].y;
			deltaAlfa = Math.toDegrees(Math.atan2(-deltaY,deltaX)); //inversão da tela
		}
		else if ( anguloTipo == ANGULO_VERTICAL ) {
			alfa1 = -90;
			
			int deltaX = ptAngulos[1].x - ptAngulos[0].x;
			int deltaY = ptAngulos[1].y - ptAngulos[0].y;
			deltaAlfa = Math.toDegrees(Math.atan2(deltaX,deltaY)); //inversão da tela
		}
		else if (anguloTipo == ANGULO_TRES_PONTOS ) { 
			int deltaX = ptAngulos[1].x - ptAngulos[0].x;
			int deltaY = ptAngulos[1].y - ptAngulos[0].y;
			alfa1 = Math.toDegrees(Math.atan2(-deltaY,deltaX)); //inversão da tela
			deltaX = ptAngulos[2].x - ptAngulos[0].x;
			deltaY = ptAngulos[2].y - ptAngulos[0].y;
			double alfa2 = Math.toDegrees(Math.atan2(-deltaY,deltaX));
			deltaAlfa = alfa2 - alfa1;
			deltaAlfa = (alfa1<alfa2) ? deltaAlfa : (deltaAlfa+360);
			if(inverteAngulo) deltaAlfa = -(360-deltaAlfa);
		}
		else if (anguloTipo == ANGULO_QUATRO_PONTOS ) { 
			//reta 1
			double deltaX1 = ptAngulos[1].x - ptAngulos[0].x;
			double deltaY1 = ptAngulos[1].y - ptAngulos[0].y;
			alfa1 = Math.atan2(deltaY1,deltaX1);
			double b1 = ptAngulos[0].y - Math.tan(alfa1)*ptAngulos[0].x;
			//reta 2
			int deltaX2 = ptAngulos[3].x - ptAngulos[2].x;
			int deltaY2 = ptAngulos[3].y - ptAngulos[2].y;
			double alfa2 = Math.atan2(deltaY2,deltaX2);
			double b2 = ptAngulos[2].y - Math.tan(alfa2)*ptAngulos[2].x;
			//ponto de intersessão das retas
			ptAngulos[4].x = (int)((b2-b1)/(Math.tan(alfa1)-Math.tan(alfa2)));
			ptAngulos[4].y = (int)(Math.tan(alfa1)*ptAngulos[4].x+b1);
			deltaAlfa = - Math.toDegrees((alfa2 - alfa1));
			alfa1 = Math.toDegrees(alfa1);
		}
	}
	
	public void setptAngulos(Point[] pt){
		ptAngulos = pt;
		calculaAngulos();
	}
	
	public void inverteAngulo(){
		inverteAngulo = inverteAngulo ? false : true;
	}
	
	public Point[] getptAngulos(){
		Point[] pt = new Point[5];
		for (int i=0; i < 5; i++)  pt[i] = new Point(ptAngulos[i]);
		return pt;
	}
	
	public double getAngulo(){ return deltaAlfa;}
	
	public void setPontos(ArrayList pt, ArrayList ap){
		pontos = pt;
		apresenta = ap;
		repaint();
	}
	
	
	public void addPonto(Point pt){
		pontos.add(pt);
		apresenta.add(Boolean.TRUE);
		repaint();
	}
	
	public void addPonto(Point pt, boolean mostra){
		pontos.add(pt);
		apresenta.add(new Boolean(mostra));
		repaint();
	}
	
	public void setZoomArea(Point p1, Point p2){
		zoom = true; 
		ptZoomP1 = p1;
		ptZoomP2 = p2;
		repaint();
	}
	
	public void setLine(int X1, int X2, int Y1, int Y2, Color cor, int esp){
		desenhoTipo=0;
		pX1=X1;
		pX2=X2;
		pY1=Y1;
		pY2=Y2;
		corDesenho = cor;
		espLinha = new BasicStroke(esp);
	}
	
	public void setRect(int X1, int X2, int W, int H, Color cor, int esp){
		desenhoTipo=1;
		pX1=X1;
		pX2=X2;
		pY1=W;
		pY2=H;
		corDesenho = cor;
		espLinha = new BasicStroke(esp);
	}
	
	public void setRectFill(int X1, int X2, int W, int H, Color cor, int esp){
		desenhoTipo=4;
		pX1=X1;
		pX2=X2;
		pY1=W;
		pY2=H;
		corDesenho = cor;
		espLinha = new BasicStroke(esp);
	}
	
	public void setCircle(int X1, int X2, int W, int H, Color cor, int esp){
		desenhoTipo=2;
		pX1=X1;
		pX2=X2;
		pY1=W;
		pY2=H;
		corDesenho = cor;
		espLinha = new BasicStroke(esp);
	}
	
	public void setString(int X1, int X2, Color cor, Font fnt, String txt){
		desenhoTipo=3;
		pX1=X1;
		pX2=X2;
		corDesenho = cor;
		font = fnt;
		txtGraph = txt;
	}
	
	public void setEspLinhaCorLinha(Color cor, int esp){
		corDesenho = cor;
		espLinha = new BasicStroke(esp);
	}
	
	public void setptDistancia(Point[] pt, boolean dstBoolean){
		ptDistancia = pt;
		distanciaBoolean = dstBoolean;
		distanciaBlDraw = true;
	}
	
	public Point[] getptDistancia(){
		Point[] pt = new Point[2];
		for (int i=0; i < 2; i++)  pt[i] = new Point(ptDistancia[i]);
		return pt;
	}
	
	public void addDistancia(Point[] pt, boolean bl){
		DistanciaMedida md = new DistanciaMedida(); 
		md.p = pt;
		md.apresentaDist = bl;
		distancias.add(md);
		repaint();      
	}
	
	
	public void removeDistancia(int index){
		distancias.remove(index);
		repaint();    
	}
	
	public void limpaListaDistancia(){
		distancias.clear();
		repaint();
	}
	
	public void atualizaApresentaDistancia(ArrayList arrL){
		distancias.clear();
		distancias = new ArrayList(arrL);
		distanciaBlDraw = false;
		distanciaBoolean = true;
		repaint();
	}
	public void setZoom(float z){
		zoomEscala = z;
	}
	
	private Point[] colocaZoom(Point[] pt){
		Point[] ptEscala = new Point[2];
		for(int i=0; i<2; i++){
			ptEscala[i] = new Point(pt[i]);
			ptEscala[i].x = Math.round(pt[i].x*zoomEscala);
			ptEscala[i].y = Math.round(pt[i].y*zoomEscala);
		}
		return ptEscala;
	}
	
}
