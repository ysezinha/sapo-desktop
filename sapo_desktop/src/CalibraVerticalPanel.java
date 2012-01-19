
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedImageAdapter;
import javax.swing.JOptionPane;

/*
 * CalibraVerticalPanel.java
 *
 * Created on 7 de Fevereiro de 2005, 23:17
 */

/**
 *
 * @author Edison Puig Maldonado
 */

public class CalibraVerticalPanel extends javax.swing.JPanel {
	
	private int X1,X2,Y1,Y2;
	private boolean jtfCVELockFocus;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2389164774776339513L;
	SAPO sapo;
	
	public CalibraVerticalPanel(SAPO sapo) {
		this.sapo = sapo;
		initComponents();
	}

	public void alinhar () {
		try {
			calculaAngulo();
			sapo.showInternalFrameWithImage(sapo.numImg); 
			calibraVertical();
			if (sapo.user.dados.mostradicas) {
				Object[] options = {"Sim","Não"};
				int result = JOptionPane.showOptionDialog(sapo,
						"Confirma calibração e rotação da imagem?  ",
						"Confirmação da calibração",JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null, options, options[0]);
				if( result == JOptionPane.YES_OPTION) 
					rodaImagem();
				else {
					resetImage();
					calibraVerticalClear();
				}
			}
			else rodaImagem();
		} catch (NumberFormatException e) {
			sapo.errorReport.jtxtErrorReport.append(e.toString()+"\n");
			e.printStackTrace();
			sapo.error = e.getStackTrace();
			for(int k=0; k<sapo.error.length; k++)
				sapo.errorReport.jtxtErrorReport.append(sapo.error[k].toString()+"\n");
			sapo.errorReport.jtxtErrorReport.append("==========================================="+"\n");
			sapo.jdlgErrorReport.pack();
			sapo.jdlgErrorReport.setVisible(true);      
		}            
	}
	
	private void calculaAngulo() {
		X1 = Integer.parseInt(jlCVX1.getText());
		Y1 = Integer.parseInt(jlCVY1.getText());
		X2 = Integer.parseInt(jlCVX2.getText());
		Y2 = Integer.parseInt(jlCVY2.getText());
		double angulo = Math.atan2((X2-X1),(Y2-Y1));
		
		sapo.paciente.dados.imgData[sapo.numImg].setAnguloVertical(angulo);
		sapo.paciente.dados.imgData[sapo.numImg].setXAnchor(X1);
		sapo.paciente.dados.imgData[sapo.numImg].setYAnchor(Y1);
		sapo.paciente.dados.imgData[sapo.numImg].setImgRotate(true);
	}

	private void resetImage() {
		sapo.paciente.dados.imgData[sapo.numImg].setAnguloVertical(0.0);
		sapo.paciente.dados.imgData[sapo.numImg].setXAnchor(0);
		sapo.paciente.dados.imgData[sapo.numImg].setYAnchor(0);
		sapo.paciente.dados.imgData[sapo.numImg].setImgRotate(false);
		sapo.showInternalFrameWithImage(sapo.numImg);
	}

	private void rodaImagem() {
		resetImage();
		desenhaLinha();
		calculaAngulo();
		RenderedImage rImage = ImageRotate.rotateImage(sapo.jif[sapo.numImg].getRenderedImage(), 
				sapo.paciente.dados.imgData[sapo.numImg].getXAnchor(),
				sapo.paciente.dados.imgData[sapo.numImg].getYAnchor(),
				sapo.paciente.dados.imgData[sapo.numImg].getAnguloVertical());
		sapo.jif[sapo.numImg].setRenderedImage( rImage );
		rImage = null;
		sapo.paciente.dados.imgData[sapo.numImg].setImgRotate(false);
		sapo.paciente.dados.imgData[sapo.numImg].setAnguloVertical(0.0);
		sapo.paciente.dados.imgData[sapo.numImg].setXAnchor(0);
		sapo.paciente.dados.imgData[sapo.numImg].setYAnchor(0);
		jlCVAng.setText("Ângulo = 0 graus");
		jlCV5.setText("definido");
		sapo.saveImage(sapo.jif[sapo.numImg].file.getPath());
		jlCVEscala.setVisible(true);
		jtfCVE.setVisible(true);
		jbCVEAplicar.setVisible(true);
		if (sapo.user.dados.mostradicas)
			JOptionPane.showMessageDialog(sapo, 
					"Voce pode agora calibrar a imagem\n" +
					"em unidades físicas, utilizando o\n" +
					"comprimento deste traço vertical!",
					"Escala", JOptionPane.INFORMATION_MESSAGE);
		sapo.paciente.escreveDB();
		sapo.paciente.salvarAlteracoes = false;
		jtfCVELockFocus = true;
		jtfCVE.requestFocus();
	}

