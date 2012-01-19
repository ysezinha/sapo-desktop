/*
 * UIProtocol.java
 *
 * Created on 30 de Novembro de 2005, 07:00
 */

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import com.tomtessier.scrollabledesktop.BaseInternalFrame;

public class UIProtocol extends BaseInternalFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -862259072219259856L;
	
	SAPO sapo;
	Protocolo proto = new Protocolo();
	
	private int frenteX[] = proto.frenteX;
	private int frenteY[] = proto.frenteY;
	private int latEsqX[] = proto.latEsqX;
	private int latEsqY[] = proto.latEsqY;
	private int latDirX[] = proto.latDirX;
	private int latDirY[] = proto.latDirY;
	private int posterX[] = proto.posX;
	private int posterY[] = proto.posY;
	
	String frenteLabel[] = proto.frenteLabel;
	JRadioButton frente[] = new JRadioButton[frenteLabel.length];
	JCheckBox frenteCk[] = new JCheckBox[frenteLabel.length];
	
	String latEsqLabel[] = proto.latEsqLabel;
	JRadioButton latEsq[] = new JRadioButton[latEsqLabel.length];                        
	JCheckBox latEsqCk[] = new JCheckBox[latEsqLabel.length];    
	
	String latDirLabel[] = proto.latDirLabel;
	JRadioButton latDir[] = new JRadioButton[latDirLabel.length];
	JCheckBox latDirCk[] = new JCheckBox[latDirLabel.length];
	
	String posterLabel[] = proto.posteriorLabel;
	JRadioButton poster[] = new JRadioButton[posterLabel.length];
	JCheckBox posterCk[] = new JCheckBox[posterLabel.length];
	
	JRadioButton radio[] = new JRadioButton[1];
	JCheckBox check[] = new JCheckBox[1];
	String label[] = new String[1];
	
	int frenteProtocol = 0;
	int latDirProtocol = 0;
	int latEsqProtocol = 0;
	int posteriorProtocol = 0;
	String[] opcoes = {"Anterior", "Lateral Direita", "Lateral Esquerda", "Posterior"};
	int vista = 0;
	boolean verFoto = false;
	
	/** Creates new form UIUser */
	public UIProtocol(SAPO sapo) {
		super("SAPO - Define protocolos personalizados");
		this.sapo = sapo;
		initComponents();
		setVisible(true);
		jRdBtnFrontal.doClick();
	}
	
	private void salvaProtocolos() {
		String nomeProto = (String)jcmbProtocol.getSelectedItem();
		sapo.clearProtocol(nomeProto);
		if(!nomeProto.equals("")){ 
			for(int i=0; i<frente.length; i++)
				if((frente[i] != null) && frente[i].isSelected()) sapo.addPontoProto(nomeProto, "anterior", i);
			for(int i=0; i<latEsq.length; i++)
				if((latEsq[i] != null) && latEsq[i].isSelected()) sapo.addPontoProto(nomeProto, "latEsq", i);
			for(int i=0; i<latDir.length; i++)
				if((latDir[i] != null) && latDir[i].isSelected()) sapo.addPontoProto(nomeProto, "latDir", i);
			for(int i=0; i<poster.length; i++)
				if((poster[i] != null) && poster[i].isSelected()) sapo.addPontoProto(nomeProto, "posterior", i); 
			sapo.user.escreveDB();
			JOptionPane.showMessageDialog(sapo,"A configuração do protocolo " + nomeProto + " foi salva.");
		}
		else
			JOptionPane.showMessageDialog(sapo,"Nenhum protocolo selecionado!");
	}
	
	private void chooseProtocol() {
		int index = jcmbProtocol.getSelectedIndex();
		String nomeProto = (String)jcmbProtocol.getSelectedItem();
		switch (index){
			case  0: sapo.protoUser1.nome = nomeProto; break;
			case  1: sapo.protoUser2.nome = nomeProto; break;
			case  2: sapo.protoUser3.nome = nomeProto; break;
		}
		jRdBtnFrontal.doClick();
	}
	
	private void montaVista(int view) {
		jPanel4.removeAll();
		jPanel11.removeAll();
		final int numPoints;
		String nomeProto = (String)jcmbProtocol.getSelectedItem();
		String vista = "";
		switch (view) {
			case 1: vista = "anterior"; radio = frente; check = frenteCk; numPoints = frente.length; label = frenteLabel; break;
			case 2: vista = "latEsq"; radio = latEsq; check = latEsqCk; numPoints = latEsq.length; label = latEsqLabel; break;
			case 3: vista = "latDir"; radio = latDir; check = latDirCk; numPoints = latDir.length; label = latDirLabel; break;
			case 4: vista = "posterior"; radio = poster; check = posterCk; numPoints = poster.length; label = posterLabel; break;
			default: numPoints = 0;
		}
		for(int i=0; i<numPoints; i++){
			int x=0,y=0;
			radio[i] = new javax.swing.JRadioButton();
			switch (view) {
				case 1: x = frenteX[i]; y = frenteY[i]; break;
				case 2: x = latEsqX[i]; y = latEsqY[i]; break;
				case 3: x = latDirX[i]; y = latDirY[i]; break;
				case 4: x = posterX[i]; y = posterY[i]; break;
			}
			jPanel4.add(radio[i]);
			radio[i].setBounds(x, y, 20, 20);
			radio[i].setContentAreaFilled(false);
			radio[i].setEnabled(true);
			radio[i].setSelected(sapo.hasPontoProto(nomeProto, vista, i));
			radio[i].addFocusListener(new DeslocaParaVerComponente());
			radio[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Object obj = evt.getSource();
					synchronizeRadio((JRadioButton)obj, numPoints);
				}
			});
			jPanel4.setComponentZOrder(radio[i],0);
		}
		javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
		String pict = "";
		switch (view) {
			case 1: pict = "/res/frente.png"; break;
			case 2: pict = "/res/lado1.png" ; break;
			case 3: pict = "/res/lado2.png" ; break;
			case 4: pict = "/res/costas.png"; break;
		}
		
		jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(pict)));
		jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jLabel1.setMaximumSize(new java.awt.Dimension(288, 650));
		jLabel1.setMinimumSize(new java.awt.Dimension(288, 650));
		jLabel1.setPreferredSize(new java.awt.Dimension(288, 650));
		jPanel4.add(jLabel1);
		jLabel1.setBounds(0, 0, 288, 650);
		jPanel3.removeAll();
		
		for(int i=0; i<numPoints; i++){
			check[i] = new javax.swing.JCheckBox(label[i]);
			jPanel3.add(check[i]);
			if (sapo.hasPontoProto(nomeProto, vista, i)) {
				check[i].setSelected(true);
				check[i].setForeground(SystemColor.textHighlight);
				check[i].setFont(new Font(check[i].getFont().getName(),Font.BOLD,check[i].getFont().getSize()));
			}
			check[i].addFocusListener(new DeslocaParaVerComponente());
			check[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Object obj = evt.getSource();
					synchronizeCheck((JCheckBox)obj, numPoints);
				}
			});
		}
		//Coloca imagem do paciente ao lado
		if(verFoto){
			jPanelView.add(jScrollPane5);
			apresentaImagem(view);
		} //if verFoto
		else{
			jPanelView.remove(jScrollPane5);
		}
		
		this.updateUI();
	}
	
	protected void synchronizeRadio(JRadioButton jrb, int npts) {
		for(int i=0; i<npts; i++){
			if ((jrb == radio[i])) {
				check[i].setSelected(radio[i].isSelected());
				check[i].grabFocus();
				if (check[i].isSelected()) {
					check[i].setForeground(SystemColor.textHighlight);
					check[i].setFont(new Font(check[i].getFont().getName(),Font.BOLD,check[i].getFont().getSize()));
				}
				else {
					check[i].setForeground(SystemColor.BLACK);
					check[i].setFont(new Font(check[i].getFont().getName(),Font.PLAIN,check[i].getFont().getSize()));
				}
			}
		}
	}
	
	protected void synchronizeCheck(JCheckBox jcb, int npts) {
		for(int i=0; i<npts; i++){
			if ((jcb == check[i])) {
				radio[i].setSelected(check[i].isSelected());
				radio[i].grabFocus();
				if (check[i].isSelected()) {
					check[i].setForeground(SystemColor.textHighlight);
					check[i].setFont(new Font(check[i].getFont().getName(),Font.BOLD,check[i].getFont().getSize()));
				}
				else {
					check[i].setForeground(SystemColor.BLACK);
					check[i].setFont(new Font(check[i].getFont().getName(),Font.PLAIN,check[i].getFont().getSize()));
				}
			}
		}
	}
	
	protected void apresentaImagem(int view){
		javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
		String strVista = "";
		Image img = null;
		if (sapo.paciente != null)
			switch (view) {
				case 1: {
					for(int i=0; i<sapo.paciente.dados.imgData.length; i++){
						strVista = sapo.paciente.dados.imgData[i].getVista();
						if((strVista!=null)&&(strVista.equals(opcoes[0])))
							img = (java.awt.Image)javax.media.jai.PlanarImage.wrapRenderedImage(sapo.jif[i].getRenderedImage()).getAsBufferedImage();
					}
				}; break;
				case 2:{
					for(int i=0; i<sapo.paciente.dados.imgData.length; i++){
						strVista = sapo.paciente.dados.imgData[i].getVista();
						if((strVista!=null)&&(strVista.equals(opcoes[2])))
							img = (java.awt.Image)javax.media.jai.PlanarImage.wrapRenderedImage(sapo.jif[i].getRenderedImage()).getAsBufferedImage();
					}
				}; break;
				case 3: {
					for(int i=0; i<sapo.paciente.dados.imgData.length; i++){
						strVista = sapo.paciente.dados.imgData[i].getVista();
						if((strVista!=null)&&(strVista.equals(opcoes[1])))
							img = (java.awt.Image)javax.media.jai.PlanarImage.wrapRenderedImage(sapo.jif[i].getRenderedImage()).getAsBufferedImage();
					}
				}; break;
				case 4: {
					for(int i=0; i<sapo.paciente.dados.imgData.length; i++){
						strVista = sapo.paciente.dados.imgData[i].getVista();
						if((strVista!=null)&&(strVista.equals(opcoes[3])))
							img = (java.awt.Image)javax.media.jai.PlanarImage.wrapRenderedImage(sapo.jif[i].getRenderedImage()).getAsBufferedImage();
					}
				}; break;
			}//switch view
		if(img!=null){
			//tem que melhorar esse zoom
			int w = img.getWidth(this);
			int h = img.getHeight(this);
			double zz = w/(h*1.0);
			w = 288;
			h = (int)Math.round(w/zz);
			img = img.getScaledInstance(w,h,java.awt.Image.SCALE_FAST);        
			jLabel2.setIcon(new javax.swing.ImageIcon(img));
			jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			jLabel2.setMaximumSize(new java.awt.Dimension(288, 650));
			jLabel2.setMinimumSize(new java.awt.Dimension(288, 650));
			jLabel2.setPreferredSize(new java.awt.Dimension(288, 650));
			jPanel11.add(jLabel2);
			jLabel2.setBounds(0, 0, 288, 650);
		}
		else {
			JOptionPane.showMessageDialog(sapo,"Nenhuma imagem disponível!");
			jTBtnVerFoto.doClick();
		}
	}//apresentaImagem
	
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel7 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jcmbProtocol = new javax.swing.JComboBox();
		jRdBtnFrontal = new javax.swing.JRadioButton();
		jRdBtnLatEsq = new javax.swing.JRadioButton();
		jRdBtnLatDir = new javax.swing.JRadioButton();
		jRdBtnPosterior = new javax.swing.JRadioButton();
		jPanelView = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane2.getVerticalScrollBar().setUnitIncrement(25);
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(25);
		jPanel8 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane5 = new javax.swing.JScrollPane();
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(25);
		jPanel10 = new javax.swing.JPanel();
		jPanel11 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jbtnOK = new javax.swing.JButton();
		jbtnAplicar = new javax.swing.JButton();
		jTBtnVerFoto = new javax.swing.JToggleButton();
		jbtnCancel = new javax.swing.JButton();
		
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("SAPO - Configura\u00e7\u00e3o de Protocolos Personalizados");
		setDesktopIcon(getDesktopIcon());
		setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("res/frog.gif")));
		try {
			setIcon(true);
		} catch (java.beans.PropertyVetoException e1) {
			e1.printStackTrace();
		}
		setPreferredSize(new java.awt.Dimension(500, 410));
		jPanel7.setLayout(new java.awt.BorderLayout());
		
		jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel5.setMinimumSize(new java.awt.Dimension(200, 33));
		jLabel1.setText("Nome:");
		jPanel5.add(jLabel1);
		
		jcmbProtocol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "User1", "User2", "User3" }));
		jcmbProtocol.setToolTipText("Nome do protocolo");
		jcmbProtocol.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmbProtocolActionPerformed(evt);
			}
		});
		
		jPanel5.add(jcmbProtocol);
		
		buttonGroup1.add(jRdBtnFrontal);
		jRdBtnFrontal.setText("Anterior");
		jRdBtnFrontal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRdBtnFrontalActionPerformed(evt);
			}
		});
		
		jPanel5.add(jRdBtnFrontal);
		
		buttonGroup1.add(jRdBtnLatEsq);
		jRdBtnLatEsq.setText("Esquerda");
		jRdBtnLatEsq.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRdBtnLatEsqActionPerformed(evt);
			}
		});
		
		jPanel5.add(jRdBtnLatEsq);
		
		buttonGroup1.add(jRdBtnLatDir);
		jRdBtnLatDir.setText("Direita");
		jRdBtnLatDir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRdBtnLatDirActionPerformed(evt);
			}
		});
		
		jPanel5.add(jRdBtnLatDir);
		
		buttonGroup1.add(jRdBtnPosterior);
		jRdBtnPosterior.setText("Posterior");
		jRdBtnPosterior.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRdBtnPosteriorActionPerformed(evt);
			}
		});
		
		jPanel5.add(jRdBtnPosterior);
		
		jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);
		
		jPanelView.setLayout(new java.awt.GridLayout(1, 3));
		
		jScrollPane2.setAutoscrolls(true);
		jScrollPane2.setMinimumSize(null);
		jScrollPane2.setPreferredSize(null);
		jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
		
		jScrollPane2.setViewportView(jPanel3);
		
		jPanelView.add(jScrollPane2);
		
		jScrollPane1.setAutoscrolls(true);
		jPanel8.setLayout(new java.awt.GridBagLayout());
		
		jPanel4.setLayout(new java.awt.BorderLayout());
		
		jPanel4.setAlignmentX(0.0F);
		jPanel4.setAlignmentY(0.0F);
		jPanel4.setFocusable(false);
		jPanel8.add(jPanel4, new java.awt.GridBagConstraints());
		
		jScrollPane1.setViewportView(jPanel8);
		
		jPanelView.add(jScrollPane1);
		
		jScrollPane5.setAutoscrolls(true);
		jPanel10.setLayout(new java.awt.GridBagLayout());
		
		jPanel11.setAlignmentX(0.0F);
		jPanel11.setAlignmentY(0.0F);
		jPanel11.setFocusable(false);
		jPanel10.add(jPanel11, new java.awt.GridBagConstraints());
		
		jScrollPane5.setViewportView(jPanel10);
		
		jPanelView.add(jScrollPane5);
		
		jPanel1.add(jPanelView, java.awt.BorderLayout.CENTER);
		
		jPanel7.add(jPanel1, java.awt.BorderLayout.CENTER);
		
		getContentPane().add(jPanel7, java.awt.BorderLayout.CENTER);
		
		jPanel2.setMinimumSize(new java.awt.Dimension(219, 40));
		jPanel2.setPreferredSize(new java.awt.Dimension(219, 35));
		jbtnOK.setText("Ok");
		jbtnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtnOKActionPerformed(evt);
			}
		});
		
		jPanel2.add(jbtnOK);
		
		jbtnAplicar.setText("Aplicar");
		jbtnAplicar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtnAplicarActionPerformed(evt);
			}
		});
		
		jPanel2.add(jbtnAplicar);
		
		jTBtnVerFoto.setText("Ver foto");
		jTBtnVerFoto.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTBtnVerFotoActionPerformed(evt);
			}
		});
		
		jPanel2.add(jTBtnVerFoto);
		
		jbtnCancel.setText("Cancela");
		jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtnCancelActionPerformed(evt);
			}
		});
		
		jPanel2.add(jbtnCancel);
		
		getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
		
		pack();
	}
	// </editor-fold>//GEN-END:initComponents
	
	private void jTBtnVerFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTBtnVerFotoActionPerformed
		if (jTBtnVerFoto.isSelected()) verFoto = true;
		else verFoto = false;
		montaVista(vista);
	}//GEN-LAST:event_jTBtnVerFotoActionPerformed
	
	private void jRdBtnPosteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdBtnPosteriorActionPerformed
		vista = 4;
		montaVista(vista);
	}//GEN-LAST:event_jRdBtnPosteriorActionPerformed
	
	private void jRdBtnLatDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdBtnLatDirActionPerformed
		vista = 3;
		montaVista(vista);
	}//GEN-LAST:event_jRdBtnLatDirActionPerformed
	
	private void jRdBtnLatEsqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdBtnLatEsqActionPerformed
		vista = 2;
		montaVista(vista);
	}//GEN-LAST:event_jRdBtnLatEsqActionPerformed
	
	private void jRdBtnFrontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdBtnFrontalActionPerformed
		vista = 1;
		montaVista(vista);
	}//GEN-LAST:event_jRdBtnFrontalActionPerformed
	
	private void jcmbProtocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmbProtocolActionPerformed
		chooseProtocol();
	}//GEN-LAST:event_jcmbProtocolActionPerformed
	
	private void jbtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelActionPerformed
		dispose();
	}//GEN-LAST:event_jbtnCancelActionPerformed
	
	private void jbtnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnOKActionPerformed
		salvaProtocolos();
		dispose();
	}//GEN-LAST:event_jbtnOKActionPerformed
	
	private void jbtnAplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAplicarActionPerformed
		salvaProtocolos();
	}//GEN-LAST:event_jbtnAplicarActionPerformed
	
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JPanel jPanelView;
	private javax.swing.JRadioButton jRdBtnFrontal;
	private javax.swing.JRadioButton jRdBtnLatDir;
	private javax.swing.JRadioButton jRdBtnLatEsq;
	private javax.swing.JRadioButton jRdBtnPosterior;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JScrollPane jScrollPane5;
	private javax.swing.JToggleButton jTBtnVerFoto;
	private javax.swing.JButton jbtnAplicar;
	private javax.swing.JButton jbtnCancel;
	private javax.swing.JButton jbtnOK;
	private javax.swing.JComboBox jcmbProtocol;
	// End of variables declaration//GEN-END:variables
	
}
