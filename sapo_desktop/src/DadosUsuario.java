/*
 * DadosUsuario.java
 *
 * Created on 30 de Janeiro de 2005, 22:01
 */

/**
 *
 * @author Edison Puig Maldonado
 */


public class DadosUsuario /*implements Serializable*/ {
    
    protected long      nid = 0; 
    protected boolean	autentica = false;  // conf
    protected String	username = "sapo";  // conf
    protected String	senha = "sapo";  // conf
    protected String	nomecompleto = "";  // user
    protected String	qualificacao = "";  // user
    protected String	associacaoclasse = "";  // user
    protected String	instituicao = "";  // user
    protected String	depto = "";  // user
    protected String	logotipo = "";  // user
    protected String	outrosdados = "";  // user
    protected String	enderecocep = "";  // user
    protected String	cidade = "";  // user
    protected String	estado = "";  // user
    protected String	telefone = "";  // user
    protected String	fax = "";  // user
    protected String	email = "";  // user
    protected String	site = "";  // user
    protected String	dirprojetos = "";  // conf
    protected String	dirimagens = "";  // conf
    protected String	dirrelatorios = "";  // conf
    protected String	dirbackup = "";  // conf
    protected String	idioma = "";  // conf
    protected String	servidoremail = "";  // conf
    protected String	useremail = "";  // conf
    protected String	senhaemail = "";  // conf
    protected boolean   boasvindas = true;  // conf
    protected boolean   mostradicas = true;  // conf
    protected boolean   toolbar = true;  // comando
    protected boolean   tb1 = true;  // comando
    protected boolean   tb2 = true;  // comando
    protected boolean   tb3 = true;  // comando
    protected boolean   tb4 = true;  // comando
    protected boolean   tb5 = true;  // comando
    protected boolean   tb6 = true;  // comando
    protected boolean   tb7 = true;  // comando
    protected boolean   tb8 = true;  // comando
    protected boolean   tb9 = true;  // comando
    protected boolean   tb10 = true;  // comando
    protected String	ajudaonline = "";  // conf
    protected String	unidadecomprimento = "cm";  // conf
    protected String	unidadeangulo = "graus";   // conf
    protected int       decimalNumbersFormat = 1;
    
    protected Protocolo protoSapo  = new Protocolo();
    protected Protocolo protoUser1 = new Protocolo();
    protected Protocolo protoUser2 = new Protocolo();
    protected Protocolo protoUser3 = new Protocolo();

}
