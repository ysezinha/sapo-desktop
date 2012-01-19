import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import com.dati.image.AnguloMedido;

/*
 * AngulosPanel.java
 *
 * Created on 8 de Fevereiro de 2005, 02:39
 */

/**
 *
 * @author Edison Puig Maldonado
 */

public class MedeAngulosPanel extends javax.swing.JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4581110427027348992L;
	SAPO sapo;
    //int anguloTipo = 0;
    //Point ptAngulos[] = new Point[5];// #ev 29jun
    int numImg;
    
    /** Creates new form AngulosPanel */
    public MedeAngulosPanel(SAPO sapo) {
        this.sapo = sapo;
        initComponents();
        jTableMedidas.getTableHeader().setReorderingAllowed(false);
    }

    public void atualizar() {
        this.numImg = sapo.numImg;
        limpaTabela();
        javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMedidas.getModel();
        java.util.ArrayList valores = sapo.paciente.dados.imgData[numImg].getAnguloMedido();
        try {
            repaint();
            for (int i=0; i<valores.size(); i++){
                a.insertRow(i, new Object[]{null});
                String nomeTemp = ((AnguloMedido)valores.get(i)).nome;
                double anguloTemp = ((AnguloMedido)valores.get(i)).angulo;
                jTableMedidas.setValueAt(nomeTemp, i, 0);
                jTableMedidas.setValueAt(new Double(anguloTemp), i, 1);
            }
            sapo.jif[numImg].jaiP.setPaint(false, false, false, false);
        } catch (Exception e) {
            sapo.errorReport.jtxtErrorReport.append(e.toString()+"\n");
            e.printStackTrace();
            sapo.error = e.getStackTrace();
            for(int k=0; k< sapo.error.length; k++)
                sapo.errorReport.jtxtErrorReport.append(sapo.error[k].toString()+"\n");
            sapo.errorReport.jtxtErrorReport.append("==========================================="+"\n");
            sapo.jdlgErrorReport.pack();
            sapo.jdlgErrorReport.setVisible(true);
        }
    }

    private void coloreButton(JButton oButton) {
        jbtnAngulo1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jbtnAngulo2.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jbtnAngulo3.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jbtnAngulo4.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        if (oButton != null) oButton.setBackground(java.awt.SystemColor.controlLtHighlight);
    }
    
    private void aplica() {
        this.numImg = sapo.numImg;
        javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMedidas.getModel();
        int numLinhas = a.getRowCount();
        for(int i=0; i<numLinhas; i++){
            String labelAngulo = String.valueOf(jTableMedidas.getValueAt(i,0)); //nome das colunas
            sapo.paciente.dados.imgData[numImg].atualizaAnguloMedido(labelAngulo, i);
        }
        sapo.paciente.escreveDB();
        sapo.paciente.salvarAlteracoes = false;
        coloreButton(null);
        sapo.restauraInternalFrameOriginal();
    }
    
    public void limpaTabela() {
        coloreButton(null);
        this.numImg = sapo.numImg;
        
        javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMedidas.getModel();
        int numLinha = a.getRowCount(); 
        if (numLinha>0)
            for (int i=(numLinha-1); i>=0; i--){
                a.removeRow(i);
                }
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        for(int i=0 ;i<5; i++)
            ptAngulos[i] = new Point(0,0);
        sapo.jif[numImg].jaiP.setptAngulos(ptAngulos); 
        jTxtAngulo.setText("");
        
        sapo.jif[numImg].jaiP.resetMousePosition();
        sapo.jif[numImg].jaiP.repaint();
        
    }
    
    private void apagar() {
   
        javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMedidas.getModel();
        try{
            int index = jTableMedidas.getSelectedRow();
            a.removeRow(index);
            sapo.paciente.dados.imgData[sapo.numImg].removeAnguloMedido(index);
        } catch(ArrayIndexOutOfBoundsException e){JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");};        
        this.numImg = sapo.numImg;
        coloreButton(null);
        sapo.jif[numImg].jaiP.setPaint(false, false, false, false);
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        sapo.jif[numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_INDEFINIDO ;
        for(int i=0 ;i<5; i++)
            ptAngulos[i] = new Point(0,0);
        sapo.jif[numImg].jaiP.setptAngulos(ptAngulos);
    }
    
    private void inserir() {
        if( jTxtAngulo.getText().length() == 0){
            JOptionPane.showMessageDialog(sapo,
                "Verifique sua digitação. \nFormato correto: \"XX,XX\" ",
                "Formato atual é inválido", JOptionPane.PLAIN_MESSAGE);
            return;
            }  
        
        this.numImg = sapo.numImg;
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        double anguloMedido = sapo.doubleLoc(jTxtAngulo.getText());
        jTxtAngulo.setText("");
        javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMedidas.getModel();
        int numLinha = a.getRowCount(); 
        a.insertRow(numLinha, new Object[]{null});
        jTableMedidas.setValueAt("Ângulo "+numLinha, numLinha, 0);
        jTableMedidas.setValueAt(new Double(anguloMedido), numLinha, 1);
        jTableMedidas.setValueAt(new Boolean(true), numLinha, 2);
        float scX, scY;
        scX = scY = sapo.jif[numImg].zoom / 100.0F;
        for(int i=0; i<ptAngulos.length; i++){
            ptAngulos[i].x = Math.round((ptAngulos[i].x)/scX);
            ptAngulos[i].y = Math.round((ptAngulos[i].y)/scX);
        }
        String nome = String.valueOf(a.getValueAt(numLinha,0));
	sapo.paciente.dados.imgData[numImg].addAnguloMedido(
            ptAngulos, nome, anguloMedido, sapo.jif[numImg].jaiP.anguloTipo);
        sapo.paciente.salvarAlteracoes = true;
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpAngulos = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jbtnAngulo1 = new javax.swing.JButton();
        jbtnAngulo2 = new javax.swing.JButton();
        jbtnAngulo3 = new javax.swing.JButton();
        jbtnAngulo4 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jchkBoxWizardAngulo = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jLblAngulo = new javax.swing.JLabel();
        jTxtAngulo = new javax.swing.JTextField();
        jbtnInserirAngulo = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableMedidas = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jbtnAnguloOk = new javax.swing.JButton();
        jbtnAnguloDeletar = new javax.swing.JButton();
        jbMClear = new javax.swing.JButton();
        jbtnAnguloAjuda = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpAngulos.setLayout(new java.awt.BorderLayout());

        jpAngulos.setMaximumSize(new java.awt.Dimension(100, 100));
        jpAngulos.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel6.setPreferredSize(new java.awt.Dimension(241, 35));
        jLabel7.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Escolha o tipo de medida de \u00e2ngulo");
        jPanel6.add(jLabel7);

        jpAngulos.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel15.setLayout(new java.awt.BorderLayout());

        jbtnAngulo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/angle_h.png")));
        jbtnAngulo1.setToolTipText("mede o \u00e2ngulo entre uma reta e a defini\u00e7\u00e3o de horizontal");
        jbtnAngulo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAngulo1ActionPerformed(evt);
            }
        });

        jPanel23.add(jbtnAngulo1);

        jbtnAngulo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/angle_v.png")));
        jbtnAngulo2.setToolTipText("mede o \u00e2ngulo entre uma reta e a defini\u00e7\u00e3o de vertical");
        jbtnAngulo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAngulo2ActionPerformed(evt);
            }
        });

        jPanel23.add(jbtnAngulo2);

        jbtnAngulo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/angle_3p.png")));
        jbtnAngulo3.setToolTipText("mede o \u00e2ngulo entre duas retas com um ponto em comum (\u00e2ngulo entre tr\u00eas pontos)");
        jbtnAngulo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAngulo3ActionPerformed(evt);
            }
        });

        jPanel23.add(jbtnAngulo3);

        jbtnAngulo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/angle_4p.png")));
        jbtnAngulo4.setToolTipText("mede o \u00e2ngulo entre duas retas sem pontos em comum (\u00e2ngulo entre quatro pontos)");
        jbtnAngulo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAngulo4ActionPerformed(evt);
            }
        });

        jPanel23.add(jbtnAngulo4);

        jPanel15.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel24.setLayout(new java.awt.GridLayout(2, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Na figura, clique com o bot\u00e3o direito para medir o \u00e2ngulo complementar");
        jPanel24.add(jLabel1);

        jchkBoxWizardAngulo.setText("passo a passo");
        jchkBoxWizardAngulo.setEnabled(false);
        jchkBoxWizardAngulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel24.add(jchkBoxWizardAngulo);

        jPanel15.add(jPanel24, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel15, java.awt.BorderLayout.CENTER);

        jLblAngulo.setText("\u00c2ngulo:");
        jPanel16.add(jLblAngulo);

        jTxtAngulo.setColumns(6);
        jTxtAngulo.setEditable(false);
        jTxtAngulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtAngulo.setText("                      ");
        jPanel16.add(jTxtAngulo);

        jbtnInserirAngulo.setText("Inserir");
        jbtnInserirAngulo.setToolTipText("Insere a medida na tabela");
        jbtnInserirAngulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnInserirAnguloActionPerformed(evt);
            }
        });

        jPanel16.add(jbtnInserirAngulo);

        jPanel1.add(jPanel16, java.awt.BorderLayout.SOUTH);

        jPanel8.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel17.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane3.setAutoscrolls(true);
        jScrollPane3.setMinimumSize(new java.awt.Dimension(100, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(150, 150));
        jTableMedidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medida", "Ângulo (graus)", "Apresentar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMedidas.setToolTipText("A primeira coluna \u00e9 edit\u00e1vel, escolha um nome para sua medida");
        jScrollPane3.setViewportView(jTableMedidas);

        jPanel17.add(jScrollPane3);

        jPanel8.add(jPanel17, java.awt.BorderLayout.CENTER);

        jpAngulos.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel7.setMinimumSize(new java.awt.Dimension(288, 40));
        jPanel7.setPreferredSize(new java.awt.Dimension(288, 35));
        jbtnAnguloOk.setFont(getFont());
        jbtnAnguloOk.setText("Ok");
        jbtnAnguloOk.setToolTipText("Escreve tabela na base de dados do projeto e encerra esta opera\u00e7\u00e3o");
        jbtnAnguloOk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnAnguloOk.setMaximumSize(null);
        jbtnAnguloOk.setMinimumSize(new java.awt.Dimension(75, 23));
        jbtnAnguloOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jbtnAnguloOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAnguloOkActionPerformed(evt);
            }
        });

        jPanel7.add(jbtnAnguloOk);

        jbtnAnguloDeletar.setFont(getFont());
        jbtnAnguloDeletar.setText("Apagar");
        jbtnAnguloDeletar.setToolTipText("Apaga uma linha da tabela");
        jbtnAnguloDeletar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnAnguloDeletar.setMaximumSize(null);
        jbtnAnguloDeletar.setMinimumSize(new java.awt.Dimension(75, 23));
        jbtnAnguloDeletar.setPreferredSize(new java.awt.Dimension(75, 23));
        jbtnAnguloDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAnguloDeletarActionPerformed(evt);
            }
        });

        jPanel7.add(jbtnAnguloDeletar);

        jbMClear.setFont(getFont());
        jbMClear.setText("Limpar");
        jbMClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbMClear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbMClear.setMaximumSize(null);
        jbMClear.setMinimumSize(new java.awt.Dimension(75, 23));
        jbMClear.setPreferredSize(new java.awt.Dimension(75, 23));
        jbMClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbMClearActionPerformed(evt);
            }
        });

        jPanel7.add(jbMClear);

        jbtnAnguloAjuda.setFont(getFont());
        jbtnAnguloAjuda.setText("Ajuda");
        jbtnAnguloAjuda.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnAnguloAjuda.setMaximumSize(null);
        jbtnAnguloAjuda.setMinimumSize(new java.awt.Dimension(75, 23));
        jbtnAnguloAjuda.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jbtnAnguloAjuda);

        jpAngulos.add(jPanel7, java.awt.BorderLayout.SOUTH);

        add(jpAngulos, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jbMClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMClearActionPerformed
         int result = JOptionPane.showConfirmDialog (sapo,
                "Apagar TODOS os ângulos\n" +
                "marcados para esta imagem ?\n" +
                "(esta ação não pode ser desfeita)\n",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if( result == JOptionPane.YES_OPTION) {
             limpaTabela();
             ArrayList array = sapo.paciente.dados.imgData[sapo.numImg].getAnguloMedido();
             for ( ; array.size() > 0 ; )
                sapo.paciente.dados.imgData[sapo.numImg].removeAnguloMedido(0);
        }
    }//GEN-LAST:event_jbMClearActionPerformed

    private void jbtnAngulo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAngulo4ActionPerformed
        this.numImg = sapo.numImg;
        coloreButton(jbtnAngulo4);
        sapo.jif[numImg].jaiP.setPaint(false, false, false, false);
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        sapo.jif[numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_QUATRO_PONTOS;
        for(int i=0 ;i<5; i++)
            ptAngulos[i] = new Point(0,0);
        sapo.jif[numImg].jaiP.setptAngulos(ptAngulos);
        jTxtAngulo.setText("");
        sapo.jif[numImg].jaiP.repaint();
    }//GEN-LAST:event_jbtnAngulo4ActionPerformed

    private void jbtnAngulo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAngulo3ActionPerformed
        this.numImg = sapo.numImg;
        coloreButton(jbtnAngulo3);
        sapo.jif[numImg].jaiP.setPaint(false, false, false, false);
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        sapo.jif[numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_TRES_PONTOS ;
        for(int i=0 ;i<5; i++)
            ptAngulos[i] = new Point(0,0);
        sapo.jif[numImg].jaiP.setptAngulos(ptAngulos);
        jTxtAngulo.setText("");
        sapo.jif[numImg].jaiP.repaint();
    }//GEN-LAST:event_jbtnAngulo3ActionPerformed

    private void jbtnAngulo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAngulo2ActionPerformed
        this.numImg = sapo.numImg;
        coloreButton(jbtnAngulo2);
        sapo.jif[numImg].jaiP.setPaint(false, false, false, false);
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        sapo.jif[numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_VERTICAL;
        for(int i=0 ;i<5; i++)
            ptAngulos[i] = new Point(0,0);
        sapo.jif[numImg].jaiP.setptAngulos(ptAngulos);
        jTxtAngulo.setText("");
        sapo.jif[numImg].jaiP.repaint();
    }//GEN-LAST:event_jbtnAngulo2ActionPerformed

    private void jbtnAngulo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAngulo1ActionPerformed
        this.numImg = sapo.numImg;
        coloreButton(jbtnAngulo1);
        sapo.jif[numImg].jaiP.setPaint(false, false, false, false);
        Point[] ptAngulos = sapo.jif[numImg].jaiP.getptAngulos();
        sapo.jif[numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_HORIZONTAL;
        for(int i=0 ;i<5; i++)
            ptAngulos[i] = new Point(0,0);
        sapo.jif[numImg].jaiP.setptAngulos(ptAngulos);
        jTxtAngulo.setText("");
        sapo.jif[numImg].jaiP.repaint();
    }//GEN-LAST:event_jbtnAngulo1ActionPerformed

    private void jbtnInserirAnguloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnInserirAnguloActionPerformed
        inserir();
    }//GEN-LAST:event_jbtnInserirAnguloActionPerformed

    private void jbtnAnguloDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAnguloDeletarActionPerformed
        apagar();
    }//GEN-LAST:event_jbtnAnguloDeletarActionPerformed

    private void jbtnAnguloOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAnguloOkActionPerformed
        sapo.restauraZoom();
        aplica();
    }//GEN-LAST:event_jbtnAnguloOkActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLblAngulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTable jTableMedidas;
    javax.swing.JTextField jTxtAngulo;
    private javax.swing.JButton jbMClear;
    private javax.swing.JButton jbtnAngulo1;
    private javax.swing.JButton jbtnAngulo2;
    private javax.swing.JButton jbtnAngulo3;
    private javax.swing.JButton jbtnAngulo4;
    private javax.swing.JButton jbtnAnguloAjuda;
    private javax.swing.JButton jbtnAnguloDeletar;
    private javax.swing.JButton jbtnAnguloOk;
    private javax.swing.JButton jbtnInserirAngulo;
    public javax.swing.JCheckBox jchkBoxWizardAngulo;
    private javax.swing.JPanel jpAngulos;
    // End of variables declaration//GEN-END:variables
    
}
