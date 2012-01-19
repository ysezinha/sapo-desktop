import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPolygon;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.jdesktop.swingworker.SwingWorker;

import com.dati.image.AnguloMedido;
import com.dati.image.DistanciaMedida;
import com.dati.image.PontoMedido;
import com.dati.util.DataHora;
import com.sun.EFileFilter;


/**
 * @author Edison Puig Maldonado
 * 03/12/2005
 * 
 */

public class ExcelReport {
	
	SAPO sapo;
	
	HSSFWorkbook wb;
	HSSFSheet sheet;
	HSSFPrintSetup ps;
	HSSFPatriarch patriarch;
	HSSFPalette palette;
	
	HSSFRow row;
	HSSFCell cell;
	HSSFCellStyle cellStyle;
	HSSFFont font;
	
	ImageFileInfo[] ifi = new ImageFileInfo[4];
	MedidasAnalise md;
	double calculos[];
	
	DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.US);
	int maxHeight, numRow, columnFrom, columnTo;
	final short defaultLineHeight = (short)0x140;
	final int EMU = 12700;
	
	public ExcelReport(SAPO sapo) {
		this.sapo = sapo;
		md = new MedidasAnalise(sapo);
		calculos = new double[md.medidas.length];
		wb = new HSSFWorkbook();
		criaExcelReport();
	}

	private void criaExcelReport() {
		final MsgPane waitMsg = new MsgPane();
		SwingWorker aWorker = new SwingWorker() {
			public Object doInBackground() {
				calculos = md.calculaAnalise(sapo);
				criaSheet1();
				criaSheet2();
				criaSheet3();
				criaSheet4();
				criaSheet5();
				criaSheet6();
				writeXLS();
				sapo.clearGlassPane(sapo);
				return null;
			}
		};
		sapo.setGlassPane(waitMsg);
		waitMsg.setText("Exportando relatório ...");
		sapo.getGlassPane().setVisible(true);
		aWorker.execute();
	}
	
	private void criaSheet1() {
		createSheet("Informações Gerais");
		sheet.setDefaultColumnWidth((short)1); // largura padrão das colunas é 10 pt (1 caractere de tamanho 10 pt) para a folha 1
		sheet.setDefaultRowHeightInPoints(10); // altura  padrão das linhas  é 10 pt para a folha 1
		createRow();
		row.setHeight((short)0x430);
		createCell("",14,"bold","center",3,43, "Relatório de avaliação postural de " + 
        		sapo.paciente.dados.nome + "\n\nProtocolo SAPO-1");
		createRow();
		createRow();
		for (int i=3; i<43; i++) createCell("bordertop",14,"plain","center",i,i, " ");
		
		createRow();
		createCell("",12,"bold","left",3,43, "Dados do Sujeito:");
        createRow();

        createCell("",10,"plain","left",3,43, "Profissão: " + sapo.paciente.dados.profissao);
        createRow();

        createCell("",10,"plain","left",3,43, "Data de Nascimento: " + 
        		sapo.paciente.dados.diaNasc + " de " +
        		sapo.paciente.dados.mesNasc + " de " +
        		sapo.paciente.dados.anoNasc);
        createRow();

        createCell("",10,"plain","left",3,43, "Data da Avaliação: " + 
        		DataHora.getSQLDate(sapo.paciente.dados.datacriacao));
        createRow();
        for (int i=3; i<43; i++) createCell("borderbottom",14,"plain","center",i,i, " ");
        createRow();
        
		createRow();
		createCell("",12,"bold","left",3,43, "Fotos:");
		insereVistas(11);
	}

	private void criaSheet2() {
		df.applyPattern("0.0");
		createSheet("Resultados-1");
		setColumnWidth(0,70);
		setColumnWidth(1,20);
		setColumnWidth(2,20);
		setColumnWidth(3,40);
		createRow();
		row.setHeight((short)0x160);
		createCell("",14,"bold","center",0,3, "Medidas segundo protocolo SAPO-1");
		
		createRow();
		reportRow(true, "Vista Anterior", "Referência", "Valor", "Observações");
		
		createRow(); createCell("",10,"bold","left",0,0, "Cabeça");
		reportRow(false, md.medidas[0], formataR(0), formataC(0), "");
		createRow(); createCell("",10,"bold","left",0,0, "Tronco");
		reportRow(false, md.medidas[1], formataR(1), formataC(1), "");
		reportRow(false, md.medidas[2], formataR(2), formataC(2), "");
		reportRow(false, md.medidas[3], formataR(3), formataC(3), "");
		createRow(); createCell("",10,"bold","left",0,0, "Membros inferiores");
		reportRow(false, md.medidas[4], formataR(4), formataC(4), "");
		reportRow(false, md.medidas[5], formataR(5), formataC(5), "");
		reportRow(false, md.medidas[6], formataR(6), formataC(6), "");
		reportRow(false, md.medidas[7], formataR(7), formataC(7), "");
		reportRow(false, md.medidas[8], formataR(8), formataC(8), "");
		reportRow(false, md.medidas[9], formataR(9), formataC(9), "");

		createRow();
		reportRow(true, "Vista Posterior", "Referência", "Valor", "Observações");
		
		createRow(); createCell("",10,"bold","left",0,0, "Tronco");
		reportRow(false, md.medidas[10], formataR(10), formataC(10), "");
		createRow(); createCell("",10,"bold","left",0,0, "Membros inferiores");
		reportRow(false, md.medidas[11], formataR(11), formataC(11), "");
		reportRow(false, md.medidas[12], formataR(12), formataC(12), "");
		
		createRow();
		reportRow(true, "Vista Lateral Direita", "Referência", "Valor", "Observações");
		
		createRow(); createCell("",10,"bold","left",0,0, "Cabeça");
		reportRow(false, md.medidas[13], formataR(13), formataC(13), "");
		reportRow(false, md.medidas[14], formataR(14), formataC(14), "");
		createRow(); createCell("",10,"bold","left",0,0, "Tronco");
		reportRow(false, md.medidas[15], formataR(15), formataC(15), "");
		reportRow(false, md.medidas[16], formataR(16), formataC(16), "");
		reportRow(false, md.medidas[17], formataR(17), formataC(17), "");
		reportRow(false, md.medidas[18], formataR(18), formataC(18), "");
		createRow(); createCell("",10,"bold","left",0,0, "Membros inferiores");
		reportRow(false, md.medidas[19], formataR(19), formataC(19), "");
		reportRow(false, md.medidas[20], formataR(20), formataC(20), "");
		
		createRow();
		reportRow(true, "Vista Lateral Esquerda", "Referência", "Valor", "Observações");
		
		createRow(); createCell("",10,"bold","left",0,0, "Cabeça");
		reportRow(false, md.medidas[21], formataR(21), formataC(21), "");
		reportRow(false, md.medidas[22], formataR(22), formataC(22), "");
		createRow(); createCell("",10,"bold","left",0,0, "Tronco");
		reportRow(false, md.medidas[23], formataR(23), formataC(23), "");
		reportRow(false, md.medidas[24], formataR(24), formataC(24), "");
		reportRow(false, md.medidas[25], formataR(25), formataC(25), "");
		reportRow(false, md.medidas[26], formataR(26), formataC(26), "");
		createRow(); createCell("",10,"bold","left",0,0, "Membros inferiores");
		reportRow(false, md.medidas[27], formataR(27), formataC(27), "");
		reportRow(false, md.medidas[28], formataR(28), formataC(28), "");
	}
	
	public String formataC(int index){
		return (calculos[index] != 1E300)?df.format(calculos[index]):"";
	}
	
	public String formataR(int index){
		String ref = (md.valEsp[index] != 1E300)?df.format(md.valEsp[index]):""; 
		String tol = (md.valEspTol[index] != 0)?(" +/- " + df.format(md.valEspTol[index])):"";
		return (ref.equals("")?"não disponível":ref+tol+" "+md.unidade[index]);
	}
	
	private void criaSheet3() {
		createSheet("Resultados-2");
		sheet.setDefaultColumnWidth((short)1); // largura padrão das colunas é 10 pt (1 caracter de tamanho 10 pt) para a folha 3
		sheet.setDefaultRowHeightInPoints(10); // altura  padrão das linhas  é 10 pt para a folha 3
		createRow();
		row.setHeight((short)0x180);
		createCell("",14,"bold","center",3,42, "Medidas segundo protocolo SAPO-1");
		
		createRow();
		createRow();
		row.setHeight((short)0x180);
		createCell("bordertopbottom",14,"bold","center",3,42, "Centro de Gravidade");
		for (int i=4; i<43; i++) createCell("bordertopbottom",14,"plain","center",i,i, " ");
		
		createRow();
		createRow();
		row.setHeight((short)0x160);
		createCell("",12,"plain","center",3,42, md.medidas[37] + " " + formataC(37) + " %");
		
		createRow();
		row.setHeight((short)0x160);
		createCell("",12,"plain","center",3,42, md.medidas[39] + " " + formataC(39) + " %");
		
		desenhaCG(9);
	}
	
	private void criaSheet4() {
		createSheet("Pontos Medidos");
		setColumnWidth(0,80);
		setColumnWidth(1,12);
		setColumnWidth(2,12);
		createRow();
		row.setHeight((short)0x160);
		createCell("",14,"bold","center",0,2, "Pontos medidos");
		
		if (sapo.paciente.dados.getVista("anterior") != -1) 
			reportPontos("Anterior");
		if (sapo.paciente.dados.getVista("posterior") != -1)
			reportPontos("Posterior");
		if (sapo.paciente.dados.getVista("lateral direita") != -1)
			reportPontos("Lateral Direita");
		if (sapo.paciente.dados.getVista("lateral esquerda") != -1)
			reportPontos("Lateral Esquerda");

		createRow();
		for (int i=0; i<3; i++) createCell("borderbottom",14,"plain","center",i,i, " ");
	}

	private void criaSheet5() {
		createSheet("Ângulos Livres");
		setColumnWidth(0,80);
		setColumnWidth(1,12);
		setColumnWidth(2,24);
		createRow();
		row.setHeight((short)0x160);
		createCell("",14,"bold","center",0,2, "Ângulos livres medidos");
		
		if (sapo.paciente.dados.getVista("anterior") != -1) 
			reportAngulos("Anterior");
		if (sapo.paciente.dados.getVista("posterior") != -1)
			reportAngulos("Posterior");
		if (sapo.paciente.dados.getVista("lateral direita") != -1)
			reportAngulos("Lateral Direita");
		if (sapo.paciente.dados.getVista("lateral esquerda") != -1)
			reportAngulos("Lateral Esquerda");

		createRow();
		//for (int i=0; i<3; i++) createCell("borderbottom",14,"plain","center",i,i, " ");
	}
	
	private void criaSheet6() {
		createSheet("Distâncias Livres");
		setColumnWidth(0,80);
		setColumnWidth(1,12);
		setColumnWidth(2,12);
		createRow();
		row.setHeight((short)0x160);
		createCell("",14,"bold","center",0,1, "Distâncias livres medidas");
		
		if (sapo.paciente.dados.getVista("anterior") != -1) 
			reportDistancias("Anterior");
		if (sapo.paciente.dados.getVista("posterior") != -1)
			reportDistancias("Posterior");
		if (sapo.paciente.dados.getVista("lateral direita") != -1)
			reportDistancias("Lateral Direita");
		if (sapo.paciente.dados.getVista("lateral esquerda") != -1)
			reportDistancias("Lateral Esquerda");

		createRow();
		//for (int i=0; i<2; i++) createCell("borderbottom",14,"plain","center",i,i, " ");
	}
	
	private void reportPontos(String vista) {
		ArrayList pontos;
		PontoMedido ponto;
		String nomePonto, pontoX, pontoY;
		
		String uc = sapo.user.dados.unidadecomprimento;
		double x, y, escalaX, escalaY;
		int numVista;
		
		createRow();
		numVista = sapo.paciente.dados.getVista(vista);
		boolean isCal = sapo.paciente.dados.imgData[numVista].isScaleCal();
		
		reportRow(true, "Vista " + vista, "X (" + (isCal?uc:"pixels") + ")", "Y (" + (isCal?uc:"pixels") + ")", "");
		
		if (numVista != -1) {
			pontos = sapo.paciente.dados.imgData[numVista].pontosList;
			escalaX = sapo.paciente.dados.imgData[numVista].getEscalaX();
            escalaY = sapo.paciente.dados.imgData[numVista].getEscalaY();
			for (int i=0; i<pontos.size(); i++) {
				nomePonto = ((PontoMedido)pontos.get(i)).nome;
				x = ((PontoMedido)pontos.get(i)).p.x * escalaX;
				y = ((PontoMedido)pontos.get(i)).p.y * escalaY;
				if ((x<0.01)||(x>999)) df.applyPattern("0.#E0");
	            else df.applyPattern("0.0");
				pontoX = df.format(x);
				if ((y<0.01)||(y>999)) df.applyPattern("0.#E0");
	            else df.applyPattern("0.0");
				pontoY = df.format(y);
				reportRow(false, nomePonto, pontoX, pontoY, "");
			}
		}
	}
	
	private void reportAngulos(String vista) {
		ArrayList angulos;
		AnguloMedido angulo;
		String nomeAngulo, valor, tipo;
		
		String ua = sapo.user.dados.unidadeangulo;
		double dblValor;
		int intTipo;
		int numVista;
		
		createRow();
		numVista = sapo.paciente.dados.getVista(vista);
		boolean isCal = sapo.paciente.dados.imgData[numVista].isScaleCal();
		
		reportRow(true, "Vista " + vista, "(" + (isCal?ua:"?") + ")", "Tipo", "");
		
		if (numVista != -1) {
			angulos = sapo.paciente.dados.imgData[numVista].angulosList;
			for (int i=0; i<angulos.size(); i++) {
				nomeAngulo = ((AnguloMedido)angulos.get(i)).nome;
				dblValor   = ((AnguloMedido)angulos.get(i)).angulo;
				intTipo    = ((AnguloMedido)angulos.get(i)).tipo;
				df.applyPattern("0.0");
				valor = df.format(dblValor);
				if ( intTipo == JAIPanelSAPO.ANGULO_HORIZONTAL ) 
					tipo = "(HORIZONTAL)";
				else if ( intTipo == JAIPanelSAPO.ANGULO_VERTICAL ) 
					tipo = "(VERTICAL)";
				else if ( intTipo == JAIPanelSAPO.ANGULO_TRES_PONTOS ) 
					tipo = "(TRÊS PONTOS)";
				else if ( intTipo == JAIPanelSAPO.ANGULO_QUATRO_PONTOS ) 
					tipo = "(QUATRO PONTOS)";
				else  
					tipo = "(INDEFINIDO)";
				reportRow(false, nomeAngulo, valor, tipo, "");
			}
		}
	}
	
	private void reportDistancias(String vista) {
		ArrayList distancias;
		DistanciaMedida distancia;
		String nome,strDistancia;
		
		String uc = sapo.user.dados.unidadecomprimento;
		double distanciaMedida;
		int numVista;
		
		createRow();
		numVista = sapo.paciente.dados.getVista(vista);
		boolean isCal = sapo.paciente.dados.imgData[numVista].isScaleCal();
		
		reportRow(true, "Vista " + vista, "(" + (isCal?uc:"pixels") + ")", "", "");
		
		if (numVista != -1) {
			distancias = sapo.paciente.dados.imgData[numVista].distanciaList;
			for (int i=0; i<distancias.size(); i++) {
				nome            = ((DistanciaMedida)distancias.get(i)).nome;
				distanciaMedida = ((DistanciaMedida)distancias.get(i)).distanciaMedida;
				if ((distanciaMedida<0.01)||(distanciaMedida>999)) df.applyPattern("0.#E0");
	            else df.applyPattern("0.0");
				strDistancia = df.format(distanciaMedida);
				reportRow(false, nome, strDistancia, "", "");
			}
		}
	}
	
	private void reportRow(boolean title, String f0, String f1, String f2, String f3) {
		createRow();
		createCell(title?"bordertopbottom":"",title?11:10,title?"bold":"plain",title?"center":"left",0,0,f0);
		createCell(title?"bordertopbottom":"",title?11:10,title?"bold":"plain",title?"center":"center",1,1,f1);
		createCell(title?"bordertopbottom":"",title?11:10,title?"bold":"plain",title?"center":"center",2,2,f2);
		createCell(title?"bordertopbottom":"",title?11:10,title?"bold":"plain",title?"center":"right",3,3,f3);
	}

	private void createBorder(String frame, short border, short color) {
		if (frame.indexOf("bottom") != -1) {
			cellStyle.setBorderBottom(border);
			cellStyle.setBottomBorderColor(color);
		}
		if (frame.indexOf("left") != -1) {
			cellStyle.setBorderLeft(border);
			cellStyle.setLeftBorderColor(color);
		}
		if (frame.indexOf("right") != -1) {
			cellStyle.setBorderRight(border);
			cellStyle.setRightBorderColor(color);
		}
		if (frame.indexOf("top") != -1) {
			cellStyle.setBorderTop(border);
			cellStyle.setTopBorderColor(color);
		}
	}

	private void setColumnWidth(int column, int caracteres) {
		sheet.setColumnWidth((short)column,(short)(256*caracteres));
	}

	private void createCell(String border, int fontSize, String style, String alignment, int fromCol, int toCol, String cellValue) {
		if (cellValue.length() != 0) {
			this.columnFrom = fromCol;
			this.columnTo   = toCol;
			cell = row.createCell((short)fromCol);
			if (toCol > fromCol)
				sheet.addMergedRegion(new Region(numRow,(short)fromCol,numRow,(short)toCol));
			font = wb.createFont();
			font.setFontHeightInPoints((short)fontSize);
			font.setFontName("SansSerif");
			if (style.indexOf("bold") != -1) 
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			if (style.indexOf("italic") != -1) 
				font.setItalic(true);
			cellStyle = wb.createCellStyle();
			if (border.indexOf("border") != -1)
				createBorder(border,HSSFCellStyle.BORDER_MEDIUM,HSSFColor.GREY_50_PERCENT.index);
			cellStyle.setWrapText(true);
			if (alignment.equals("center")) 
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			if (alignment.equals("right")) 
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cellStyle.setFont(font);
			/*if (border.indexOf("bordertopbottom") != -1) {
				cellStyle.setFillPattern(HSSFCellStyle.LESS_DOTS);
				cellStyle.setFillBackgroundColor((short)9);
			}*/
			cell.setCellStyle(cellStyle);
			try {
				int intnum = Integer.parseInt(cellValue);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(intnum);
			} catch (NumberFormatException e) {
				try {
					double doublenum = Double.parseDouble(cellValue);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(doublenum);
				} catch (NumberFormatException e1) {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(cellValue);
				}
			}
		}
	}

	private void createRow() {
		numRow++;
		row = sheet.createRow((short)numRow);
		row.setHeight(defaultLineHeight);
	}

	private void createSheet(String titulo) {
		sheet = wb.createSheet(titulo);
		printSetup();
		criaHeader();
		criaFooter();
		numRow = -1;
	}
	
	private void insereVistas(int rowNumber) {
		patriarch = sheet.createDrawingPatriarch();
		for (int i=0; i<4; i++) ifi[i] = new ImageFileInfo();
		inicializaImageFileInfos();
		maxHeight = 0;
		for (int i=0; i<4; i++) if (ifi[i].height > maxHeight) maxHeight = ifi[i].height; 
		if (sapo.paciente.dados.getVista("Anterior") != -1) insereVista(rowNumber, "Anterior");
		if (sapo.paciente.dados.getVista("Posterior") != -1) insereVista(rowNumber, "Posterior");
		if (sapo.paciente.dados.getVista("Lateral Direita") != -1) insereVista(rowNumber, "Lateral Direita");
		if (sapo.paciente.dados.getVista("Lateral Esquerda") != -1) insereVista(rowNumber, "Lateral Esquerda");
	}
	
	private void inicializaImageFileInfos() {
		String strVista;
		String[] vista = {"Anterior","Posterior","Lateral Direita","Lateral Esquerda"};
		int numVista = -1;
		for (int i=0; i<4; i++) { 
			numVista = sapo.paciente.dados.getVista(vista[i]);
			if (numVista != -1) ifi[i] = optimizaTamanhoImagemReport(numVista);
			ifi[i].width = (int)(ifi[i].width/2);
			ifi[i].height= (int)(ifi[i].height/2); 
		}
	}

	private void insereVista(int rowNumber, String vista) {
		ImageFileInfo ifiVista = new ImageFileInfo();
		int colWidth  = 10; // largura das colunas é 10 pt (1 caractere de tamanho 10 pt) para a folha 1
		int rowHeight = 11; // altura  das linhas  é 10 pt para folha 1
		
		int maxRowHeight = Math.round((maxHeight)/rowHeight);
		
		int linha1=0, linha2, coluna1=0, coluna2=0;
		if (vista.equalsIgnoreCase("Anterior")) { ifiVista = ifi[0]; linha1 = rowNumber; coluna1 = 4; }
		if (vista.equalsIgnoreCase("Posterior")) { ifiVista = ifi[1]; linha1 = rowNumber; coluna1 = 22; }
		if (vista.equalsIgnoreCase("Lateral Direita")) { ifiVista = ifi[2]; linha1 = rowNumber+maxRowHeight+2; coluna1 = 4; }
		if (vista.equalsIgnoreCase("Lateral Esquerda")) { ifiVista = ifi[3]; linha1 = rowNumber+maxRowHeight+2; coluna1 = 22; }
		
		coluna1 = coluna1 + Math.round((20-(ifiVista.width)/colWidth)/2);
		if (coluna1 < 0) coluna1 = 0;
		coluna2 = coluna1 + Math.round((ifiVista.width)/colWidth);
		
		linha2 = linha1 + Math.round((ifiVista.height)/rowHeight);
		
		HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,255,(short)coluna1,linha1,(short)coluna2,linha2);
		anchor.setAnchorType(2);
		patriarch.createPicture(anchor, loadPicture(new File(ifiVista.filePath),wb));
	}

	private void desenhaCG(int rowNumber) {
		patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,255,(short)3,rowNumber,(short)43,rowNumber+30);
		anchor.setAnchorType(3);
		
		File cgImgFile = null;
		URL  cgImgURL  = null;
		try {
			cgImgURL  = getClass().getResource("/res/cg-feet.png");
			cgImgFile = new File(sapo.figDirPath + StartSAPO.sep + "cg-feet.png");
			StartSAPO.copy(cgImgURL,cgImgFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		patriarch.createPicture(anchor, loadPicture(cgImgFile,wb));
		
		int cgW = 100;
		int cgH = 100;
		
		if((calculos[37]!=1E300)&&(calculos[39]!=1E300)) {
			int xPicCG = Double.valueOf((cgW/2)*(calculos[37]/100)+cgW/2).intValue();
			int yPicCG = Double.valueOf((-0.791*cgH)*(calculos[39]/100)+0.791*cgH).intValue();
			
			HSSFPolygon pol1 = patriarch.createPolygon(anchor);
			pol1.setFillColor(255,255,0);
			pol1.setLineStyle(HSSFPolygon.LINESTYLE_SOLID);
			pol1.setLineWidth(2*EMU);
			pol1.setLineStyleColor(255,255,0);
			int[] xPoints1 = {xPicCG-2,xPicCG,xPicCG+2,xPicCG,xPicCG-2};
			int[] yPoints1 = {yPicCG,yPicCG-2,yPicCG,yPicCG+2,yPicCG};
			pol1.setPoints(xPoints1,yPoints1);
			pol1.setNoFill(false);
			
			HSSFPolygon pol2 = patriarch.createPolygon(anchor);
			pol2.setFillColor(255,0,0);
			pol2.setLineStyle(HSSFPolygon.LINESTYLE_SOLID);
			pol2.setLineWidth(2*EMU);
			pol2.setLineStyleColor(255,0,0);
			int[] xPoints2 = {xPicCG-1,xPicCG,xPicCG+1,xPicCG,xPicCG-1};
			int[] yPoints2 = {yPicCG,yPicCG-1,yPicCG,yPicCG+1,yPicCG};
			pol2.setPoints(xPoints2,yPoints2);
			pol2.setNoFill(false);
		}
		
	}
	
	public ImageFileInfo optimizaTamanhoImagemReport(int numImg){
		ImageFileInfo ifi = new ImageFileInfo();
		int H = sapo.jif[numImg].getRenderedImage().getHeight();             
        int escala = (int)Math.round(40000/H);
        RenderedImage img = sapo.zoomImage(sapo.jif[numImg].getRenderedImage(),0,0,escala,Interpolation.INTERP_BICUBIC);
        ifi.width  = img.getWidth();
        ifi.height = img.getHeight(); 
        String fileName = sapo.jif[numImg].file.getPath();
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + "-amostra.jpg";
        JAI.create("filestore",img,fileName,"JPEG");
        ifi.filePath = fileName;
		return ifi;
	}
	
	private static int loadPicture(File file, HSSFWorkbook wb) {
        int pictureIndex = 0;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        if (file != null)
        	try {
        		fis = new FileInputStream(file);
        		bos = new ByteArrayOutputStream( );
        		int c;
        		while ( (c = fis.read()) != -1)
        			bos.write( c );
        		pictureIndex = wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG );
        		fis.close();
        		bos.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	} 
        	return pictureIndex;
    }
	
	private void printSetup() {
		ps = sheet.getPrintSetup();
		ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		sheet.setAutobreaks(true);
		ps.setFitHeight((short)1);
		ps.setFitWidth((short)1);
	}

	private void criaHeader() {
		HSSFHeader header = sheet.getHeader();
	    header.setCenter(sapo.user.dados.qualificacao + " " + sapo.user.dados.nomecompleto);
	    header.setLeft(sapo.user.dados.instituicao);
	    header.setRight(HSSFHeader.page() + " / " + HSSFHeader.numPages());
	}
	
	private void criaFooter() {
		HSSFFooter footer = sheet.getFooter();
		footer.setCenter(HSSFFooter.font("Stencil-Normal", "Italic") + 
                HSSFFooter.fontSize((short) 10) + 
                "Sujeito: " + sapo.paciente.dados.nome + 
                " - ID: " + sapo.paciente.dados.identidade + "\n" + 
                "Relatório gerado automaticamente pelo SAPO v." + 
                SAPO.VERSION + " - http://sapo.incubadora.fapesp.br/" + 
                "\n" + 
                getTime());
		
	}
	
	private String getTime() {
		return (java.text.DateFormat.getDateTimeInstance(
				java.text.DateFormat.FULL,java.text.DateFormat.DEFAULT)
				).format((new java.util.Date(System.currentTimeMillis())));
	}

	private void writeXLS() {
		File file = selecionarArquivo();
		if (file != null) {
			try {
				FileOutputStream fileOut = new FileOutputStream(file);
				wb.write(fileOut);
				fileOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private File selecionarArquivo() {
		SAPO.setUILanguageJFileChooser();
		String arquivo = "";
		JFileChooser fc = new JFileChooser();
		String[] extensoes = new String[] {"xls"};
		EFileFilter filter = new EFileFilter(extensoes, "Arquivos XLS");
		fc.addChoosableFileFilter(filter);
		fc.setDialogTitle("SAPO - Exportar Relatório Para Excel");
		boolean nomeArquivoOk = false;
		while (!nomeArquivoOk) {
			if (sapo.user.dados.dirrelatorios != "") {
				File prjDir = new File(sapo.user.dados.dirrelatorios);
				if (prjDir.exists()) fc.setCurrentDirectory(prjDir);
			}
			if (fc.showSaveDialog(sapo) != JFileChooser.APPROVE_OPTION) return null;
			arquivo = fc.getSelectedFile().getPath();
			
			if (!arquivo.toUpperCase().endsWith(".XLS")) {
				if ((arquivo.length() - arquivo.lastIndexOf(".") ) <= 4) {
					JOptionPane.showMessageDialog(sapo,"a extensão do arquivo deve ser .xls");
					continue;
				}
				arquivo += ".xls";
			}
			nomeArquivoOk = true;
		}
		sapo.user.dados.dirrelatorios = (fc.getSelectedFile()).getParent();
		return (new File(arquivo));	
	}

	class ImageFileInfo {
		String filePath = "";
		int width = 0;
		int height = 0;
	}
	
}
