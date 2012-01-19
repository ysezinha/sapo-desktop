import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.dati.image.PontoMedido;
/*
 * MarcaPontosPanel.java
 *
 * Created on 8 de Fevereiro de 2005, 02:04
 */

/**
 *
 * @author Edison Puig Maldonado
 * @author Anderson Zanardi de Freitas
 */

public class MarcaPontosPanel extends javax.swing.JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 970129131304561264L;
	SAPO sapo;
	
	/** Creates new form MarcaPontosPanel */
	public MarcaPontosPanel(SAPO sapo) {
		this.sapo = sapo;
		initComponents(); 
                jTableMarcaPontos.getTableHeader().setReorderingAllowed(false);
                jTableMarcaPontos.getColumn("Ponto").setMinWidth(240);
	}
	
	public void atualizar() {
		if ((sapo.numImg > 0) && (sapo.jif[sapo.numImg] != null) && sapo.jif[sapo.numImg].flagImg) {
			atualizaTabela();
			atualizaPontos();
		}
	}
	
	public void atualizaPontos() {
		ArrayList valores = sapo.paciente.dados.imgData[sapo.numImg].getPontos();
		sapo.jif[sapo.numImg].jaiP.repaint();
		sapo.jif[sapo.numImg].jaiP.setPaint(false, true, false, false);
		sapo.jif[sapo.numImg].jaiP.resetMousePosition();
		float zoom = sapo.jif[sapo.numImg].zoom / 100.0F;
		for ( int i= 0; i < valores.size(); i++){
			Point pto = ((PontoMedido)valores.get(i)).p;
			Point pt2 = new Point(Math.round((int)pto.getX()*zoom), Math.round((int)pto.getY()*zoom));
			boolean apresentaTemp = ((PontoMedido)valores.get(i)).apresenta;
			sapo.jif[sapo.numImg].jaiP.addPonto(pt2, apresentaTemp);
		}
	}
	
	public void atualizaTabela() {
		limpaTabela();
		javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMarcaPontos.getModel();
		ArrayList valores = sapo.paciente.dados.imgData[sapo.numImg].getPontos();
		//double escalaX = sapo.paciente.dados.imgData[sapo.numImg].getEscalaX();
                //double escalaY = sapo.paciente.dados.imgData[sapo.numImg].getEscalaY();
                for (int i=0; i<valores.size(); i++){
			Point pto = ((PontoMedido)valores.get(i)).p;
			a.insertRow(i, new Object[]{null});
			String nomeTemp = ((PontoMedido)valores.get(i)).nome;
			boolean apresentaTemp = ((PontoMedido)valores.get(i)).apresenta;
			int pX = pto.x;//*escalaX; 
			int pY = pto.y;//*escalaY; 
			jTableMarcaPontos.setValueAt(nomeTemp, i, 0);
			jTableMarcaPontos.setValueAt(new Integer(pX), i, 1);
			jTableMarcaPontos.setValueAt(new Integer(pY), i, 2);
			jTableMarcaPontos.setValueAt(new Boolean(apresentaTemp), i, 3);
		}
                jTableMarcaPontos.getColumn("Ponto").sizeWidthToFit();
	}
	
	public void aplica() {
		javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMarcaPontos.getModel();
		int numLinha = a.getRowCount();
		if (numLinha > 0) {
			sapo.paciente.dados.imgData[sapo.numImg].limpaPontos();
			sapo.jif[sapo.numImg].jaiP.resetMousePosition();
			for(int i=0; i<numLinha; i++){
				String labelPonto = String.valueOf(jTableMarcaPontos.getValueAt(i,0)); //nome das colunas
				int x = ((Integer)jTableMarcaPontos.getValueAt(i, 1)).intValue();
				int y = ((Integer)jTableMarcaPontos.getValueAt(i, 2)).intValue();
				boolean apresentaTemp = ((Boolean)jTableMarcaPontos.getValueAt(i,3)).booleanValue();
				sapo.paciente.dados.imgData[sapo.numImg].addPoint(x, y, labelPonto, apresentaTemp);
				sapo.jif[sapo.numImg].jaiP.addPonto(new Point(x,y), apresentaTemp);
			}
			sapo.jif[sapo.numImg].flagPontos = true;
		}
	}
	
	public void atualizaPropriedadesPontos() {
		if ((sapo.numImg > 0) && (sapo.jif[sapo.numImg] != null) && sapo.jif[sapo.numImg].flagImg) {
			int numLinha = jTableMarcaPontos.getEditingRow();
			boolean numPontos = sapo.paciente.dados.imgData[sapo.numImg].pontosList.isEmpty();
			if ((numLinha >= 0)&&(!numPontos)) {
				sapo.jif[sapo.numImg].jaiP.resetMousePosition();
				String labelPonto = String.valueOf(jTableMarcaPontos.getValueAt(numLinha,0)); //nome das colunas
				sapo.paciente.dados.imgData[sapo.numImg].setPointLabel(labelPonto, numLinha);
				boolean apresentaTemp = ((Boolean)jTableMarcaPontos.getValueAt(numLinha,3)).booleanValue();
				sapo.paciente.dados.imgData[sapo.numImg].setPointApresenta(apresentaTemp, numLinha);//addPoint(x, y, labelPonto, apresentaTemp);
			}
			sapo.jif[sapo.numImg].flagPontos = true;
		}
	}
	
	public void limpaTabela() {
		javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMarcaPontos.getModel();
		int numLinha = a.getRowCount();
		if(numLinha>0)
			for(int i=(numLinha-1); i>=0; i--) a.removeRow(i);
	}
	
	public void limpaTudo() {
		limpaTabela();
		sapo.paciente.dados.imgData[sapo.numImg].limpaPontos();
		sapo.jif[sapo.numImg].flagPontos = false;
		atualizar();
	}
	
	public void apaga() {
		javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMarcaPontos.getModel();
		try{
			//int index = jTableMarcaPontos.getSelectedRow();
                        int indexs[] = jTableMarcaPontos.getSelectedRows();
			for(int i=(indexs.length-1); i>=0; i--){
                            a.removeRow(indexs[i]);
                            sapo.paciente.dados.imgData[sapo.numImg].removePoint(indexs[i]);
                        }
                        atualizar();
		} catch(ArrayIndexOutOfBoundsException e){JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");};
	}
	
	public void apagaAllPontosProtocol(String label[], boolean aviso){
		javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMarcaPontos.getModel();
		try{
			for(int i=0; i<label.length; i++)
				for(int j=0; j<jTableMarcaPontos.getRowCount(); j++)
					if(label[i].equals(getLabel(j))){
						a.removeRow(j);              
						sapo.paciente.dados.imgData[sapo.numImg].removePoint(j);
					}
			if (aviso) {
				Object[] options = {"Sim","Não"};
				int result = JOptionPane.showOptionDialog (sapo,
						"Apagar TODOS os pontos\n" +
						"marcados para esta imagem ?\n" +
						"(esta ação não pode ser desfeita)\n",
						"Confirmação", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null, options, options[0]);
				if( result == JOptionPane.YES_OPTION) limpaTudo();
			}
			else limpaTudo();
			atualizar();
		} catch(ArrayIndexOutOfBoundsException e){JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");};
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpopupMenuMarca = new javax.swing.JPopupMenu();
        jmItemApagar = new javax.swing.JMenuItem();
        jmItemLimpar = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jmenuItemOtimo = new javax.swing.JMenuItem();
        jmenuItem80 = new javax.swing.JMenuItem();
        jmenuItem100 = new javax.swing.JMenuItem();
        jmenuItem150 = new javax.swing.JMenuItem();
        jmtemZoom200 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jmItemSair = new javax.swing.JMenuItem();
        jpMarca = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jspMarca = new javax.swing.JScrollPane();
        jTableMarcaPontos = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jbtnMarcaPontosOk = new javax.swing.JButton();
        jbtnMarcarPontosDeletar = new javax.swing.JButton();
        jbMClear = new javax.swing.JButton();

        jmItemApagar.setText("Apaga ponto");
        jmItemApagar.setToolTipText("Apaga este ponto");
        jmItemApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmItemApagarActionPerformed(evt);
            }
        });

        jpopupMenuMarca.add(jmItemApagar);

        jmItemLimpar.setText("Limpar");
        jmItemLimpar.setToolTipText("Apaga todos os pontos da tabela");
        jmItemLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmItemLimparActionPerformed(evt);
            }
        });

        jpopupMenuMarca.add(jmItemLimpar);

        jMenu1.setText("Zoom");
        jMenu1.setToolTipText("Aplica zoom");
        jmenuItemOtimo.setText("Ajusta");
        jmenuItemOtimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuItemOtimoActionPerformed(evt);
            }
        });

        jMenu1.add(jmenuItemOtimo);

        jmenuItem80.setText("80%");
        jmenuItem80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuItem80ActionPerformed(evt);
            }
        });

        jMenu1.add(jmenuItem80);

        jmenuItem100.setText("100%");
        jmenuItem100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuItem100ActionPerformed(evt);
            }
        });

        jMenu1.add(jmenuItem100);

        jmenuItem150.setText("150%");
        jmenuItem150.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuItem150ActionPerformed(evt);
            }
        });

        jMenu1.add(jmenuItem150);

        jmtemZoom200.setText("200%");
        jmtemZoom200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtemZoom200ActionPerformed(evt);
            }
        });

        jMenu1.add(jmtemZoom200);

        jpopupMenuMarca.add(jMenu1);

        jpopupMenuMarca.add(jSeparator1);

        jmItemSair.setText("Sair");
        jpopupMenuMarca.add(jmItemSair);

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(376, 150));
        setPreferredSize(new java.awt.Dimension(376, 150));
        jpMarca.setLayout(new java.awt.BorderLayout());

        jpMarca.setMaximumSize(new java.awt.Dimension(2147483647, 250));
        jpMarca.setMinimumSize(new java.awt.Dimension(11, 36));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jPanel20.setMinimumSize(new java.awt.Dimension(186, 35));
        jPanel20.setPreferredSize(new java.awt.Dimension(186, 35));
        jLabel6.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        jLabel6.setText("Marcar pontos para an\u00e1lise");
        jPanel20.add(jLabel6, new java.awt.GridBagConstraints());

        jpMarca.add(jPanel20, java.awt.BorderLayout.NORTH);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jspMarca.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jspMarca.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspMarca.setMaximumSize(new java.awt.Dimension(300, 70));
        jspMarca.setMinimumSize(new java.awt.Dimension(250, 70));
        jspMarca.setPreferredSize(new java.awt.Dimension(250, 200));
        jspMarca.setRequestFocusEnabled(false);
        jTableMarcaPontos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ponto", "X", "Y", "OK"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMarcaPontos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableMarcaPontos.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTableMarcaPontosPropertyChange(evt);
            }
        });

        jspMarca.setViewportView(jTableMarcaPontos);

        jPanel9.add(jspMarca, java.awt.BorderLayout.CENTER);

        jpMarca.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel19.setMinimumSize(new java.awt.Dimension(288, 40));
        jPanel19.setPreferredSize(new java.awt.Dimension(288, 35));
        jbtnMarcaPontosOk.setFont(getFont());
        jbtnMarcaPontosOk.setText("OK");
        jbtnMarcaPontosOk.setToolTipText("Escreve pontos na base de dados do projeto e encerra esta opera\u00e7\u00e3o");
        jbtnMarcaPontosOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnMarcaPontosOk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnMarcaPontosOk.setMaximumSize(null);
        jbtnMarcaPontosOk.setMinimumSize(new java.awt.Dimension(75, 23));
        jbtnMarcaPontosOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jbtnMarcaPontosOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMarcaPontosOkActionPerformed(evt);
            }
        });

        jPanel19.add(jbtnMarcaPontosOk);

        jbtnMarcarPontosDeletar.setFont(getFont());
        jbtnMarcarPontosDeletar.setText("Apagar");
        jbtnMarcarPontosDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnMarcarPontosDeletar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnMarcarPontosDeletar.setMaximumSize(null);
        jbtnMarcarPontosDeletar.setMinimumSize(new java.awt.Dimension(75, 23));
        jbtnMarcarPontosDeletar.setPreferredSize(new java.awt.Dimension(75, 23));
        jbtnMarcarPontosDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMarcarPontosDeletarActionPerformed(evt);
            }
        });

        jPanel19.add(jbtnMarcarPontosDeletar);

        jbMClear.setFont(getFont());
        jbMClear.setText("Limpar");
        jbMClear.setToolTipText("Apaga todos os pontos da tabela");
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

        jPanel19.add(jbMClear);

        jpMarca.add(jPanel19, java.awt.BorderLayout.SOUTH);

        add(jpMarca, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void jmenuItemOtimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuItemOtimoActionPerformed
        sapo.restauraZoom();
        sapo.mostraZoom(sapo.jpMarca, "MarcaPontos");
    }//GEN-LAST:event_jmenuItemOtimoActionPerformed

    private void jmtemZoom200ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtemZoom200ActionPerformed
        sapo.jif[sapo.numImg].zoom = 200;
        if (sapo.jif[sapo.numImg].flagImg)
            sapo.showInternalFrameWithImage(sapo.numImg);
        sapo.mostraZoom(sapo.jpMarca, "MarcaPontos");
    }//GEN-LAST:event_jmtemZoom200ActionPerformed

    private void jmenuItem150ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuItem150ActionPerformed
        sapo.jif[sapo.numImg].zoom = 150;
        if (sapo.jif[sapo.numImg].flagImg)
            sapo.showInternalFrameWithImage(sapo.numImg);
        sapo.mostraZoom(sapo.jpMarca, "MarcaPontos");
    }//GEN-LAST:event_jmenuItem150ActionPerformed

    private void jmenuItem100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuItem100ActionPerformed
        sapo.jif[sapo.numImg].zoom = 100;
        if (sapo.jif[sapo.numImg].flagImg)
            sapo.showInternalFrameWithImage(sapo.numImg);
        sapo.mostraZoom(sapo.jpMarca, "MarcaPontos");
    }//GEN-LAST:event_jmenuItem100ActionPerformed

    private void jmenuItem80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuItem80ActionPerformed
        sapo.jif[sapo.numImg].zoom = 80;
        if (sapo.jif[sapo.numImg].flagImg)
            sapo.showInternalFrameWithImage(sapo.numImg);
        sapo.mostraZoom(sapo.jpMarca, "MarcaPontos");
    }//GEN-LAST:event_jmenuItem80ActionPerformed
	
	private void jmItemLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmItemLimparActionPerformed
		jbMClearActionPerformed(evt);
	}//GEN-LAST:event_jmItemLimparActionPerformed
	
	private void jmItemApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmItemApagarActionPerformed
		int index = sapo.marca.getIndex();
		if(index!=-1){
			sapo.jpMarca.selectRow(index);
			sapo.jpMarca.apaga();
		}
	}//GEN-LAST:event_jmItemApagarActionPerformed
	
	private void jTableMarcaPontosPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTableMarcaPontosPropertyChange
		atualizaPropriedadesPontos();
		atualizar();
	}//GEN-LAST:event_jTableMarcaPontosPropertyChange
		
	private void jbtnMarcaPontosOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMarcaPontosOkActionPerformed
            sapo.restauraZoom();
            aplica();
            sapo.restauraInternalFrameOriginal();
            if (sapo.paciente.dados.imgData[sapo.numImg].getPontos().size() == 0) sapo.jif[sapo.numImg].flagPontos = false;
            sapo.paciente.escreveDB();
            sapo.paciente.salvarAlteracoes = false;
	}//GEN-LAST:event_jbtnMarcaPontosOkActionPerformed
	
	private void jbtnMarcarPontosDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMarcarPontosDeletarActionPerformed
		apaga();
	}//GEN-LAST:event_jbtnMarcarPontosDeletarActionPerformed
	
	private void jbMClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMClearActionPerformed
		Object[] options = {"Sim","Não"};
		int result = JOptionPane.showOptionDialog (sapo,
				"Apagar TODOS os pontos\n" +
				"marcados para esta imagem ?\n" +
				"(esta ação não pode ser desfeita)\n",
				"Confirmação", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		if( result == JOptionPane.YES_OPTION) {
			limpaTudo();
		}
	}//GEN-LAST:event_jbMClearActionPerformed
	
	public void selectRow(int row){
		if(jTableMarcaPontos.getRowCount()>=row)
			jTableMarcaPontos.setRowSelectionInterval(row, row);
	}
	
	public String getLabel(int row){
		javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)jTableMarcaPontos.getModel();
		String nome = (String)a.getValueAt(row,0);
		return nome;
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTable jTableMarcaPontos;
    private javax.swing.JButton jbMClear;
    private javax.swing.JButton jbtnMarcaPontosOk;
    private javax.swing.JButton jbtnMarcarPontosDeletar;
    public javax.swing.JMenuItem jmItemApagar;
    private javax.swing.JMenuItem jmItemLimpar;
    private javax.swing.JMenuItem jmItemSair;
    private javax.swing.JMenuItem jmenuItem100;
    private javax.swing.JMenuItem jmenuItem150;
    private javax.swing.JMenuItem jmenuItem80;
    private javax.swing.JMenuItem jmenuItemOtimo;
    private javax.swing.JMenuItem jmtemZoom200;
    private javax.swing.JPanel jpMarca;
    public javax.swing.JPopupMenu jpopupMenuMarca;
    private javax.swing.JScrollPane jspMarca;
    // End of variables declaration//GEN-END:variables
	
}
