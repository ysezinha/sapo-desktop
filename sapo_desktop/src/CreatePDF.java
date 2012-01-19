/*
 * CreatePDF.java
 *
 * Created on 31 de Dezembro de 2005, 01:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.awt.Point;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jdesktop.swingworker.SwingWorker;

import com.dati.image.AnguloMedido;
import com.dati.image.DistanciaMedida;
import com.dati.image.PontoMedido;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 * @author Anderson Zanardi de Freitas
 */
public class CreatePDF extends PdfPageEventHelper{
	SAPO sapo;
	Document document = new Document(PageSize.A4, 50, 50, 100, 72);
	PdfWriter writer;
	int numPages = 0;
	
	//configuração padrão para o relatório
	boolean config[] = {
			true,
			true,        
			true,
			true,
			false,
			false,
			false,
			false
	};
	String coment;
	
	/** Creates a new instance of CreatePDF */
	public CreatePDF(final SAPO sapo) {
		this.sapo = sapo;
	}
	
	protected void cabecalhoEDados(Document doc) {
		PdfPTable cabecTable = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Paragraph("Relatório de Avaliação Postural"));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setGrayFill(0.5f);
		cabecTable.addCell(cell);
		PdfPCell cell2 = new PdfPCell(new Paragraph("Dados do Sujeito"));
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell2.setGrayFill(0.75f);
		cabecTable.addCell(cell2);
		/*String nome = "Nome: "+sapo.paciente.dados.nome;
		cabecTable.addCell(nome);*/
		String DataNasc = "Dada de nascimento: "+sapo.paciente.dados.diaNasc+"/"+sapo.paciente.dados.mesNasc+"/"+sapo.paciente.dados.anoNasc;
		cabecTable.addCell(DataNasc);
		String DataAval = "Data da avaliação: "+sapo.paciente.dados.dataalteracao.toString();
		cabecTable.addCell(DataAval);
		try{   
			doc.add(cabecTable);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void pontosMedidos(Document doc){
		float[] widths = {4f, 1f, 1f};
		PdfPTable pontosTable = new PdfPTable(widths);
		PdfPCell cellc = new PdfPCell(new Paragraph("Pontos Medidos"));
		cellc.setColspan(3);
		cellc.setGrayFill(0.75f);
		pontosTable.addCell(cellc);
		for (int i=1; i<SAPO.maxImg; i++){
			if ( sapo.jif[i] == null ) continue;  
			double escalaX = sapo.paciente.dados.imgData[i].getEscalaX();
			double escalaY = sapo.paciente.dados.imgData[i].getEscalaY();
			ArrayList valores = sapo.paciente.dados.imgData[i].getPontos();
			String vista = sapo.paciente.dados.imgData[i].vista;
			if(vista.equals("Anterior")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(3);
				cell.setGrayFill(0.75f);
				pontosTable.addCell(cell);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY;
					pontosTable.addCell(nomeTemp);
					pontosTable.addCell(sapo.numFormat.format(pX));
					pontosTable.addCell(sapo.numFormat.format(pY));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Posterior")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(3);
				cell.setGrayFill(0.75f);
				pontosTable.addCell(cell);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY;
					pontosTable.addCell(nomeTemp);
					pontosTable.addCell(sapo.numFormat.format(pX));
					pontosTable.addCell(sapo.numFormat.format(pY));
				}//for j
			}//if
			if(vista.equalsIgnoreCase("Lateral Direita")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(3);
				cell.setGrayFill(0.75f);
				pontosTable.addCell(cell);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY;
					pontosTable.addCell(nomeTemp);
					pontosTable.addCell(sapo.numFormat.format(pX));
					pontosTable.addCell(sapo.numFormat.format(pY));
				}//for j
			}//if
			if(vista.equalsIgnoreCase("Lateral Esquerda")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(3);
				cell.setGrayFill(0.75f);
				pontosTable.addCell(cell);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY;
					pontosTable.addCell(nomeTemp);
					pontosTable.addCell(sapo.numFormat.format(pX));
					pontosTable.addCell(sapo.numFormat.format(pY));
				}//for j
			}//if
		}// for i
		try{   
			doc.add(pontosTable);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}//PontosMedidos
	
	private void distanciasMedidas(Document doc){
		float[] widths = {4f, 1f};
		PdfPTable distanciasTable = new PdfPTable(widths);
		PdfPCell cellc = new PdfPCell(new Paragraph("Distâncias Medidas"));
		cellc.setColspan(3);
		cellc.setGrayFill(0.75f);
		distanciasTable.addCell(cellc);
		for (int i=1; i<SAPO.maxImg; i++){
			if ( sapo.jif[i] == null ) continue;
			java.util.ArrayList valores = sapo.paciente.dados.imgData[i].getDistanciaMedida();
			String vista = sapo.paciente.dados.imgData[i].vista;
			if(vista.equalsIgnoreCase("Anterior")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				distanciasTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					distanciasTable.addCell(nomeTemp);
					distanciasTable.addCell(String.valueOf(distanciaTemp));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Posterior")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				distanciasTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					distanciasTable.addCell(nomeTemp);
					distanciasTable.addCell(String.valueOf(distanciaTemp));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Direita")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				distanciasTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					distanciasTable.addCell(nomeTemp);
					distanciasTable.addCell(String.valueOf(distanciaTemp));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Esquerda")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				distanciasTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					distanciasTable.addCell(nomeTemp);
					distanciasTable.addCell(String.valueOf(distanciaTemp));
				}//for j
			}//if vista
		}//for i
		try{   
			doc.add(distanciasTable);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}//DistanciaMedida
	
	private void angulosMedidos(Document doc){
		float[] widths = {4f, 1f};
		PdfPTable angleTable = new PdfPTable(widths);
		PdfPCell cellc = new PdfPCell(new Paragraph("Ângulos Medidos"));
		cellc.setColspan(3);
		cellc.setGrayFill(0.75f);
		angleTable.addCell(cellc);
		for (int i=1; i<SAPO.maxImg; i++){
			if ( sapo.jif[i] == null ) continue;   
			java.util.ArrayList valores = sapo.paciente.dados.imgData[i].getAnguloMedido();
			String vista = sapo.paciente.dados.imgData[i].vista;
			if(vista.equalsIgnoreCase("Anterior")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				angleTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					angleTable.addCell(nomeTemp);
					angleTable.addCell(String.valueOf(anguloTemp));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Posterior")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				angleTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					angleTable.addCell(nomeTemp);
					angleTable.addCell(String.valueOf(anguloTemp));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Direita")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				angleTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					angleTable.addCell(nomeTemp);
					angleTable.addCell(String.valueOf(anguloTemp));
				}//for j
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Esquerda")){
				PdfPCell cell = new PdfPCell(new Paragraph("Vista "+vista));
				cell.setColspan(2);
				cell.setGrayFill(0.75f);
				angleTable.addCell(cell);
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					angleTable.addCell(nomeTemp);
					angleTable.addCell(String.valueOf(anguloTemp));
				}//for j
			}//if vista
		}//for i
		try{   
			doc.add(angleTable);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}//angulosMedidos
	
	private void addAllImages(Document doc){
		float[] widths = {1f, 1f};
		PdfPTable PdfTableImg = new PdfPTable(widths);
		for (int i=1; i<SAPO.maxImg; i++) {
			if ( sapo.jif[i] == null ) continue;   
			String pict = "";
			Image image = null;
			String vistaStr = sapo.paciente.dados.imgData[i].vista;
			if(vistaStr.equalsIgnoreCase("Anterior")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					image = Image.getInstance(pict);
				}catch(Exception erro){}
				if(image!=null){
					PdfTableImg.addCell(image);	
				}
			}
			if(vistaStr.equalsIgnoreCase("Posterior")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					image = Image.getInstance(pict);
				}catch(Exception erro){}
				if(image!=null){
					PdfTableImg.addCell(image);	
				}
			}
			if(vistaStr.equalsIgnoreCase("Lateral Direita")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					image = Image.getInstance(pict);
				}catch(Exception erro){}
				if(image!=null){
					PdfTableImg.addCell(image);	
				}
			}
			if(vistaStr.equalsIgnoreCase("Lateral Esquerda")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					image = Image.getInstance(pict);
				}catch(Exception erro){}
				if(image!=null){
					PdfTableImg.addCell(image);	
				}
			}
		}//SAPO.maxImg
		try{   
			doc.add(PdfTableImg);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}//addAllImages
	
	private void centroGravidade(Document doc){
		saveComponentAsJPEG(sapo.figDirPath + StartSAPO.sep + "cgtemp.jpg");
		MedidasAnalise md = new MedidasAnalise(sapo);
		double calculos[] = new double[md.medidas.length];
		calculos = md.calculaAnalise(sapo);
		String resultStr = "";
		String resultNA = "";
		String medida = "";
		double result;
		float[] width = {1f, 1f};
		PdfPTable cgTable = new PdfPTable(width);
		PdfPCell cellc = new PdfPCell(new Paragraph("Projeção do centro de gravidade"));
		cellc.setColspan(2);
		cellc.setGrayFill(0.75f);
		cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
		cgTable.addCell(cellc);
		Image img = null;
		try{
			
			
			result = 1E300;
			result = calculos[37];
			medida = md.medidas[37];
			if(result!=1E300){
				resultStr = medida+" (%): "+sapo.numFormat.format(result);
			}
			else
				resultNA = medida+" (%): " +"NA";
			
			result = 1E300;
			result = calculos[39];
			medida = md.medidas[39];
			if(result!=1E300){
				resultStr = resultStr + "\n" + medida+" (%): "+sapo.numFormat.format(result);
			}
			else
				resultNA = resultNA + "\n" + medida+" (%): "+ "NA";
			
			result = 1E300;
			result = calculos[41];
			medida = md.medidas[41];
			if(result!=1E300){
				resultStr = resultStr + "\n" + medida+" (cm): "+sapo.numFormat.format(result);
			}
			else
				resultNA = resultNA + "\n" + medida+" (cm): "+ "NA";
			result = 1E300;
			result = calculos[42];
			medida = md.medidas[42];
			if(result!=1E300){
				resultStr = resultStr + "\n" + medida+" (cm): "+sapo.numFormat.format(result);
			}
			else
				resultNA = resultNA + "\n" + medida+" (cm): "+ "NA";
			
			if(!resultStr.equals(""))
				cgTable.addCell(resultStr);
			else
				cgTable.addCell(resultNA);
			
			img = Image.getInstance(sapo.figDirPath + StartSAPO.sep + "cgtemp.jpg");
			if(img!=null){
				cgTable.addCell(img);	
				doc.add(cgTable);
			}
		}
		catch(Exception erro){
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void saveComponentAsJPEG(String filename) {
		java.awt.Image image = null;
		try{
			image =  javax.imageio.ImageIO.read(getClass().getResource("/res/cg-feet.png"));
		}catch(Exception e){}
		int cgW = image.getWidth(null);
		int cgH = image.getHeight(null);
		java.awt.image.BufferedImage myImage = (java.awt.image.BufferedImage)image;
		
		MedidasAnalise md = new MedidasAnalise(sapo);
		double calculos[] = new double[md.medidas.length];
		calculos = md.calculaAnalise(sapo);
		java.awt.Graphics2D g2 = myImage.createGraphics();
		if((calculos[37]!=1E300)&&(calculos[39]!=1E300)){
			int xPicCG = Double.valueOf(((0.782*cgW)/md.distMaleolos)*(calculos[41])+cgW/2).intValue();
			int yPicCG = Double.valueOf(((-0.560*cgH)/md.tamPeMedio)*(calculos[42])+0.791*cgH).intValue();
			g2.setColor(java.awt.Color.RED);
			g2.fillOval(xPicCG-20,yPicCG-20,40,40);
			g2.setColor(java.awt.Color.BLUE);
			g2.drawLine(xPicCG-25, yPicCG, xPicCG+25, yPicCG);
			g2.drawLine(xPicCG, yPicCG-25, xPicCG, yPicCG+25);
		}
		try {
			java.io.OutputStream out = new java.io.FileOutputStream(filename);
			com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(out);
			encoder.encode(myImage);
			out.close();
			StartSAPO.deleteOnExit(filename);
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String valEspTol(double val, double tol){
		String ref = (val != 1E300)?(sapo.numFormat.format(val)):"";
		String stol = (tol != 0)?(" +/- " + sapo.numFormat.format(tol)):"";
		return (ref.equals("")?"não disponível":ref+stol);
	}
	
	private void analise(Document doc){
		float[] widths = {4f, 1f, 1f};
		PdfPTable analiseTable = new PdfPTable(widths);
		MedidasAnalise md = new MedidasAnalise(sapo);
		double calculos[] = new double[md.medidas.length];
		calculos = md.calculaAnalise(sapo);
		String resultStr = "";
		String medida = "";
		double result;
		double valEsp;
		double valEspTol;
		
		PdfPCell cell = new PdfPCell(new Paragraph("Medidas segundo o protocolo SAPO"));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph(" "));
		cell.setGrayFill(1f);
		analiseTable.addCell(cell); //linha em branco
		
		cell.setPhrase(new Paragraph("Vista Anterior"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor de Referência (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor Medido (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Cabeça"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[0];
		valEsp = md.valEsp[0];
		valEspTol = md.valEspTol[0];
		medida = md.medidas[0];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Tronco"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[1];
		valEsp = md.valEsp[1];
		valEspTol = md.valEspTol[1];
		medida = md.medidas[1];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[2];
		valEsp = md.valEsp[2];
		valEspTol = md.valEspTol[2];
		medida = md.medidas[2];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[3];
		valEsp = md.valEsp[3];
		valEspTol = md.valEspTol[3];
		medida = md.medidas[3];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Membros Inferiores"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[4];
		valEsp = md.valEsp[4];
		valEspTol = md.valEspTol[4];
		medida = md.medidas[4];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[5];
		valEsp = md.valEsp[5];
		valEspTol = md.valEspTol[5];
		medida = md.medidas[5];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[6];
		valEsp = md.valEsp[6];
		valEspTol = md.valEspTol[6];
		medida = md.medidas[6];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[7];
		valEsp = md.valEsp[7];
		valEspTol = md.valEspTol[7];
		medida = md.medidas[7];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[8];
		valEsp = md.valEsp[8];
		valEspTol = md.valEspTol[8];
		medida = md.medidas[8];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[9];
		valEsp = md.valEsp[9];
		valEspTol = md.valEspTol[9];
		medida = md.medidas[9];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setPhrase(new Paragraph("Vista Posterior"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor de Referência (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor Medido (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Tronco"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[10];
		valEsp = md.valEsp[10];
		valEspTol = md.valEspTol[10];
		medida = md.medidas[10];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Membros Inferiores"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[11];
		valEsp = md.valEsp[11];
		valEspTol = md.valEspTol[11];
		medida = md.medidas[11];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[12];
		valEsp = md.valEsp[12];
		valEspTol = md.valEspTol[12];
		medida = md.medidas[12];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setPhrase(new Paragraph("Vista Lateral Direita"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor de Referência (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor Medido (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Cabeça"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[13];
		valEsp = md.valEsp[13];
		valEspTol = md.valEspTol[13];
		medida = md.medidas[13];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[14];
		valEsp = md.valEsp[14];
		valEspTol = md.valEspTol[14];
		medida = md.medidas[14];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Tronco"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[15];
		valEsp = md.valEsp[15];
		valEspTol = md.valEspTol[15];
		medida = md.medidas[15];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[16];
		valEsp = md.valEsp[16];
		valEspTol = md.valEspTol[16];
		medida = md.medidas[16];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[17];
		valEsp = md.valEsp[17];
		valEspTol = md.valEspTol[17];
		medida = md.medidas[17];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[18];
		valEsp = md.valEsp[18];
		valEspTol = md.valEspTol[18];
		medida = md.medidas[18];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Membros Inferiores"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[19];
		valEsp = md.valEsp[19];
		valEspTol = md.valEspTol[19];
		medida = md.medidas[19];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[20];
		valEsp = md.valEsp[20];
		valEspTol = md.valEspTol[20];
		medida = md.medidas[20];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setPhrase(new Paragraph("Vista Lateral Esquerda"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor de Referência (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		cell.setPhrase(new Paragraph("Valor Medido (graus)"));
		cell.setColspan(1);
		cell.setGrayFill(0.40f);
		analiseTable.addCell(cell);
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Cabeça"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[21];
		valEsp = md.valEsp[21];
		valEspTol = md.valEspTol[21];
		medida = md.medidas[21];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[22];
		valEsp = md.valEsp[22];
		valEspTol = md.valEspTol[22];
		medida = md.medidas[22];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Tronco"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = calculos[23];
		valEsp = md.valEsp[23];
		valEspTol = md.valEspTol[23];
		medida = md.medidas[23];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[24];
		valEsp = md.valEsp[24];
		valEspTol = md.valEspTol[24];
		medida = md.medidas[24];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[25];
		valEsp = md.valEsp[25];
		valEspTol = md.valEspTol[25];
		medida = md.medidas[25];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[26];
		valEsp = md.valEsp[26];
		valEspTol = md.valEspTol[26];
		medida = md.medidas[26];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		
		cell.setColspan(3);
		cell.setPhrase(new Paragraph("Membros Inferiores"));
		cell.setGrayFill(0.75f);
		analiseTable.addCell(cell);
		cell.setColspan(0);
		result = 1E300;
		result = calculos[27];
		valEsp = md.valEsp[27];
		valEspTol = md.valEspTol[27];
		medida = md.medidas[27];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		result = 1E300;
		result = calculos[28];
		valEsp = md.valEsp[28];
		valEspTol = md.valEspTol[28];
		medida = md.medidas[28];
		analiseTable.addCell(medida);
		analiseTable.addCell(valEspTol(valEsp, valEspTol)); //valor de referência
		if(result!=1E300){
			analiseTable.addCell(sapo.numFormat.format(result));
		}
		else
			analiseTable.addCell("não disponínel");
		try{   
			doc.add(analiseTable);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void capa(Document doc){
		try {
			Rectangle r = document.getPageSize();
			float w = r.width();
			float h = r.height();
			float leftMargin = document.leftMargin();
			float rightMargin = document.rightMargin();
			float bottomMargin = document.bottomMargin();
			float topMargin = document.topMargin();
			
			//Espaço para centralizar o texto na vertical
			Paragraph p;
			for (int i = 0; i < 20; i++){ 
				p = new Paragraph();
				p.setAlignment(Paragraph.ALIGN_CENTER);
				p.add(new Chunk(" "));
				doc.add(p);
			}
			
			String nome = "Nome: "+sapo.paciente.dados.nome;
			p = new Paragraph();
			p.setAlignment(Paragraph.ALIGN_CENTER);
			p.add(new Chunk(nome));
			doc.add(p);
			
			String dataNasc = "Dada de nascimento: "+sapo.paciente.dados.diaNasc+"/"+sapo.paciente.dados.mesNasc+"/"+sapo.paciente.dados.anoNasc;
			p = new Paragraph();
			p.setAlignment(Paragraph.ALIGN_CENTER);
			p.add(new Chunk(dataNasc));
			doc.add(p);
			
			String dataAval = "Data da avaliação: "+sapo.paciente.dados.dataalteracao.toString();        
			p = new Paragraph();
			p.setAlignment(Paragraph.ALIGN_CENTER);
			p.add(new Chunk(dataAval));
			doc.add(p);
			
			Image img = Image.getInstance(getClass().getResource("/res/capa01.jpg"));
			img.scaleToFit(w-(leftMargin+rightMargin), h-(bottomMargin+topMargin));
			img.setAbsolutePosition(leftMargin, bottomMargin);
			doc.add(img);
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		catch(IOException ioe) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), ioe.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void comentarios(Document doc, String str){
		PdfPTable coment = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Paragraph("Comentários"));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setGrayFill(0.5f);
		coment.addCell(cell);
		PdfPCell cell2 = new PdfPCell(new Paragraph(str));
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		coment.addCell(cell2);
		try{   
			doc.add(coment);
		}
		catch(DocumentException de) {
			System.err.println(de.getMessage());
		}
	}
	
	private void printPDF(){
		try {
			String filename = sapo.figDirPath + StartSAPO.sep + "temp.pdf";
			StartSAPO.deleteOnExit(filename);
			writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
			writer.setPageEvent(this);
			document.addTitle("Relatório de Avaliação Postural");
			document.addAuthor(sapo.user.dados.nomecompleto);
			document.addSubject("Relatório gerado automaticamente pelo SAPO.");
			document.addKeywords("SAPO, Relatório, avaliação postual, fisioterapia");
			document.addCreator("SAPO versão: "+SAPO.VERSION);
			document.open();
			int n=0;
			//Capa
			capa(document);
			
			//Cabecalho
			if(config[0]){
				document.newPage();
				n = adicionaCapitulo(document, "Dados do paciente\n ", n);
				cabecalhoEDados(document);
			}
			//Imagens
			if(config[1]){
				addAllImages(document);
			}
			
			//Analise
			if(config[2]){
				document.newPage();
				n = adicionaCapitulo(document, "Análise dos dados\n ", n);
				analise(document);
			}    
			
			//Centro e Gravidade
			if(config[3]){
				//document.newPage();
				n = adicionaCapitulo(document, "Centro de gravidade\n ", n);
				centroGravidade(document);
			}
			
			//Distancias Medidas
			if(config[4]){
				document.newPage();
				n = adicionaCapitulo(document, "Distancias Medidas\n ", n);
				distanciasMedidas(document);
			}
			
			//Angulos Medidos
			if(config[5]){
				document.newPage();
				n = adicionaCapitulo(document, "Ângulos medidos\n ", n);
				angulosMedidos(document);
				
			}
			
			//Pontos Medidos
			if(config[6]){
				document.newPage();
				n = adicionaCapitulo(document, "Pontos medidos\n ", n);
				pontosMedidos(document);
			}
			
			//Comentários
			if(config[7]){
				document.newPage();
				n = adicionaCapitulo(document, "Comentários\n ", n);
				comentarios(document, coment);
			}
		}
		catch(DocumentException de) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), de.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		catch(IOException ioe) {
			javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), ioe.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		document.close();
	}
	
	public int adicionaCapitulo(Document doc, String str, int i){
		int n = i+1;
		com.lowagie.text.Chapter ch = new com.lowagie.text.Chapter(str, n);
		try{
			doc.add(ch);
		}catch(Exception e){}
		return n;
	}
	
	public void setConfig(boolean cng[], String str){
		if(cng!=null){
			config = new boolean[cng.length];
			config = cng;
			coment = str;
		}
		final MsgPane waitMsg = new MsgPane();
		SwingWorker aWorker = new SwingWorker() {
			public Object doInBackground() {
				printPDF();
				PrintRelatorioPanel relPdf = new PrintRelatorioPanel(sapo);
				sapo.clearGlassPane(sapo);
				return null;
			}
		};
		sapo.setGlassPane(waitMsg);
		waitMsg.setText("Preparando impressão ...");
		sapo.getGlassPane().setVisible(true);
		aWorker.execute();
		
	}
	
	
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			PdfContentByte cb = writer.getDirectContent();
			if(writer.getPageNumber()>1){
				cb.saveState();
				Rectangle page = document.getPageSize();
				float width[] = {4f,1f};
				PdfPTable head = new PdfPTable(width);
				head.getDefaultCell().setBackgroundColor( new java.awt.Color(255,255,255));
				head.getDefaultCell().setBorderWidth(0);
				head.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				head.addCell("Relatório gerado automaticamente pelo programa SAPO versão: "+SAPO.VERSION+
						"\n\nNome: "+sapo.paciente.dados.nome);
				head.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				head.addCell("Página "+writer.getPageNumber());
				head.setTotalWidth(page.width() - document.leftMargin() - document.rightMargin());
				head.writeSelectedRows(0, -1, document.left(), document.getPageSize().height() - 50, cb);
				cb.restoreState();
			}
		}
		catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}
	
	
}
