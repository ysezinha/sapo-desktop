/*
 * UIPaciente.java
 *
 * Created on 18 de Dezembro de 2004, 22:04
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

import com.dati.util.DataFormat;
import com.dati.util.DataHora;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;

class UIPaciente extends BaseInternalFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7356783060135555617L;
	public Paciente paciente;
	public DadosPaciente dados;
	Locale locale = new Locale("pt","BR");
	NumberFormat numFormat = NumberFormat.getInstance(locale);
	NumberFormat numFormat2 = NumberFormat.getInstance(locale);
	
	javax.swing.JTextField jtfInfo4 = new javax.swing.JTextField(""); // necessário por questões de compatibilidade com versão anterior : NÃO RETIRE
	
	UIPaciente(Paciente paciente){
		super("SAPO - Dados Associados");
		this.paciente = paciente;
		this.dados = paciente.dados;
		initComponents();
		setVisible(true);
		((DecimalFormat)numFormat).applyPattern("00000000");
		((DecimalFormat)numFormat2).applyPattern("#0.00#");
	}
	
	protected void entraDados() {
		dados.nome      = jtfNome.getText();
		String cepcom   = jtfCEPCom.getText();
		dados.CEPCom    = (cepcom != "")?DataFormat.parseLong(cepcom):0;
		String cepres   = jtfCEPRes.getText();
		dados.CEPRes    = (cepcom != "")?DataFormat.parseLong(cepres):0;
		dados.cidadeCom = jtfCidadeCom.getText() ;
		dados.cidadeRes = jtfCidadeRes.getText() ;
		dados.convenio  = jtfConvenio.getText() ;
		String numconv  = jtfConvenioNumero.getText();
		dados.numConvenio = (numconv != "")?DataFormat.parseLong(numconv):0;
		dados.email       = jtfEmail.getText() ;
		dados.emergencia  = jtfEmergencia.getText() ;
		dados.enderecoCom = jtfEndCom.getText() ;
		dados.enderecoRes = jtfEndRes.getText() ;
		dados.identidade  = jtfIdentidade.getText() ;
		String cpf        = jtfCPF.getText();
		dados.cpf         = (cpf != "")?DataFormat.parseLong(cpf):0; 
		dados.IM1         = jtfInfo1.getText() ; // altura em m
		dados.IM2         = jtfInfo2.getText() ; // massa em kg
		dados.IM3         = jtfInfo3.getText() ; // imc em kg / m^2
		dados.IM4         = jtfInfo4.getText() ; // preferência lateral ou "handedness" (destro, canhoto ou ambidestro)
		dados.instrucao    = jtfInstrucao.getText() ;
		dados.naturalidade = jtfNaturalidade.getText() ;
		dados.observacoes  = jtfObservacoes.getText() ;
		dados.profissao    = jtfProfissao.getText() ;
		dados.site         = jtfSite.getText() ;
		dados.telCel       = jtfTelCelular.getText() ;
		dados.telCom       = jtfTelCom.getText() ;
		dados.telRes       = jtfTelRes.getText() ;
		
		dados.AnteBrac = jckAnteBrac.isSelected();
		dados.txtAnteBrac = jtxtAnteBrac.getText();
		dados.cmbAnteBrac = (String)jcmbAnteBrac.getSelectedItem();
		
		dados.Brac = jckBrac.isSelected();
		dados.txtBrac = jtxtBrac.getText();
		dados.cmbBrac = (String)jcmbBrac.getSelectedItem();
		
		dados.Cabe = jckCabe.isSelected();
		dados.txtCabe = jtxtCabe.getText();
		dados.cmbCabe = (String)jcmbCabe.getSelectedItem();
		
		dados.ColCerv = jckColCerv.isSelected();
		dados.txtColCerv = jtxtColCerv.getText();
		dados.cmbColCerv = (String)jcmbColCerv.getSelectedItem();
		
		dados.ColLomb = jckColLomb.isSelected();
		dados.txtColLomb = jtxtColLomb.getText();
		dados.cmbColLomb = (String)jcmbColLomb.getSelectedItem();
		
		dados.ColTora = jckColTora.isSelected();
		dados.txtColTora = jtxtColTora.getText();
		dados.cmbColTora = (String)jcmbColTora.getSelectedItem();
		
		dados.Coto = jckCoto.isSelected();
		dados.txtCoto = jtxtCoto.getText();
		dados.cmbCoto = (String)jcmbCoto.getSelectedItem();
		
		dados.Coxa = jckCoxa.isSelected();
		dados.txtCoxa = jtxtCoxa.getText();
		dados.cmbCoxa = (String)jcmbCoxa.getSelectedItem();
		
		dados.Dedo = jckDedo.isSelected();
		dados.txtDedo = jtxtDedo.getText();
		dados.cmbDedo = (String)jcmbDedo.getSelectedItem();
		
		dados.Joel = jckJoel.isSelected();
		dados.txtJoel = jtxtJoel.getText();
		dados.cmbJoel = (String)jcmbJoel.getSelectedItem();
		
		dados.Mao = jckMao.isSelected();
		dados.txtMao = jtxtMao.getText();
		dados.cmbMao = (String)jcmbMao.getSelectedItem();
		
		dados.Ombr = jckOmbr.isSelected();
		dados.txtOmbr = jtxtOmbr.getText();
		dados.cmbOmbr = (String)jcmbOmbr.getSelectedItem();
		
		dados.Pelv = jckPelv.isSelected();
		dados.txtPelv = jtxtPelv.getText();
		dados.cmbPelv = (String)jcmbPelv.getSelectedItem();
		
		dados.Pern = jckPern.isSelected();
		dados.txtPern = jtxtPern.getText();
		dados.cmbPern = (String)jcmbPern.getSelectedItem();
		
		dados.Punh = jckPunh.isSelected();
		dados.txtPunh = jtxtPunh.getText();
		dados.cmbPunh = (String)jcmbPunh.getSelectedItem();
		
		dados.Quad = jckQuad.isSelected();
		dados.txtQuad = jtxtQuad.getText();
		dados.cmbQuad = (String)jcmbQuad.getSelectedItem();
		
		dados.Torn = jckTorn.isSelected();
		dados.txtTorn = jtxtTorn.getText();
		dados.cmbTorn = (String)jcmbTorn.getSelectedItem();
		
		dados.Pe = jckPe.isSelected();
		dados.txtPe = jtxtPe.getText();
		dados.cmbPe = (String)jcmbPe.getSelectedItem();
		
		dados.cmbDCPQ1 = (String)jcmbDCPQ1.getSelectedItem();
		dados.txtDCPQ1 = jtxtDCPQ1.getText();
		dados.cmbDCPQ2 = (String)jcmbDCPQ2.getSelectedItem();
		dados.txtDCPQ2 = jtxtDCPQ2.getText();
		dados.cmbDCPQ3 = (String)jcmbDCPQ3.getSelectedItem();
		dados.txtDCPQ3 = jtxtDCPQ3.getText();
		dados.cmbDCPQ4 = (String)jcmbDCPQ4.getSelectedItem();
		dados.txtDCPQ4 = jtxtDCPQ4.getText();
		dados.cmbDCPQ5 = (String)jcmbDCPQ5.getSelectedItem();
		dados.txtDCPQ5 = jtxtDCPQ5.getText();
		dados.cmbDCPQ6 = (String)jcmbDCPQ6.getSelectedItem();
		dados.txtDCPQ6 = jtxtDCPQ6.getText();
		dados.cmbDCPQ7 = (String)jcmbDCPQ7.getSelectedItem();
		dados.txtDCPQ7 = jtxtDCPQ7.getText();
		dados.edtPaneDCP = jedtPaneDCP.getText();
		
		try { dados.obsOutros = jepObservacoes.getText(); }
		catch (Exception e){ dados.obsOutros = ""; e.printStackTrace();}
		dados.estadoCom    = (String)jcbEstadoCom.getSelectedItem();
		dados.estadoRes	   = (String)jcbEstadoRes.getSelectedItem();
		dados.estadoCivil  = (String)jcbEstadoCivil.getSelectedItem();
		String sex         = (String)jcbSexo.getSelectedItem(); 
		String ano         = (String)jcbAnoNasc.getSelectedItem();
		String mes         = (String)jcbMesNasc.getSelectedItem(); 
		String dia         = (String)jcbDiaNasc.getSelectedItem(); 
		dados.anoNasc      = (ano!="")? DataFormat.parseLong(ano):0;
		dados.mesNasc      = (mes!="")? DataFormat.parseLong(mes):0;
		dados.diaNasc      = (dia!="")? DataFormat.parseLong(dia):0;
		dados.sexo         = (sex.toCharArray())[0];
		dados.criadopor    = paciente.sapo.user.dados.nomecompleto;
		dados.dataalteracao = DataHora.getDate();
		paciente.salvarAlteracoes = false;
	}
	
	protected void mostraDados() {
		jtfNome.setText(dados.nome);
		jtfEndRes.setText(dados.enderecoRes);
		if (dados.CEPCom != 0) jtfCEPCom.setText( numFormat.format(dados.CEPCom) );
		else jtfCEPCom.setText("");
		if (dados.CEPRes != 0) jtfCEPRes.setText( numFormat.format(dados.CEPRes) );
		else jtfCEPRes.setText("");
		jtfCidadeCom.setText( dados.cidadeCom );
		jtfCidadeRes.setText( dados.cidadeRes );
		jtfConvenio.setText( dados.convenio );
		jtfConvenioNumero.setText( Long.toString( dados.numConvenio ) );
		jtfEmail.setText( dados.email );
		jtfEmergencia.setText( dados.emergencia );
		jtfEndCom.setText( dados.enderecoCom );
		jtfIdentidade.setText( dados.identidade );
		jtfInfo1.setText( dados.IM1 );
		jtfInfo2.setText( dados.IM2 );
		jtfInfo3.setText( dados.IM3 );
		jtfInfo4.setText( dados.IM4 );
		if (dados.IM4.equals("destro"))
			this.jrbDestro.setSelected(true);
		else if (dados.IM4.equals("canhoto"))
			this.jrbCanhoto.setSelected(true);
		else if (dados.IM4.equals("ambidestro"))
			this.jrbAmbidestro.setSelected(true);
		jtfInstrucao.setText( dados.instrucao );
		jtfNaturalidade.setText( dados.naturalidade );
		jtfObservacoes.setText( dados.observacoes );
		jtfProfissao.setText( dados.profissao );
		jtfSite.setText( dados.site );
		jtfTelCelular.setText( dados.telCel );
		jtfTelCom.setText( dados.telCom );
		jtfTelRes.setText( dados.telRes );
		jepObservacoes.setText( dados.obsOutros );
		if (dados.sexo == 'F') jcbSexo.setSelectedIndex(1);
		jcbEstadoCom.setSelectedItem( dados.estadoCom );
		jcbEstadoRes.setSelectedItem( dados.estadoRes );
		jcbEstadoCivil.setSelectedItem( dados.estadoCivil );
		jcbDiaNasc.setSelectedItem( Long.toString(dados.diaNasc ));
		jcbMesNasc.setSelectedItem( Long.toString(dados.mesNasc ));
		jcbAnoNasc.setSelectedItem( Long.toString(dados.anoNasc ));
		jtfCPF.setText(Long.toString(dados.cpf));
		if (dados.nID != -1) jtfNID.setText(numFormat.format(dados.nID));
		else jtfNID.setText("não consta");
		jtfCriacao.setText(DataHora.getSQLDate(dados.datacriacao));
		jtfAlteracao.setText(DataHora.getSQLDate(dados.dataalteracao));
		
		jckAnteBrac.setSelected(dados.AnteBrac);
		jckBrac.setSelected(dados.Brac);
		jckCabe.setSelected(dados.Cabe);
		jckColCerv.setSelected(dados.ColCerv);
		jckColLomb.setSelected(dados.ColLomb);
		jckColTora.setSelected(dados.ColTora);
		jckCoto.setSelected(dados.Coto);
		jckCoxa.setSelected(dados.Coxa);
		jckDedo.setSelected(dados.Dedo);
		jckJoel.setSelected(dados.Joel);
		jckMao.setSelected(dados.Mao);
		jckOmbr.setSelected(dados.Ombr);
		jckPe.setSelected(dados.Pe);
		jckPelv.setSelected(dados.Pelv);
		jckPern.setSelected(dados.Pern);
		jckPunh.setSelected(dados.Punh);
		jckQuad.setSelected(dados.Quad);
		jckTorn.setSelected(dados.Torn);
		
		jcmbAnteBrac.setSelectedItem(dados.cmbAnteBrac);
		jcmbBrac.setSelectedItem( dados.cmbBrac);
		jcmbCabe.setSelectedItem( dados.cmbCabe);
		jcmbColCerv.setSelectedItem( dados.cmbColCerv);
		jcmbColLomb.setSelectedItem( dados.cmbColLomb);
		jcmbColTora.setSelectedItem( dados.cmbColTora);
		jcmbCoto.setSelectedItem( dados.cmbCoto);
		jcmbCoxa.setSelectedItem( dados.cmbCoxa);
		jcmbDedo.setSelectedItem( dados.cmbDedo);
		jcmbJoel.setSelectedItem( dados.cmbJoel);
		jcmbMao.setSelectedItem( dados.cmbMao);
		jcmbOmbr.setSelectedItem( dados.cmbOmbr);
		jcmbPe.setSelectedItem( dados.cmbPe);
		jcmbPelv.setSelectedItem( dados.cmbPelv);
		jcmbPern.setSelectedItem( dados.cmbPern);
		jcmbPunh.setSelectedItem( dados.cmbPunh);
		jcmbQuad.setSelectedItem( dados.cmbQuad);
		jcmbTorn.setSelectedItem( dados.cmbTorn);
		
		jtxtAnteBrac.setText( dados.txtAnteBrac);
		jtxtBrac.setText( dados.txtBrac);
		jtxtCabe.setText( dados.txtCabe);
		jtxtColCerv.setText( dados.txtColCerv);
		jtxtColLomb.setText( dados.txtColLomb);
		jtxtColTora.setText( dados.txtColTora);
		jtxtCoto.setText( dados.txtCoto);
		jtxtCoxa.setText( dados.txtCoxa);
		jtxtDedo.setText( dados.txtDedo);
		jtxtJoel.setText( dados.txtJoel);
		jtxtMao.setText( dados.txtMao);
		jtxtOmbr.setText( dados.txtOmbr);
		jtxtPe.setText( dados.txtPe);
		jtxtPelv.setText( dados.txtPelv);
		jtxtPern.setText( dados.txtPern);
		jtxtPunh.setText( dados.txtPunh);
		jtxtQuad.setText( dados.txtQuad);
		jtxtTorn.setText( dados.txtTorn);
		
		jcmbDCPQ1.setSelectedItem(dados.cmbDCPQ1);
		jtxtDCPQ1.setText(dados.txtDCPQ1);
		jcmbDCPQ2.setSelectedItem(dados.cmbDCPQ2);
		jtxtDCPQ2.setText(dados.txtDCPQ2);
		jcmbDCPQ3.setSelectedItem(dados.cmbDCPQ3);
		jtxtDCPQ3.setText(dados.txtDCPQ3);
		jcmbDCPQ4.setSelectedItem(dados.cmbDCPQ4);
		jtxtDCPQ4.setText(dados.txtDCPQ4);
		jcmbDCPQ5.setSelectedItem(dados.cmbDCPQ5);
		jtxtDCPQ5.setText(dados.txtDCPQ5);
		jcmbDCPQ6.setSelectedItem(dados.cmbDCPQ6);
		jtxtDCPQ6.setText(dados.txtDCPQ6);
		jcmbDCPQ7.setSelectedItem(dados.cmbDCPQ7);
		jtxtDCPQ7.setText(dados.txtDCPQ7);
		jedtPaneDCP.setText(dados.edtPaneDCP);
		
		calcIMC();
		
		if (dados.publicado) {
			jlStatus.setForeground(new Color(128,0,0));
			jlStatus.setText("Publicado");
		}
		
		else {
			jlStatus.setForeground(new Color(0,128,0));
			jlStatus.setText("Não publicado");
		}
		
		this.repaint();
	}
        
        private void atualiza_jtfInfo4(String handedness) {
            jtfInfo4.setText(handedness);
        }
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnGroupLateral = new javax.swing.ButtonGroup();
        jtpPaciente = new javax.swing.JTabbedPane();
        jspInfoPessoais = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jpnInfoPessoais = new javax.swing.JPanel();
        jlblNome = new javax.swing.JLabel();
        jlProfissao = new javax.swing.JLabel();
        jlbDataNasc = new javax.swing.JLabel();
        jlNaturalidade = new javax.swing.JLabel();
        jlblSexo = new javax.swing.JLabel();
        jtfNome = new javax.swing.JTextField();
        jcbDiaNasc = new javax.swing.JComboBox();
        jcbMesNasc = new javax.swing.JComboBox();
        jcbAnoNasc = new javax.swing.JComboBox();
        jcbSexo = new javax.swing.JComboBox();
        jtfProfissao = new javax.swing.JTextField();
        jtfNaturalidade = new javax.swing.JTextField();
        jlInstrucao = new javax.swing.JLabel();
        jtfInstrucao = new javax.swing.JTextField();
        jlblEstCivil = new javax.swing.JLabel();
        jcbEstadoCivil = new javax.swing.JComboBox();
        jlIdentidade = new javax.swing.JLabel();
        jtfIdentidade = new javax.swing.JTextField();
        jlCriacao = new javax.swing.JLabel();
        jtfCriacao = new javax.swing.JTextField();
        jlAlteracao = new javax.swing.JLabel();
        jtfAlteracao = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlCriacao1 = new javax.swing.JLabel();
        jtfNID = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jltxtStatus = new javax.swing.JLabel();
        jlStatus = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jbHelpPublish = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jlblInfo1 = new javax.swing.JLabel();
        jtfInfo1 = new javax.swing.JTextField();
        jlblInfo2 = new javax.swing.JLabel();
        jtfInfo2 = new javax.swing.JTextField();
        jlblInfo3 = new javax.swing.JLabel();
        jtfInfo3 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jlblInfo4 = new javax.swing.JLabel();
        jrbDestro = new javax.swing.JRadioButton();
        jPanel18 = new javax.swing.JPanel();
        jrbCanhoto = new javax.swing.JRadioButton();
        jPanel19 = new javax.swing.JPanel();
        jrbAmbidestro = new javax.swing.JRadioButton();
        jspInfoComplementares = new javax.swing.JScrollPane();
        jpnInfoComplementares = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlEndRes = new javax.swing.JLabel();
        jtfEndRes = new javax.swing.JTextField();
        jlCEPRes = new javax.swing.JLabel();
        jtfCEPRes = new javax.swing.JTextField();
        jlCidadeRes = new javax.swing.JLabel();
        jtfCidadeRes = new javax.swing.JTextField();
        jlEstadoRes = new javax.swing.JLabel();
        jcbEstadoRes = new javax.swing.JComboBox();
        jlEndCom = new javax.swing.JLabel();
        jtfEndCom = new javax.swing.JTextField();
        jlCEPCom = new javax.swing.JLabel();
        jtfCEPCom = new javax.swing.JTextField();
        jlCidadeCom = new javax.swing.JLabel();
        jtfCidadeCom = new javax.swing.JTextField();
        jlEstadoCom = new javax.swing.JLabel();
        jcbEstadoCom = new javax.swing.JComboBox();
        jlTelRes = new javax.swing.JLabel();
        jtfTelRes = new javax.swing.JTextField();
        jlTelCom = new javax.swing.JLabel();
        jtfTelCom = new javax.swing.JTextField();
        jlTelTelCelular = new javax.swing.JLabel();
        jtfTelCelular = new javax.swing.JTextField();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jlSite = new javax.swing.JLabel();
        jtfSite = new javax.swing.JTextField();
        jlCPF = new javax.swing.JLabel();
        jtfCPF = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jlEmergencia = new javax.swing.JLabel();
        jtfEmergencia = new javax.swing.JTextField();
        jlTelEmergencia = new javax.swing.JLabel();
        jtfTelEmergencia = new javax.swing.JTextField();
        jlConvenio = new javax.swing.JLabel();
        jtfConvenio = new javax.swing.JTextField();
        jlConvenioNumero = new javax.swing.JLabel();
        jtfConvenioNumero = new javax.swing.JTextField();
        jlObservacoes = new javax.swing.JLabel();
        jtfObservacoes = new javax.swing.JTextField();
        jspInfoDadoClinicoAtual = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jckCabe = new javax.swing.JCheckBox();
        jtxtCabe = new javax.swing.JTextField();
        jcmbCabe = new javax.swing.JComboBox();
        jckColCerv = new javax.swing.JCheckBox();
        jtxtColCerv = new javax.swing.JTextField();
        jcmbColCerv = new javax.swing.JComboBox();
        jckColTora = new javax.swing.JCheckBox();
        jtxtColTora = new javax.swing.JTextField();
        jcmbColTora = new javax.swing.JComboBox();
        jckColLomb = new javax.swing.JCheckBox();
        jtxtColLomb = new javax.swing.JTextField();
        jcmbColLomb = new javax.swing.JComboBox();
        jckPelv = new javax.swing.JCheckBox();
        jtxtPelv = new javax.swing.JTextField();
        jcmbPelv = new javax.swing.JComboBox();
        jckQuad = new javax.swing.JCheckBox();
        jtxtQuad = new javax.swing.JTextField();
        jcmbQuad = new javax.swing.JComboBox();
        jckCoxa = new javax.swing.JCheckBox();
        jtxtCoxa = new javax.swing.JTextField();
        jcmbCoxa = new javax.swing.JComboBox();
        jckJoel = new javax.swing.JCheckBox();
        jtxtJoel = new javax.swing.JTextField();
        jcmbJoel = new javax.swing.JComboBox();
        jckPern = new javax.swing.JCheckBox();
        jtxtPern = new javax.swing.JTextField();
        jcmbPern = new javax.swing.JComboBox();
        jckTorn = new javax.swing.JCheckBox();
        jtxtTorn = new javax.swing.JTextField();
        jcmbTorn = new javax.swing.JComboBox();
        jckPe = new javax.swing.JCheckBox();
        jtxtPe = new javax.swing.JTextField();
        jcmbPe = new javax.swing.JComboBox();
        jckOmbr = new javax.swing.JCheckBox();
        jtxtOmbr = new javax.swing.JTextField();
        jcmbOmbr = new javax.swing.JComboBox();
        jckBrac = new javax.swing.JCheckBox();
        jtxtBrac = new javax.swing.JTextField();
        jcmbBrac = new javax.swing.JComboBox();
        jckCoto = new javax.swing.JCheckBox();
        jtxtCoto = new javax.swing.JTextField();
        jcmbCoto = new javax.swing.JComboBox();
        jckAnteBrac = new javax.swing.JCheckBox();
        jtxtAnteBrac = new javax.swing.JTextField();
        jcmbAnteBrac = new javax.swing.JComboBox();
        jckPunh = new javax.swing.JCheckBox();
        jtxtPunh = new javax.swing.JTextField();
        jcmbPunh = new javax.swing.JComboBox();
        jckMao = new javax.swing.JCheckBox();
        jtxtMao = new javax.swing.JTextField();
        jcmbMao = new javax.swing.JComboBox();
        jckDedo = new javax.swing.JCheckBox();
        jtxtDedo = new javax.swing.JTextField();
        jcmbDedo = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jspInfoDadoClinicoPregresso = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jcmbDCPQ1 = new javax.swing.JComboBox();
        jtxtDCPQ1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jcmbDCPQ2 = new javax.swing.JComboBox();
        jtxtDCPQ2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jcmbDCPQ3 = new javax.swing.JComboBox();
        jtxtDCPQ3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jcmbDCPQ4 = new javax.swing.JComboBox();
        jtxtDCPQ4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jcmbDCPQ5 = new javax.swing.JComboBox();
        jtxtDCPQ5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jcmbDCPQ6 = new javax.swing.JComboBox();
        jtxtDCPQ6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jcmbDCPQ7 = new javax.swing.JComboBox();
        jtxtDCPQ7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jedtPaneDCP = new javax.swing.JEditorPane();
        jspInfoOutros = new javax.swing.JScrollPane();
        jpnInfoOutros = new javax.swing.JPanel();
        jlblObs = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jepObservacoes = new javax.swing.JEditorPane();
        jPanel3 = new javax.swing.JPanel();
        jbtn1 = new javax.swing.JButton();
        jbtn2 = new javax.swing.JButton();
        jbtn3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("SAPO - Dados da An\u00e1lise");
        setDesktopIcon(getDesktopIcon());
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("res/frog.gif")));
        try {
            setIcon(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(750, 470));
        setPreferredSize(new java.awt.Dimension(750, 470));
        jtpPaciente.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jtpPaciente.setFocusable(false);
        jtpPaciente.setPreferredSize(new java.awt.Dimension(535, 400));
        jspInfoPessoais.setAlignmentX(1.0F);
        jspInfoPessoais.setAlignmentY(1.0F);
        jspInfoPessoais.setMinimumSize(new java.awt.Dimension(510, 340));
        jspInfoPessoais.setPreferredSize(null);
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jPanel9.setPreferredSize(null);
        jPanel20.setMinimumSize(new java.awt.Dimension(15, 10));
        jPanel20.setPreferredSize(new java.awt.Dimension(15, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel9.add(jPanel20, gridBagConstraints);

        jpnInfoPessoais.setLayout(new java.awt.GridBagLayout());

        jpnInfoPessoais.setMinimumSize(new java.awt.Dimension(600, 250));
        jlblNome.setFont(getFont());
        jlblNome.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlblNome.setText("Nome:");
        jlblNome.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblNome.setMinimumSize(new java.awt.Dimension(65, 14));
        jlblNome.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlblNome, gridBagConstraints);

        jlProfissao.setFont(getFont());
        jlProfissao.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlProfissao.setText("Profiss\u00e3o:");
        jlProfissao.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlProfissao.setMinimumSize(new java.awt.Dimension(65, 14));
        jlProfissao.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlProfissao, gridBagConstraints);

        jlbDataNasc.setFont(getFont());
        jlbDataNasc.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlbDataNasc.setText("Data Nasc.:");
        jlbDataNasc.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlbDataNasc.setMinimumSize(new java.awt.Dimension(65, 14));
        jlbDataNasc.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlbDataNasc, gridBagConstraints);

        jlNaturalidade.setFont(getFont());
        jlNaturalidade.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlNaturalidade.setText("Naturalidade:");
        jlNaturalidade.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlNaturalidade.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlNaturalidade, gridBagConstraints);

        jlblSexo.setFont(getFont());
        jlblSexo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlblSexo.setText("Sexo:");
        jlblSexo.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblSexo.setMinimumSize(new java.awt.Dimension(40, 14));
        jlblSexo.setPreferredSize(new java.awt.Dimension(85, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlblSexo, gridBagConstraints);

        jtfNome.setFont(getFont());
        jtfNome.setMinimumSize(new java.awt.Dimension(400, 20));
        jtfNome.setPreferredSize(new java.awt.Dimension(400, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfNome, gridBagConstraints);

        jcbDiaNasc.setFont(getFont());
        jcbDiaNasc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jcbDiaNasc.setMinimumSize(new java.awt.Dimension(40, 20));
        jcbDiaNasc.setPreferredSize(new java.awt.Dimension(55, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jcbDiaNasc, gridBagConstraints);

        jcbMesNasc.setFont(getFont());
        jcbMesNasc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        jcbMesNasc.setMinimumSize(new java.awt.Dimension(40, 20));
        jcbMesNasc.setPreferredSize(new java.awt.Dimension(55, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jcbMesNasc, gridBagConstraints);

        jcbAnoNasc.setFont(getFont());
        jcbAnoNasc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1900", "1901", "1902", "1903", "1904", "1905", "1906", "1907", "1908", "1909", "1910", "1911", "1912", "1913", "1914", "1915", "1916", "1917", "1918", "1919", "1920", "1921", "1922", "1923", "1924", "1925", "1926", "1927", "1928", "1929", "1930", "1931", "1932", "1933", "1934", "1935", "1936", "1937", "1938", "1939", "1940", "1941", "1942", "1943", "1944", "1945", "1946", "1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010" }));
        jcbAnoNasc.setSelectedItem(jcbAnoNasc.getModel().getElementAt(80));
        jcbAnoNasc.setMinimumSize(new java.awt.Dimension(70, 20));
        jcbAnoNasc.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jcbAnoNasc, gridBagConstraints);

        jcbSexo.setFont(getFont());
        jcbSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));
        jcbSexo.setMinimumSize(new java.awt.Dimension(40, 20));
        jcbSexo.setPreferredSize(new java.awt.Dimension(45, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jcbSexo, gridBagConstraints);

        jtfProfissao.setFont(getFont());
        jtfProfissao.setMinimumSize(new java.awt.Dimension(160, 20));
        jtfProfissao.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfProfissao, gridBagConstraints);

        jtfNaturalidade.setFont(getFont());
        jtfNaturalidade.setMinimumSize(new java.awt.Dimension(160, 20));
        jtfNaturalidade.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfNaturalidade, gridBagConstraints);

        jlInstrucao.setFont(getFont());
        jlInstrucao.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlInstrucao.setText("Instru\u00e7\u00e3o:");
        jlInstrucao.setPreferredSize(new java.awt.Dimension(85, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlInstrucao, gridBagConstraints);

        jtfInstrucao.setFont(getFont());
        jtfInstrucao.setMinimumSize(new java.awt.Dimension(160, 20));
        jtfInstrucao.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfInstrucao, gridBagConstraints);

        jlblEstCivil.setFont(getFont());
        jlblEstCivil.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlblEstCivil.setText("Estado Civil:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlblEstCivil, gridBagConstraints);

        jcbEstadoCivil.setFont(getFont());
        jcbEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "(N/D)", "Solteiro(a)", "Casado(a)", "N\u00e3o Oficial", "Divorciado(a)", "Separado(a)", "Vi\u00favo(a)" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jcbEstadoCivil, gridBagConstraints);

        jlIdentidade.setFont(getFont());
        jlIdentidade.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlIdentidade.setText("Identidade:");
        jlIdentidade.setPreferredSize(new java.awt.Dimension(85, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlIdentidade, gridBagConstraints);

        jtfIdentidade.setFont(getFont());
        jtfIdentidade.setMinimumSize(new java.awt.Dimension(160, 20));
        jtfIdentidade.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfIdentidade, gridBagConstraints);

        jlCriacao.setFont(getFont());
        jlCriacao.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCriacao.setText("Criado em :  ");
        jlCriacao.setMinimumSize(new java.awt.Dimension(65, 14));
        jlCriacao.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlCriacao, gridBagConstraints);

        jtfCriacao.setEditable(false);
        jtfCriacao.setFont(getFont());
        jtfCriacao.setMinimumSize(new java.awt.Dimension(160, 20));
        jtfCriacao.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfCriacao, gridBagConstraints);

        jlAlteracao.setFont(getFont());
        jlAlteracao.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlAlteracao.setText("Alterado em :  ");
        jlAlteracao.setPreferredSize(new java.awt.Dimension(85, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jlAlteracao, gridBagConstraints);

        jtfAlteracao.setEditable(false);
        jtfAlteracao.setFont(getFont());
        jtfAlteracao.setMinimumSize(new java.awt.Dimension(160, 20));
        jtfAlteracao.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jpnInfoPessoais.add(jtfAlteracao, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel9.add(jpnInfoPessoais, gridBagConstraints);

        jlCriacao1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        jlCriacao1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCriacao1.setText("nID :");
        jlCriacao1.setPreferredSize(new java.awt.Dimension(60, 15));
        jPanel7.add(jlCriacao1);

        jtfNID.setEditable(false);
        jtfNID.setFont(getFont());
        jtfNID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNID.setPreferredSize(new java.awt.Dimension(90, 20));
        jPanel7.add(jtfNID);

        jPanel8.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanel7.add(jPanel8);

        jltxtStatus.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12));
        jltxtStatus.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jltxtStatus.setText("Status : ");
        jPanel7.add(jltxtStatus);

        jlStatus.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12));
        jlStatus.setForeground(new java.awt.Color(0, 102, 0));
        jlStatus.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlStatus.setText("N\u00e3o publicado");
        jPanel7.add(jlStatus);

        jPanel7.add(jPanel15);

        jbHelpPublish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/question.gif")));
        jbHelpPublish.setToolTipText("o qu\u00ea \u00e9 isso?");
        jbHelpPublish.setBorderPainted(false);
        jbHelpPublish.setFocusPainted(false);
        jbHelpPublish.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbHelpPublish.setPreferredSize(new java.awt.Dimension(20, 20));
        jbHelpPublish.setRolloverEnabled(true);
        jbHelpPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbHelpPublishActionPerformed(evt);
            }
        });

        jPanel7.add(jbHelpPublish);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 10, 0);
        jPanel9.add(jPanel7, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(515, 70));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Informa\u00e7\u00f5es Cl\u00ednicas B\u00e1sicas :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 12, 0);
        jPanel2.add(jLabel1, gridBagConstraints);

        jPanel17.setLayout(new java.awt.GridBagLayout());

        jPanel17.setMinimumSize(new java.awt.Dimension(500, 31));
        jPanel17.setPreferredSize(new java.awt.Dimension(500, 31));
        jlblInfo1.setFont(getFont());
        jlblInfo1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlblInfo1.setText("Altura (m) : ");
        jlblInfo1.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblInfo1.setMinimumSize(new java.awt.Dimension(90, 14));
        jlblInfo1.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(jlblInfo1, gridBagConstraints);

        jtfInfo1.setFont(getFont());
        jtfInfo1.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jtfInfo1.setMinimumSize(new java.awt.Dimension(50, 21));
        jtfInfo1.setPreferredSize(new java.awt.Dimension(50, 21));
        jtfInfo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfInfo1FocusLost(evt);
            }
        });

        jPanel17.add(jtfInfo1, new java.awt.GridBagConstraints());

        jlblInfo2.setFont(getFont());
        jlblInfo2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlblInfo2.setText("Massa (Kg) : ");
        jlblInfo2.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblInfo2.setMinimumSize(new java.awt.Dimension(80, 15));
        jlblInfo2.setPreferredSize(new java.awt.Dimension(95, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(jlblInfo2, gridBagConstraints);

        jtfInfo2.setFont(getFont());
        jtfInfo2.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jtfInfo2.setMinimumSize(new java.awt.Dimension(50, 21));
        jtfInfo2.setPreferredSize(new java.awt.Dimension(50, 21));
        jtfInfo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfInfo2FocusLost(evt);
            }
        });

        jPanel17.add(jtfInfo2, new java.awt.GridBagConstraints());

        jlblInfo3.setFont(getFont());
        jlblInfo3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlblInfo3.setText("IMC : ");
        jlblInfo3.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblInfo3.setMinimumSize(new java.awt.Dimension(55, 15));
        jlblInfo3.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(jlblInfo3, gridBagConstraints);

        jtfInfo3.setEditable(false);
        jtfInfo3.setFont(getFont());
        jtfInfo3.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jtfInfo3.setMinimumSize(new java.awt.Dimension(50, 21));
        jtfInfo3.setPreferredSize(new java.awt.Dimension(50, 21));
        jPanel17.add(jtfInfo3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel17, gridBagConstraints);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jPanel16.setMinimumSize(new java.awt.Dimension(393, 30));
        jPanel16.setPreferredSize(new java.awt.Dimension(393, 30));
        jlblInfo4.setFont(getFont());
        jlblInfo4.setText("Prefer\u00eancia lateral :");
        jlblInfo4.setIconTextGap(9);
        jlblInfo4.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblInfo4.setMinimumSize(new java.awt.Dimension(110, 16));
        jlblInfo4.setPreferredSize(new java.awt.Dimension(140, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel16.add(jlblInfo4, gridBagConstraints);

        btnGroupLateral.add(jrbDestro);
        jrbDestro.setFont(getFont());
        jrbDestro.setText(" destro");
        jrbDestro.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbDestro.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jrbDestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDestroActionPerformed(evt);
            }
        });

        jPanel16.add(jrbDestro, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 20;
        jPanel16.add(jPanel18, gridBagConstraints);

        btnGroupLateral.add(jrbCanhoto);
        jrbCanhoto.setFont(getFont());
        jrbCanhoto.setText(" canhoto");
        jrbCanhoto.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbCanhoto.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jrbCanhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbCanhotoActionPerformed(evt);
            }
        });

        jPanel16.add(jrbCanhoto, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 20;
        jPanel16.add(jPanel19, gridBagConstraints);

        btnGroupLateral.add(jrbAmbidestro);
        jrbAmbidestro.setFont(getFont());
        jrbAmbidestro.setText(" ambidestro   ");
        jrbAmbidestro.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbAmbidestro.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jrbAmbidestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbAmbidestroActionPerformed(evt);
            }
        });

        jPanel16.add(jrbAmbidestro, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        jPanel2.add(jPanel16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel9.add(jPanel2, gridBagConstraints);

        jspInfoPessoais.setViewportView(jPanel9);

        jtpPaciente.addTab("Informa\u00e7\u00f5es Pessoais 1", jspInfoPessoais);

        jspInfoComplementares.setAlignmentX(1.0F);
        jspInfoComplementares.setAlignmentY(1.0F);
        jspInfoComplementares.setMinimumSize(new java.awt.Dimension(510, 340));
        jspInfoComplementares.setPreferredSize(new java.awt.Dimension(510, 340));
        jpnInfoComplementares.setLayout(new java.awt.GridBagLayout());

        jpnInfoComplementares.setMinimumSize(new java.awt.Dimension(450, 300));
        jpnInfoComplementares.setPreferredSize(new java.awt.Dimension(450, 300));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setMinimumSize(new java.awt.Dimension(510, 180));
        jPanel12.setPreferredSize(new java.awt.Dimension(510, 200));
        jlEndRes.setFont(getFont());
        jlEndRes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlEndRes.setText("End. Resid.:");
        jPanel12.add(jlEndRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 70, 20));

        jtfEndRes.setFont(getFont());
        jPanel12.add(jtfEndRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 430, -1));

        jlCEPRes.setFont(getFont());
        jlCEPRes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCEPRes.setText("CEP:");
        jPanel12.add(jlCEPRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 70, 20));

        jtfCEPRes.setFont(getFont());
        jPanel12.add(jtfCEPRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 90, -1));

        jlCidadeRes.setFont(getFont());
        jlCidadeRes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCidadeRes.setText("Cidade:");
        jPanel12.add(jlCidadeRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 70, 20));

        jtfCidadeRes.setFont(getFont());
        jPanel12.add(jtfCidadeRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 130, -1));

        jlEstadoRes.setFont(getFont());
        jlEstadoRes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlEstadoRes.setText("Estado:");
        jPanel12.add(jlEstadoRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 60, 20));

        jcbEstadoRes.setFont(getFont());
        jcbEstadoRes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "AC", "AL", "AP", "AM", "BA", "CE", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        jPanel12.add(jcbEstadoRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 50, -1));

        jlEndCom.setFont(getFont());
        jlEndCom.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlEndCom.setText("End. Comerc.:");
        jPanel12.add(jlEndCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 70, 20));

        jtfEndCom.setFont(getFont());
        jPanel12.add(jtfEndCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 410, -1));

        jlCEPCom.setFont(getFont());
        jlCEPCom.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCEPCom.setText("CEP:");
        jPanel12.add(jlCEPCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 70, 20));

        jtfCEPCom.setFont(getFont());
        jPanel12.add(jtfCEPCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 90, -1));

        jlCidadeCom.setFont(getFont());
        jlCidadeCom.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCidadeCom.setText("Cidade:");
        jPanel12.add(jlCidadeCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 70, 20));

        jtfCidadeCom.setFont(getFont());
        jPanel12.add(jtfCidadeCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 130, -1));

        jlEstadoCom.setFont(getFont());
        jlEstadoCom.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlEstadoCom.setText("Estado:");
        jPanel12.add(jlEstadoCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 60, 20));

        jcbEstadoCom.setFont(getFont());
        jcbEstadoCom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "AC", "AL", "AP", "AM", "BA", "CE", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        jPanel12.add(jcbEstadoCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 50, -1));

        jlTelRes.setFont(getFont());
        jlTelRes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlTelRes.setText("Tel. Resid.:");
        jPanel12.add(jlTelRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 70, 20));

        jtfTelRes.setFont(getFont());
        jPanel12.add(jtfTelRes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 90, -1));

        jlTelCom.setFont(getFont());
        jlTelCom.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlTelCom.setText("Tel. Comerc.:");
        jPanel12.add(jlTelCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 70, 20));

        jtfTelCom.setFont(getFont());
        jPanel12.add(jtfTelCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 90, -1));

        jlTelTelCelular.setFont(getFont());
        jlTelTelCelular.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlTelTelCelular.setText("Celular:");
        jPanel12.add(jlTelTelCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 50, 20));

        jtfTelCelular.setFont(getFont());
        jPanel12.add(jtfTelCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 90, -1));

        jlEmail.setFont(getFont());
        jlEmail.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlEmail.setText("E-mail:");
        jPanel12.add(jlEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 70, 20));

        jtfEmail.setFont(getFont());
        jPanel12.add(jtfEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 160, -1));

        jlSite.setFont(getFont());
        jlSite.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlSite.setText("Site:");
        jPanel12.add(jlSite, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 70, 20));

        jtfSite.setFont(getFont());
        jPanel12.add(jtfSite, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, 180, -1));

        jlCPF.setFont(getFont());
        jlCPF.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlCPF.setText("CPF (somente n\u00fameros, sem espa\u00e7os) :  ");
        jPanel12.add(jlCPF, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 210, 20));

        jtfCPF.setFont(getFont());
        jPanel12.add(jtfCPF, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 270, -1));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        jpnInfoComplementares.add(jPanel12, gridBagConstraints);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(510, 90));
        jPanel1.setPreferredSize(new java.awt.Dimension(510, 90));
        jlEmergencia.setFont(getFont());
        jlEmergencia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlEmergencia.setText("Emerg\u00eancia:");
        jPanel1.add(jlEmergencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jtfEmergencia.setFont(getFont());
        jtfEmergencia.setMinimumSize(new java.awt.Dimension(90, 20));
        jtfEmergencia.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel1.add(jtfEmergencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 160, -1));

        jlTelEmergencia.setFont(getFont());
        jlTelEmergencia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlTelEmergencia.setText("Tel. / outros:");
        jPanel1.add(jlTelEmergencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jtfTelEmergencia.setFont(getFont());
        jtfTelEmergencia.setMinimumSize(new java.awt.Dimension(90, 20));
        jtfTelEmergencia.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel1.add(jtfTelEmergencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 160, -1));

        jlConvenio.setFont(getFont());
        jlConvenio.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlConvenio.setText("Conv\u00eanio:");
        jPanel1.add(jlConvenio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jtfConvenio.setFont(getFont());
        jtfConvenio.setMinimumSize(new java.awt.Dimension(90, 20));
        jtfConvenio.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel1.add(jtfConvenio, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 160, -1));

        jlConvenioNumero.setFont(getFont());
        jlConvenioNumero.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlConvenioNumero.setText("N\u00famero:");
        jPanel1.add(jlConvenioNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, -1, -1));

        jtfConvenioNumero.setFont(getFont());
        jtfConvenioNumero.setMinimumSize(new java.awt.Dimension(90, 20));
        jtfConvenioNumero.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel1.add(jtfConvenioNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 160, -1));

        jlObservacoes.setFont(getFont());
        jlObservacoes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlObservacoes.setText("Obs./Outros");
        jPanel1.add(jlObservacoes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jtfObservacoes.setFont(getFont());
        jPanel1.add(jtfObservacoes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 430, -1));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        jpnInfoComplementares.add(jPanel1, gridBagConstraints);

        jspInfoComplementares.setViewportView(jpnInfoComplementares);

        jtpPaciente.addTab("Informa\u00e7\u00f5es Pessoais 2", jspInfoComplementares);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(498, 313));
        jLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        jLabel2.setText("Assinale os locais onde o paciente relata sentir dor e o tempo");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, -1, -1));

        jckCabe.setText("cabe\u00e7a");
        jPanel4.add(jckCabe, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jPanel4.add(jtxtCabe, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 30, 20));

        jcmbCabe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbCabe, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, -1, 20));

        jckColCerv.setText("coluna cervical");
        jPanel4.add(jckColCerv, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jPanel4.add(jtxtColCerv, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 30, 20));

        jcmbColCerv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbColCerv, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, -1, 20));

        jckColTora.setText("coluna tor\u00e1cica");
        jckColTora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jckColToraActionPerformed(evt);
            }
        });

        jPanel4.add(jckColTora, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jPanel4.add(jtxtColTora, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 30, 20));

        jcmbColTora.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbColTora, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, -1, 20));

        jckColLomb.setText("coluna lombar");
        jPanel4.add(jckColLomb, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jPanel4.add(jtxtColLomb, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 30, 20));

        jcmbColLomb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbColLomb, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, -1, 20));

        jckPelv.setText("pelve");
        jPanel4.add(jckPelv, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jPanel4.add(jtxtPelv, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 30, 20));

        jcmbPelv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbPelv, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, -1, 20));

        jckQuad.setText("quadril");
        jPanel4.add(jckQuad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jPanel4.add(jtxtQuad, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 30, 20));

        jcmbQuad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbQuad, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, -1, 20));

        jckCoxa.setText("coxa");
        jPanel4.add(jckCoxa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        jPanel4.add(jtxtCoxa, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 30, 20));

        jcmbCoxa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbCoxa, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, -1, 20));

        jckJoel.setText("joelho");
        jPanel4.add(jckJoel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        jPanel4.add(jtxtJoel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 30, 20));

        jcmbJoel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbJoel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, -1, 20));

        jckPern.setText("perna");
        jPanel4.add(jckPern, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jPanel4.add(jtxtPern, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 30, 20));

        jcmbPern.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbPern, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, -1, 20));

        jckTorn.setText("tornozelo");
        jPanel4.add(jckTorn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jPanel4.add(jtxtTorn, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 30, 20));

        jcmbTorn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbTorn, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, -1, 20));

        jckPe.setText("p\u00e9");
        jPanel4.add(jckPe, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        jPanel4.add(jtxtPe, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 30, 20));

        jcmbPe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbPe, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, -1, 20));

        jckOmbr.setText("ombro");
        jPanel4.add(jckOmbr, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        jPanel4.add(jtxtOmbr, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 30, 20));

        jcmbOmbr.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbOmbr, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, -1, 20));

        jckBrac.setText("bra\u00e7o");
        jPanel4.add(jckBrac, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        jPanel4.add(jtxtBrac, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 270, 30, 20));

        jcmbBrac.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbBrac, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, -1, 20));

        jckCoto.setText("cotovelo");
        jPanel4.add(jckCoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        jPanel4.add(jtxtCoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, 30, 20));

        jcmbCoto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbCoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, -1, 20));

        jckAnteBrac.setText("antebra\u00e7o");
        jPanel4.add(jckAnteBrac, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, -1));

        jPanel4.add(jtxtAnteBrac, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 30, 20));

        jcmbAnteBrac.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbAnteBrac, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, 20));

        jckPunh.setText("punho");
        jPanel4.add(jckPunh, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        jPanel4.add(jtxtPunh, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 30, 20));

        jcmbPunh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbPunh, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, -1, 20));

        jckMao.setText("m\u00e3o");
        jPanel4.add(jckMao, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, -1, -1));

        jPanel4.add(jtxtMao, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 30, 20));

        jcmbMao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbMao, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, -1, 20));

        jckDedo.setText("dedos");
        jPanel4.add(jckDedo, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, -1, -1));

        jPanel4.add(jtxtDedo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, 30, 20));

        jcmbDedo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dias", "semanas", "meses", "anos" }));
        jPanel4.add(jcmbDedo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, -1, 20));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 10, 280));

        jPanel10.add(jPanel4, new java.awt.GridBagConstraints());

        jspInfoDadoClinicoAtual.setViewportView(jPanel10);

        jtpPaciente.addTab("Dados Cl\u00ednicos Atuais", jspInfoDadoClinicoAtual);

        jspInfoDadoClinicoPregresso.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setMinimumSize(new java.awt.Dimension(493, 600));
        jPanel6.setPreferredSize(new java.awt.Dimension(493, 600));
        jLabel3.setText("O paciente apresentou algum diagn\u00f3stico ortop\u00e9dico ou reumatol\u00f3gico nos \u00faltimos cinco anos? Qual? ");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jcmbDCPQ1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, 20));

        jPanel6.add(jtxtDCPQ1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 440, 20));

        jLabel4.setText("O paciente sentiu dor intensa em algum local do corpo nos \u00faltimos tr\u00eas anos?  Em que local?");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jcmbDCPQ2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, 20));

        jPanel6.add(jtxtDCPQ2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 440, 20));

        jLabel5.setText("O paciente sentiu dor leve em algum local do corpo nos \u00faltimos tr\u00eas anos? Em que local?");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jcmbDCPQ3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, 20));

        jPanel6.add(jtxtDCPQ3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 440, 20));

        jLabel6.setText("O paciente utiliza algum medicamento atualmente?  Qual?");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jcmbDCPQ4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 20));

        jPanel6.add(jtxtDCPQ4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 440, 20));

        jLabel7.setText("O paciente teve alguma queda nos \u00faltimos tr\u00eas anos?");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jcmbDCPQ5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, 20));

        jPanel6.add(jtxtDCPQ5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 440, 20));

        jLabel8.setText("O paciente fez tratamento de fisioterapia nos \u00faltimos tr\u00eas anos?");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));

        jcmbDCPQ6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, 20));

        jPanel6.add(jtxtDCPQ6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 440, 20));

        jLabel9.setText("O paciente praticou atividade f\u00edsica nos \u00faltimos tr\u00eas anos? Que tipo e com qual freq\u00fc\u00eancia?");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        jcmbDCPQ7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "n\u00e3o", "sim" }));
        jPanel6.add(jcmbDCPQ7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, 20));

        jtxtDCPQ7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDCPQ7ActionPerformed(evt);
            }
        });

        jPanel6.add(jtxtDCPQ7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 440, 20));

        jLabel10.setText("Observa\u00e7\u00f5es:");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, -1, -1));

        jScrollPane1.setViewportView(jedtPaneDCP);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 480, 120));

        jPanel11.add(jPanel6, new java.awt.GridBagConstraints());

        jspInfoDadoClinicoPregresso.setViewportView(jPanel11);

        jtpPaciente.addTab("Dados Cl\u00ednicos Pregressos", jspInfoDadoClinicoPregresso);

        jspInfoOutros.setAlignmentX(1.0F);
        jspInfoOutros.setAlignmentY(1.0F);
        jspInfoOutros.setPreferredSize(new java.awt.Dimension(510, 340));
        jpnInfoOutros.setLayout(new java.awt.BorderLayout());

        jpnInfoOutros.setPreferredSize(new java.awt.Dimension(450, 300));
        jlblObs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblObs.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jlblObs.setMinimumSize(new java.awt.Dimension(100, 30));
        jlblObs.setPreferredSize(new java.awt.Dimension(100, 30));
        jpnInfoOutros.add(jlblObs, java.awt.BorderLayout.NORTH);

        jPanel5.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jPanel5.setMinimumSize(new java.awt.Dimension(100, 30));
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 30));
        jpnInfoOutros.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel13.setMinimumSize(new java.awt.Dimension(50, 100));
        jPanel13.setPreferredSize(new java.awt.Dimension(50, 100));
        jpnInfoOutros.add(jPanel13, java.awt.BorderLayout.EAST);

        jPanel14.setMinimumSize(new java.awt.Dimension(50, 100));
        jPanel14.setPreferredSize(new java.awt.Dimension(50, 100));
        jpnInfoOutros.add(jPanel14, java.awt.BorderLayout.WEST);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jepObservacoes.setFont(getFont());
        jepObservacoes.setDragEnabled(true);
        jepObservacoes.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        jepObservacoes.setMinimumSize(new java.awt.Dimension(450, 280));
        jScrollPane2.setViewportView(jepObservacoes);

        jpnInfoOutros.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jspInfoOutros.setViewportView(jpnInfoOutros);

        jtpPaciente.addTab("Observa\u00e7\u00f5es e Anota\u00e7\u00f5es", jspInfoOutros);

        getContentPane().add(jtpPaciente, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setMinimumSize(new java.awt.Dimension(180, 40));
        jPanel3.setPreferredSize(new java.awt.Dimension(180, 35));
        jbtn1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtn1.setText("Ok");
        jbtn1.setToolTipText("Aplica dados ao projeto atual, salva na base de dados do projeto e minimiza esta janela.");
        jbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn1ActionPerformed(evt);
            }
        });

        jPanel3.add(jbtn1);

        jbtn2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtn2.setText("Aplicar");
        jbtn2.setToolTipText("Aplica dados ao projeto atual e salva na base de dados do projeto.");
        jbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn2ActionPerformed(evt);
            }
        });

        jPanel3.add(jbtn2);

        jbtn3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jbtn3.setText("Limpar");
        jbtn3.setToolTipText("Limpa todos os campos (incluindo outras fichas)");
        jbtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn3ActionPerformed(evt);
            }
        });

        jPanel3.add(jbtn3);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jrbAmbidestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbAmbidestroActionPerformed
        atualiza_jtfInfo4("ambidestro");
    }//GEN-LAST:event_jrbAmbidestroActionPerformed

    private void jrbCanhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbCanhotoActionPerformed
        atualiza_jtfInfo4("canhoto");
    }//GEN-LAST:event_jrbCanhotoActionPerformed

    private void jrbDestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDestroActionPerformed
        atualiza_jtfInfo4("destro");
    }//GEN-LAST:event_jrbDestroActionPerformed
	
	private void jbHelpPublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbHelpPublishActionPerformed
		// ajuda sobre publicação na base de dados nacional
		String message;
		if (jlStatus.getText().equalsIgnoreCase("Publicado"))
			message = "Esta análise já foi publicada na base nacional de dados posturais.\n" +
			"Isto tipicamente é realizado através de uma conexão à Internet, à partir deste programa.\n" +
			"Nomes, informações pessoais e fotos (imagens) não são publicadas, por questão de privacidade.\n" +
			"Este projeto/análise pode ser alterado localmente, mas não poderá ser publicado novamente.\n" +
			"Se desejar contribuir para a base nacional de dados posturais com novos dados deste mesmo\n" +
			"paciente, um novo projeto deve ser iniciado.";
		else
			message = "Esta análise não foi ainda publicada na base nacional de dados posturais.\n" +
			"Isto tipicamente é realizado através de uma conexão à Internet, à partir deste programa.\n" +
			"Nomes, informações pessoais e fotos (imagens) não são publicadas, por questão de privacidade.\n" +
			"Este projeto/análise pode ser alterado localmente, e posteriormente publicado através do\n" +
			"diálogo \"Base de Dados de Análises (Local)\", acessível através do item de menu \"Projetos Criados\".";
		JOptionPane.showMessageDialog(paciente.sapo,message,"Informação",JOptionPane.INFORMATION_MESSAGE);
		
	}//GEN-LAST:event_jbHelpPublishActionPerformed
	
	private void jtfInfo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfInfo1FocusLost
		calcIMC();
	}//GEN-LAST:event_jtfInfo1FocusLost
	
	private void jtfInfo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfInfo2FocusLost
		calcIMC();
	}//GEN-LAST:event_jtfInfo2FocusLost
	
	private void calcIMC() {
		String info2 = jtfInfo2.getText().replace(',','.');
		String info1 = jtfInfo1.getText().replace(',','.');
		double imc;
		try {
			double altura = Double.valueOf(info1).doubleValue();
			altura = (altura > 10)?(altura/100):altura;
			double massa  = Double.valueOf(info2).doubleValue();
			imc = massa/Math.pow(altura, 2);
			jtfInfo3.setText(numFormat2.format(imc));
		} catch (NumberFormatException e) {} 
	}
	
	private void jtxtDCPQ7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDCPQ7ActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jtxtDCPQ7ActionPerformed
	
	private void jckColToraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jckColToraActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jckColToraActionPerformed
	
	private void jbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn1ActionPerformed
		entraDados();
		paciente.escreveDB();
		mostraDados();
		repaint();
		try {
			setIcon(true);
		} catch (PropertyVetoException e) { }
	}//GEN-LAST:event_jbtn1ActionPerformed
	
	private void jbtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn3ActionPerformed
		Object[] options = {"Sim","Não"};
		int result = JOptionPane.showOptionDialog (paciente.sapo,
				"Limpar alterações em \"Dados Associados\" ?\n",
				"Confirmação", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		if( result == JOptionPane.YES_OPTION) {
			mostraDados();
			repaint();
		}
	}//GEN-LAST:event_jbtn3ActionPerformed
	
	private void jbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn2ActionPerformed
		entraDados();
		paciente.escreveDB();
		mostraDados();
		repaint();
	}//GEN-LAST:event_jbtn2ActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupLateral;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbHelpPublish;
    private javax.swing.JButton jbtn1;
    private javax.swing.JButton jbtn2;
    private javax.swing.JButton jbtn3;
    javax.swing.JComboBox jcbAnoNasc;
    javax.swing.JComboBox jcbDiaNasc;
    javax.swing.JComboBox jcbEstadoCivil;
    javax.swing.JComboBox jcbEstadoCom;
    javax.swing.JComboBox jcbEstadoRes;
    javax.swing.JComboBox jcbMesNasc;
    javax.swing.JComboBox jcbSexo;
    private javax.swing.JCheckBox jckAnteBrac;
    private javax.swing.JCheckBox jckBrac;
    private javax.swing.JCheckBox jckCabe;
    private javax.swing.JCheckBox jckColCerv;
    private javax.swing.JCheckBox jckColLomb;
    private javax.swing.JCheckBox jckColTora;
    private javax.swing.JCheckBox jckCoto;
    private javax.swing.JCheckBox jckCoxa;
    private javax.swing.JCheckBox jckDedo;
    private javax.swing.JCheckBox jckJoel;
    private javax.swing.JCheckBox jckMao;
    private javax.swing.JCheckBox jckOmbr;
    private javax.swing.JCheckBox jckPe;
    private javax.swing.JCheckBox jckPelv;
    private javax.swing.JCheckBox jckPern;
    private javax.swing.JCheckBox jckPunh;
    private javax.swing.JCheckBox jckQuad;
    private javax.swing.JCheckBox jckTorn;
    private javax.swing.JComboBox jcmbAnteBrac;
    private javax.swing.JComboBox jcmbBrac;
    private javax.swing.JComboBox jcmbCabe;
    private javax.swing.JComboBox jcmbColCerv;
    private javax.swing.JComboBox jcmbColLomb;
    private javax.swing.JComboBox jcmbColTora;
    private javax.swing.JComboBox jcmbCoto;
    private javax.swing.JComboBox jcmbCoxa;
    private javax.swing.JComboBox jcmbDCPQ1;
    private javax.swing.JComboBox jcmbDCPQ2;
    private javax.swing.JComboBox jcmbDCPQ3;
    private javax.swing.JComboBox jcmbDCPQ4;
    private javax.swing.JComboBox jcmbDCPQ5;
    private javax.swing.JComboBox jcmbDCPQ6;
    private javax.swing.JComboBox jcmbDCPQ7;
    private javax.swing.JComboBox jcmbDedo;
    private javax.swing.JComboBox jcmbJoel;
    private javax.swing.JComboBox jcmbMao;
    private javax.swing.JComboBox jcmbOmbr;
    private javax.swing.JComboBox jcmbPe;
    private javax.swing.JComboBox jcmbPelv;
    private javax.swing.JComboBox jcmbPern;
    private javax.swing.JComboBox jcmbPunh;
    private javax.swing.JComboBox jcmbQuad;
    private javax.swing.JComboBox jcmbTorn;
    private javax.swing.JEditorPane jedtPaneDCP;
    javax.swing.JEditorPane jepObservacoes;
    private javax.swing.JLabel jlAlteracao;
    private javax.swing.JLabel jlCEPCom;
    private javax.swing.JLabel jlCEPRes;
    private javax.swing.JLabel jlCPF;
    private javax.swing.JLabel jlCidadeCom;
    private javax.swing.JLabel jlCidadeRes;
    private javax.swing.JLabel jlConvenio;
    private javax.swing.JLabel jlConvenioNumero;
    private javax.swing.JLabel jlCriacao;
    private javax.swing.JLabel jlCriacao1;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlEmergencia;
    private javax.swing.JLabel jlEndCom;
    private javax.swing.JLabel jlEndRes;
    private javax.swing.JLabel jlEstadoCom;
    private javax.swing.JLabel jlEstadoRes;
    private javax.swing.JLabel jlIdentidade;
    private javax.swing.JLabel jlInstrucao;
    private javax.swing.JLabel jlNaturalidade;
    private javax.swing.JLabel jlObservacoes;
    private javax.swing.JLabel jlProfissao;
    private javax.swing.JLabel jlSite;
    public javax.swing.JLabel jlStatus;
    private javax.swing.JLabel jlTelCom;
    private javax.swing.JLabel jlTelEmergencia;
    private javax.swing.JLabel jlTelRes;
    private javax.swing.JLabel jlTelTelCelular;
    private javax.swing.JLabel jlbDataNasc;
    private javax.swing.JLabel jlblEstCivil;
    private javax.swing.JLabel jlblInfo1;
    private javax.swing.JLabel jlblInfo2;
    private javax.swing.JLabel jlblInfo3;
    private javax.swing.JLabel jlblInfo4;
    private javax.swing.JLabel jlblNome;
    private javax.swing.JLabel jlblObs;
    private javax.swing.JLabel jlblSexo;
    private javax.swing.JLabel jltxtStatus;
    private javax.swing.JPanel jpnInfoComplementares;
    private javax.swing.JPanel jpnInfoOutros;
    private javax.swing.JPanel jpnInfoPessoais;
    private javax.swing.JRadioButton jrbAmbidestro;
    private javax.swing.JRadioButton jrbCanhoto;
    private javax.swing.JRadioButton jrbDestro;
    private javax.swing.JScrollPane jspInfoComplementares;
    private javax.swing.JScrollPane jspInfoDadoClinicoAtual;
    private javax.swing.JScrollPane jspInfoDadoClinicoPregresso;
    private javax.swing.JScrollPane jspInfoOutros;
    private javax.swing.JScrollPane jspInfoPessoais;
    protected javax.swing.JTextField jtfAlteracao;
    javax.swing.JTextField jtfCEPCom;
    javax.swing.JTextField jtfCEPRes;
    protected javax.swing.JTextField jtfCPF;
    javax.swing.JTextField jtfCidadeCom;
    javax.swing.JTextField jtfCidadeRes;
    javax.swing.JTextField jtfConvenio;
    javax.swing.JTextField jtfConvenioNumero;
    protected javax.swing.JTextField jtfCriacao;
    javax.swing.JTextField jtfEmail;
    javax.swing.JTextField jtfEmergencia;
    javax.swing.JTextField jtfEndCom;
    javax.swing.JTextField jtfEndRes;
    javax.swing.JTextField jtfIdentidade;
    javax.swing.JTextField jtfInfo1;
    javax.swing.JTextField jtfInfo2;
    javax.swing.JTextField jtfInfo3;
    javax.swing.JTextField jtfInstrucao;
    protected javax.swing.JTextField jtfNID;
    javax.swing.JTextField jtfNaturalidade;
    javax.swing.JTextField jtfNome;
    javax.swing.JTextField jtfObservacoes;
    javax.swing.JTextField jtfProfissao;
    javax.swing.JTextField jtfSite;
    javax.swing.JTextField jtfTelCelular;
    javax.swing.JTextField jtfTelCom;
    javax.swing.JTextField jtfTelEmergencia;
    javax.swing.JTextField jtfTelRes;
    private javax.swing.JTabbedPane jtpPaciente;
    private javax.swing.JTextField jtxtAnteBrac;
    private javax.swing.JTextField jtxtBrac;
    private javax.swing.JTextField jtxtCabe;
    private javax.swing.JTextField jtxtColCerv;
    private javax.swing.JTextField jtxtColLomb;
    private javax.swing.JTextField jtxtColTora;
    private javax.swing.JTextField jtxtCoto;
    private javax.swing.JTextField jtxtCoxa;
    private javax.swing.JTextField jtxtDCPQ1;
    private javax.swing.JTextField jtxtDCPQ2;
    private javax.swing.JTextField jtxtDCPQ3;
    private javax.swing.JTextField jtxtDCPQ4;
    private javax.swing.JTextField jtxtDCPQ5;
    private javax.swing.JTextField jtxtDCPQ6;
    private javax.swing.JTextField jtxtDCPQ7;
    private javax.swing.JTextField jtxtDedo;
    private javax.swing.JTextField jtxtJoel;
    private javax.swing.JTextField jtxtMao;
    private javax.swing.JTextField jtxtOmbr;
    private javax.swing.JTextField jtxtPe;
    private javax.swing.JTextField jtxtPelv;
    private javax.swing.JTextField jtxtPern;
    private javax.swing.JTextField jtxtPunh;
    private javax.swing.JTextField jtxtQuad;
    private javax.swing.JTextField jtxtTorn;
    // End of variables declaration//GEN-END:variables
	
}