	private void desenhaLinha() {
		float sc = sapo.jif[sapo.numImg].zoom / 100.0F;
		PlanarImage pimg = new RenderedImageAdapter(sapo.jif[sapo.numImg].getRenderedImage());
        BufferedImage bimg = pimg.getAsBufferedImage();
        Graphics2D g2D = bimg.createGraphics();
        g2D.setColor(sapo.corDesenho);
        g2D.setStroke(new BasicStroke(Math.round(3/sc)));
        g2D.setColor(Color.DARK_GRAY);
    	g2D.drawLine(Math.round(X1/sc),Math.round(Y1/sc),Math.round(X2/sc),Math.round(Y2/sc));
    	sapo.jif[sapo.numImg].setRenderedImage(bimg);
    	sapo.showInternalFrameWithImage(sapo.numImg);
		calibraVertical();
		jtfCVE.requestFocus();
	}

	public void calibraVerticalClear() {
		jlCVX1.setText("");
		jlCVY1.setText("");
		jlCVX2.setText("");
		jlCVY2.setText("");
		jlCVAng.setText("Ângulo");
		jlCV5.setText("");
		sapo.jif[sapo.numImg].jaiP.resetMousePosition();
		sapo.jif[sapo.numImg].jaiP.repaint();
		calibraVertical();
	}
	
	public void calibraVertical() {
		sapo.inserePainelEmInternalFrame(jpCalVertical, "WEST");
		sapo.calVert.acoplar(sapo.numImg);
		sapo.jif[sapo.numImg].jaiP.setPaint(true, false, false, false);
	}
	
