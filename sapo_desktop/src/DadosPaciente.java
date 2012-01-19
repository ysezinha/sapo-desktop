/*
 * DadosPaciente.java
 *
 * Created on 18 de Dezembro de 2004, 22:02
 */

/**
 *
 * @author Edison Puig Maldonado
 */

import java.sql.Timestamp;

import com.dati.util.DataHora;


class DadosPaciente {
    /** Dados do paciente */
    public String nome="";
    public String estadoCivil="(N/D)"; 
    public String profissao="";
    public String instrucao=""; 
    public String naturalidade="";
    public String nacionalidade="";
    public String identidade="";
    public String enderecoRes="";
    public String cidadeRes="";
    public String estadoRes="--";
    public String enderecoCom="";
    public String cidadeCom="";
    public String estadoCom="--";
    public String telRes="";
    public String telCom="";
    public String telCel="";
    public String email="";
    public String site="";
    public String emergencia="";
    public String recado="";
    public String convenio="";
    public String obsOutros="";
    public String IM1="";
    public String IM2="";
    public String IM3="";
    public String IM4="";
    public String observacoes="";
    public String criadopor="";
    public long nID = -1; // ID do banco de dados
    public long diaNasc = 1;
    public long mesNasc = 1;
    public long anoNasc = 1980;
    public long CEPRes = 0;
    public long CEPCom = 0;
    public long numConvenio = 0;
    public long cpf = 0;
    public char sexo = 'M';
    public Timestamp datacriacao = DataHora.getDate();
    public Timestamp dataalteracao = DataHora.getDate();
   
    public boolean AnteBrac = false;
    public String  txtAnteBrac = "";
    public String cmbAnteBrac = "";
    
    public boolean Brac = false;
    public String  txtBrac = "";
    public String cmbBrac = "";
    
    public boolean Cabe = false;
    public String  txtCabe = "";
    public String cmbCabe = "";
    
    public boolean ColCerv = false;
    public String  txtColCerv = "";
    public String cmbColCerv = "";
    
    public boolean ColLomb = false;
    public String  txtColLomb = "";
    public String cmbColLomb = "";
    
    public boolean ColTora = false;
    public String  txtColTora = "";
    public String cmbColTora = "";
    
    public boolean Coto = false;
    public String  txtCoto = "";
    public String cmbCoto = "";
    
    public boolean Coxa = false;
    public String  txtCoxa = "";
    public String cmbCoxa = "";
    
    public boolean Dedo = false;
    public String  txtDedo = "";
    public String cmbDedo = "";
    
    public boolean Joel = false;
    public String  txtJoel = "";
    public String cmbJoel = "";
    
    public boolean Mao = false;
    public String  txtMao = "";
    public String cmbMao = "";
    
    public boolean Ombr = false;
    public String  txtOmbr = "";
    public String cmbOmbr = "";
    
    public boolean Pelv = false;
    public String  txtPelv = "";
    public String cmbPelv = "";
    
    public boolean Pern = false;
    public String  txtPern = "";
    public String cmbPern = "";
    
    public boolean Punh = false;
    public String  txtPunh = "";
    public String cmbPunh = "";
    
    public boolean Quad = false;
    public String  txtQuad = "";
    public String cmbQuad = "";
    
    public boolean Torn = false;
    public String  txtTorn = "";
    public String cmbTorn = "";
    
    public boolean Pe = false;
    public String  txtPe = "";
    public String cmbPe = "";
    
    public String cmbDCPQ1 = "";
    public String txtDCPQ1 = "";
    
    public String cmbDCPQ2 = "";
    public String txtDCPQ2 = "";    
       
    public String cmbDCPQ3 = "";
    public String txtDCPQ3 = "";
    
    public String cmbDCPQ4 = "";
    public String txtDCPQ4 = "";
    
    public String cmbDCPQ5 = "";
    public String txtDCPQ5 = "";
    
    public String cmbDCPQ6 = "";
    public String txtDCPQ6 = "";
    
    public String cmbDCPQ7 = "";
    public String txtDCPQ7 = "";
    
    public String edtPaneDCP = "";
    
    
    /** classe que tem os dados e os modelos das analises */
    public ImageData[] imgData;
    
    // indica se foi publicado na base de dados nacional (true)
    public boolean publicado = false;
    
    DadosPaciente(){
        imgData = new ImageData[SAPO.maxImg+1];
		for (int i=0; i < SAPO.maxImg+1 ; i++) imgData[i] = new ImageData();
    }
    
    public int getVista(String vista) {
    	int retorno = -1;
    	for (int i=0; i < SAPO.maxImg+1 ; i++) {
			if ((imgData[i].vista!=null) && (imgData[i].vista.equalsIgnoreCase(vista))) {
				retorno = i;
				break;
			}
		}
    	return retorno;
    }
    
}
