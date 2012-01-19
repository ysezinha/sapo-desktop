/*
 * BaseInternalFrameSAPO.java
 *
 * Desenvolvimento iniciado em 24 de abril de 2005
 */

/**
 * <p>
 * BaseInternalFrameSAPO - Gerencia a parte gráfica dos dados do Paciente
 */

/**
 * @author  Edison Puig Maldonado (mail@puig.pro.br)
 */

import javax.swing.JScrollPane;
import javax.media.jai.JAI;
import java.io.IOException;
import java.awt.image.RenderedImage;
import java.io.File;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.sun.media.jai.codec.FileSeekableStream;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;

public class BaseInternalFrameSAPO extends BaseInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1441821001547807659L;
	private SAPO sapo;
	public JAIPanelSAPO jaiP;
	public boolean flagImg;
	public boolean flagPontos;
	public int numImg = 0;
	public int zoom; 
	public File file;
	private RenderedImage image;
	
	BaseInternalFrameSAPO( SAPO sapo, File file ){
		this.sapo = sapo;    
		this.file = file;
		image = buildRenderedImage();
		if (image != null) {
			jaiP = new JAIPanelSAPO( this.image );
			//this.jaiP.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));		
			getContentPane().setLayout(new java.awt.BorderLayout());
			setMinimumSize(new Dimension(200,150));
			setResizable(true);
			setClosable(true);
			setMaximizable(true);
			setIconifiable(true);
			Icon ic = new ImageIcon(SAPO.ICONPATH);
			setFrameIcon(ic);
			setTitle( this.file.getName() );
			setName( this.file.getName() );
			setFocusCycleRoot(false);
			
			JScrollPane sPane = new JScrollPane( jaiP );    
			getContentPane().add(sPane, java.awt.BorderLayout.CENTER);
			
			pack();
			setVisible(true);  
		}
		else {
			System.out.println("Não foi possível abrir o arquivo: " + file.getPath());
			JOptionPane.showMessageDialog(sapo, 
					"Não foi possível abrir o arquivo com a imagem.\n" +
					"Isto pode ser porque o arquivo foi apagado ou está corrompido.",
					"ERRO", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}
	
	public RenderedImage getRenderedImage(){ return image; }
	public void setRenderedImage( RenderedImage img ){ this.image = img;}
	
	private RenderedImage buildRenderedImage( ){
		FileSeekableStream stream = null;
		RenderedImage renderedImage = null;
		try {
			if (file.exists()) {
				stream = new FileSeekableStream( file.getPath() );
				renderedImage = JAI.create("stream", stream);
			}
			else {
				if (!file.exists()) System.out.println("Arquivo " + file.getPath() + " não existe.");
			}
		} catch (IOException e) {
			sapo.errorReport.jtxtErrorReport.append(e.toString()+"\n");
			sapo.error = e.getStackTrace();
			for(int k=0; k< sapo.error.length; k++)
				sapo.errorReport.jtxtErrorReport.append(sapo.error[k].toString()+"\n");
			sapo.errorReport.jtxtErrorReport.append("==========================================="+"\n");
			sapo.jdlgErrorReport.pack();
			sapo.jdlgErrorReport.setVisible(true);      
			e.printStackTrace();
			System.exit(0);
		}
		/* Create an operator to decode the image file. */
		return renderedImage;
		
	}//getRenderedImage
	
} // fim do BaseInternalFrameSAPO
