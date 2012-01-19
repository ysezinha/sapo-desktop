package com.dati.util;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.dati.gui.EscapeDialog;
import com.dati.gui.FormUtilities;

/**
 * @author Edison Puig Maldonado
 *
 */
public class FreeMemory extends EscapeDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4698441643669721083L;
	private JPanel jContentPane = null;

	long beforeFREE = 0;
    long beforeTOTAL = 0;
    Runtime runtime;
    
	private JLabel jLabelAntes = null;
	private JLabel jMemLabelAntes = null;
	private JLabel jLabelDepois = null;
	private JLabel jMemLabelDepois = null;
	private JLabel jLabelProcessors = null;
	private JLabel jLabelMaxMemory = null;
	private JLabel jLabelTitle = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	/**
	 * This is the default constructor
	 */
	public FreeMemory() {
		super();
		getRuntime();
		initialize();
		this.setVisible(true);
	}

	public FreeMemory(Frame owner, Component parent, String title, boolean modal) {
		super(owner, title, true);
		getRuntime();
		initialize();
		FormUtilities.centerInForm(parent, this);
		this.setVisible(true);
	}
	
	private void getRuntime() {
		runtime = Runtime.getRuntime();
		beforeFREE = (long) (runtime.freeMemory() / 1024.0);
        beforeTOTAL = (long) (runtime.totalMemory() / 1024.0);
		FreeMemory.runGarbageCollector(runtime);
	}
	
	public final static void runGarbageCollector(Runtime runtime) { 
        System.runFinalization();
        runtime.gc();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setSize(638, 337);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(getJContentPane());
		pack();
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.gridwidth = 2;
			gridBagConstraints32.insets = new java.awt.Insets(6,36,24,36);
			gridBagConstraints32.ipadx = 3;
			gridBagConstraints32.ipady = 3;
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.gridy = 7;
			jLabel2 = new JLabel();
			jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabel2.setText("Arquitetura : " + System.getProperty("os.arch"));
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.gridwidth = 2;
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.insets = new java.awt.Insets(6,36,6,36);
			gridBagConstraints22.ipadx = 3;
			gridBagConstraints22.ipady = 3;
			gridBagConstraints22.gridy = 6;
			jLabel1 = new JLabel();
			jLabel1.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabel1.setText("Versão Java : " + System.getProperty("java.version"));
			jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.insets = new java.awt.Insets(6,36,6,36);
			gridBagConstraints12.ipadx = 3;
			gridBagConstraints12.ipady = 3;
			gridBagConstraints12.gridy = 4;
			jLabel = new JLabel();
			jLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabel.setText("Sistema Operacional : " + System.getProperty("os.name"));
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.insets = new java.awt.Insets(24,36,9,36);
			gridBagConstraints31.gridy = 0;
			jLabelTitle = new JLabel();
			jLabelTitle.setText("Resultado da Limpeza de Memória :");
			jLabelTitle.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 18));
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new java.awt.Insets(9,36,6,36);
			gridBagConstraints21.ipadx = 3;
			gridBagConstraints21.ipady = 3;
			gridBagConstraints21.gridy = 3;
			jLabelMaxMemory = new JLabel();
			jLabelMaxMemory.setText("Máxima memória possível para o programa : " + Math.round(runtime.maxMemory() / 1024.0 / 1024.0) + "MB");
			jLabelMaxMemory.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabelMaxMemory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.insets = new java.awt.Insets(6,36,6,36);
			gridBagConstraints11.ipadx = 3;
			gridBagConstraints11.ipady = 3;
			gridBagConstraints11.gridy = 5;
			jLabelProcessors = new JLabel();
			jLabelProcessors.setText("Número de Processadores: "+runtime.availableProcessors());
			jLabelProcessors.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabelProcessors.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.ipadx = 3;
			gridBagConstraints3.ipady = 3;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.insets = new java.awt.Insets(3,3,3,36);
			gridBagConstraints3.gridy = 2;
			jMemLabelDepois = new MemoryLabel(500);
			jMemLabelDepois.setText("JLabel");
			jMemLabelDepois.setVisible(true);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.ipadx = 3;
			gridBagConstraints2.ipady = 3;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(3,36,3,3);
			gridBagConstraints2.gridy = 2;
			jLabelDepois = new JLabel();
			jLabelDepois.setText("Depois : ");
			jLabelDepois.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabelDepois.setVisible(true);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.ipadx = 3;
			gridBagConstraints1.ipady = 3;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.insets = new java.awt.Insets(3,3,3,36);
			gridBagConstraints1.gridy = 1;
			jMemLabelAntes = new MemoryLabel(beforeFREE, beforeTOTAL);
			jMemLabelAntes.setVisible(true);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.ipadx = 3;
			gridBagConstraints.ipady = 3;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.insets = new java.awt.Insets(3,36,3,3);
			gridBagConstraints.gridy = 1;
			jLabelAntes = new JLabel();
			jLabelAntes.setText("Antes : ");
			jLabelAntes.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
			jLabelAntes.setVisible(true);
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setBackground(java.awt.SystemColor.control);
			jContentPane.add(jLabelAntes, gridBagConstraints);
			jContentPane.add(jMemLabelAntes, gridBagConstraints1);
			jContentPane.add(jLabelDepois, gridBagConstraints2);
			jContentPane.add(jMemLabelDepois, gridBagConstraints3);
			jContentPane.add(jLabelProcessors, gridBagConstraints11);
			jContentPane.add(jLabelMaxMemory, gridBagConstraints21);
			jContentPane.add(jLabelTitle, gridBagConstraints31);
			jContentPane.add(jLabel, gridBagConstraints12);
			jContentPane.add(jLabel1, gridBagConstraints22);
			jContentPane.add(jLabel2, gridBagConstraints32);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