	private void aplicaEscala() {
		float scX, scY;
		scX = scY = sapo.jif[sapo.numImg].zoom / 100.0F;
		try {
			int X1 = Integer.parseInt(jlCVX1.getText());
			int Y1 = Integer.parseInt(jlCVY1.getText());
			int X2 = Integer.parseInt(jlCVX2.getText());
			int Y2 = Integer.parseInt(jlCVY2.getText()); 
			double dist = Math.sqrt(Math.pow(((X2-X1)/scX),2) + Math.pow(((Y2-Y1)/scY),2));
			sapo.paciente.dados.imgData[sapo.numImg].setEscalaX(sapo.doubleLoc(jtfCVE.getText())/dist);
			sapo.paciente.dados.imgData[sapo.numImg].setEscalaY(sapo.doubleLoc(jtfCVE.getText())/dist);
			jlCV5.setText("definido e calibrado");
			sapo.paciente.escreveDB();
			sapo.paciente.salvarAlteracoes = false;
			jtfCVE.setVisible(false);
			jlCVEscala.setVisible(false);
			jbCVEAplicar.setVisible(false);
			jpCalVertical.repaint();
		} catch (ArithmeticException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(sapo,
					"Erro aritmético !",
					"Erro", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jpCalVertical = new javax.swing.JPanel();
        jtxtPaneVertical = new javax.swing.JTextPane();
        jpnCV = new javax.swing.JPanel();
        jlCVX = new javax.swing.JLabel();
        jlCVY = new javax.swing.JLabel();
        jlCVX1 = new javax.swing.JLabel();
        jlCVY1 = new javax.swing.JLabel();
        jlCVX2 = new javax.swing.JLabel();
        jlCVY2 = new javax.swing.JLabel();
        jlCVAng = new javax.swing.JLabel();
        jlCV5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jbCVAplicar = new javax.swing.JButton();
        jbCVClear = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jbCVOk = new javax.swing.JButton();
        jbCVAjuda = new javax.swing.JButton();
        jlCVEscala = new javax.swing.JLabel();
        jlCVEscala2 = new javax.swing.JLabel();
        jlCVEscala1 = new javax.swing.JLabel();
        jtfCVE = new javax.swing.JTextField();
        jbCVEAplicar = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpCalVertical.setLayout(new java.awt.GridBagLayout());

        jpCalVertical.setMaximumSize(new java.awt.Dimension(2147483647, 250));
        jpCalVertical.setMinimumSize(new java.awt.Dimension(11, 36));
        jpCalVertical.setPreferredSize(new java.awt.Dimension(150, 250));
        jtxtPaneVertical.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jtxtPaneVertical.setEditable(false);
        jtxtPaneVertical.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        jtxtPaneVertical.setText("para  definir  a\nvertical, clique\ncom  o  mouse\nem  um  ponto \ndessa imagem,\narraste e solte");
        jtxtPaneVertical.setToolTipText("Para definir a vertical, clique em um ponto da imagem, arraste e solte.");
        jtxtPaneVertical.setFocusable(false);
        jtxtPaneVertical.setMinimumSize(new java.awt.Dimension(125, 48));
        jtxtPaneVertical.setOpaque(false);
        jtxtPaneVertical.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jpCalVertical.add(jtxtPaneVertical, gridBagConstraints);

        jpnCV.setLayout(new java.awt.GridBagLayout());

        jpnCV.setMinimumSize(new java.awt.Dimension(43, 69));
        jpnCV.setOpaque(false);
        jlCVX.setText("X");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jpnCV.add(jlCVX, gridBagConstraints);

        jlCVY.setText("Y");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jpnCV.add(jlCVY, gridBagConstraints);

        jlCVX1.setFont(new java.awt.Font("Dialog", 0, 11));
        jlCVX1.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jpnCV.add(jlCVX1, gridBagConstraints);

        jlCVY1.setFont(new java.awt.Font("Dialog", 0, 11));
        jlCVY1.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jpnCV.add(jlCVY1, gridBagConstraints);

        jlCVX2.setFont(new java.awt.Font("Dialog", 0, 11));
        jlCVX2.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jpnCV.add(jlCVX2, gridBagConstraints);

        jlCVY2.setFont(new java.awt.Font("Dialog", 0, 11));
        jlCVY2.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jpnCV.add(jlCVY2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jpCalVertical.add(jpnCV, gridBagConstraints);

        jlCVAng.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12));
        jlCVAng.setText("\u00c2ngulo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        jpCalVertical.add(jlCVAng, gridBagConstraints);

        jlCV5.setFont(new java.awt.Font("MS Sans Serif", 0, 10));
        jlCV5.setForeground(new java.awt.Color(0, 102, 0));
        jlCV5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlCV5.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlCV5.setMinimumSize(new java.awt.Dimension(50, 25));
        jlCV5.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jpCalVertical.add(jlCV5, gridBagConstraints);

        jbCVAplicar.setFont(new java.awt.Font("Arial", 0, 12));
        jbCVAplicar.setText("Aplicar");
        jbCVAplicar.setToolTipText("Escreve opera\u00e7\u00e3o na base de dados do projeto e encerra esta opera\u00e7\u00e3o");
        jbCVAplicar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCVAplicar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbCVAplicar.setMaximumSize(new java.awt.Dimension(50, 25));
        jbCVAplicar.setMinimumSize(new java.awt.Dimension(50, 25));
        jbCVAplicar.setPreferredSize(new java.awt.Dimension(50, 25));
        jbCVAplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCVAplicarActionPerformed(evt);
            }
        });

        jPanel10.add(jbCVAplicar);

