import java.sql.ResultSet;
import java.sql.SQLException;

import com.dati.data.DBConnection;

/*
 * User.java
 *
 * Created on 30 de Janeiro de 2005, 21:58
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

public class User {
	
	public DadosUsuario dados;
	public UIUser dadosInterface;
	private SAPO sapo;
	private DBConnection db;
	
	public User(SAPO sapo) {
		dados = new DadosUsuario();
		this.sapo = sapo;
	}
	
	public User(DBConnection db, SAPO sapo) {
		dados = new DadosUsuario();
		this.sapo = sapo;
		this.db = db;
		lerDB();
	}
	
	public UIUser getUIUser() {
		return new UIUser(this);
	}
	
	
	/** lê configurações do usuário no banco e dados */
	public void lerDB () {
		ResultSet rs = db.executeQuery("SELECT * FROM configuracoes");
		try {
			if (rs.next()) {
				dados.nid = rs.getLong("nid");
				dados.autentica = rs.getBoolean("autentica");
				dados.username = rs.getString("username");
				dados.senha = rs.getString("senha");
				dados.nomecompleto = rs.getString("nomecompleto");
				dados.qualificacao = rs.getString("qualificacao");
				dados.associacaoclasse = rs.getString("associacaoclasse");
				dados.instituicao = rs.getString("instituicao");
				dados.depto = rs.getString("depto");
				dados.logotipo = rs.getString("logotipo");
				dados.outrosdados = rs.getString("outrosdados");
				dados.enderecocep = rs.getString("enderecocep");
				dados.cidade = rs.getString("cidade");
				dados.estado = rs.getString("estado");
				dados.telefone = rs.getString("telefone");
				dados.fax = rs.getString("fax");
				dados.email = rs.getString("email");
				dados.site = rs.getString("site");
				dados.dirprojetos = rs.getString("dirprojetos");
				dados.dirimagens = rs.getString("dirimagens");
				dados.dirrelatorios = rs.getString("dirrelatorios");
				dados.dirbackup = rs.getString("dirbackup");
				dados.idioma = rs.getString("idioma");
				dados.servidoremail = rs.getString("servidoremail");
				dados.useremail = rs.getString("useremail");
				dados.senhaemail = rs.getString("senhaemail");
				dados.boasvindas = rs.getBoolean("boasvindas");
				dados.mostradicas = rs.getBoolean("mostradicas");
				dados.toolbar = rs.getBoolean("toolbar");
				dados.tb1 = rs.getBoolean("tb1");
				dados.tb2 = rs.getBoolean("tb2");
				dados.tb3 = rs.getBoolean("tb3");
				dados.tb4 = rs.getBoolean("tb4");
				dados.tb5 = rs.getBoolean("tb5");
				dados.tb6 = rs.getBoolean("tb6");
				dados.tb7 = rs.getBoolean("tb7");
				dados.tb8 = rs.getBoolean("tb8");
				dados.tb9 = rs.getBoolean("tb9");
				dados.tb10 = rs.getBoolean("tb10");
				dados.ajudaonline = rs.getString("ajudaonline");
				dados.unidadecomprimento = rs.getString("unidadecomprimento");
				dados.unidadeangulo = rs.getString("unidadeangulo");
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		//le protocolos
		String sqlPreparado3;
		sqlPreparado3 = "SELECT * FROM protofrente";
		rs = db.executeQuery(sqlPreparado3);
		try {
			while (rs.next()) {
				String nome = rs.getString("nome"); 
				if(nome.equalsIgnoreCase("User1"))
					dados.protoUser1.frente.add(new Integer(rs.getInt("frente")));
				if(nome.equalsIgnoreCase("User2"))
					dados.protoUser2.frente.add(new Integer(rs.getInt("frente")));
				if(nome.equalsIgnoreCase("User3"))
					dados.protoUser3.frente.add(new Integer(rs.getInt("frente")));
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		sqlPreparado3 = "SELECT * FROM protolatesq";
		rs = db.executeQuery(sqlPreparado3);
		try {
			while (rs.next()) {
				String nome = rs.getString("nome"); 
				if(nome.equalsIgnoreCase("User1"))
					dados.protoUser1.latEsq.add(new Integer(rs.getInt("latesq")));
				if(nome.equalsIgnoreCase("User2"))
					dados.protoUser2.latEsq.add(new Integer(rs.getInt("latesq")));
				if(nome.equalsIgnoreCase("User3"))
					dados.protoUser3.latEsq.add(new Integer(rs.getInt("latesq")));
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		sqlPreparado3 = "SELECT * FROM protolatdir";
		rs = db.executeQuery(sqlPreparado3);
		try {
			while (rs.next()) {
				String nome = rs.getString("nome"); 
				if(nome.equalsIgnoreCase("User1"))
					dados.protoUser1.latDir.add(new Integer(rs.getInt("latdir")));
				if(nome.equalsIgnoreCase("User2"))
					dados.protoUser2.latDir.add(new Integer(rs.getInt("latdir")));
				if(nome.equalsIgnoreCase("User3"))
					dados.protoUser3.latDir.add(new Integer(rs.getInt("latdir")));
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		sqlPreparado3 = "SELECT * FROM protoposter";
		rs = db.executeQuery(sqlPreparado3);
		try {
			while (rs.next()) {
				String nome = rs.getString("nome"); 
				if(nome.equalsIgnoreCase("User1"))
					dados.protoUser1.poster.add(new Integer(rs.getInt("poster")));
				if(nome.equalsIgnoreCase("User2"))
					dados.protoUser2.poster.add(new Integer(rs.getInt("poster")));
				if(nome.equalsIgnoreCase("User3"))
					dados.protoUser3.poster.add(new Integer(rs.getInt("poster")));
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
                
		
	}//lerDB
	
	/** escreve configurações do usuário no banco e dados */
	public void escreveDB () {
		String sqlPreparado = "UPDATE configuracoes SET autentica=?, username=?, senha=?, nomecompleto=?," +
		" qualificacao=?, associacaoclasse=?, instituicao=?, depto=?, logotipo=?," +
		" outrosdados=?, enderecocep=?, cidade=?, estado=?, telefone=?, fax=?, email=?, site=?," +
		" dirprojetos=?, dirimagens=?, dirrelatorios=?, dirbackup=?, idioma=?, servidoremail=?," +
		" useremail=?, senhaemail=?, boasvindas=?, mostradicas=?, toolbar=?, tb1=?, tb2=?, tb3=?," +
		" tb4=?, tb5=?, tb6=?, tb7=?, tb8=?, tb9=?, tb10=?, ajudaonline=?, unidadecomprimento=?," +
		" unidadeangulo=? WHERE nid=?";
		DBConnection.PreparaInsereEnvia pie = db.getPreparaInsereEnvia(sqlPreparado);
		pie.setSQL(1,dados.autentica);
		pie.setSQL(2,dados.username);
		pie.setSQL(3,dados.senha);
		pie.setSQL(4,dados.nomecompleto);
		pie.setSQL(5,dados.qualificacao);
		pie.setSQL(6,dados.associacaoclasse);
		pie.setSQL(7,dados.instituicao);
		pie.setSQL(8,dados.depto);
		pie.setSQL(9,dados.logotipo);
		pie.setSQL(10,dados.outrosdados);
		pie.setSQL(11,dados.enderecocep);
		pie.setSQL(12,dados.cidade);
		pie.setSQL(13,dados.estado);
		pie.setSQL(14,dados.telefone);
		pie.setSQL(15,dados.fax);
		pie.setSQL(16,dados.email);
		pie.setSQL(17,dados.site);
		pie.setSQL(18,dados.dirprojetos);
		pie.setSQL(19,dados.dirimagens);
		pie.setSQL(20,dados.dirrelatorios);
		pie.setSQL(21,dados.dirbackup);
		pie.setSQL(22,dados.idioma);
		pie.setSQL(23,dados.servidoremail);
		pie.setSQL(24,dados.useremail);
		pie.setSQL(25,dados.senhaemail);
		pie.setSQL(26,dados.boasvindas);
		pie.setSQL(27,dados.mostradicas);
		pie.setSQL(28,dados.toolbar);
		pie.setSQL(29,dados.tb1);
		pie.setSQL(30,dados.tb2);
		pie.setSQL(31,dados.tb3);
		pie.setSQL(32,dados.tb4);
		pie.setSQL(33,dados.tb5);
		pie.setSQL(34,dados.tb6);
		pie.setSQL(35,dados.tb7);
		pie.setSQL(36,dados.tb8);
		pie.setSQL(37,dados.tb9);
		pie.setSQL(38,dados.tb10);
		pie.setSQL(39,dados.ajudaonline);
		pie.setSQL(40,dados.unidadecomprimento);
		pie.setSQL(41,dados.unidadeangulo);
		pie.setSQL(42,dados.nid);
		pie.executa();
		

		

                ResultSet rs;
                rs = db.executeQuery("DELETE FROM protofrente");
		rs = db.executeQuery("DELETE FROM protolatdir");

		rs = db.executeQuery("DELETE FROM protolatesq");
		rs = db.executeQuery("DELETE FROM protoposter");

		//Escreve protocolo
		String sqlPreparado3;
		
		if (!dados.protoUser1.frente.isEmpty()) {
			for (int j=0; j < dados.protoUser1.frente.size(); j++) {
				sqlPreparado3 = "INSERT INTO protofrente (nome, frente) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser1.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser1.frente.get(j)).intValue());
				pie.executa();
			}
		} // protoUser1frente
		if (!dados.protoUser1.latDir.isEmpty()) {
			for (int j=0; j < dados.protoUser1.latDir.size(); j++) {
				sqlPreparado3 = "INSERT INTO protolatdir (nome, latdir) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser1.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser1.latDir.get(j)).intValue());
				pie.executa();
			}
		} // protoUser1LatDir
		if (!dados.protoUser1.latEsq.isEmpty()) {
			for (int j=0; j < dados.protoUser1.latEsq.size(); j++) {
				sqlPreparado3 = "INSERT INTO protolatesq (nome, latesq) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser1.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser1.latEsq.get(j)).intValue());
				pie.executa();
			}
		} // protoUser1latEsq
		if (!dados.protoUser1.poster.isEmpty()) {
			for (int j=0; j < dados.protoUser1.poster.size(); j++) {
				sqlPreparado3 = "INSERT INTO protoposter (nome, poster) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser1.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser1.poster.get(j)).intValue());
				pie.executa();
			}
		} // protoUser1poster
		
		if (!dados.protoUser2.frente.isEmpty()) {
			for (int j=0; j < dados.protoUser2.frente.size(); j++) {
				sqlPreparado3 = "INSERT INTO protofrente (nome, frente) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser2.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser2.frente.get(j)).intValue());
				pie.executa();
			}
		} // protoUser2frente
		if (!dados.protoUser2.latDir.isEmpty()) {
			for (int j=0; j < dados.protoUser2.latDir.size(); j++) {
				sqlPreparado3 = "INSERT INTO protolatdir (nome, latdir) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser2.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser2.latDir.get(j)).intValue());
				pie.executa();
			}
		} // protoUser2LatDir
		if (!dados.protoUser2.latEsq.isEmpty()) {
			for (int j=0; j < dados.protoUser2.latEsq.size(); j++) {
				sqlPreparado3 = "INSERT INTO protolatesq (nome, latesq) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser2.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser2.latEsq.get(j)).intValue());
				pie.executa();
			}
		} // protoUser2latEsq
		if (!dados.protoUser2.poster.isEmpty()) {
			for (int j=0; j < dados.protoUser2.poster.size(); j++) {
				sqlPreparado3 = "INSERT INTO protoposter (nome, poster) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser2.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser2.poster.get(j)).intValue());
				pie.executa();
			}
		} // protoUser2poster
		
		if (!dados.protoUser3.frente.isEmpty()) {
			for (int j=0; j < dados.protoUser3.frente.size(); j++) {
				sqlPreparado3 = "INSERT INTO protofrente (nome, frente) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser3.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser3.frente.get(j)).intValue());
				pie.executa();
			}
		} // protoUser3frente
		if (!dados.protoUser3.latDir.isEmpty()) {
			for (int j=0; j < dados.protoUser3.latDir.size(); j++) {
				sqlPreparado3 = "INSERT INTO protolatdir (nome, latdir) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser3.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser3.latDir.get(j)).intValue());
				pie.executa();
			}
		} // protoUser3LatDir
		if (!dados.protoUser3.latEsq.isEmpty()) {
			for (int j=0; j < dados.protoUser3.latEsq.size(); j++) {
				sqlPreparado3 = "INSERT INTO protolatesq (nome, latesq) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser3.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser3.latEsq.get(j)).intValue());
				pie.executa();
			}
		} // protoUser3latEsq
		if (!dados.protoUser3.poster.isEmpty()) {
			for (int j=0; j < dados.protoUser3.poster.size(); j++) {
				sqlPreparado3 = "INSERT INTO protoposter (nome, poster) VALUES (?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado3);
				pie.setSQL(1, dados.protoUser3.nome); 
				pie.setSQL(2, ((Integer)dados.protoUser3.poster.get(j)).intValue());
				pie.executa();
			}
		} // protoUser3poster
		
	}
	
}
