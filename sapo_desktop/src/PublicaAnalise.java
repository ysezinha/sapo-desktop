import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.dati.image.PontoMedido;
import com.thoughtworks.xstream.XStream;

/**
 * 
 */

/**
 * @author Edison Puig Maldonado
 *
 */

public class PublicaAnalise {
	
	SAPO sapo;
	Protocolo protoSAPO;
	DadosPaciente dados = new DadosPaciente();
	String dadosXML;
	String username, senha;
	int nAnt, nPos, nLD,  nLE;
	boolean servico = false;
	
	PublicaAnalise(SAPO sapo) {
		this.sapo = sapo;
		protoSAPO = sapo.getProtocol("Sapo");
	}
	
	public boolean publica() {
		boolean sucesso = false;
		username = sapo.user.dados.username;
		senha = sapo.user.dados.senha;
		delay(2000);
		if (!confere()) return false;
		copiaDadosPaciente();
		if (!servico) {
			sapo.projMan.waitMsg.setText("Acessando serviço...");
			acessaServlet();
			servico = true;
		}
		sapo.projMan.waitMsg.setText("Enviando para servidor...");
		delay(3000);
		SAPOWebServiceProxy SWSProxyid = new SAPOWebServiceProxy();
		SAPOWebService sapoWeb = SWSProxyid.getSAPOWebService();
		int resposta = -1;
		try {
			resposta = sapoWeb.recebeDados(username, dadosXML, senha);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		switch (resposta) {
			case -1: JOptionPane.showMessageDialog(sapo,
						"Houve um erro de comunicação.\n" +
						"Tente novamente após alguns instantes, ou entre em contato\n" +
						"com a coordenação do projeto para solucionar este problema.","Erro de Comunicação",JOptionPane.ERROR_MESSAGE); servico=false; break;
			case  0: sucesso = true; break;
			case  1: JOptionPane.showMessageDialog(sapo,
						"É necessário obter um nome de usuário e senha de acesso p/\n" +
						"publicar na base de dados nacional.  Entre em contato com a\n" +
						"coordenação do projeto para solicitar a autorização. Depois,\n" +
						"configure no menu  \"Ferramentas\",  item \"Configurações do\n" +
						"Programa\", na aba \"Segurança\"","Autenticação Falhou",JOptionPane.ERROR_MESSAGE); break;
			case  2: JOptionPane.showMessageDialog(sapo,
						"Houve um erro de banco de dados.\n" +
						"Tente novamente após alguns instantes, ou entre em contato\n" +
						"com a coordenação do projeto para solucionar este problema.","Erro de Banco de Dados",JOptionPane.ERROR_MESSAGE); break;
		}
		return sucesso;
	}

	private void acessaServlet() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(0,0);
		SAPO.center(frame);
		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);
		jep.setContentType("text/html");
		frame.getContentPane().add(jep);
		frame.setVisible(true);
		jep.setVisible(true);
		URL url = null;
		try {
			url = new URL(SAPO.SAPOWEB);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (url != null) {
			try {
				jep.setPage(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		delay(3000);
		frame.setVisible(false);
		frame.dispose();
	}

	private boolean copiaDadosPaciente() {
		boolean retorno = true; 
	    
		//dados.nome = sapo.paciente.dados.nome;
		dados.estadoCivil = sapo.paciente.dados.estadoCivil ;
		dados.profissao = sapo.paciente.dados.profissao;
		dados.instrucao = sapo.paciente.dados.instrucao;
		dados.naturalidade = sapo.paciente.dados.naturalidade;
		dados.nacionalidade = sapo.paciente.dados.nacionalidade;
		//dados.identidade = sapo.paciente.dados.identidade;
		//dados.enderecoRes = sapo.paciente.dados.enderecoRes;
		dados.cidadeRes = sapo.paciente.dados.cidadeRes;
		dados.estadoRes = sapo.paciente.dados.estadoRes;
		//dados.enderecoCom = sapo.paciente.dados.enderecoCom;
		dados.cidadeCom = sapo.paciente.dados.cidadeCom;
		dados.estadoCom = sapo.paciente.dados.estadoCom;
		//dados.telRes = sapo.paciente.dados.telRes;
		//dados.telCom = sapo.paciente.dados.telCom;
		//dados.telCel = sapo.paciente.dados.telCel;
		//dados.email = sapo.paciente.dados.email;
		//dados.site = sapo.paciente.dados.site;
		//dados.emergencia = sapo.paciente.dados.emergencia;
		//dados.recado = sapo.paciente.dados.recado;
		//dados.convenio = sapo.paciente.dados.convenio;
		//dados.obsOutros = sapo.paciente.dados.obsOutros;
		dados.IM1 = sapo.paciente.dados.IM1;
		dados.IM2 = sapo.paciente.dados.IM2;
		dados.IM3 = sapo.paciente.dados.IM3;
		dados.IM4 = sapo.paciente.dados.IM4;
		//dados.observacoes = sapo.paciente.dados.observacoes;
		dados.criadopor = sapo.paciente.dados.criadopor;
		//dados.nID = sapo.paciente.dados.nID;
		dados.diaNasc = sapo.paciente.dados.diaNasc;
		dados.mesNasc = sapo.paciente.dados.mesNasc;
		dados.anoNasc = sapo.paciente.dados.anoNasc;
		//dados.CEPRes = sapo.paciente.dados.CEPRes;
		//dados.CEPCom = sapo.paciente.dados.CEPCom;
		//dados.numConvenio = sapo.paciente.dados.numConvenio;
		//dados.cpf = sapo.paciente.dados.cpf;
		dados.sexo = sapo.paciente.dados.sexo;
		dados.datacriacao = sapo.paciente.dados.datacriacao;
		dados.dataalteracao = sapo.paciente.dados.dataalteracao;
		dados.AnteBrac = sapo.paciente.dados.AnteBrac;
		dados.txtAnteBrac = sapo.paciente.dados.txtAnteBrac;
		dados.cmbAnteBrac = sapo.paciente.dados.cmbAnteBrac;
		dados.Brac = sapo.paciente.dados.Brac;
		dados.txtBrac = sapo.paciente.dados.txtBrac;
		dados.cmbBrac = sapo.paciente.dados.cmbBrac;
		dados.Cabe = sapo.paciente.dados.Cabe;
		dados.txtCabe = sapo.paciente.dados.txtCabe;
		dados.cmbCabe = sapo.paciente.dados.cmbCabe;
		dados.ColCerv = sapo.paciente.dados.ColCerv;
		dados.txtColCerv = sapo.paciente.dados.txtColCerv;
		dados.cmbColCerv = sapo.paciente.dados.cmbColCerv;
		dados.ColLomb = sapo.paciente.dados.ColLomb;
		dados.txtColLomb = sapo.paciente.dados.txtColLomb;
		dados.cmbColLomb = sapo.paciente.dados.cmbColLomb;
		dados.ColTora = sapo.paciente.dados.ColTora;
		dados.txtColTora = sapo.paciente.dados.txtColTora;
		dados.cmbColTora = sapo.paciente.dados.cmbColTora;
		dados.Coto = sapo.paciente.dados.Coto;
		dados.txtCoto = sapo.paciente.dados.txtCoto;
		dados.cmbCoto = sapo.paciente.dados.cmbCoto;
		dados.Coxa = sapo.paciente.dados.Coxa;
		dados.txtCoxa = sapo.paciente.dados.txtCoxa;
		dados.cmbCoxa = sapo.paciente.dados.cmbCoxa;
		dados.Dedo = sapo.paciente.dados.Dedo;
		dados.txtDedo = sapo.paciente.dados.txtDedo;
		dados.cmbDedo = sapo.paciente.dados.cmbDedo;
		dados.Joel = sapo.paciente.dados.Joel;
		dados.txtJoel = sapo.paciente.dados.txtJoel;
		dados.cmbJoel = sapo.paciente.dados.cmbJoel;
		dados.Mao = sapo.paciente.dados.Mao;
		dados.txtMao = sapo.paciente.dados.txtMao;
		dados.cmbMao = sapo.paciente.dados.cmbMao;
		dados.Ombr = sapo.paciente.dados.Ombr;
		dados.txtOmbr = sapo.paciente.dados.txtOmbr;
		dados.cmbOmbr = sapo.paciente.dados.cmbOmbr;
		dados.Pelv = sapo.paciente.dados.Pelv;
		dados.txtPelv = sapo.paciente.dados.txtPelv;
		dados.cmbPelv = sapo.paciente.dados.cmbPelv;
		dados.Pern = sapo.paciente.dados.Pern;
		dados.txtPern = sapo.paciente.dados.txtPern;
		dados.cmbPern = sapo.paciente.dados.cmbPern;
		dados.Punh = sapo.paciente.dados.Punh;
		dados.txtPunh = sapo.paciente.dados.txtPunh;
		dados.cmbPunh = sapo.paciente.dados.cmbPunh;
		dados.Quad = sapo.paciente.dados.Quad;
		dados.txtQuad = sapo.paciente.dados.txtQuad;
		dados.cmbQuad = sapo.paciente.dados.cmbQuad;
		dados.Torn = sapo.paciente.dados.Torn;
		dados.txtTorn = sapo.paciente.dados.txtTorn;
		dados.cmbTorn = sapo.paciente.dados.cmbTorn;
		dados.Pe = sapo.paciente.dados.Pe;
		dados.txtPe = sapo.paciente.dados.txtPe;
		dados.cmbPe = sapo.paciente.dados.cmbPe;
		dados.cmbDCPQ1 = sapo.paciente.dados.cmbDCPQ1;
		dados.txtDCPQ1 = sapo.paciente.dados.txtDCPQ1;
		dados.cmbDCPQ2 = sapo.paciente.dados.cmbDCPQ2;
		dados.txtDCPQ2 = sapo.paciente.dados.txtDCPQ2;
		dados.cmbDCPQ3 = sapo.paciente.dados.cmbDCPQ3;
		dados.txtDCPQ3 = sapo.paciente.dados.txtDCPQ3;
		dados.cmbDCPQ4 = sapo.paciente.dados.cmbDCPQ4;
		dados.txtDCPQ4 = sapo.paciente.dados.txtDCPQ4;
		dados.cmbDCPQ5 = sapo.paciente.dados.cmbDCPQ5;
		dados.txtDCPQ5 = sapo.paciente.dados.txtDCPQ5;
		dados.cmbDCPQ6 = sapo.paciente.dados.cmbDCPQ6;
		dados.txtDCPQ6 = sapo.paciente.dados.txtDCPQ6;
		dados.cmbDCPQ7 = sapo.paciente.dados.cmbDCPQ7;
		dados.txtDCPQ7 = sapo.paciente.dados.txtDCPQ7;
		//dados.edtPaneDCP = sapo.paciente.dados.edtPaneDCP;
		copiaImageDatas();

		XStream xstream = new XStream();
		dadosXML = xstream.toXML(dados);

		return retorno;
	}

	private void copiaImageDatas() {
		ImageData imd;
		imd = sapo.paciente.dados.imgData[nAnt];
		dados.imgData[0].setEssencial(imd.pontosList, imd.anguloVertical, imd.escalaX, imd.escalaY, imd.xAnchor, imd.yAnchor,imd.imgRotate, imd.vista);
		imd = sapo.paciente.dados.imgData[nPos];
		dados.imgData[1].setEssencial(imd.pontosList, imd.anguloVertical, imd.escalaX, imd.escalaY, imd.xAnchor, imd.yAnchor,imd.imgRotate, imd.vista);
		imd = sapo.paciente.dados.imgData[nLD];
		dados.imgData[2].setEssencial(imd.pontosList, imd.anguloVertical, imd.escalaX, imd.escalaY, imd.xAnchor, imd.yAnchor,imd.imgRotate, imd.vista);
		imd = sapo.paciente.dados.imgData[nLE];
		dados.imgData[3].setEssencial(imd.pontosList, imd.anguloVertical, imd.escalaX, imd.escalaY, imd.xAnchor, imd.yAnchor,imd.imgRotate, imd.vista);
	}

	private boolean confere() {
		boolean retorno = true;
		String message = "";
		
		nAnt = sapo.paciente.dados.getVista("anterior");
		nPos = sapo.paciente.dados.getVista("posterior");
		nLD  = sapo.paciente.dados.getVista("lateral direita");
		nLE  = sapo.paciente.dados.getVista("lateral esquerda");
		if (nAnt == -1) message += " Não foi encontrada vista anterior.\n";
		if (nPos == -1) message += " Não foi encontrada vista posterior.\n";
		if (nLD == -1)  message += " Não foi encontrada vista lateral esquerda.\n";
		if (nLE == -1)  message += " Não foi encontrada vista lateral direita.\n";
		if (!message.equals("")) {
			JOptionPane.showMessageDialog(sapo,message,"ERROS Foram Encontrados",JOptionPane.ERROR_MESSAGE);
			retorno = false;
		}
		else {
			if (!sapo.paciente.dados.imgData[nAnt].isVertCal())     message += " A imagem da vista anterior não foi calibrada quanto à vertical.\n";
			if (!sapo.paciente.dados.imgData[nAnt].isScaleCal())    message += " A imagem da vista anterior não foi calibrada quanto à escala.\n";
			if (!sapo.paciente.dados.imgData[nPos].isVertCal())     message += " A imagem da vista posterior não foi calibrada quanto à vertical.\n";
			if (!sapo.paciente.dados.imgData[nPos].isScaleCal())    message += " A imagem da vista posterior não foi calibrada quanto à escala.\n";
			if (!sapo.paciente.dados.imgData[nLD].isVertCal())      message += " A imagem da vista lateral direita não foi calibrada quanto à vertical.\n";
			if (!sapo.paciente.dados.imgData[nLD].isScaleCal())     message += " A imagem da vista lateral direita não foi calibrada quanto à escala.\n";
			if (!sapo.paciente.dados.imgData[nLE].isVertCal())      message += " A imagem da vista lateral esquerda não foi calibrada quanto à vertical.\n";
			if (!sapo.paciente.dados.imgData[nLE].isScaleCal())     message += " A imagem da vista lateral esquerda não foi calibrada quanto à escala.\n";
			if (!isSAPOProtocol(sapo.paciente.dados.imgData[nAnt])) message += " Os pontos da vista anterior não contemplam o protocolo SAPO.\n";
			if (!isSAPOProtocol(sapo.paciente.dados.imgData[nPos])) message += " Os pontos da vista posterior não contemplam o protocolo SAPO.\n";
			if (!isSAPOProtocol(sapo.paciente.dados.imgData[nLD] )) message += " Os pontos da vista lateral direita não contemplam o protocolo SAPO.\n";
			if (!isSAPOProtocol(sapo.paciente.dados.imgData[nLE] )) message += " Os pontos da vista lateral esquerda não contemplam o protocolo SAPO.\n";
			if (!message.equals("")) {
				JOptionPane.showMessageDialog(sapo,message,"ERROS Foram Encontrados",JOptionPane.ERROR_MESSAGE);
				retorno = false;
			}
		}
		return retorno;
	}

	public boolean isSAPOProtocol(ImageData imgData) {
		boolean retorno = true;
		boolean contem;
		String vista = imgData.vista;
		String nomePonto, nomePontoImgData;
		String[] labels = {""}; 
		ArrayList pontosSAPO = new ArrayList();
		if (vista.equalsIgnoreCase("anterior")) pontosSAPO = protoSAPO.frente;
		if (vista.equalsIgnoreCase("posterior")) pontosSAPO = protoSAPO.poster;
		if (vista.equalsIgnoreCase("lateral direita")) pontosSAPO = protoSAPO.latDir;
		if (vista.equalsIgnoreCase("lateral esquerda")) pontosSAPO = protoSAPO.latEsq;
		if (pontosSAPO.size() == 0) return false;
		labels = protoSAPO.getLabels(vista);
		for (int i=0; i<pontosSAPO.size(); i++) {
			contem = false;
			nomePonto = labels[((Integer)pontosSAPO.get(i)).intValue()];
			for (int j=0; j<imgData.pontosList.size(); j++) {
				nomePontoImgData = ((PontoMedido)imgData.pontosList.get(j)).nome;
				if (nomePontoImgData.equalsIgnoreCase(nomePonto)) {
					contem = true;
					break;
				}
			}
			if (!contem) {
				retorno = false;
				break;
			}
		}
		return retorno;
	}
	
	private void delay(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
