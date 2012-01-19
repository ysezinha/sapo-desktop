/*
 * ImageData.java
 *
 * Created on 25 de Fevereiro de 2004, 22:50
 */

/**
 * 
 * Classe ImageData
 *
 * Essa classe contém a "lógica de negócio" do programa SAPO
 * além dos dados analíticos de cada imagem.
 *
 */

import java.awt.Point;
import java.io.File;
//import java.io.Serializable;
import java.util.ArrayList;

import com.dati.image.AnguloMedido;
import com.dati.image.PontoMedido;
import com.dati.image.DistanciaMedida;

public class ImageData {
	
	public ArrayList pontosList = new ArrayList(); // <PontoMedido>
	public ArrayList angulosList = new ArrayList(); // <AnguloMedido>
	public ArrayList distanciaList = new ArrayList(); // <DistanciaMedida>
	
	public double anguloVertical = 360, escalaX = 1, escalaY = 1;
	public int xAnchor = 0, yAnchor = 0;
	protected long nID;
	public boolean imgRotate = false;
	protected File fileImage;
	public String vista; // "Anterior","Posterior","Lateral Direita","Lateral Esquerda" 
	SAPO sapo;
	
	ImageData (File nomeArq) {
		fileImage = nomeArq;
	}
	
	ImageData () { }
	
	public void setEssencial(ArrayList pontosList, 
			double anguloVertical, double escalaX, double escalaY,
			int xAnchor, int yAnchor, boolean imgRotate, String vista) {
		this.pontosList = pontosList;
		this.anguloVertical = anguloVertical;
		this.escalaX = escalaX;
		this.escalaY = escalaY;
		this.xAnchor = xAnchor;
		this.yAnchor = yAnchor;
		this.imgRotate = imgRotate;
		this.vista = vista;
	}
	
	public boolean isVertCal() {
		return (anguloVertical != 360);
	}
	
	public boolean isScaleCal() {
		return ((escalaX != 1) && (escalaY != 1));
	}
	
	public void limpaFileImage() {
		fileImage = null;
	}
	
	public void limpaTudo() {
		pontosList.clear();
		angulosList.clear();
		anguloVertical = 0;
		escalaX = 1;
		escalaY = 1;
		xAnchor = 0;
		yAnchor = 0;
		nID = -1;
		imgRotate = false;
		fileImage = null;
		vista = "";
	}
	
	public void limpaPontos() {
		pontosList.clear();
	}
	
	public File getFileImage(){
		return fileImage;
	}
	
	public void setFileImage(File fImg){
		fileImage = fImg;
	}
	
	public void setVista(String str){
		vista = str;
	}
	
	public String getVista(){
		return vista;
	}
	
	public void addPoint(int x, int y, String s1, boolean b1) {
		Point p = new Point(x,y);
		PontoMedido ponto = new PontoMedido();
		ponto.p = p;
		ponto.nome = new String(s1);
		ponto.apresenta = b1;
		pontosList.add(ponto);
	}
	
	public void setPoint(int x, int y, int index) {
		Point p = new Point(x,y);
		PontoMedido ponto = new PontoMedido();
		ponto = (PontoMedido)pontosList.get(index);
		ponto.p = p;
		pontosList.set(index, ponto);
	}
	
	public void setDistanciaMedida(int x, int y, int i, int j) {
		Point p = new Point(x,y);
		DistanciaMedida distancia = new DistanciaMedida();
		distancia = (DistanciaMedida)distanciaList.get(i);
		distancia.p[j] = p;
		distanciaList.set(i, distancia);
	}
	
	public void setPointApresenta(boolean b, int index) {
		PontoMedido ponto = (PontoMedido)pontosList.get(index);
		ponto.apresenta = b;
		pontosList.set(index, ponto);
	}
	
	public void setPointLabel(String b, int index) {
		PontoMedido ponto = (PontoMedido)pontosList.get(index);
		ponto.nome = b;
		pontosList.set(index, ponto);
	}
	
