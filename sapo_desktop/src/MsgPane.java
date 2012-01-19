import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.SwingConstants;

public class MsgPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4417896821391393143L;
	private JPanel jPanel = null;
	private SmoothLabel smoothLabel1 = null;
	private SmoothLabel smoothLabel2 = null;
	private JLabel jLabel2 = null;
	
	/**
	 * This is the default constructor
	 */
	public MsgPane() {
		super();
		initialize();
	}

	public void setText(String msg) {
		smoothLabel1.setText(msg);
		smoothLabel2.setText(msg);
	}
	
	public void setIcon(Icon icon) {
		smoothLabel1.setIcon(icon);
		smoothLabel2.setIcon(icon);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.ipadx = 12;
		gridBagConstraints1.ipady = 38;
		gridBagConstraints1.gridy = 0;
		this.setSize(400, 200);
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(null);
		this.setMaximumSize(null);
		this.setMinimumSize(null);
		this.add(getJPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 0;
			smoothLabel2 = new SmoothLabel();
			smoothLabel2.setBackground(Color.white);
			smoothLabel2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
			smoothLabel2.setForeground(new java.awt.Color(179,152,125));
			smoothLabel2.setBorder(null);
			smoothLabel2.setDoubleBuffered(true);
			smoothLabel2.setMaximumSize(null);
			smoothLabel2.setMinimumSize(null);
			smoothLabel2.setOpaque(false);
			smoothLabel2.setPreferredSize(null);
			smoothLabel2.setRequestFocusEnabled(false);
			smoothLabel2.setVerifyInputWhenFocusTarget(false);
			smoothLabel2.setHorizontalAlignment(SwingConstants.CENTER);
			smoothLabel2.setIcon(null);
			smoothLabel2.setIconTextGap(4);
			smoothLabel2.setText("SmoothLabel");
			smoothLabel2.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new java.awt.Insets(12,12,12,12);
			gridBagConstraints2.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("");
			jLabel2.setDoubleBuffered(true);
			jLabel2.setOpaque(false);
			jLabel2.setIcon(new ImageIcon(getClass().getResource("/res/wait.gif")));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(6,12,12,12);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			smoothLabel1 = new SmoothLabel();
			smoothLabel1.setText("SmoothLabel");
			smoothLabel1.setForeground(new java.awt.Color(217,186,163));
			smoothLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			smoothLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
			smoothLabel1.setMaximumSize(null);
			smoothLabel1.setMinimumSize(null);
			smoothLabel1.setRequestFocusEnabled(false);
			smoothLabel1.setVerifyInputWhenFocusTarget(false);
			smoothLabel1.setPreferredSize(null);
			smoothLabel1.setIcon(null);
			smoothLabel1.setBorder(null);
			smoothLabel1.setBackground(java.awt.Color.white);
			smoothLabel1.setOpaque(false);
			smoothLabel1.setIconTextGap(4);
			smoothLabel1.setDoubleBuffered(true);
			smoothLabel1.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBackground(java.awt.Color.white);
			jPanel.setOpaque(false);
			jPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			jPanel.setMinimumSize(null);
			jPanel.setDoubleBuffered(false);
			jPanel.setMaximumSize(null);
			jPanel.setPreferredSize(null);
			jPanel.add(smoothLabel1, gridBagConstraints);
			jPanel.add(jLabel2, gridBagConstraints2);
			jPanel.add(smoothLabel2, gridBagConstraints11);
		}
		return jPanel;
	}
	
	
}
