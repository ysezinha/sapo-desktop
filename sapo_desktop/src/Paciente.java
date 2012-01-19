/*
 * Paciente.java
 *
 * Created on 11 de Julho de 2004=?, 20:15
 */

/**
 *
 * @author  Edison Puig Maldonado
 * @author Anderson Zanardi de Freitas
 */

import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.dati.data.DBConnection;
import com.dati.util.DataFormat;
import com.sun.EFileFilter;
import com.thoughtworks.xstream.XStream;


class Paciente {
	
	transient boolean salvarAlteracoes = false;
	public DadosPaciente dados;
	public transient UIPaciente dadosInterface;
	public transient SAPO sapo;
	private transient DBConnection db;
	private transient File arquivoPaciente;
	
	
	public void inicializa(SAPO sapo) {
		this.sapo = sapo;
		this.db = sapo.db;
		dadosInterface = new UIPaciente(this);
		dadosInterface.mostraDados();
	}
	
	Paciente(SAPO sapo){
		this.db = sapo.db;
		arquivoPaciente = null;
		dados = new DadosPaciente();
		inicializa(sapo);
	}
	
	Paciente(final long nID, final SAPO sapo){
		this.db = sapo.db;
		arquivoPaciente = null;
		dados = new DadosPaciente();
		dados.nID = nID;
		sapo.cursorControl(sapo, new PerformOp(){
			public void doAction() throws Exception {
				lerDB(nID);
				DBImageData.lerImageDatas(nID, Paciente.this, db);
				inicializa(sapo);
			}
		});
	}
	
	/** Mas não destroi. Faz imagem n-1 no array image ficar null. Faz dados.imgData[n] = null */
	public int destroiImagem (int n) {
		dados.imgData[n].limpaFileImage();
		dados.imgData[n].limpaTudo();
		salvarAlteracoes = true;
		System.gc();
		return 0;
	}
	
	
	public int getNumImagens(){
		int n=0;
		try {	
			for (int i=1; i < SAPO.maxImg; i++)
				if( sapo.jif[i] != null) n++;
		} catch (Exception e) { return -1;}
		return n;	
	}
	
	public void salvarComo(){
		arquivoPaciente = selecionarArquivo();
		if( arquivoPaciente != null ) sapo.cursorControl(sapo, new PerformOp(){
			public void doAction() throws Exception {
				gravarPaciente();
			}
		});
	}
	
	public void salvar(){
		if( arquivoPaciente == null )
			arquivoPaciente = selecionarArquivo();
		if( arquivoPaciente != null ) sapo.cursorControl(sapo, new PerformOp(){
			public void doAction() throws Exception {
				gravarPaciente();
			}
		});
	}
	
