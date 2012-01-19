/*
 * UIProtocol.java
 *
 * Created on 30 de Novembro de 2005, 07:00
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingworker.SwingWorker;

import com.dati.image.AnguloMedido;
import com.dati.image.DistanciaMedida;
import com.dati.image.PontoMedido;
import com.dati.util.DataHora;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;


public class UIRelatorio extends BaseInternalFrame {
	
	private static final long serialVersionUID = -8079739023471800512L;
	SAPO sapo;
	int jwidth, jheight = 0;
	int picW, picH = 0;
	int cgW, cgH = 0;
        ArrayList pontosDesenho[];
	
	public UIRelatorio(final SAPO sapo) {
		super("SAPO - Relatório de Análise");
		this.sapo = sapo;
		final MsgPane waitMsg = new MsgPane();
		sapo.setGlassPane(waitMsg);
		waitMsg.setText("Criando relatório ...");
		sapo.getGlassPane().setVisible(true);
		initComponents();
		SwingWorker aWorker = new SwingWorker() {
			public Object doInBackground() {
				cabecalhoEDados();
				initComponents2();
				resultadoAnalise2();
                                resultadoAnaliseTabelaVista();
				pontosMedidos();
				distanciasMedidas();
				angulosMedidos();
				UIRelatorio.this.pack();
				try{
					UIRelatorio.this.setMaximum(true);
				}catch(Exception e){};
				UIRelatorio.this.setVisible(true);
				sapo.clearGlassPane(sapo);
				
				return null;
			}
		};
		aWorker.execute();
	}
	
	protected void cabecalhoEDados() {
                String str = sapo.paciente.dados.nome;
		jNome.setText("Nome: "+str);
                jNome1.setText("Nome: "+str);
                jNome6.setText("Nome: "+str);
                jNome7.setText("Nome: "+str);
                jNome5.setText("Nome: "+str);
                jNome2.setText("Nome: "+str);
                jNome8.setText("Nome: "+str);
                                
		//jDataNasc.setText("Dada de nascimento: "+sapo.paciente.dados.diaNasc+"/"+sapo.paciente.dados.mesNasc+"/"+sapo.paciente.dados.anoNasc);
		String strDataCria = DataHora.getSQLDate(sapo.paciente.dados.datacriacao);
                String strDataAlt = DataHora.getSQLDate(sapo.paciente.dados.dataalteracao);
                String data = "Data da avaliação: "+strDataCria+"   --->   "+"Alterado em: "+strDataAlt;
                jDataAval.setText("Data da avaliação: "+data);
		jDataAval1.setText("Data da avaliação: "+data);
                jDataAval6.setText("Data da avaliação: "+data);
                jDataAval7.setText("Data da avaliação: "+data);
                jDataAval5.setText("Data da avaliação: "+data);
                jDataAval2.setText("Data da avaliação: "+data);
                jDataAval8.setText("Data da avaliação: "+data);
                //jwidth = jPanel4.getWidth();
		//jheight = jPanel4.getHeight();
                
	}
	
	private void desenhaCG(){
		java.awt.Image image = null;
		try{
			image =  javax.imageio.ImageIO.read(getClass().getResource("/res/cg-feet.png"));
		}catch(Exception e){}
		int w = image.getWidth(this);
		int h = image.getHeight(this);
		optimizaZoom(w,h);
		java.awt.image.BufferedImage myImage = (java.awt.image.BufferedImage)image;
		cgW = myImage.getWidth();
		cgH = myImage.getHeight();
		MedidasAnalise md = new MedidasAnalise(sapo);
		double calculos[] = new double[md.medidas.length];
		calculos = md.calculaAnalise(sapo);
		java.awt.Graphics2D g2 = myImage.createGraphics();
		if((calculos[37]!=1E300)&&(calculos[39]!=1E300)){
			int xPicCG = Double.valueOf(((0.782*cgW)/md.distMaleolos)*(calculos[41])+cgW/2).intValue();
                        int yPicCG = Double.valueOf(((-0.560*cgH)/md.tamPeMedio)*(calculos[42])+0.791*cgH).intValue();
                        //int yPicCG = Double.valueOf(((-0.560*cgH)*calculos[42]/calculos[39])*(calculos[42])+0.791*cgH).intValue();
			g2.setColor(java.awt.Color.RED);
			g2.fillOval(xPicCG-20,yPicCG-20,40,40);
			g2.setColor(java.awt.Color.BLUE);
			g2.drawLine(xPicCG-25, yPicCG, xPicCG+25, yPicCG);
			g2.drawLine(xPicCG, yPicCG-25, xPicCG, yPicCG+25);
		}
		image = myImage.getScaledInstance(picW, picH, java.awt.Image.SCALE_FAST);
		jlblCG.setIcon(new javax.swing.ImageIcon(image));
	}
	
	private void optimizaZoom(int iw, int ih){
		double scala = ih*1.0/iw;
		picW = 400; //fixa um tamnaho p/ as imagens
		picH = (int)Math.round(scala*picW);
                escala = iw/400;
	}//optimizaZoom
	
        double escala;
        
	private void distanciasMedidas(){
		int max = 0;
		javax.swing.table.DefaultTableModel p4 = (javax.swing.table.DefaultTableModel)jTableP4Dist.getModel();
		javax.swing.table.DefaultTableModel p5 = (javax.swing.table.DefaultTableModel)jTableP5Dist.getModel();
		javax.swing.table.DefaultTableModel p6 = (javax.swing.table.DefaultTableModel)jTableP6Dist.getModel();
		javax.swing.table.DefaultTableModel p7 = (javax.swing.table.DefaultTableModel)jTableP7Dist.getModel();
		for (int i=1; i<SAPO.maxImg; i++){
			if ( sapo.jif[i] == null ) continue;   
			java.util.ArrayList valores = sapo.paciente.dados.imgData[i].getDistanciaMedida();
			String vista = sapo.paciente.dados.imgData[i].vista;
			if(vista.equalsIgnoreCase("Anterior")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					p4.insertRow(j, new Object[]{null});
					jTableP4Dist.setValueAt(nomeTemp, j, 0);
					jTableP4Dist.setValueAt(new Double(distanciaTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP4Dist.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
			if(vista.equalsIgnoreCase("Posterior")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					p5.addRow(new Object[]{null});
					jTableP5Dist.setValueAt(nomeTemp, j, 0);
					jTableP5Dist.setValueAt(new Double(distanciaTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP5Dist.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Direita")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					p6.addRow(new Object[]{null});
					jTableP6Dist.setValueAt(nomeTemp, j, 0);
					jTableP6Dist.setValueAt(new Double(distanciaTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP6Dist.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Esquerda")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((DistanciaMedida)valores.get(j)).nome;
					double distanciaTemp = ((DistanciaMedida)valores.get(j)).distanciaMedida;
					p7.addRow(new Object[]{null});
					jTableP7Dist.setValueAt(nomeTemp, j, 0);
					jTableP7Dist.setValueAt(new Double(distanciaTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP7Dist.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
		}//for i
	}//DistanciaMedida
	
	private void angulosMedidos(){
		int max = 0;
		javax.swing.table.DefaultTableModel p4 = (javax.swing.table.DefaultTableModel)jTableP4Ang.getModel();
		javax.swing.table.DefaultTableModel p5 = (javax.swing.table.DefaultTableModel)jTableP5Ang.getModel();
		javax.swing.table.DefaultTableModel p6 = (javax.swing.table.DefaultTableModel)jTableP6Ang.getModel();
		javax.swing.table.DefaultTableModel p7 = (javax.swing.table.DefaultTableModel)jTableP7Ang.getModel();
		for (int i=1; i<SAPO.maxImg; i++){
			if ( sapo.jif[i] == null ) continue;   
			java.util.ArrayList valores = sapo.paciente.dados.imgData[i].getAnguloMedido();
			String vista = sapo.paciente.dados.imgData[i].vista;
			if(vista.equalsIgnoreCase("Anterior")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					p4.insertRow(j, new Object[]{null});
					jTableP4Ang.setValueAt(nomeTemp, j, 0);
					jTableP4Ang.setValueAt(new Double(anguloTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP4Ang.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
			if(vista.equalsIgnoreCase("Posterior")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					p5.insertRow(j, new Object[]{null});
					jTableP5Ang.setValueAt(nomeTemp, j, 0);
					jTableP5Ang.setValueAt(new Double(anguloTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP5Ang.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Direita")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					p6.insertRow(j, new Object[]{null});
					jTableP6Ang.setValueAt(nomeTemp, j, 0);
					jTableP6Ang.setValueAt(new Double(anguloTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP6Ang.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
			if(vista.equalsIgnoreCase("Lateral Esquerda")){
				for(int j=0; j<valores.size(); j++){
					String nomeTemp = ((AnguloMedido)valores.get(j)).nome;
					double anguloTemp = ((AnguloMedido)valores.get(j)).angulo;
					p7.insertRow(j, new Object[]{null});
					jTableP7Ang.setValueAt(nomeTemp, j, 0);
					jTableP7Ang.setValueAt(new Double(anguloTemp), j, 1);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP7Ang.getColumnModel().getColumn(0).setPreferredWidth(max*4);
			}//if vista
		}//for i
	}//angulosMedidos
	
	private void pontosMedidos(){
		int max = 0;
		javax.swing.table.DefaultTableModel p4 = (javax.swing.table.DefaultTableModel)jTableP4Pontos.getModel();
		javax.swing.table.DefaultTableModel p5 = (javax.swing.table.DefaultTableModel)jTableP5Pontos.getModel();
		javax.swing.table.DefaultTableModel p6 = (javax.swing.table.DefaultTableModel)jTableP6Pontos.getModel();
		javax.swing.table.DefaultTableModel p7 = (javax.swing.table.DefaultTableModel)jTableP7Pontos.getModel();
		for (int i=1; i<SAPO.maxImg; i++){
			if ( sapo.jif[i] == null ) continue;   
			ArrayList valores = sapo.paciente.dados.imgData[i].getPontos();
			String vista = sapo.paciente.dados.imgData[i].vista;
			double escalaX = sapo.paciente.dados.imgData[i].getEscalaX();
			double escalaY = sapo.paciente.dados.imgData[i].getEscalaY();
			if(vista.equalsIgnoreCase("Anterior")){
				jLabelP4Cab.setText("Vista "+vista);
				jTabbedPane1.setTitleAt(0,"Vista "+vista);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					p4.insertRow(j, new Object[]{null});
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY;
					jTableP4Pontos.setValueAt(nomeTemp, j, 0);
					jTableP4Pontos.setValueAt(sapo.numFormat.format(pX),j,1);
					jTableP4Pontos.setValueAt(sapo.numFormat.format(pY),j,2);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				//jTableP4Pontos.getColumnModel().getColumn(0).setPreferredWidth(max*4);
                                jTableP4Pontos.getColumnModel().getColumn(0).setPreferredWidth(200);    
                        }//if vista
                        
			if(vista.equalsIgnoreCase("Posterior")){
				jLabelP5Cab.setText("Vista "+vista);
				jTabbedPane1.setTitleAt(3,"Vista "+vista);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					p5.insertRow(j, new Object[]{null});
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY; 
					jTableP5Pontos.setValueAt(nomeTemp, j, 0);
					jTableP5Pontos.setValueAt(sapo.numFormat.format(pX),j,1);
					jTableP5Pontos.setValueAt(sapo.numFormat.format(pY),j,2);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP5Pontos.getColumnModel().getColumn(0).setPreferredWidth(200);
			}//if
                        
			if(vista.equalsIgnoreCase("Lateral Direita")){
				jLabelP6Cab.setText("Vista "+vista);
				jTabbedPane1.setTitleAt(1,"Vista "+vista);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					p6.insertRow(j, new Object[]{null});
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY; 
					jTableP6Pontos.setValueAt(nomeTemp, j, 0);
					jTableP6Pontos.setValueAt(sapo.numFormat.format(pX),j,1);
					jTableP6Pontos.setValueAt(sapo.numFormat.format(pY),j,2);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP6Pontos.getColumnModel().getColumn(0).setPreferredWidth(200);
			}//if
                        
			if(vista.equalsIgnoreCase("Lateral Esquerda")){
				jLabelP7Cab.setText("Vista "+vista);
				jTabbedPane1.setTitleAt(2,"Vista "+vista);
				for (int j=0; j<valores.size(); j++){
					Point pto = ((PontoMedido)valores.get(j)).p;
					p7.insertRow(j, new Object[]{null});
					String nomeTemp = ((PontoMedido)valores.get(j)).nome;
					double pX = (pto.x)*escalaX; 
					double pY = (pto.y)*escalaY; 
					jTableP7Pontos.setValueAt(nomeTemp, j, 0);
					jTableP7Pontos.setValueAt(sapo.numFormat.format(pX),j,1);
					jTableP7Pontos.setValueAt(sapo.numFormat.format(pY),j,2);
					if(max<nomeTemp.length())
						max = nomeTemp.length();
				}//for j
				jTableP7Pontos.getColumnModel().getColumn(0).setPreferredWidth(200);
			}//if
                        
		}// for i
                if(jTabbedPane1.getTitleAt(0).equalsIgnoreCase("")) jTabbedPane1.remove(0);
                if(jTabbedPane1.getTitleAt(1).equalsIgnoreCase("")) jTabbedPane1.remove(1);
                if(jTabbedPane1.getTitleAt(2).equalsIgnoreCase("")) jTabbedPane1.remove(2);
                if(jTabbedPane1.getTitleAt(3).equalsIgnoreCase("")) jTabbedPane1.remove(3);
	}//PontosMedidos
	
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPagina4 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jTitulo1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jNome1 = new javax.swing.JLabel();
        jDataAval1 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jLabelP4Cab = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabelP4 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTableP4Pontos1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableP4Ang = new javax.swing.JTable();
        jPanel38 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableP4Dist = new javax.swing.JTable();
        jPanel37 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPagina6 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jTitulo6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jNome6 = new javax.swing.JLabel();
        jDataAval6 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jLabelP6Cab = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jLabelP6 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jPanel72 = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTableP4Pontos2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTableP6Ang = new javax.swing.JTable();
        jPanel59 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTableP6Dist = new javax.swing.JTable();
        jPanel57 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPagina7 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jTitulo7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jNome7 = new javax.swing.JLabel();
        jDataAval7 = new javax.swing.JLabel();
        jPanel60 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jPanel62 = new javax.swing.JPanel();
        jLabelP7Cab = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jLabelP7 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jPanel75 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTableP4Pontos3 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTableP7Ang = new javax.swing.JTable();
        jPanel69 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTableP7Dist = new javax.swing.JTable();
        jPanel67 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jPagina5 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jTitulo5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jNome5 = new javax.swing.JLabel();
        jDataAval5 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jLabelP5Cab = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jLabelP5 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTableP4Pontos4 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTableP5Ang = new javax.swing.JTable();
        jPanel49 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTableP5Dist = new javax.swing.JTable();
        jPanel47 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPagina3 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jTitulo2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jNome2 = new javax.swing.JLabel();
        jDataAval2 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlblAssFront = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jlblAssFrontRel = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jlblAssSag = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jlblAssSagRel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel16 = new javax.swing.JPanel();
        jlblCG = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jTitulo3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jNome = new javax.swing.JLabel();
        jDataAval = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jPagina9 = new javax.swing.JPanel();
        jPanel83 = new javax.swing.JPanel();
        jPanel86 = new javax.swing.JPanel();
        jPanel87 = new javax.swing.JPanel();
        jPanel88 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableP4Pontos = new javax.swing.JTable();
        jPanel54 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableP6Pontos = new javax.swing.JTable();
        jPanel55 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel64 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTableP7Pontos = new javax.swing.JTable();
        jPanel65 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTableP5Pontos = new javax.swing.JTable();
        jPanel45 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jckbDadosPac = new javax.swing.JCheckBox();
        jckbFotos = new javax.swing.JCheckBox();
        jckbAnalis = new javax.swing.JCheckBox();
        jckbCG = new javax.swing.JCheckBox();
        jckbDist = new javax.swing.JCheckBox();
        jckbAngle = new javax.swing.JCheckBox();
        jckbPontos = new javax.swing.JCheckBox();
        jckbComent = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jtxtPaneComent = new javax.swing.JTextPane();
        jPanel21 = new javax.swing.JPanel();
        jbtnImprimir = new javax.swing.JButton();
        jPanel71 = new javax.swing.JPanel();
        jTitulo8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jNome8 = new javax.swing.JLabel();
        jDataAval8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jbtnCancel = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("SAPO - Relat\u00f3rio de An\u00e1lise");
        setDesktopIcon(getDesktopIcon());
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("res/frog.gif")));
        try {
            setIcon(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setPreferredSize(new java.awt.Dimension(500, 410));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(600, 840));
        jPagina4.setLayout(new java.awt.BorderLayout());

        jPanel25.setLayout(new java.awt.GridLayout(4, 1));

        jPanel25.setBackground(new java.awt.Color(153, 153, 153));
        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo1.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo1.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel25.add(jTitulo1);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel25.add(jLabel4);

        jNome1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome1.setText("Nome:");
        jPanel25.add(jNome1);

        jDataAval1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval1.setText("Data de avalia\u00e7\u00e3o:");
        jPanel25.add(jDataAval1);

        jPagina4.add(jPanel25, java.awt.BorderLayout.NORTH);

        jPanel30.setLayout(new java.awt.GridLayout(1, 2));

        jPanel31.setLayout(new java.awt.BorderLayout());

        jPanel39.setBackground(new java.awt.Color(153, 153, 153));
        jLabelP4Cab.setFont(new java.awt.Font("Arial", 1, 12));
        jLabelP4Cab.setText("Vista: ");
        jPanel39.add(jLabelP4Cab);

        jPanel31.add(jPanel39, java.awt.BorderLayout.NORTH);

        jLabelP4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane1.setViewportView(jLabelP4);

        jPanel31.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel31);

        jPanel32.setLayout(new java.awt.GridLayout(2, 1));

        jPanel33.setLayout(new java.awt.BorderLayout());

        jPanel36.setBackground(new java.awt.Color(153, 153, 153));
        jLabel10.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel10.setText("Pontos medidos");
        jPanel36.add(jLabel10);

        jPanel33.add(jPanel36, java.awt.BorderLayout.NORTH);

        jTableP4Pontos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Apresenta", "Nome", "Val. Esperado", "Val Medido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableP4Pontos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableP4Pontos1MouseClicked(evt);
            }
        });

        jScrollPane25.setViewportView(jTableP4Pontos1);

        jPanel33.add(jScrollPane25, java.awt.BorderLayout.CENTER);

        jPanel32.add(jPanel33);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jPanel35.setLayout(new java.awt.BorderLayout());

        jTableP4Ang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Ângulo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTableP4Ang);

        jPanel35.add(jScrollPane10, java.awt.BorderLayout.CENTER);

        jPanel38.setBackground(new java.awt.Color(153, 153, 153));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel12.setText("\u00c2ngulos Medidos Livremente");
        jPanel38.add(jLabel12);

        jPanel35.add(jPanel38, java.awt.BorderLayout.NORTH);

        jPanel4.add(jPanel35);

        jPanel34.setLayout(new java.awt.BorderLayout());

        jTableP4Dist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Distância"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(jTableP4Dist);

        jPanel34.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel37.setBackground(new java.awt.Color(153, 153, 153));
        jLabel11.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel11.setText("Dist\u00e2ncias Medidas Livremente");
        jPanel37.add(jLabel11);

        jPanel34.add(jPanel37, java.awt.BorderLayout.NORTH);

        jPanel4.add(jPanel34);

        jPanel32.add(jPanel4);

        jPanel30.add(jPanel32);

        jPagina4.add(jPanel30, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("", jPagina4);

        jPagina6.setLayout(new java.awt.BorderLayout());

        jPanel29.setLayout(new java.awt.GridLayout(4, 1));

        jPanel29.setBackground(new java.awt.Color(153, 153, 153));
        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo6.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo6.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel29.add(jTitulo6);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel29.add(jLabel8);

        jNome6.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome6.setText("Nome:");
        jPanel29.add(jNome6);

        jDataAval6.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval6.setText("Data de avalia\u00e7\u00e3o:");
        jPanel29.add(jDataAval6);

        jPagina6.add(jPanel29, java.awt.BorderLayout.NORTH);

        jPanel50.setLayout(new java.awt.GridLayout(1, 2));

        jPanel51.setLayout(new java.awt.BorderLayout());

        jPanel52.setBackground(new java.awt.Color(153, 153, 153));
        jLabelP6Cab.setFont(new java.awt.Font("Arial", 1, 12));
        jLabelP6Cab.setText("Vista: ");
        jPanel52.add(jLabelP6Cab);

        jPanel51.add(jPanel52, java.awt.BorderLayout.NORTH);

        jLabelP6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane21.setViewportView(jLabelP6);

        jPanel51.add(jScrollPane21, java.awt.BorderLayout.CENTER);

        jPanel50.add(jPanel51);

        jPanel53.setLayout(new java.awt.GridLayout(2, 1));

        jPanel72.setLayout(new java.awt.BorderLayout());

        jPanel73.setBackground(new java.awt.Color(153, 153, 153));
        jLabel14.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel14.setText("Pontos medidos");
        jPanel73.add(jLabel14);

        jPanel72.add(jPanel73, java.awt.BorderLayout.NORTH);

        jTableP4Pontos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Apresenta", "Nome", "Val. Esperado", "Val Medido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane26.setViewportView(jTableP4Pontos2);

        jPanel72.add(jScrollPane26, java.awt.BorderLayout.CENTER);

        jPanel53.add(jPanel72);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        jPanel58.setLayout(new java.awt.BorderLayout());

        jTableP6Ang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Ângulo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane16.setViewportView(jTableP6Ang);

        jPanel58.add(jScrollPane16, java.awt.BorderLayout.CENTER);

        jPanel59.setBackground(new java.awt.Color(153, 153, 153));
        jLabel24.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel24.setText("\u00c2ngulos Medidos Livremente");
        jPanel59.add(jLabel24);

        jPanel58.add(jPanel59, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel58);

        jPanel56.setLayout(new java.awt.BorderLayout());

        jTableP6Dist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Distância"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(jTableP6Dist);

        jPanel56.add(jScrollPane15, java.awt.BorderLayout.CENTER);

        jPanel57.setBackground(new java.awt.Color(153, 153, 153));
        jLabel23.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel23.setText("Dist\u00e2ncias Medidas Livremente");
        jPanel57.add(jLabel23);

        jPanel56.add(jPanel57, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel56);

        jPanel53.add(jPanel3);

        jPanel50.add(jPanel53);

        jPagina6.add(jPanel50, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("", jPagina6);

        jPagina7.setLayout(new java.awt.BorderLayout());

        jPanel70.setLayout(new java.awt.GridLayout(4, 1));

        jPanel70.setBackground(new java.awt.Color(153, 153, 153));
        jPanel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo7.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo7.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel70.add(jTitulo7);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel70.add(jLabel9);

        jNome7.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome7.setText("Nome:");
        jPanel70.add(jNome7);

        jDataAval7.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval7.setText("Data de avalia\u00e7\u00e3o:");
        jPanel70.add(jDataAval7);

        jPagina7.add(jPanel70, java.awt.BorderLayout.NORTH);

        jPanel60.setLayout(new java.awt.GridLayout(1, 2));

        jPanel61.setLayout(new java.awt.BorderLayout());

        jPanel62.setBackground(new java.awt.Color(153, 153, 153));
        jLabelP7Cab.setFont(new java.awt.Font("Arial", 1, 12));
        jLabelP7Cab.setText("Vista: ");
        jPanel62.add(jLabelP7Cab);

        jPanel61.add(jPanel62, java.awt.BorderLayout.NORTH);

        jLabelP7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane22.setViewportView(jLabelP7);

        jPanel61.add(jScrollPane22, java.awt.BorderLayout.CENTER);

        jPanel60.add(jPanel61);

        jPanel63.setLayout(new java.awt.GridLayout(2, 1));

        jPanel74.setLayout(new java.awt.BorderLayout());

        jPanel75.setBackground(new java.awt.Color(153, 153, 153));
        jLabel15.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel15.setText("Pontos medidos");
        jPanel75.add(jLabel15);

        jPanel74.add(jPanel75, java.awt.BorderLayout.NORTH);

        jTableP4Pontos3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Apresenta", "Nome", "Val. Esperado", "Val Medido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane27.setViewportView(jTableP4Pontos3);

        jPanel74.add(jScrollPane27, java.awt.BorderLayout.CENTER);

        jPanel63.add(jPanel74);

        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jPanel68.setLayout(new java.awt.BorderLayout());

        jTableP7Ang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Ângulo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane19.setViewportView(jTableP7Ang);

        jPanel68.add(jScrollPane19, java.awt.BorderLayout.CENTER);

        jPanel69.setBackground(new java.awt.Color(153, 153, 153));
        jLabel29.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel29.setText("\u00c2ngulos Medidos Livremente");
        jPanel69.add(jLabel29);

        jPanel68.add(jPanel69, java.awt.BorderLayout.NORTH);

        jPanel5.add(jPanel68);

        jPanel66.setLayout(new java.awt.BorderLayout());

        jTableP7Dist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Distância"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane18.setViewportView(jTableP7Dist);

        jPanel66.add(jScrollPane18, java.awt.BorderLayout.CENTER);

        jPanel67.setBackground(new java.awt.Color(153, 153, 153));
        jLabel28.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel28.setText("Dist\u00e2ncias Medidas Livremente");
        jPanel67.add(jLabel28);

        jPanel66.add(jPanel67, java.awt.BorderLayout.NORTH);

        jPanel5.add(jPanel66);

        jPanel63.add(jPanel5);

        jPanel60.add(jPanel63);

        jPagina7.add(jPanel60, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("", jPagina7);

        jPagina5.setLayout(new java.awt.BorderLayout());

        jPanel28.setLayout(new java.awt.GridLayout(4, 1));

        jPanel28.setBackground(new java.awt.Color(153, 153, 153));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo5.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo5.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel28.add(jTitulo5);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel28.add(jLabel7);

        jNome5.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome5.setText("Nome:");
        jPanel28.add(jNome5);

        jDataAval5.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval5.setText("Data de avalia\u00e7\u00e3o:");
        jPanel28.add(jDataAval5);

        jPagina5.add(jPanel28, java.awt.BorderLayout.NORTH);

        jPanel40.setLayout(new java.awt.GridLayout(1, 2));

        jPanel41.setLayout(new java.awt.BorderLayout());

        jPanel42.setBackground(new java.awt.Color(153, 153, 153));
        jLabelP5Cab.setFont(new java.awt.Font("Arial", 1, 12));
        jLabelP5Cab.setText("Vista: ");
        jPanel42.add(jLabelP5Cab);

        jPanel41.add(jPanel42, java.awt.BorderLayout.NORTH);

        jLabelP5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane20.setViewportView(jLabelP5);

        jPanel41.add(jScrollPane20, java.awt.BorderLayout.CENTER);

        jPanel40.add(jPanel41);

        jPanel43.setLayout(new java.awt.GridLayout(2, 1));

        jPanel76.setLayout(new java.awt.BorderLayout());

        jPanel77.setBackground(new java.awt.Color(153, 153, 153));
        jLabel16.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel16.setText("Pontos medidos");
        jPanel77.add(jLabel16);

        jPanel76.add(jPanel77, java.awt.BorderLayout.NORTH);

        jTableP4Pontos4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Apresenta", "Nome", "Val. Esperado", "Val Medido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane28.setViewportView(jTableP4Pontos4);

        jPanel76.add(jScrollPane28, java.awt.BorderLayout.CENTER);

        jPanel43.add(jPanel76);

        jPanel6.setLayout(new java.awt.GridLayout(2, 1));

        jPanel48.setLayout(new java.awt.BorderLayout());

        jTableP5Ang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Ângulo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane13.setViewportView(jTableP5Ang);

        jPanel48.add(jScrollPane13, java.awt.BorderLayout.CENTER);

        jPanel49.setBackground(new java.awt.Color(153, 153, 153));
        jLabel19.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel19.setText("\u00c2ngulos Medidos Livremente");
        jPanel49.add(jLabel19);

        jPanel48.add(jPanel49, java.awt.BorderLayout.NORTH);

        jPanel6.add(jPanel48);

        jPanel46.setLayout(new java.awt.BorderLayout());

        jTableP5Dist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Distância"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(jTableP5Dist);

        jPanel46.add(jScrollPane12, java.awt.BorderLayout.CENTER);

        jPanel47.setBackground(new java.awt.Color(153, 153, 153));
        jLabel18.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel18.setText("Dist\u00e2ncias Medidas Livremente");
        jPanel47.add(jLabel18);

        jPanel46.add(jPanel47, java.awt.BorderLayout.NORTH);

        jPanel6.add(jPanel46);

        jPanel43.add(jPanel6);

        jPanel40.add(jPanel43);

        jPagina5.add(jPanel40, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("", jPagina5);

        jPagina3.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(4, 1));

        jPanel14.setBackground(new java.awt.Color(153, 153, 153));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo2.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo2.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel14.add(jTitulo2);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Proje\u00e7\u00e3o do Centro de Gravidade");
        jPanel14.add(jLabel1);

        jNome2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome2.setText("Nome:");
        jPanel14.add(jNome2);

        jDataAval2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval2.setText("Data de avalia\u00e7\u00e3o:");
        jPanel14.add(jDataAval2);

        jPagina3.add(jPanel14, java.awt.BorderLayout.NORTH);

        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel17.setLayout(new java.awt.GridLayout(2, 2));

        jlblAssFront.setText("Assimetria no plano frontal (%):");
        jPanel2.add(jlblAssFront);

        jPanel17.add(jPanel2);

        jlblAssFrontRel.setText("Posi\u00e7\u00e3o da proje\u00e7\u00e3o do CG relativo a posi\u00e7ao m\u00e9dia dos mal\u00e9olos (plano frontal):");
        jPanel22.add(jlblAssFrontRel);

        jPanel17.add(jPanel22);

        jlblAssSag.setText("Assimetria no plano sagital (%):");
        jPanel9.add(jlblAssSag);

        jPanel17.add(jPanel9);

        jlblAssSagRel.setText("Posi\u00e7\u00e3o da proje\u00e7\u00e3o do CG relativo a posi\u00e7ao m\u00e9dia dos mal\u00e9olos (plano lateral):");
        jPanel23.add(jlblAssSagRel);

        jPanel17.add(jPanel23);

        jPanel15.add(jPanel17, java.awt.BorderLayout.WEST);

        jPanel13.add(jPanel15, java.awt.BorderLayout.NORTH);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jlblCG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        jPanel16.add(jlblCG, gridBagConstraints);

        jScrollPane5.setViewportView(jPanel16);

        jPanel13.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPagina3.add(jPanel13, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Proje\u00e7\u00e3o do Centro de Gravidade", jPagina3);

        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel26.setLayout(new java.awt.GridLayout(4, 1));

        jPanel26.setBackground(new java.awt.Color(153, 153, 153));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo3.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo3.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel26.add(jTitulo3);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Resultados da An\u00e1lise");
        jPanel26.add(jLabel5);

        jNome.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome.setText("Nome:");
        jPanel26.add(jNome);

        jDataAval.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval.setText("Data de avalia\u00e7\u00e3o:");
        jPanel26.add(jDataAval);

        jPanel24.add(jPanel26, java.awt.BorderLayout.NORTH);

        jPanel8.setLayout(new java.awt.GridLayout(45, 1, 2, 2));

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane7.setViewportView(jPanel8);

        jPanel24.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Resultados da An\u00e1lise", jPanel24);

        jPagina9.setLayout(new java.awt.BorderLayout());

        jPanel83.setLayout(new java.awt.GridLayout(1, 2));

        jPanel86.setLayout(new java.awt.GridLayout(4, 1));

        jPanel87.setLayout(new java.awt.BorderLayout());

        jPanel88.setBackground(new java.awt.Color(153, 153, 153));
        jLabel21.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel21.setText("Vista Anterior");
        jPanel88.add(jLabel21);

        jPanel87.add(jPanel88, java.awt.BorderLayout.NORTH);

        jTableP4Pontos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "X", "Y"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTableP4Pontos);

        jPanel87.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jPanel86.add(jPanel87);

        jPanel54.setLayout(new java.awt.BorderLayout());

        jTableP6Pontos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "X", "Y"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(jTableP6Pontos);

        jPanel54.add(jScrollPane14, java.awt.BorderLayout.CENTER);

        jPanel55.setBackground(new java.awt.Color(153, 153, 153));
        jLabel22.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel22.setText("Vista Laterial Direita");
        jPanel55.add(jLabel22);

        jPanel54.add(jPanel55, java.awt.BorderLayout.NORTH);

        jPanel86.add(jPanel54);

        jPanel64.setLayout(new java.awt.BorderLayout());

        jTableP7Pontos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "X", "Y"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane17.setViewportView(jTableP7Pontos);

        jPanel64.add(jScrollPane17, java.awt.BorderLayout.CENTER);

        jPanel65.setBackground(new java.awt.Color(153, 153, 153));
        jLabel27.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel27.setText("Vista Lateral Esquerda");
        jPanel65.add(jLabel27);

        jPanel64.add(jPanel65, java.awt.BorderLayout.NORTH);

        jPanel86.add(jPanel64);

        jPanel44.setLayout(new java.awt.BorderLayout());

        jTableP5Pontos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "X", "Y"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTableP5Pontos);

        jPanel44.add(jScrollPane11, java.awt.BorderLayout.CENTER);

        jPanel45.setBackground(new java.awt.Color(153, 153, 153));
        jLabel17.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel17.setText("Vista Posterior");
        jPanel45.add(jLabel17);

        jPanel44.add(jPanel45, java.awt.BorderLayout.NORTH);

        jPanel86.add(jPanel44);

        jPanel83.add(jPanel86);

        jPagina9.add(jPanel83, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Pontos Medidos", jPagina9);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.GridLayout(2, 1));

        jPanel12.setLayout(new java.awt.GridLayout(10, 1));

        jckbDadosPac.setSelected(true);
        jckbDadosPac.setText("Nome do paciente, Data de nascimento e Data de avalia\u00e7\u00e3o");
        jckbDadosPac.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbDadosPac.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbDadosPac);

        jckbFotos.setSelected(true);
        jckbFotos.setText("Fotos do paciente");
        jckbFotos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbFotos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbFotos);

        jckbAnalis.setSelected(true);
        jckbAnalis.setText("Resultados da an\u00e1lise pelo protocolo SAPO");
        jckbAnalis.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbAnalis.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbAnalis);

        jckbCG.setSelected(true);
        jckbCG.setText("Proje\u00e7\u00e3o do centro de gravidade");
        jckbCG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbCG.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbCG);

        jckbDist.setText("Dist\u00e2ncias medidas livremente");
        jckbDist.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbDist.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbDist);

        jckbAngle.setText("\u00c2ngulos medidos livremente");
        jckbAngle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbAngle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbAngle);

        jckbPontos.setText("Todos os pontos medidos");
        jckbPontos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbPontos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbPontos);

        jckbComent.setText("Coment\u00e1rios");
        jckbComent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckbComent.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jckbComent);

        jScrollPane23.setViewportView(jPanel12);

        jPanel19.add(jScrollPane23);

        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel20.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel3.setText("Coment\u00e1rios");
        jPanel20.add(jLabel3);

        jPanel18.add(jPanel20, java.awt.BorderLayout.NORTH);

        jScrollPane24.setViewportView(jtxtPaneComent);

        jPanel18.add(jScrollPane24, java.awt.BorderLayout.CENTER);

        jbtnImprimir.setText("Imprimir");
        jbtnImprimir.setToolTipText("Imprime o relat\u00f3rio");
        jbtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnImprimirActionPerformed(evt);
            }
        });

        jPanel21.add(jbtnImprimir);

        jPanel18.add(jPanel21, java.awt.BorderLayout.SOUTH);

        jPanel19.add(jPanel18);

        jPanel10.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel71.setLayout(new java.awt.GridLayout(4, 1));

        jPanel71.setBackground(new java.awt.Color(153, 153, 153));
        jPanel71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTitulo8.setFont(new java.awt.Font("Arial", 1, 14));
        jTitulo8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitulo8.setText("Relat\u00f3rio de avalia\u00e7\u00e3o postural");
        jPanel71.add(jTitulo8);

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Informa\u00e7\u00f5es para o relat\u00f3rio impresso");
        jPanel71.add(jLabel13);

        jNome8.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jNome8.setText("Nome:");
        jPanel71.add(jNome8);

        jDataAval8.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jDataAval8.setText("Data de avalia\u00e7\u00e3o:");
        jPanel71.add(jDataAval8);

        jPanel10.add(jPanel71, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Imprimir", new javax.swing.ImageIcon(getClass().getResource("/res/imprime.gif")), jPanel10);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jbtnCancel.setText("Fechar");
        jbtnCancel.setToolTipText("Cancela a impress\u00e3o");
        jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelActionPerformed(evt);
            }
        });

        jPanel1.add(jbtnCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableP4Pontos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableP4Pontos1MouseClicked
        // TODO add your handling code here:
/*
        for (int i=1; i<SAPO.maxImg; i++){
            if ( sapo.jif[i] == null ) continue;   
            
            String vista = sapo.paciente.dados.imgData[i].vista;
            
            if(vista.equalsIgnoreCase("Anterior")){
             
                int numLinha = jTableP4Pontos1.getSelectedRow();
                String labelPonto = String.valueOf(jTableP4Pontos1.getValueAt(numLinha,1));
                Boolean booleanPonto = (Boolean)(jTableP4Pontos1.getValueAt(numLinha,0));
                if(booleanPonto.booleanValue() == true){
                    //System.out.println(booleanPonto.booleanValue());
                    desenhanaFoto(i, labelPonto, true);
                } //if
                else{
                    desenhanaFoto(i, labelPonto, false);
                }

                
		               
            }//if vista
                        
        } //for i
 */
    }//GEN-LAST:event_jTableP4Pontos1MouseClicked
	
    private void desenhanaFoto(int index, String label, boolean desenha){
        MedidasAnalise md = new MedidasAnalise(sapo);
        ArrayList valores = sapo.paciente.dados.imgData[index].getPontos();
        Point2D.Double p1, p2, p3, p4;
        
        //------------------------------------------------------------------
        Graphics xxx = jLabelP4.getGraphics();
        int www = jLabelP4.getSize().width;
        int hhh = jLabelP4.getSize().height;
        int rrr = (www-400)/2;
        xxx.setColor(Color.YELLOW);
        
        if(label.equalsIgnoreCase(md.medidas[0])){
            p1 = (Point2D.Double)pontosDesenho[0].get(0);
            p2 = (Point2D.Double)pontosDesenho[0].get(1);
            xxx.drawLine(Math.round((int)(p1.x/escala+rrr)),Math.round((int)(p1.y/escala)),Math.round((int)(p2.x/escala+rrr)),Math.round((int)(p2.y/escala)));
        }
        if(label.equalsIgnoreCase(md.medidas[1])){
            p3 = (Point2D.Double)pontosDesenho[1].get(0);
            p4 = (Point2D.Double)pontosDesenho[1].get(1);
            xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        }
        if(label.equalsIgnoreCase(md.medidas[2])){
            p3 = (Point2D.Double)pontosDesenho[2].get(0);
            p4 = (Point2D.Double)pontosDesenho[2].get(1);
            xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        
        p1 = (Point2D.Double)pontosDesenho[4].get(0);
        p2 = (Point2D.Double)pontosDesenho[4].get(1);
        p3 = (Point2D.Double)pontosDesenho[4].get(2);
        p4 = (Point2D.Double)pontosDesenho[4].get(3);
        xxx.drawLine(Math.round((int)(p1.x/escala+rrr)),Math.round((int)(p1.y/escala)),Math.round((int)(p2.x/escala+rrr)),Math.round((int)(p2.y/escala)));
        xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        
        p1 = (Point2D.Double)pontosDesenho[5].get(0);
        p2 = (Point2D.Double)pontosDesenho[5].get(1);
        p3 = (Point2D.Double)pontosDesenho[5].get(2);
        p4 = (Point2D.Double)pontosDesenho[5].get(3);
        xxx.drawLine(Math.round((int)(p1.x/escala+rrr)),Math.round((int)(p1.y/escala)),Math.round((int)(p2.x/escala+rrr)),Math.round((int)(p2.y/escala)));
        xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        
        p3 = (Point2D.Double)pontosDesenho[7].get(0);
        p4 = (Point2D.Double)pontosDesenho[7].get(1);
        xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        
        p1 = (Point2D.Double)pontosDesenho[8].get(0);
        p2 = (Point2D.Double)pontosDesenho[8].get(1);
        p3 = (Point2D.Double)pontosDesenho[8].get(2);
        p4 = (Point2D.Double)pontosDesenho[8].get(3);
        xxx.drawLine(Math.round((int)(p1.x/escala+rrr)),Math.round((int)(p1.y/escala)),Math.round((int)(p2.x/escala+rrr)),Math.round((int)(p2.y/escala)));
        xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        
        p1 = (Point2D.Double)pontosDesenho[9].get(0);
        p2 = (Point2D.Double)pontosDesenho[9].get(1);
        p3 = (Point2D.Double)pontosDesenho[9].get(2);
        p4 = (Point2D.Double)pontosDesenho[9].get(3);
        xxx.drawLine(Math.round((int)(p1.x/escala+rrr)),Math.round((int)(p1.y/escala)),Math.round((int)(p2.x/escala+rrr)),Math.round((int)(p2.y/escala)));
        xxx.drawLine(Math.round((int)(p3.x/escala+rrr)),Math.round((int)(p3.y/escala)),Math.round((int)(p4.x/escala+rrr)),Math.round((int)(p4.y/escala)));
        
        }
        
        
    } //desenhaFoto
    
    //public void paint(Graphics g){
        
    //}
    
	private void jbtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnImprimirActionPerformed
		boolean config[] = {
				jckbDadosPac.isSelected(),
				jckbFotos.isSelected(),        
				jckbAnalis.isSelected(),
				jckbCG.isSelected(),
				jckbDist.isSelected(),
				jckbAngle.isSelected(),
				jckbPontos.isSelected(),
				jckbComent.isSelected()
		};
		CreatePDF pdf = new CreatePDF(sapo);
		pdf.setConfig(config, jtxtPaneComent.getText());
	}//GEN-LAST:event_jbtnImprimirActionPerformed
	
        private void jbtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelActionPerformed
		dispose();
        }//GEN-LAST:event_jbtnCancelActionPerformed
	
	private void initComponents2(){
		jScrollPane7.getVerticalScrollBar().setUnitIncrement(25);
		for (int i=1; i<SAPO.maxImg; i++) {
			if ( sapo.jif[i] == null ) continue;   
			String pict = "";
			java.awt.Image ttt = null;
			String vistaStr = sapo.paciente.dados.imgData[i].vista;
			if(vistaStr.equalsIgnoreCase("Anterior")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					com.sun.media.jai.codec.FileSeekableStream stream = new com.sun.media.jai.codec.FileSeekableStream( sapo.paciente.dados.imgData[i].fileImage.getPath() );
					ttt = javax.media.jai.JAI.create("stream", stream).getAsBufferedImage(); //.getScaledInstance(100,100,0);
				}catch(Exception erro){}
				if(ttt!=null){
					int w = ttt.getWidth(this);
					int h = ttt.getHeight(this);
					optimizaZoom(w,h);
					ttt = ttt.getScaledInstance(picW, picH, java.awt.Image.SCALE_FAST);
					//jFotoAnterior.setIcon(new javax.swing.ImageIcon(ttt));
					//jFotoAnterior.setVerticalAlignment(javax.swing.SwingConstants.TOP);
					jLabelP4.setIcon(new javax.swing.ImageIcon(ttt));
				}
			}
			if(vistaStr.equalsIgnoreCase("Posterior")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					com.sun.media.jai.codec.FileSeekableStream stream = new com.sun.media.jai.codec.FileSeekableStream( sapo.paciente.dados.imgData[i].fileImage.getPath() );
					ttt = javax.media.jai.JAI.create("stream", stream).getAsBufferedImage(); //.getScaledInstance(100,100,0);
				}catch(Exception erro){}
				if(ttt!=null){
					int w = ttt.getWidth(this);
					int h = ttt.getHeight(this);
					optimizaZoom(w,h);
					ttt = ttt.getScaledInstance(picW, picH, java.awt.Image.SCALE_FAST);
					//jFotoPosterior.setIcon(new javax.swing.ImageIcon(ttt));
					//jFotoPosterior.setVerticalAlignment(javax.swing.SwingConstants.TOP);
					jLabelP5.setIcon(new javax.swing.ImageIcon(ttt));
				}
			}
			if(vistaStr.equalsIgnoreCase("Lateral Direita")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					com.sun.media.jai.codec.FileSeekableStream stream = new com.sun.media.jai.codec.FileSeekableStream( sapo.paciente.dados.imgData[i].fileImage.getPath() );
					ttt = javax.media.jai.JAI.create("stream", stream).getAsBufferedImage(); //.getScaledInstance(100,100,0);
				}catch(Exception erro){}
				if(ttt!=null){
					int w = ttt.getWidth(this);
					int h = ttt.getHeight(this);
					optimizaZoom(w,h);
					ttt = ttt.getScaledInstance(picW, picH, java.awt.Image.SCALE_FAST);
					//jFotoLatDir.setIcon(new javax.swing.ImageIcon(ttt));
					//jFotoLatDir.setVerticalAlignment(javax.swing.SwingConstants.TOP);
					jLabelP6.setIcon(new javax.swing.ImageIcon(ttt));
				}
			}
			if(vistaStr.equalsIgnoreCase("Lateral Esquerda")){
				try{
					pict = sapo.paciente.dados.imgData[i].fileImage.getAbsolutePath();
					com.sun.media.jai.codec.FileSeekableStream stream = new com.sun.media.jai.codec.FileSeekableStream( sapo.paciente.dados.imgData[i].fileImage.getPath() );
					ttt = javax.media.jai.JAI.create("stream", stream).getAsBufferedImage(); //.getScaledInstance(100,100,0);
				}catch(Exception erro){}
				if(ttt!=null){
					int w = ttt.getWidth(this);
					int h = ttt.getHeight(this);
					optimizaZoom(w,h);
					ttt = ttt.getScaledInstance(picW, picH, java.awt.Image.SCALE_FAST);
					//jFotoLatEsq.setIcon(new javax.swing.ImageIcon(ttt));
					//jFotoLatEsq.setVerticalAlignment(javax.swing.SwingConstants.TOP);
					jLabelP7.setIcon(new javax.swing.ImageIcon(ttt));
				}
			}
		}//SAPO.maxImg
		desenhaCG();
		
	}//initComponets2
        
        private void resultadoAnaliseTabelaVista(){
            //fazer  atualizaï¿½ï¿½o das tabelas
            MedidasAnalise md = new MedidasAnalise(sapo);
            double calculos[] = new double[md.medidas.length];
            JLabel lblmd[] = new JLabel[md.medidas.length]; // tipo de medida
            JLabel lblcc[] = new JLabel[md.medidas.length]; // resultado dos calculos
            JLabel lblesp[] = new JLabel[md.medidas.length]; //valores esperados
            JPanel pnl[] = new JPanel[md.medidas.length]; //panel p/ organizar tudo
            calculos = md.calculaAnalise(sapo);
            String resultStr = "";
            String medida = "";
            String valTol = "";
            double result;
            double valEsp;
            double valEspTol;
            javax.swing.table.DefaultTableModel p4 = (javax.swing.table.DefaultTableModel)jTableP4Pontos1.getModel();
            for(int h=0; h<lblmd.length; h++){
			lblmd[h] = new JLabel("   " + md.medidas[h]);
			valEsp = md.valEsp[h];
			if(valEsp!=1E300){
				String ref = sapo.numFormat.format(valEsp);
				valEspTol = md.valEspTol[h]; 
				if(valEspTol!=0)
					valTol = " +/- "+sapo.numFormat.format(valEspTol);
				else
					valTol = "";
				lblesp[h] = new JLabel(ref+valTol + " " + md.unidade[h]);
			}
			else
				lblesp[h] = new JLabel("não disponível");
			lblesp[h].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lblcc[h] = new JLabel(sapo.numFormat.format(calculos[h]) + " " + md.unidade[h]);
			lblcc[h].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			
            }
            
            //frontal
            int index = -1;
            for(int h=0; h<10; h++){
                index = index + 1;
                p4.insertRow(index, new Object[]{null});
                String nomeTemp = md.medidas[h];
                double pX = md.valEspTol[h]; 
                double pY = calculos[h];
                jTableP4Pontos1.setValueAt(nomeTemp, index, 1);
                jTableP4Pontos1.setValueAt(sapo.numFormat.format(pX),index,2);
                jTableP4Pontos1.setValueAt(sapo.numFormat.format(pY),index,3);
            }
            jTableP4Pontos1.getColumnModel().getColumn(1).setPreferredWidth(300);
            
            //lateral esquerda
            index = -1;
            javax.swing.table.DefaultTableModel p5 = (javax.swing.table.DefaultTableModel)jTableP4Pontos2.getModel();
            for(int h=13; h<21; h++){
                index = index + 1;
                p5.insertRow(index, new Object[]{null});
                String nomeTemp = md.medidas[h];
                double pX = md.valEspTol[h]; 
                double pY = calculos[h];
                jTableP4Pontos2.setValueAt(nomeTemp, index, 1);
                jTableP4Pontos2.setValueAt(sapo.numFormat.format(pX),index,2);
                jTableP4Pontos2.setValueAt(sapo.numFormat.format(pY),index,3);
            }
            jTableP4Pontos2.getColumnModel().getColumn(1).setPreferredWidth(300);
            
            //lateral esquerda
            index = -1;
            javax.swing.table.DefaultTableModel p6 = (javax.swing.table.DefaultTableModel)jTableP4Pontos3.getModel();
            for(int h=21; h<29; h++){
                index = index + 1;
                p6.insertRow(index, new Object[]{null});
                String nomeTemp = md.medidas[h];
                double pX = md.valEspTol[h]; 
                double pY = calculos[h];
                jTableP4Pontos3.setValueAt(nomeTemp, index, 1);
                jTableP4Pontos3.setValueAt(sapo.numFormat.format(pX),index,2);
                jTableP4Pontos3.setValueAt(sapo.numFormat.format(pY),index,3);
            }
            jTableP4Pontos3.getColumnModel().getColumn(1).setPreferredWidth(300);  
            
            //posterior
            index = -1;
            javax.swing.table.DefaultTableModel p7 = (javax.swing.table.DefaultTableModel)jTableP4Pontos4.getModel();
            for(int h=10; h<13; h++){
                index = index + 1;
                p7.insertRow(index, new Object[]{null});
                String nomeTemp = md.medidas[h];
                double pX = md.valEspTol[h]; 
                double pY = calculos[h];
                jTableP4Pontos4.setValueAt(nomeTemp, index, 1);
                jTableP4Pontos4.setValueAt(sapo.numFormat.format(pX),index,2);
                jTableP4Pontos4.setValueAt(sapo.numFormat.format(pY),index,3);
            }
            jTableP4Pontos4.getColumnModel().getColumn(1).setPreferredWidth(300); 
             
        }


	private void resultadoAnalise2(){
		MedidasAnalise md = new MedidasAnalise(sapo);
		double calculos[] = new double[md.medidas.length];
		JLabel lblmd[] = new JLabel[md.medidas.length]; // tipo de medida
		JLabel lblcc[] = new JLabel[md.medidas.length]; // resultado dos calculos
		JLabel lblesp[] = new JLabel[md.medidas.length]; //valores esperados
		JPanel pnl[] = new JPanel[md.medidas.length]; //panel p/ organizar tudo
		calculos = md.calculaAnalise(sapo);
                pontosDesenho = new ArrayList[md.medidas.length];
                pontosDesenho = md.pontosCalculo;             
		String resultStr = "";
		String medida = "";
		String valTol = "";
		double result;
		double valEsp;
		double valEspTol;
		
		//prepara para apresentação
		for(int h=0; h<lblmd.length; h++){
			lblmd[h] = new JLabel("   " + md.medidas[h]);
			valEsp = md.valEsp[h];
			if(valEsp!=1E300){
				String ref = sapo.numFormat.format(valEsp);
				valEspTol = md.valEspTol[h]; 
				if(valEspTol!=0)
					valTol = " +/- "+sapo.numFormat.format(valEspTol);
				else
					valTol = "";
				lblesp[h] = new JLabel(ref+valTol + " " + md.unidade[h]);
			}
			else
				lblesp[h] = new JLabel("não disponível");
			lblesp[h].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lblcc[h] = new JLabel(sapo.numFormat.format(calculos[h]) + " " + md.unidade[h]);
			lblcc[h].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			pnl[h] = new JPanel();
			pnl[h].setLayout(new java.awt.GridLayout(1,3,2,2));
			pnl[h].setBackground(Color.WHITE);
			pnl[h].add(lblmd[h]);
			pnl[h].add(lblesp[h]);
			pnl[h].add(lblcc[h]);
		}
		lblesp[8].setText("< "+lblesp[8].getText());
		lblesp[9].setText("< "+lblesp[9].getText());
		
		JPanel p0 = new JPanel();
		p0.setBackground(Color.DARK_GRAY);
		p0.setLayout(new java.awt.GridLayout(1,3,2,2));
		JLabel l01 = new JLabel("  Nome");
		l01.setFont(new java.awt.Font("Arial", 1, 12));
		l01.setForeground(Color.WHITE);
		p0.add(l01);
		JLabel l02 = new JLabel("Valor de Referência");
		l02.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		l02.setFont(new java.awt.Font("Arial", 1, 12));
		l02.setForeground(Color.WHITE);
		p0.add(l02);
		JLabel l03 = new JLabel("Valor Medido");
		l03.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		l03.setFont(new java.awt.Font("Arial", 1, 12));
		l03.setForeground(Color.WHITE);
		p0.add(l03);
		jPanel8.add(p0);
		
		JPanel p1 = new JPanel();
		p1.setBackground(Color.GRAY);
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel l1 = new JLabel("Vista Anterior");
		l1.setFont(new java.awt.Font("Arial", 1, 12));
		p1.add(l1);
		jPanel8.add(p1);
		JPanel pnlCabeca1 = new JPanel();
		pnlCabeca1.setBackground(Color.LIGHT_GRAY);
		pnlCabeca1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblCabeca1 = new JLabel("Cabeça");
		lblCabeca1.setFont(new java.awt.Font("Arial", 1, 12));
		pnlCabeca1.add(lblCabeca1);
		jPanel8.add(pnlCabeca1);
		result = calculos[0];
		medida = md.medidas[0];
		
		if(result!=1E300){
			jPanel8.add(pnl[0]);
		}
		JPanel pnlTronco1 = new JPanel();
		pnlTronco1.setBackground(Color.LIGHT_GRAY);
		pnlTronco1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTronco1 = new JLabel("Tronco");
		lblTronco1.setFont(new java.awt.Font("Arial", 1, 12));
		pnlTronco1.add(lblTronco1);
		jPanel8.add(pnlTronco1);
		result = 1E300;
		result = calculos[1];
		medida = md.medidas[1];
		if(result!=1E300){            
			jPanel8.add(pnl[1]);
		}
		result = 1E300;
		result = calculos[2];
		medida = md.medidas[2];
		if(result!=1E300){            
			jPanel8.add(pnl[2]);
		}
		result = 1E300;
		result = calculos[3];
		medida = md.medidas[3];
		if(result!=1E300){
			jPanel8.add(pnl[3]);
		}
		JPanel pnlMemInf1 = new JPanel();
		pnlMemInf1.setBackground(Color.LIGHT_GRAY);
		pnlMemInf1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMemInf1 = new JLabel("Membros Inferiores");
		lblMemInf1.setFont(new java.awt.Font("Arial", 1, 12));
		pnlMemInf1.add(lblMemInf1);
		jPanel8.add(pnlMemInf1);
		result = 1E300;
		result = calculos[4];
		medida = md.medidas[4];
		if(result!=1E300){
			jPanel8.add(pnl[4]);
		}
		result = 1E300;
		result = calculos[5];
		medida = md.medidas[5];
		if(result!=1E300){
			jPanel8.add(pnl[5]);
		}
		result = 1E300;
		result = calculos[6];
		medida = md.medidas[6];
		if(result!=1E300){
			jPanel8.add(pnl[6]);
		}
		result = 1E300;
		result = calculos[7];
		medida = md.medidas[7];
		if(result!=1E300){
			jPanel8.add(pnl[7]);
		}
		result = 1E300;
		result = calculos[8];
		medida = md.medidas[8];
		if(result!=1E300){
			jPanel8.add(pnl[8]);
		}
		result = 1E300;
		result = calculos[9];
		medida = md.medidas[9];
		if(result!=1E300){
			jPanel8.add(pnl[9]);
		}
		
		JPanel p2 = new JPanel();
		p2.setBackground(Color.GRAY);
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel l2 = new JLabel("Vista Posterior");
		l2.setFont(new java.awt.Font("Arial", 1, 12));
		p2.add(l2);
		jPanel8.add(p2);
		JPanel pnlTronco2 = new JPanel();
		pnlTronco2.setBackground(Color.LIGHT_GRAY);
		pnlTronco2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTronco2 = new JLabel("Tronco");
		lblTronco2.setFont(new java.awt.Font("Arial", 1, 12));
		pnlTronco2.add(lblTronco2);
		jPanel8.add(pnlTronco2);
		result = 1E300;
		result = calculos[10];
		medida = md.medidas[10];
		if(result!=1E300){
			jPanel8.add(pnl[10]);
		}
		JPanel pnlMemInf2 = new JPanel();
		pnlMemInf2.setBackground(Color.LIGHT_GRAY);
		pnlMemInf2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMemInf2 = new JLabel("Membros Inferiores");
		lblMemInf2.setFont(new java.awt.Font("Arial", 1, 12));
		pnlMemInf2.add(lblMemInf2);
		jPanel8.add(pnlMemInf2);
		result = 1E300;
		result = calculos[11];
		medida = md.medidas[11];
		if(result!=1E300){
			jPanel8.add(pnl[11]);
		}
		result = 1E300;
		result = calculos[12];
		medida = md.medidas[12];
		if(result!=1E300){
			jPanel8.add(pnl[12]);
		}
		
		JPanel p3 = new JPanel();
		p3.setBackground(Color.GRAY);
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel l3 = new JLabel("Vista Lateral Direita");
		l3.setFont(new java.awt.Font("Arial", 1, 12));
		p3.add(l3);
		jPanel8.add(p3);
		JPanel pnlCabeca2 = new JPanel();
		pnlCabeca2.setBackground(Color.LIGHT_GRAY);
		pnlCabeca2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblCabeca2 = new JLabel("Cabeça");
		lblCabeca2.setFont(new java.awt.Font("Arial", 1, 12));
		pnlCabeca2.add(lblCabeca2);  
		jPanel8.add(pnlCabeca2);
		result = 1E300;
		result = calculos[13];
		medida = md.medidas[13];
		if(result!=1E300){
			jPanel8.add(pnl[13]);
		}
		result = 1E300;
		result = calculos[14];
		medida = md.medidas[14];
		if(result!=1E300){
			jPanel8.add(pnl[14]);
		}
		JPanel pnlTronco3 = new JPanel();
		pnlTronco3.setBackground(Color.LIGHT_GRAY);
		pnlTronco3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTronco3 = new JLabel("Tronco");
		lblTronco3.setFont(new java.awt.Font("Arial", 1, 12));
		pnlTronco3.add(lblTronco3);
		jPanel8.add(pnlTronco3);
		result = 1E300;
		result = calculos[15];
		medida = md.medidas[15];
		if(result!=1E300){
			jPanel8.add(pnl[15]);
		}
		result = 1E300;
		result = calculos[16];
		medida = md.medidas[16];
		if(result!=1E300){
			jPanel8.add(pnl[16]);
		}
		result = 1E300;
		result = calculos[17];
		medida = md.medidas[17];
		if(result!=1E300){
			jPanel8.add(pnl[17]);
		}
		result = 1E300;
		result = calculos[18];
		medida = md.medidas[18];
		if(result!=1E300){
			jPanel8.add(pnl[18]);
		}
		JPanel pnlMemInf3 = new JPanel();
		pnlMemInf3.setBackground(Color.LIGHT_GRAY);
		pnlMemInf3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMemInf3 = new JLabel("Membros Inferiores");
		lblMemInf3.setFont(new java.awt.Font("Arial", 1, 12));
		pnlMemInf3.add(lblMemInf3);
		jPanel8.add(pnlMemInf3);
		result = 1E300;
		result = calculos[19];
		medida = md.medidas[19];
		if(result!=1E300){
			jPanel8.add(pnl[19]);
		}
		result = 1E300;
		result = calculos[20];
		medida = md.medidas[20];
		if(result!=1E300){
			jPanel8.add(pnl[20]);
		}
		
		JPanel p4 = new JPanel();
		p4.setBackground(Color.GRAY);
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel l4 = new JLabel("Vista Lateral Esquerda");
		l4.setFont(new java.awt.Font("Arial", 1, 12));
		p4.add(l4);
		jPanel8.add(p4);
		JPanel pnlCabeca3 = new JPanel();
		pnlCabeca3.setBackground(Color.LIGHT_GRAY);
		pnlCabeca3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblCabeca3 = new JLabel("Cabeça");
		lblCabeca3.setFont(new java.awt.Font("Arial", 1, 12));
		pnlCabeca3.add(lblCabeca3); 
		jPanel8.add(pnlCabeca3);
		result = 1E300;
		result = calculos[21];
		medida = md.medidas[21];
		if(result!=1E300){
			jPanel8.add(pnl[21]);
		}
		result = 1E300;
		result = calculos[22];
		medida = md.medidas[22];
		if(result!=1E300){
			jPanel8.add(pnl[22]);
		}
		JPanel pnlTronco4 = new JPanel();
		pnlTronco4.setBackground(Color.LIGHT_GRAY);
		pnlTronco4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTronco4 = new JLabel("Tronco");
		lblTronco4.setFont(new java.awt.Font("Arial", 1, 12));
		pnlTronco4.add(lblTronco4);
		jPanel8.add(pnlTronco4);
		result = 1E300;
		result = calculos[23];
		medida = md.medidas[23];
		if(result!=1E300){
			jPanel8.add(pnl[23]);
		}
		result = 1E300;
		result = calculos[24];
		medida = md.medidas[24];
		if(result!=1E300){
			jPanel8.add(pnl[24]);
		}
		result = 1E300;
		result = calculos[25];
		medida = md.medidas[25];
		if(result!=1E300){
			jPanel8.add(pnl[25]);
		}
		result = 1E300;
		result = calculos[26];
		medida = md.medidas[26];
		if(result!=1E300){
			jPanel8.add(pnl[26]);
		}
		JPanel pnlMemInf4 = new JPanel();
		pnlMemInf4.setBackground(Color.LIGHT_GRAY);
		pnlMemInf4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMemInf4 = new JLabel("Membros Inferiores");
		lblMemInf4.setFont(new java.awt.Font("Arial", 1, 12));
		pnlMemInf4.add(lblMemInf4);
		jPanel8.add(pnlMemInf4);
		result = 1E300;
		result = calculos[27];
		medida = md.medidas[27];
		if(result!=1E300){
			jPanel8.add(pnl[27]);
		}
		result = 1E300;
		result = calculos[28];
		medida = md.medidas[28];
		if(result!=1E300){
			jPanel8.add(pnl[28]);
		}
		result = 1E300;
		result = calculos[37];
		medida = md.medidas[37];
		if(result!=1E300){
			resultStr = sapo.numFormat.format(result);
			jlblAssFront.setText(medida+" "+resultStr+" %");    
		}
		result = 1E300;
		result = calculos[39];
		medida = md.medidas[39];
		if(result!=1E300){
			resultStr = sapo.numFormat.format(result);
			jlblAssSag.setText(medida+" "+resultStr+" %");
		}
		result = 1E300;
		result = calculos[41];
		medida = md.medidas[41];
		if(result!=1E300){
			resultStr = sapo.numFormat.format(result);
			jlblAssFrontRel.setText(medida+" "+resultStr+" cm");    
		}
		result = 1E300;
		result = calculos[42];
		medida = md.medidas[42];
		if(result!=1E300){
			resultStr = sapo.numFormat.format(result);
			jlblAssSagRel.setText(medida+" "+resultStr+" cm");    
		}
		
	}//fim resultadoAnalise
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jDataAval;
    private javax.swing.JLabel jDataAval1;
    private javax.swing.JLabel jDataAval2;
    private javax.swing.JLabel jDataAval5;
    private javax.swing.JLabel jDataAval6;
    private javax.swing.JLabel jDataAval7;
    private javax.swing.JLabel jDataAval8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelP4;
    private javax.swing.JLabel jLabelP4Cab;
    private javax.swing.JLabel jLabelP5;
    private javax.swing.JLabel jLabelP5Cab;
    private javax.swing.JLabel jLabelP6;
    private javax.swing.JLabel jLabelP6Cab;
    private javax.swing.JLabel jLabelP7;
    private javax.swing.JLabel jLabelP7Cab;
    private javax.swing.JLabel jNome;
    private javax.swing.JLabel jNome1;
    private javax.swing.JLabel jNome2;
    private javax.swing.JLabel jNome5;
    private javax.swing.JLabel jNome6;
    private javax.swing.JLabel jNome7;
    private javax.swing.JLabel jNome8;
    private javax.swing.JPanel jPagina3;
    private javax.swing.JPanel jPagina4;
    private javax.swing.JPanel jPagina5;
    private javax.swing.JPanel jPagina6;
    private javax.swing.JPanel jPagina7;
    private javax.swing.JPanel jPagina9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel86;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableP4Ang;
    private javax.swing.JTable jTableP4Dist;
    private javax.swing.JTable jTableP4Pontos;
    private javax.swing.JTable jTableP4Pontos1;
    private javax.swing.JTable jTableP4Pontos2;
    private javax.swing.JTable jTableP4Pontos3;
    private javax.swing.JTable jTableP4Pontos4;
    private javax.swing.JTable jTableP5Ang;
    private javax.swing.JTable jTableP5Dist;
    private javax.swing.JTable jTableP5Pontos;
    private javax.swing.JTable jTableP6Ang;
    private javax.swing.JTable jTableP6Dist;
    private javax.swing.JTable jTableP6Pontos;
    private javax.swing.JTable jTableP7Ang;
    private javax.swing.JTable jTableP7Dist;
    private javax.swing.JTable jTableP7Pontos;
    private javax.swing.JLabel jTitulo1;
    private javax.swing.JLabel jTitulo2;
    private javax.swing.JLabel jTitulo3;
    private javax.swing.JLabel jTitulo5;
    private javax.swing.JLabel jTitulo6;
    private javax.swing.JLabel jTitulo7;
    private javax.swing.JLabel jTitulo8;
    private javax.swing.JButton jbtnCancel;
    private javax.swing.JButton jbtnImprimir;
    private javax.swing.JCheckBox jckbAnalis;
    public javax.swing.JCheckBox jckbAngle;
    public javax.swing.JCheckBox jckbCG;
    public javax.swing.JCheckBox jckbComent;
    public javax.swing.JCheckBox jckbDadosPac;
    public javax.swing.JCheckBox jckbDist;
    private javax.swing.JCheckBox jckbFotos;
    public javax.swing.JCheckBox jckbPontos;
    private javax.swing.JLabel jlblAssFront;
    private javax.swing.JLabel jlblAssFrontRel;
    private javax.swing.JLabel jlblAssSag;
    private javax.swing.JLabel jlblAssSagRel;
    private javax.swing.JLabel jlblCG;
    public javax.swing.JTextPane jtxtPaneComent;
    // End of variables declaration//GEN-END:variables
	
}