        jbCVClear.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        jbCVClear.setText("Limpar");
        jbCVClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCVClear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbCVClear.setMaximumSize(new java.awt.Dimension(50, 25));
        jbCVClear.setMinimumSize(new java.awt.Dimension(50, 25));
        jbCVClear.setPreferredSize(new java.awt.Dimension(50, 25));
        jbCVClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCVClearActionPerformed(evt);
            }
        });

        jPanel10.add(jbCVClear);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jpCalVertical.add(jPanel10, gridBagConstraints);

        jbCVOk.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12));
        jbCVOk.setText("Sair");
        jbCVOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCVOk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbCVOk.setMaximumSize(new java.awt.Dimension(50, 25));
        jbCVOk.setMinimumSize(new java.awt.Dimension(50, 25));
        jbCVOk.setPreferredSize(new java.awt.Dimension(50, 25));
        jbCVOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCVOkActionPerformed(evt);
            }
        });

        jPanel13.add(jbCVOk);

        jbCVAjuda.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        jbCVAjuda.setText("Ajuda");
        jbCVAjuda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCVAjuda.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbCVAjuda.setMaximumSize(new java.awt.Dimension(50, 25));
        jbCVAjuda.setMinimumSize(new java.awt.Dimension(50, 25));
        jbCVAjuda.setPreferredSize(new java.awt.Dimension(50, 25));
        jbCVAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCVAjudaActionPerformed(evt);
            }
        });

        jPanel13.add(jbCVAjuda);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jpCalVertical.add(jPanel13, gridBagConstraints);

        jlCVEscala.setFont(new java.awt.Font("Arial", 0, 10));
        jlCVEscala.setText("Calibra\u00e7\u00e3o deste tra\u00e7o:");
        jlCVEscala.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlCVEscala.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.ipady = 6;
        jpCalVertical.add(jlCVEscala, gridBagConstraints);

        jlCVEscala2.setFont(new java.awt.Font("Arial", 0, 10));
        jlCVEscala2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCVEscala2.setText("(decimais com v\u00edrgula)");
        jlCVEscala2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlCVEscala2.setMinimumSize(new java.awt.Dimension(110, 8));
        jlCVEscala2.setPreferredSize(new java.awt.Dimension(110, 8));
        jlCVEscala.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.ipady = 6;
        jpCalVertical.add(jlCVEscala2, gridBagConstraints);

        jlCVEscala1.setText("unidade: "+sapo.user.dados.unidadecomprimento);
        jlCVEscala1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jlCVEscala.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.ipady = 6;
        jpCalVertical.add(jlCVEscala1, gridBagConstraints);

        jtfCVE.setMinimumSize(new java.awt.Dimension(72, 21));
        jtfCVE.setPreferredSize(new java.awt.Dimension(72, 21));
        jtfCVE.setVisible(false);
        jtfCVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCVEActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jpCalVertical.add(jtfCVE, gridBagConstraints);

        jbCVEAplicar.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        jbCVEAplicar.setText("Calibrar");
        jbCVEAplicar.setToolTipText("Escreve escala da imagem na base de dados do projeto e encerra esta opera\u00e7\u00e3o");
        jbCVEAplicar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCVEAplicar.setMargin(new java.awt.Insets(0, 7, 0, 7));
        jbCVEAplicar.setMaximumSize(new java.awt.Dimension(72, 23));
        jbCVEAplicar.setMinimumSize(new java.awt.Dimension(72, 23));
        jbCVEAplicar.setPreferredSize(new java.awt.Dimension(72, 23));
        jbCVEAplicar.setVisible(false);
        jbCVEAplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCVEAplicarActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        jpCalVertical.add(jbCVEAplicar, gridBagConstraints);

        add(jpCalVertical, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jbCVAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCVAjudaActionPerformed
    	// TODO add your handling code here:
    }//GEN-LAST:event_jbCVAjudaActionPerformed
	
	private void jtfCVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfCVEActionPerformed
		if(sapo.isNumber(jtfCVE.getText())) aplicaEscala();
	}//GEN-LAST:event_jtfCVEActionPerformed
	
	private void jbCVEAplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCVEAplicarActionPerformed
		if (sapo.isNumber(jtfCVE.getText())) aplicaEscala();
	}//GEN-LAST:event_jbCVEAplicarActionPerformed
	
	private void jbCVOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCVOkActionPerformed
            sapo.restauraZoom();
            sapo.restauraInternalFrameOriginal();
	}//GEN-LAST:event_jbCVOkActionPerformed
	
	private void jbCVAplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCVAplicarActionPerformed
		if(sapo.isNumber(jlCVX1.getText()))
			if(sapo.isNumber(jlCVY1.getText()))
				if(sapo.isNumber(jlCVX2.getText()))
					if(sapo.isNumber(jlCVY2.getText()))
						alinhar();
	}//GEN-LAST:event_jbCVAplicarActionPerformed
	
	private void jbCVClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCVClearActionPerformed
		calibraVerticalClear();
	}//GEN-LAST:event_jbCVClearActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JButton jbCVAjuda;
    private javax.swing.JButton jbCVAplicar;
    private javax.swing.JButton jbCVClear;
    private javax.swing.JButton jbCVEAplicar;
    private javax.swing.JButton jbCVOk;
    private javax.swing.JLabel jlCV5;
    javax.swing.JLabel jlCVAng;
    private javax.swing.JLabel jlCVEscala;
    private javax.swing.JLabel jlCVEscala1;
    private javax.swing.JLabel jlCVEscala2;
    private javax.swing.JLabel jlCVX;
    javax.swing.JLabel jlCVX1;
    javax.swing.JLabel jlCVX2;
    private javax.swing.JLabel jlCVY;
    javax.swing.JLabel jlCVY1;
    javax.swing.JLabel jlCVY2;
    private javax.swing.JPanel jpCalVertical;
    private javax.swing.JPanel jpnCV;
    private javax.swing.JTextField jtfCVE;
    private javax.swing.JTextPane jtxtPaneVertical;
    // End of variables declaration//GEN-END:variables
	
}