	public boolean dadosAlterados(){ // #ev.21dez#
		if (dadosInterface == null) return false;
		String sex = (String)dadosInterface.jcbSexo.getSelectedItem(); 
		String ano = (String)dadosInterface.jcbAnoNasc.getSelectedItem();
		String mes = (String)dadosInterface.jcbMesNasc.getSelectedItem(); 
		String dia = (String)dadosInterface.jcbDiaNasc.getSelectedItem(); 
		if (salvarAlteracoes) return true;
		if ( dados.nome.equals(dadosInterface.jtfNome.getText()) &&
				dados.CEPCom == DataFormat.parseLong(dadosInterface.jtfCEPCom.getText()) &&
				dados.CEPRes == DataFormat.parseLong(dadosInterface.jtfCEPRes.getText()) &&
				dados.cidadeCom.equals(dadosInterface.jtfCidadeCom.getText()) &&
				dados.cidadeRes.equals(dadosInterface.jtfCidadeRes.getText()) &&
				dados.convenio.equals(dadosInterface.jtfConvenio.getText()) &&
				dados.numConvenio == DataFormat.parseLong(dadosInterface.jtfConvenioNumero.getText()) &&
				dados.email.equals(dadosInterface.jtfEmail.getText()) &&
				dados.emergencia.equals(dadosInterface.jtfEmergencia.getText()) &&
				dados.enderecoCom.equals(dadosInterface.jtfEndCom.getText()) &&
				dados.enderecoRes.equals(dadosInterface.jtfEndRes.getText()) &&
				dados.identidade.equals(dadosInterface.jtfIdentidade.getText()) &&
				dados.cpf == DataFormat.parseLong(dadosInterface.jtfCPF.getText()) &&
				dados.IM1.equals(dadosInterface.jtfInfo1.getText()) &&
				dados.IM2.equals(dadosInterface.jtfInfo2.getText()) &&
				dados.IM3.equals(dadosInterface.jtfInfo3.getText()) &&
				dados.IM4.equals(dadosInterface.jtfInfo4.getText()) &&
				dados.instrucao.equals(dadosInterface.jtfInstrucao.getText()) &&
				dados.naturalidade.equals(dadosInterface.jtfNaturalidade.getText()) &&
				dados.observacoes.equals(dadosInterface.jtfObservacoes.getText()) &&
				dados.profissao.equals(dadosInterface.jtfProfissao.getText()) &&
				dados.site.equals(dadosInterface.jtfSite.getText()) &&
				dados.telCel.equals(dadosInterface.jtfTelCelular.getText()) &&
				dados.telCom.equals(dadosInterface.jtfTelCom.getText()) &&
				dados.telRes.equals(dadosInterface.jtfTelRes.getText()) &&
				dados.obsOutros.equals(dadosInterface.jepObservacoes.getText()) &&
				dados.estadoCom.equals((String)dadosInterface.jcbEstadoCom.getSelectedItem()) &&
				dados.estadoRes.equals((String)dadosInterface.jcbEstadoRes.getSelectedItem()) &&
				dados.estadoCivil.equals((String)dadosInterface.jcbEstadoCivil.getSelectedItem()) &&
				dados.anoNasc == DataFormat.parseLong(ano) &&
				dados.mesNasc == DataFormat.parseLong(mes) &&
				dados.diaNasc == DataFormat.parseLong(dia) &&
				dados.sexo == sex.charAt(0)) 
			return false;
		salvarAlteracoes = true;
		return true;
	}
	
	private File selecionarArquivo(){
		SAPO.setUILanguageJFileChooser();
		String arquivo = "";
		JFileChooser fc = new JFileChooser();
		String[] extensoes = new String[] {"sapo"};
		EFileFilter filter = new EFileFilter(extensoes, "Arquivos de Pacientes");
		fc.addChoosableFileFilter(filter);
		fc.setDialogTitle("SAPO - Salvar Projeto em Arquivo");
		boolean nomeArquivoOk = false;
		while ( ! nomeArquivoOk ) {
			if (sapo.user.dados.dirprojetos != "") {
				File prjDir = new File(sapo.user.dados.dirprojetos);
				if (prjDir.exists()) fc.setCurrentDirectory(prjDir);
			}
			if( fc.showSaveDialog(sapo) != JFileChooser.APPROVE_OPTION ) return null;
			arquivo = fc.getSelectedFile().getPath();
			
			if( ! arquivo.toUpperCase().endsWith(".SAPO") ){
				if( ( arquivo.length() - arquivo.lastIndexOf(".") ) <= 4 ){
					JOptionPane.showMessageDialog(sapo,"extensão do arquivo deve ser .sapo");
					continue;
				}
				arquivo += ".sapo";
			}
			nomeArquivoOk = true;
		}
		sapo.user.dados.dirprojetos = (fc.getSelectedFile()).getParent();
		return( new File ( arquivo ));	
	}
	
	private void gravarPaciente() throws Exception{
		dadosInterface.entraDados();
		long dadosNID = dados.nID;
		dados.nID = -1;
		try{
			XStream xstream = new XStream();
			String xml = xstream.toXML(this);
			PrintWriter pw = new PrintWriter(arquivoPaciente);
			pw.println(xml);
			pw.flush();
			dados.nID = dadosNID;
			salvarAlteracoes = false;
		} catch (Exception e) { throw new Exception(e); }
	}
	
