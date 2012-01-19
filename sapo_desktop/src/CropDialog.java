import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dati.gui.EscapeDialog;

/**
 * 
 */

/**
 * @author Edison Puig Maldonado
 *
 */
public class CropDialog extends EscapeDialog {

	SAPO sapo;
	
	private static final long serialVersionUID = 8556420902346300254L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	private JPanel jPanel6 = null;
	private JButton jButtonOk = null;
	private JButton jButtonCancel = null;
	
	public JTextField jtfLeft = null;
	public JTextField jtfRight = null;
	public JTextField jtfTop = null;
	public JTextField jtfBottom = null;
	
	/**
	 * This is the default constructor
	 */
	public CropDialog() {
		super();
		initialize();
		this.setAlwaysOnTop(true);
	}

	public CropDialog(SAPO sapo) {
		super(sapo);
		this.sapo = sapo;
		initialize();
		this.setAlwaysOnTop(true);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(298, 263);
		this.setTitle("Cortar figura");
		this.setModal(false);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				sapo.tbrImagem.doCancel();
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel5(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJPanel6(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setOpaque(false);
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
			jPanel.setBackground(java.awt.SystemColor.window);
			jPanel.setMaximumSize(null);
			jPanel.setMinimumSize(null);
			jPanel.setPreferredSize(null);
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel3(), null);
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel4(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			jLabel1 = new JLabel();
			jLabel1.setText("pontos");
			jLabel = new JLabel();
			jLabel.setText("Esquerda : ");
			jPanel1 = new JPanel();
			jPanel1.setBackground(java.awt.SystemColor.window);
			jPanel1.setOpaque(false);
			jPanel1.setLayout(flowLayout);
			jPanel1.add(jLabel, null);
			jPanel1.add(getJtfLeft(), null);
			jPanel1.add(jLabel1, null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.RIGHT);
			jLabel5 = new JLabel();
			jLabel5.setText("pontos");
			jLabel2 = new JLabel();
			jLabel2.setText("Direita : ");
			jPanel2 = new JPanel();
			jPanel2.setBackground(java.awt.SystemColor.window);
			jPanel2.setOpaque(false);
			jPanel2.setLayout(flowLayout1);
			jPanel2.add(jLabel2, null);
			jPanel2.add(getJtfRight(), null);
			jPanel2.add(jLabel5, null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(java.awt.FlowLayout.RIGHT);
			jLabel6 = new JLabel();
			jLabel6.setText("pontos");
			jLabel3 = new JLabel();
			jLabel3.setText("Superior : ");
			jPanel3 = new JPanel();
			jPanel3.setBackground(java.awt.SystemColor.window);
			jPanel3.setOpaque(false);
			jPanel3.setLayout(flowLayout2);
			jPanel3.add(jLabel3, null);
			jPanel3.add(getJtfTop(), null);
			jPanel3.add(jLabel6, null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(java.awt.FlowLayout.RIGHT);
			jLabel7 = new JLabel();
			jLabel7.setText("pontos");
			jLabel4 = new JLabel();
			jLabel4.setText("Inferior : ");
			jPanel4 = new JPanel();
			jPanel4.setBackground(java.awt.SystemColor.window);
			jPanel4.setOpaque(false);
			jPanel4.setLayout(flowLayout3);
			jPanel4.add(jLabel4, null);
			jPanel4.add(getJtfBottom(), null);
			jPanel4.add(jLabel7, null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setBackground(java.awt.SystemColor.window);
			jPanel5.setOpaque(false);
			jPanel5.add(getJButtonOk(), null);
			jPanel5.add(getJButtonCancel(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jtfLeft	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtfLeft() {
		if (jtfLeft == null) {
			jtfLeft = new JTextField();
			jtfLeft.setPreferredSize(new java.awt.Dimension(60,20));
			jtfLeft.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			jtfLeft.setText("");
			jtfLeft.setMinimumSize(new java.awt.Dimension(60,20));
		}
		return jtfLeft;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtfRight() {
		if (jtfRight == null) {
			jtfRight = new JTextField();
			jtfRight.setMinimumSize(new java.awt.Dimension(60,20));
			jtfRight.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			jtfRight.setPreferredSize(new java.awt.Dimension(60,20));
		}
		return jtfRight;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtfTop() {
		if (jtfTop == null) {
			jtfTop = new JTextField();
			jtfTop.setMinimumSize(new java.awt.Dimension(60,20));
			jtfTop.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			jtfTop.setPreferredSize(new java.awt.Dimension(60,20));
		}
		return jtfTop;
	}

	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtfBottom() {
		if (jtfBottom == null) {
			jtfBottom = new JTextField();
			jtfBottom.setMinimumSize(new java.awt.Dimension(60,20));
			jtfBottom.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			jtfBottom.setPreferredSize(new java.awt.Dimension(60,20));
		}
		return jtfBottom;
	}

	/**
	 * This method initializes jPanel6	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(12,12,12,12);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.ipady = 0;
			gridBagConstraints.gridx = 0;
			jPanel6 = new JPanel();
			jPanel6.setLayout(new GridBagLayout());
			jPanel6.setBackground(java.awt.SystemColor.window);
			jPanel6.setOpaque(false);
			jPanel6.add(getJPanel(), gridBagConstraints);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jButtonOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText("OK");
			jButtonOk.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			jButtonOk.setPreferredSize(new java.awt.Dimension(90,23));
			jButtonOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						int x1 = Integer.parseInt(jtfLeft.getText());
						int y1 = Integer.parseInt(jtfTop.getText());
						int x2 = Integer.parseInt(jtfRight.getText());
						int y2 = Integer.parseInt(jtfBottom.getText());
						int width  = x2 - x1;
				    	int height = y2 - y1;
				    	if ((width < sapo.frmImagem.MIN_CROP_LENGTH) || (height < sapo.frmImagem.MIN_CROP_LENGTH))
				    		doNotCrop();
				    	else {
				    		sapo.frmImagem.doCrop(x1,y1,width,height);
				    		sapo.tbrImagem.doOk();
				    	}
					} catch (Exception ex) {
						doNotCrop();
					}
				}
			});
		}
		return jButtonOk;
	}

	protected void doNotCrop() {
		sapo.showInternalFrameWithImage(sapo.numImg);
		CropDialog.this.setVisible(false);
		JOptionPane.showMessageDialog(sapo, 
				"Valores incompatíveis com ação de cortar figura.\nSelecione região de corte novamente.");
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Cancelar");
			jButtonCancel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			jButtonCancel.setPreferredSize(new java.awt.Dimension(90,23));
			jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sapo.tbrImagem.doCancel();
				}
			});
		}
		return jButtonCancel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
