import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.hsqldb.util.TableSorter;
import org.jdesktop.swingworker.SwingWorker;

import com.dati.data.DBConnection;
import com.dati.util.DataFormat;
import com.dati.util.DataHora;

/*
 * OpenFromDB.java
 *
 * Created on 9 de Fevereiro de 2005, 12:46
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

public class ProjectManager extends javax.swing.JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1103157700019857793L;
	private SAPO sapo;
    private DBConnection db;
    ResultSet rs;
    TableSorter sorter;
    javax.swing.table.DefaultTableModel a;
    MsgPane waitMsg = new MsgPane();
    
    /** Creates new form OpenFromDB */
    public ProjectManager(SAPO sapo) {
        this.sapo = sapo;
        this.db = sapo.db;
        initComponents();
        a = (javax.swing.table.DefaultTableModel)jt.getModel();
        sorter = new TableSorter(a);
        jt.setModel(sorter);
        sorter.setTableHeader(jt.getTableHeader());
        jt.getTableHeader().setToolTipText("Clique para ordenar");
        mostraRows();
        jt.requestFocusInWindow();
    }
    
    private void openProject(boolean restaura) {
        int row = -1;
        try{
            row = jt.getSelectedRow();
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");
        }
        if (row != -1) {
            long nID = ((Long)jt.getValueAt(row, 0)).longValue();
            if (sapo.disponivel) 
            	sapo.openFromDB(nID,restaura);
        }
    }
    
    private void deleteProject() {
        int row = -1;
        try{
            row = jt.getSelectedRow();
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");
        }
        if (row != -1) {
            long nID = ((Long)jt.getValueAt(row, 0)).longValue();
            sapo.deleteProjectInDB(nID);
        }
        mostraRows();
    }
    
    private void cleanProject() {
        sapo.cleanAllInDB();
        mostraRows();
    }
    
    public void limpaTabela() {
        int numLinha = jt.getRowCount(); 
        if (numLinha>0)
            for (int i=(numLinha-1); i>=0; i--)
                a.removeRow(i);
    }
    
    private void mostraRows() {
    	limpaTabela();
        rs = db.executeQuery("SELECT * FROM dpaciente");
        String status;
        int i=0;
        try {
            while (rs.next()) {
                int nl = jt.getRowCount(); 
                String img = " " + String.valueOf(getNumImagens(rs.getLong("nid")));
                String pts = " " + String.valueOf(getNumPontos(rs.getLong("nid")));
                String ang = " " + String.valueOf(getNumAngulos(rs.getLong("nid")));
                String dis = " " + String.valueOf(getNumDistancias(rs.getLong("nid")));
                a.insertRow(nl, new Object[]{null});
                jt.setValueAt(new Long(rs.getLong("nid")), i, 0);
                jt.setValueAt(" " + rs.getString("nome"), i, 1);
                jt.setValueAt(" " + rs.getString("identidade"), i, 2);
                jt.setValueAt(" " + DataHora.getSQLDate(rs.getTimestamp("dataalteracao")), i, 3);
                jt.setValueAt(img, i, 4);
                jt.setValueAt(pts, i, 5);
                jt.setValueAt(ang, i, 6);
                jt.setValueAt(dis, i, 7);
                if (rs.getBoolean("publicado")) 
                	status = " publicado";
                else 
                	status = " ";
                jt.setValueAt(status, i, 8);
                i++;
            }
        } catch(SQLException sqle) {
            System.out.println("Exceção SQL : "+ sqle.getMessage());
        }
        for (int c=0; c < jt.getColumnCount(); c++) {
            DataFormat.packColumn(jt, c, 2);
        }
    }
    
    private Long getNumImagens(long nIDPaciente) {
        DBConnection.PreparaInsereEnvia pie;
        String sqlPreparado = "SELECT * FROM imagedata WHERE nidpaciente=?";
        pie = db.getPreparaInsereEnvia(sqlPreparado);
        pie.setSQL(1,nIDPaciente);
        ResultSet rs2 = pie.executaQuery();
        int i=0;
        try {
            while (rs2.next()) i++;
            return new Long(i);
        } catch(SQLException sqle) {
            System.out.println("Exceção SQL : "+ sqle.getMessage());
            return new Long(i);
        }
    }
    
    private Long getNumPontos(long nIDPaciente) {
        DBConnection.PreparaInsereEnvia pie;
        String sqlPreparado = "SELECT * FROM pontomedido WHERE nidpaciente=?";
        pie = db.getPreparaInsereEnvia(sqlPreparado);
        pie.setSQL(1,nIDPaciente);
        ResultSet rs3 = pie.executaQuery();
        int i=0;
        try {
            while (rs3.next()) i++;
            return new Long(i);
        } catch(SQLException sqle) {
            System.out.println("Exceção SQL : "+ sqle.getMessage());
            return new Long(i);
        }
    }
    
    private Long getNumAngulos(long nIDPaciente) {
        DBConnection.PreparaInsereEnvia pie;
        String sqlPreparado = "SELECT * FROM angulomedido WHERE nidpaciente=?";
        pie = db.getPreparaInsereEnvia(sqlPreparado);
        pie.setSQL(1,nIDPaciente);
        ResultSet rs3 = pie.executaQuery();
        int i=0;
        try {
            while (rs3.next()) i++;
            return new Long(i);
        } catch(SQLException sqle) {
            System.out.println("Exceção SQL : "+ sqle.getMessage());
            return new Long(i);
        }
    }
    
    private Long getNumDistancias(long nIDPaciente) {
        DBConnection.PreparaInsereEnvia pie;
        String sqlPreparado = "SELECT * FROM distanciamedida WHERE nidpaciente=?";
        pie = db.getPreparaInsereEnvia(sqlPreparado);
        pie.setSQL(1,nIDPaciente);
        ResultSet rs3 = pie.executaQuery();
        int i=0;
        try {
            while (rs3.next()) i++;
            return new Long(i);
        } catch(SQLException sqle) {
            System.out.println("Exceção SQL : "+ sqle.getMessage());
            return new Long(i);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jt = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbtnAbrir = new javax.swing.JButton();
        jbtnPublica = new javax.swing.JButton();
        jbtnArquivo = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnApagar = new javax.swing.JButton();
        jbtnLimpar = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(50, 40));
        setPreferredSize(new java.awt.Dimension(750, 550));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setPreferredSize(new java.awt.Dimension(574, 374));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(50, 40));
        jPanel3.setPreferredSize(new java.awt.Dimension(60, 80));
        jLabel13.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14));
        jLabel13.setForeground(new java.awt.Color(84, 84, 84));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Selecione um registro de an\u00e1lise salvo na base de dados");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel13.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jLabel13.setMinimumSize(new java.awt.Dimension(50, 15));
        jPanel3.add(jLabel13, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMinimumSize(new java.awt.Dimension(50, 40));
        jPanel4.setPreferredSize(new java.awt.Dimension(50, 60));
        jCheckBox1.setText("Navega\u00e7\u00e3o r\u00e1pida");
        jPanel4.add(jCheckBox1, new java.awt.GridBagConstraints());

        jPanel1.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel5.setPreferredSize(new java.awt.Dimension(50, 10));
        jPanel1.add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel6.setPreferredSize(new java.awt.Dimension(50, 10));
        jPanel1.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(470, 250));
        jScrollPane1.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(350, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 150));
        jt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " nID ", " Nome ", " Identidade ", " Data ", " Img ", " Pts ", " Ang ", "Dist", " Status"
            }
        ) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 7879417560542711912L;
			Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jt.setToolTipText("Selecione uma linha para abrir o respectivo projeto");
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jt.setIntercellSpacing(new java.awt.Dimension(3, 3));
        jt.setMaximumSize(null);
        jt.setMinimumSize(new java.awt.Dimension(650, 0));
        jt.setPreferredSize(null);
        jt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jt);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setMinimumSize(new java.awt.Dimension(219, 40));
        jPanel2.setPreferredSize(new java.awt.Dimension(465, 35));
        jbtnAbrir.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/open.gif")));
        jbtnAbrir.setMnemonic('A');
        jbtnAbrir.setText("Abrir Projeto");
        jbtnAbrir.setToolTipText("Abre projeto selecionado a partir da base de dados do programa");
        jbtnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAbrirActionPerformed(evt);
            }
        });

        jPanel2.add(jbtnAbrir);

        jbtnPublica.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtnPublica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/publish-to2.gif")));
        jbtnPublica.setMnemonic('P');
        jbtnPublica.setText("Publicar An\u00e1lise");
        jbtnPublica.setToolTipText("Publica an\u00e1lise para contribuir com a base de dados nacional");
        jbtnPublica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPublicaActionPerformed(evt);
            }
        });

        jPanel2.add(jbtnPublica);

        jbtnArquivo.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtnArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/save.gif")));
        jbtnArquivo.setMnemonic('E');
        jbtnArquivo.setText("Exportar");
        jbtnArquivo.setToolTipText("Salva projeto em arquivo texto no disco");
        jbtnArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnArquivoActionPerformed(evt);
            }
        });

        jPanel2.add(jbtnArquivo);

        jPanel8.add(jPanel2);

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jPanel9.setMinimumSize(new java.awt.Dimension(219, 40));
        jPanel9.setPreferredSize(new java.awt.Dimension(465, 35));
        jbtnNew.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/new.gif")));
        jbtnNew.setMnemonic('N');
        jbtnNew.setText("Novo Projeto");
        jbtnNew.setToolTipText("Criar novo projeto de an\u00e1lise");
        jbtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewActionPerformed(evt);
            }
        });

        jPanel9.add(jbtnNew);

        jbtnApagar.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtnApagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/delete-file.gif")));
        jbtnApagar.setMnemonic('R');
        jbtnApagar.setText("Remover Projeto");
        jbtnApagar.setToolTipText("Apaga projeto selecionado da base de dados do programa (ATEN\u00c7\u00c3O)");
        jbtnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnApagarActionPerformed(evt);
            }
        });

        jPanel9.add(jbtnApagar);

        jbtnLimpar.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/delete.gif")));
        jbtnLimpar.setMnemonic('L');
        jbtnLimpar.setText("Limpar Tudo");
        jbtnLimpar.setToolTipText("Apaga TODOS os projetos da base de dados do programa (ATEN\u00c7\u00c3O)");
        jbtnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLimparActionPerformed(evt);
            }
        });

        jPanel9.add(jbtnLimpar);

        jPanel8.add(jPanel9);

        add(jPanel8, java.awt.BorderLayout.SOUTH);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
    	sapo.jDialogSAPO.setVisible(false);
    	sapo.newProject();
    }//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLimparActionPerformed
    	Object[] options = {"Sim","Não"};
    	int result = JOptionPane.showOptionDialog (sapo,
                "Apagar TODOS os registros de análises\n" +
                "da base de dados completa do programa ?\n" +
                "(esta ação não pode ser desfeita)\n",
                "Confirmação", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
			    null, options, options[0]);
        if( result == JOptionPane.YES_OPTION) {
            cleanProject();
            repaint();
        }
    }//GEN-LAST:event_jbtnLimparActionPerformed

    private void jbtnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnApagarActionPerformed
        int row = jt.getSelectedRow();
        if (row == -1) JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");
        else {
        	long nID = ((Long)jt.getValueAt(row,0)).longValue(); 
        	Object[] options = {"Sim","Não"};
            int result = JOptionPane.showOptionDialog (sapo,
                    "Apagar registro de análise #" + nID + "\n" +
                    "da base de dados do programa ?\n" +
                    "(esta ação não pode ser desfeita)\n",
                    "Confirmação", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
				    null, options, options[0]);
            if( result == JOptionPane.YES_OPTION) {
                deleteProject();
                repaint();
            }
        }
    }//GEN-LAST:event_jbtnApagarActionPerformed

    private void jtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtMouseClicked
         if (evt.getClickCount() == 2) {
             if (!jCheckBox1.isSelected()) sapo.jDialogSAPO.setVisible(false);
             openProject(true);
             if (!jCheckBox1.isSelected()) sapo.jDialogSAPO.close();
         }
    }//GEN-LAST:event_jtMouseClicked

    private void jbtnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAbrirActionPerformed
        int row = jt.getSelectedRow();
        if (row == -1) JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");
        else {
            sapo.jDialogSAPO.setVisible(false);
            openProject(true);
            sapo.jDialogSAPO.close();
        }
    }//GEN-LAST:event_jbtnAbrirActionPerformed

    private void jbtnPublicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPublicaActionPerformed
        int row = jt.getSelectedRow();
        if (row == -1) JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");
        else {
        	long nID = ((Long)jt.getValueAt(row,0)).longValue(); 
        	if (((String)jt.getValueAt(row,8)).equalsIgnoreCase(" publicado"))
        		JOptionPane.showMessageDialog(sapo,"Esta análise já foi publicada.\n" +
        				"Não é possível publicar novamente na base de dados nacional.","Mensagem",JOptionPane.WARNING_MESSAGE);
        	else if ((sapo.user.dados.username.length() == 0) || sapo.user.dados.username.equalsIgnoreCase("sapo") ||
        			 (sapo.user.dados.senha.length() == 0) || sapo.user.dados.senha.equalsIgnoreCase("sapo"))
				JOptionPane.showMessageDialog(sapo,
						"É necessário obter um nome de usuário e senha de acesso p/\n" +
						"publicar na base de dados nacional.  Entre em contato com a\n" +
						"coordenação do projeto para solicitar a autorização. Depois,\n" +
						"configure no menu  \"Ferramentas\",  item \"Configurações do\n" +
						"Programa\", na aba \"Segurança\"","Autorização Necessária",JOptionPane.ERROR_MESSAGE);
        	else {
        		Object[] options = {"Sim","Não"};
        		int result = JOptionPane.showOptionDialog (sapo,
        				"Publicar registro de análise #" + nID + "\n" +
        				"para a base de dados nacional ?\n" +
        				"(esta ação não pode ser desfeita/refeita)\n" +
        				"UMA CONEXÃO À INTERNET DEVE ESTAR ATIVA AGORA    ",
        				"Confirmação", JOptionPane.YES_NO_OPTION,
        				JOptionPane.QUESTION_MESSAGE,
        				null, options, options[0]);
        		if( result == JOptionPane.YES_OPTION) {
        			SwingWorker aWorker = new SwingWorker() {
        				public Object doInBackground() {
                			if (!sapo.publica()) {
                				sapo.clearGlassPane(sapo.jDialogSAPO);
                				JOptionPane.showMessageDialog(sapo,"Não foi possível publicar na base de dados nacional.");
                			}
                			else {
                				sapo.clearGlassPane(sapo.jDialogSAPO);
                				JOptionPane.showMessageDialog(sapo,"A análise foi publicada na base de dados nacional com sucesso!");
                			}
                			sapo.closeProject();
        					mostraRows();
        					return null;
        				}
        			};
        			sapo.jDialogSAPO.setGlassPane(waitMsg);
        			waitMsg.setText("Preparando para publicação...");
        			sapo.jDialogSAPO.getGlassPane().setVisible(true);
        			openProject(false);
        			aWorker.execute();
        		}
        	}
        }
    }//GEN-LAST:event_jbtnPublicaActionPerformed

    private void jbtnArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnArquivoActionPerformed
        int row = jt.getSelectedRow();
        if (row == -1) JOptionPane.showMessageDialog(sapo,"Nenhuma linha foi selecionada na tabela");
        else {
            openProject(false);
            sapo.salvarEmArquivo();
            sapo.closeProject();
        }
    }//GEN-LAST:event_jbtnArquivoActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnAbrir;
    private javax.swing.JButton jbtnApagar;
    private javax.swing.JButton jbtnArquivo;
    private javax.swing.JButton jbtnLimpar;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnPublica;
    private javax.swing.JTable jt;
    // End of variables declaration//GEN-END:variables
    
}