	public void escreveDB() {
		sapo.cursorControl(sapo, new PerformOp(){
			public void doAction() throws Exception {
				escreverNoDB();
			}
		});
	}
	
	private void escreverNoDB() {
		dadosInterface.entraDados();
		if (dados.nID != -1) updateDB();
		else insereDB();
		DBImageData.escreveAtualizaImageDatas(dados, db);
		salvarAlteracoes = false;
	}
	
	/** lê configurações do usuário no banco e dados */
	public void lerDB (long nID) {
		String sqlPreparado = "SELECT * FROM dpaciente WHERE nid=?";
		DBConnection.PreparaInsereEnvia pie = db.getPreparaInsereEnvia(sqlPreparado);
		pie.setSQL(1, nID);
		ResultSet rs = pie.executaQuery();
		try {
			if (rs.next()) {
				dados.nome =            rs.getString("nome");
				dados.estadoCivil = 	rs.getString("estadocivil");
				dados.profissao = 		rs.getString("profissao");
				dados.instrucao = 		rs.getString("instrucao");
				dados.naturalidade = 	rs.getString("naturalidade");
				dados.nacionalidade = 	rs.getString("nacionalidade");
				dados.identidade = 		rs.getString("identidade");
				dados.enderecoRes = 	rs.getString("enderecores");
				dados.cidadeRes = 		rs.getString("cidaderes");
				dados.estadoRes = 		rs.getString("estadores");
				dados.enderecoCom = 	rs.getString("enderecocom");
				dados.cidadeCom = 		rs.getString("cidadecom");
				dados.estadoCom = 		rs.getString("estadocom");
				dados.telRes =          rs.getString("telres");
				dados.telCom =          rs.getString("telcom");
				dados.telCel =          rs.getString("telcel");
				dados.email =           rs.getString("email");
				dados.site =            rs.getString("site");
				dados.emergencia = 		rs.getString("emergencia");
				dados.recado =          rs.getString("recado");
				dados.convenio = 		rs.getString("convenio");
				dados.obsOutros = 		rs.getString("obsoutros");
				dados.IM1 =             rs.getString("im1");
				dados.IM2 =             rs.getString("im2");
				dados.IM3 =             rs.getString("im3");
				dados.IM4 =             rs.getString("im4");
				dados.observacoes = 	rs.getString("observacoes");
				dados.diaNasc = 		rs.getLong("dianasc");
				dados.mesNasc = 		rs.getLong("mesnasc");
				dados.anoNasc = 		rs.getLong("anonasc");
				dados.CEPRes =          rs.getLong("cepres");
				dados.CEPCom =          rs.getLong("cepcom");
				dados.numConvenio = 	rs.getLong("numconvenio");
				dados.sexo = 			rs.getString("sexo").charAt(0);
				dados.cpf =             rs.getLong("cpf");
				dados.datacriacao = 	rs.getTimestamp("datacriacao");
				dados.dataalteracao = 	rs.getTimestamp("dataalteracao");
				dados.criadopor = 		rs.getString("criadopor");
				dados.AnteBrac =        rs.getBoolean("AnteBrac");
				dados.txtAnteBrac =     rs.getString("txtAnteBrac");
				dados.cmbAnteBrac =     rs.getString("cmbAnteBrac");
				dados.Brac =            rs.getBoolean("Brac");
				dados.txtBrac =         rs.getString("txtBrac");
				dados.cmbBrac =         rs.getString("cmbBrac");
				dados.Cabe =            rs.getBoolean("Cabe");
				dados.txtCabe =         rs.getString("txtCabe");
				dados.cmbCabe =         rs.getString("cmbCabe");
				dados.ColCerv =         rs.getBoolean("ColCerv");
				dados.txtColCerv =      rs.getString("txtColCerv");
				dados.cmbColCerv =      rs.getString("cmbColCerv");
				dados.ColLomb =         rs.getBoolean("ColLomb");
				dados.txtColLomb =      rs.getString("txtColLomb");
				dados.cmbColLomb =      rs.getString("cmbColLomb");
				dados.ColTora =         rs.getBoolean("ColTora");
				dados.txtColTora =      rs.getString("txtColTora");
				dados.cmbColTora =      rs.getString("cmbColTora");
				dados.Coto =            rs.getBoolean("Coto");
				dados.txtCoto =         rs.getString("txtCoto");
				dados.cmbCoto =         rs.getString("cmbCoto");
				dados.Coxa =            rs.getBoolean("Coxa");
				dados.txtCoxa =         rs.getString("txtCoxa");
				dados.cmbCoxa =         rs.getString("cmbCoxa");
				dados.Dedo =            rs.getBoolean("Dedo");
				dados.txtDedo =         rs.getString("txtDedo");
				dados.cmbDedo =         rs.getString("cmbDedo");
				dados.Joel =            rs.getBoolean("Joel");
				dados.txtJoel =         rs.getString("txtJoel");
				dados.cmbJoel =         rs.getString("cmbJoel");
				dados.Mao =             rs.getBoolean("Mao");
				dados.txtMao =          rs.getString("txtMao");
				dados.cmbMao =          rs.getString("cmbMao");
				dados.Ombr =            rs.getBoolean("Ombr");
				dados.txtOmbr =         rs.getString("txtOmbr");
				dados.cmbOmbr =         rs.getString("cmbOmbr");
				dados.Pelv =            rs.getBoolean("Pelv");
				dados.txtPelv =         rs.getString("txtPelv");
				dados.cmbPelv =         rs.getString("cmbPelv");
				dados.Pern =            rs.getBoolean("Pern");
				dados.txtPern =         rs.getString("txtPern");
				dados.cmbPern =         rs.getString("cmbPern");
				dados.Punh =            rs.getBoolean("Punh");
				dados.txtPunh =         rs.getString("txtPunh");
				dados.cmbPunh =         rs.getString("cmbPunh");
				dados.Quad =            rs.getBoolean("Quad");
				dados.txtQuad =         rs.getString("txtQuad");
				dados.cmbQuad =         rs.getString("cmbQuad");
				dados.Torn =            rs.getBoolean("Torn");
				dados.txtTorn =         rs.getString("txtTorn");
				dados.cmbTorn =         rs.getString("cmbTorn");
				dados.Pe =              rs.getBoolean("Pe");
				dados.txtPe =           rs.getString("txtPe");
				dados.cmbPe =           rs.getString("cmbPe");
				
				dados.cmbDCPQ1 =        rs.getString("cmbDCPQ1");
				dados.txtDCPQ1 =        rs.getString("txtDCPQ1");
				dados.cmbDCPQ2 =        rs.getString("cmbDCPQ2");
				dados.txtDCPQ2 =        rs.getString("txtDCPQ2");
				dados.cmbDCPQ3 =        rs.getString("cmbDCPQ3");
				dados.txtDCPQ3 =        rs.getString("txtDCPQ3");
				dados.cmbDCPQ4 =        rs.getString("cmbDCPQ4");
				dados.txtDCPQ4 =        rs.getString("txtDCPQ4");
				dados.cmbDCPQ5 =        rs.getString("cmbDCPQ5");
				dados.txtDCPQ5 =        rs.getString("txtDCPQ5");
				dados.cmbDCPQ6 =        rs.getString("cmbDCPQ6");
				dados.txtDCPQ6 =        rs.getString("txtDCPQ6");
				dados.cmbDCPQ7 =        rs.getString("cmbDCPQ7");
				dados.txtDCPQ7 =        rs.getString("txtDCPQ7");
				dados.edtPaneDCP =      rs.getString("edtPaneDCP");
				
				dados.publicado = 			rs.getBoolean("publicado");
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
	}
	
	private void doSetSQLPaciente(DBConnection.PreparaInsereEnvia pie) {
		pie.setSQL(1, dados.nome);
		pie.setSQL(2, dados.estadoCivil);
		pie.setSQL(3, dados.profissao);
		pie.setSQL(4, dados.instrucao );
		pie.setSQL(5, dados.naturalidade);
		pie.setSQL(6, dados.nacionalidade);
		pie.setSQL(7, dados.identidade);
		pie.setSQL(8, dados.enderecoRes);
		pie.setSQL(9, dados.cidadeRes);
		pie.setSQL(10, dados.estadoRes);
		pie.setSQL(11, dados.enderecoCom);
		pie.setSQL(12, dados.cidadeCom);
		pie.setSQL(13, dados.estadoCom);
		pie.setSQL(14, dados.telRes);
		pie.setSQL(15, dados.telCom);
		pie.setSQL(16, dados.telCel);
		pie.setSQL(17, dados.email);
		pie.setSQL(18, dados.site);
		pie.setSQL(19, dados.emergencia);
		pie.setSQL(20, dados.recado);
		pie.setSQL(21, dados.convenio);
		pie.setSQL(22, dados.obsOutros);
		pie.setSQL(23, dados.IM1);
		pie.setSQL(24, dados.IM2);
		pie.setSQL(25, dados.IM3);
		pie.setSQL(26, dados.IM4);
		pie.setSQL(27, dados.observacoes);
		pie.setSQL(28, dados.diaNasc);
		pie.setSQL(29, dados.mesNasc);
		pie.setSQL(30, dados.anoNasc);
		pie.setSQL(31, dados.CEPRes);
		pie.setSQL(32, dados.CEPCom);
		pie.setSQL(33, dados.numConvenio);
		pie.setSQL(34, dados.sexo+"");
		pie.setSQL(35, dados.cpf);
		pie.setSQL(36, dados.datacriacao);
		pie.setSQL(37, dados.dataalteracao);
		pie.setSQL(38, dados.criadopor);
		pie.setSQL(39, dados.AnteBrac);
		pie.setSQL(40, dados.txtAnteBrac);
		pie.setSQL(41, dados.cmbAnteBrac);
		pie.setSQL(42, dados.Brac);
		pie.setSQL(43, dados.txtBrac);
		pie.setSQL(44, dados.cmbBrac);
		pie.setSQL(45, dados.Cabe);
		pie.setSQL(46, dados.txtCabe);
		pie.setSQL(47, dados.cmbCabe);
		pie.setSQL(48, dados.ColCerv);
		pie.setSQL(49, dados.txtColCerv);
		pie.setSQL(50, dados.cmbColCerv);
		pie.setSQL(51, dados.ColLomb);
		pie.setSQL(52, dados.txtColLomb);
		pie.setSQL(53, dados.cmbColLomb);
		pie.setSQL(54, dados.ColTora);
		pie.setSQL(55, dados.txtColTora);
		pie.setSQL(56, dados.cmbColTora);
		pie.setSQL(57, dados.Coto);
		pie.setSQL(58, dados.txtCoto);
		pie.setSQL(59, dados.cmbCoto);
		pie.setSQL(60, dados.Coxa);
		pie.setSQL(61, dados.txtCoxa);
		pie.setSQL(62, dados.cmbCoxa);
		pie.setSQL(63, dados.Dedo);
		pie.setSQL(64, dados.txtDedo);
		pie.setSQL(65, dados.cmbDedo);
		pie.setSQL(66, dados.Joel);
		pie.setSQL(67, dados.txtJoel);
		pie.setSQL(68, dados.cmbJoel);
		pie.setSQL(69, dados.Mao);
		pie.setSQL(70, dados.txtMao);
		pie.setSQL(71, dados.cmbMao);
		pie.setSQL(72, dados.Ombr);
		pie.setSQL(73, dados.txtOmbr);
		pie.setSQL(74, dados.cmbOmbr);
		pie.setSQL(75, dados.Pelv);
		pie.setSQL(76, dados.txtPelv);
		pie.setSQL(77, dados.cmbPelv);
		pie.setSQL(78, dados.Pern);
		pie.setSQL(79, dados.txtPern);
		pie.setSQL(80, dados.cmbPern);
		pie.setSQL(81, dados.Punh);
		pie.setSQL(82, dados.txtPunh);
		pie.setSQL(83, dados.cmbPunh);
		pie.setSQL(84, dados.Quad);
		pie.setSQL(85, dados.txtQuad);
		pie.setSQL(86, dados.cmbQuad);
		pie.setSQL(87, dados.Torn);
		pie.setSQL(88, dados.txtTorn);
		pie.setSQL(89, dados.cmbTorn);
		pie.setSQL(90, dados.Pe);
		pie.setSQL(91, dados.txtPe);
		pie.setSQL(92, dados.cmbPe);
		
		pie.setSQL(93, dados.cmbDCPQ1);
		pie.setSQL(94, dados.txtDCPQ1);
		pie.setSQL(95, dados.cmbDCPQ2);
		pie.setSQL(96, dados.txtDCPQ2);
		pie.setSQL(97, dados.cmbDCPQ3);
		pie.setSQL(98, dados.txtDCPQ3);
		pie.setSQL(99, dados.cmbDCPQ4);
		pie.setSQL(100, dados.txtDCPQ4);
		pie.setSQL(101, dados.cmbDCPQ5);
		pie.setSQL(102, dados.txtDCPQ5);
		pie.setSQL(103, dados.cmbDCPQ6);
		pie.setSQL(104, dados.txtDCPQ6);
		pie.setSQL(105, dados.cmbDCPQ7);
		pie.setSQL(106, dados.txtDCPQ7);
		pie.setSQL(107, dados.edtPaneDCP);
		
		pie.setSQL(108, dados.publicado);
		
	}
	
	private void insereDB() {
		String sqlPreparado1 = "INSERT INTO dpaciente (nome, estadocivil, profissao," +
		" instrucao, naturalidade, nacionalidade, identidade, enderecores," +
		" cidaderes, estadores, enderecocom, cidadecom, estadocom, telres," +
		" telcom, telcel, email, site, emergencia, recado, convenio," +
		" obsoutros, im1, im2, im3, im4, observacoes, dianasc, mesnasc," +
		" anonasc, cepres, cepcom, numconvenio, sexo, cpf, datacriacao," +
		" dataalteracao, criadopor, AnteBrac, txtAnteBrac, cmbAnteBrac, Brac,"+
		" txtBrac, cmbBrac, Cabe, txtCabe, cmbCabe, ColCerv, txtColCerv, cmbColCerv,"+
		" ColLomb, txtColLomb, cmbColLomb, ColTora, txtColTora, cmbColTora, Coto,"+
		" txtCoto, cmbCoto, Coxa, txtCoxa, cmbCoxa, Dedo, txtDedo, cmbDedo, Joel, txtJoel,"+
		" cmbJoel, Mao, txtMao, cmbMao, Ombr, txtOmbr, cmbOmbr, Pelv, txtPelv, cmbPelv, Pern,"+
		" txtPern, cmbPern, Punh, txtPunh, cmbPunh, Quad, txtQuad, cmbQuad, Torn, txtTorn,"+
		" cmbTorn, Pe, txtPe, cmbPe, cmbDCPQ1, txtDCPQ1, cmbDCPQ2, txtDCPQ2, cmbDCPQ3, txtDCPQ3,"+
		"cmbDCPQ4, txtDCPQ4, cmbDCPQ5, txtDCPQ5, cmbDCPQ6, txtDCPQ6, cmbDCPQ7,"+
		"txtDCPQ7, edtPaneDCP, publicado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
		" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"+
		" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"+
		" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"+
		" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		DBConnection.PreparaInsereEnvia pie1 = db.getPreparaInsereEnvia(sqlPreparado1);
		doSetSQLPaciente(pie1);
		pie1.executa();
		String sqlPreparado2 = "SELECT * FROM dpaciente WHERE dataalteracao=?";
		DBConnection.PreparaInsereEnvia pie2 = db.getPreparaInsereEnvia(sqlPreparado2);
		pie2.setSQL(1, dados.dataalteracao);
		ResultSet rs = pie2.executaQuery();
		try {
			if (rs.next()) dados.nID = rs.getLong("nid");
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		
	}
	
	private void updateDB() {
		String sqlPreparado = "UPDATE dpaciente SET nome=?, estadocivil=?, profissao=?," +
		" instrucao=?, naturalidade=?, nacionalidade=?, identidade=?, enderecores=?," +
		" cidaderes=?, estadores=?, enderecocom=?, cidadecom=?, estadocom=?, telres=?," +
		" telcom=?, telcel=?, email=?, site=?, emergencia=?, recado=?, convenio=?," +
		" obsoutros=?, im1=?, im2=?, im3=?, im4=?, observacoes=?, dianasc=?, mesnasc=?," +
		" anonasc=?, cepres=?, cepcom=?, numconvenio=?, sexo=?, cpf=?, datacriacao=?," +
		" dataalteracao=?, criadopor=?, AnteBrac=?, txtAnteBrac=?, cmbAnteBrac=?, Brac=?,"+
		" txtBrac=?, cmbBrac=?, Cabe=?, txtCabe=?, cmbCabe=?, ColCerv=?, txtColCerv=?, cmbColCerv=?,"+
		" ColLomb=?, txtColLomb=?, cmbColLomb=?, ColTora=?, txtColTora=?, cmbColTora=?, Coto=?,"+
		" txtCoto=?, cmbCoto=?, Coxa=?, txtCoxa=?, cmbCoxa=?, Dedo=?, txtDedo=?, cmbDedo=?, Joel=?, txtJoel=?,"+
		" cmbJoel=?, Mao=?, txtMao=?, cmbMao=?, Ombr=?, txtOmbr=?, cmbOmbr=?, Pelv=?, txtPelv=?, cmbPelv=?, Pern=?,"+
		" txtPern=?, cmbPern=?, Punh=?, txtPunh=?, cmbPunh=?, Quad=?, txtQuad=?, cmbQuad=?, Torn=?, txtTorn=?,"+
		" cmbTorn=?, Pe=?, txtPe=?, cmbPe=?, cmbDCPQ1=?, txtDCPQ1=?, cmbDCPQ2=?, txtDCPQ2=?, cmbDCPQ3=?, txtDCPQ3=?,"+
		" cmbDCPQ4=?, txtDCPQ4=?, cmbDCPQ5=?, txtDCPQ5=?, cmbDCPQ6=?, txtDCPQ6=?, cmbDCPQ7=?, txtDCPQ7=?, edtPaneDCP=?, publicado=? WHERE nid=?";
		
		DBConnection.PreparaInsereEnvia pie = db.getPreparaInsereEnvia(sqlPreparado);
		doSetSQLPaciente(pie);
		pie.setSQL(109, dados.nID); 
		pie.executa();
	}
	
	protected void deleteProject(long nID) {
		DBConnection.PreparaInsereEnvia pie;
		pie = db.getPreparaInsereEnvia("DELETE FROM dpaciente WHERE nid=?");
		pie.setSQL(1, nID);
		pie.executa();
		pie = db.getPreparaInsereEnvia("DELETE FROM imagedata WHERE nidpaciente=?");
		pie.setSQL(1, nID);
		pie.executa();
		pie = db.getPreparaInsereEnvia("DELETE FROM pontomedido WHERE nidpaciente=?");
		pie.setSQL(1, nID);
		pie.executa();
		pie = db.getPreparaInsereEnvia("DELETE FROM angulomedido WHERE nidpaciente=?");
		pie.setSQL(1, nID);
		pie.executa();
		pie = db.getPreparaInsereEnvia("DELETE FROM distanciamedida WHERE nidpaciente=?");
		pie.setSQL(1, nID);
		pie.executa();
	}
	
	protected void cleanProject() {
		db.addBatch("DELETE FROM dpaciente");
		db.addBatch("DELETE FROM imagedata");
		db.addBatch("DELETE FROM pontomedido");
		db.addBatch("DELETE FROM angulomedido");
		db.addBatch("DELETE FROM distanciamedida");
		db.executeBatch();
	}
}