	public void setMedeApresenta(boolean b, int index) {
		//DistanciaMedida distancia = new DistanciaMedida();
		DistanciaMedida distancia = (DistanciaMedida)distanciaList.get(index);
		distancia.apresentaDist = b;
		distanciaList.set(index, distancia);
	}
	
	public void setMedetLabel(String b, int index) {
		DistanciaMedida distancia = (DistanciaMedida)distanciaList.get(index);
		distancia.nome = b;
		distanciaList.set(index, distancia);
	}
	
	public Point getPoint(int i) {
		if( pontosList.size() < i)
			return new Point(-1,-1);
		
		return new Point(((PontoMedido)pontosList.get(i)).p);
	}
	
	public ArrayList getPontos() { // <PontoMedido>
		return (ArrayList)pontosList.clone();
	}    
	
	public ArrayList getMedidas() { // <DistanciaMedida>
		return (ArrayList)distanciaList.clone();
	}
	
	public void removePoint(int i){
		if(i < pontosList.size())
			pontosList.remove(i);	
	}
	
	public double getAnguloVertical() {
		return anguloVertical;
	}
	public double getEscalaX() {
		return escalaX;
	}
	public double getEscalaY() {
		return escalaY;
	}
	
	public int getXAnchor() {
		return xAnchor;
	}
	
	public int getYAnchor() {
		return yAnchor;
	}
	
	public void setAnguloVertical(double arg) {
		anguloVertical = arg;
	}
	public void setEscalaX(double arg) {
		escalaX = arg;
	}
	public void setEscalaY(double arg) {
		escalaY = arg;
	}
	public void setXAnchor(int arg) {
		xAnchor = arg;
	}
	public void setYAnchor(int arg) {
		yAnchor = arg;
	}
	
	public void addAnguloMedido(Point p1[], String s1, double ag, int tp) {
		AnguloMedido angulos = new AnguloMedido();
		
		Point[] temp = new Point[5];                    // #ev 12mar
		for(int i=0; i< p1.length ; i++){               // #ev 12mar
			Point pto = new Point( p1[i].x, p1[i].y );  // #ev 12mar
			temp[i] = pto;                              // #ev 12mar
		}                                           // #ev 12mar
		
		angulos.p = temp;                               // #ev 12mar
		angulos.nome = new String(s1);                  // #ev 12mar
		angulos.angulo = ag;
		angulos.tipo = tp; 
		angulosList.add(angulos); 
	}
	
	public ArrayList getAnguloMedido() {
		return angulosList;
	}
	
	public void removeAnguloMedido(int i) {
		angulosList.remove(i);
	}    
	
	public void atualizaAnguloMedido(String s, int i) {
		AnguloMedido angulos = (AnguloMedido)angulosList.get(i);
		angulos.nome = s;
	}    
	
	public boolean isImgRotate(){
		return imgRotate;
	}
	
	public void setImgRotate(boolean temp){
		imgRotate = temp;
	}
	
	public void limpaDistanciaMedida() {
		distanciaList.clear();
	}    
	
	public ArrayList getDistanciaMedida() {
		return distanciaList;
	}
	
	public void removeDistanciaMedida(int index) {
		distanciaList.remove(index);
	}  
	
	public void atualizaDistanciaMedida(String s, boolean bl, int i) {
		((DistanciaMedida)distanciaList.get(i)).nome = s;
		((DistanciaMedida)distanciaList.get(i)).apresentaDist = bl;
	}  
	
	public void addDistanciaMedida(Point p1[], String s1, double ag, boolean bl) {
		DistanciaMedida distancia = new DistanciaMedida();
		Point[] temp = new Point[2];                   
		for(int i=0; i< 2 ; i++){               
			Point pto = new Point( p1[i].x, p1[i].y );  
			temp[i] = pto;                              
		}                                           
		distancia.p = temp;                               
		distancia.nome = new String(s1);                  
		distancia.distanciaMedida = ag;
		distancia.apresentaDist = bl;
		distanciaList.add(distancia); 
	}
	
	public void atualizaMedidaPosicao(Point p, int i, int j){
		((DistanciaMedida)distanciaList.get(i)).p[j] = p;
	}
}
