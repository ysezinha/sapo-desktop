import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dati.data.DBConnection;

/**
 * @author Edison Puig Maldonado
 *
 */
public class AtualizaDB {

	public static boolean atualizaDB(DBConnection db) {
		
		String sqlPreparado;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		DatabaseMetaData  dbmd = null;
		ArrayList tableNames = new ArrayList();
		
		// obtém nomes das tabelas no banco de dados
		try {
			dbmd = db.con.getMetaData();
			rs = dbmd.getTables(null,null,null,null);
			if ((rs != null) && rs.isBeforeFirst())	
				while (rs.next()) tableNames.add(rs.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Verifica existência da tabela "referencias" e a cria se necessário
		boolean sucesso1 = false;
		if (!tableNames.isEmpty()) 
			for (int i=0; i<tableNames.size(); i++)
				if (((String)tableNames.get(i)).equalsIgnoreCase("referencias")) {
					sucesso1 = true;
				}
		if (!sucesso1) {  
			db.addBatch("CREATE MEMORY TABLE REFERENCIAS(NID BIGINT NOT NULL PRIMARY KEY," +
					"VALOR DOUBLE DEFAULT 0.0E0 NOT NULL," +
					"TOLERANCIA DOUBLE DEFAULT 0.0E0 NOT NULL," +
					"UNIDADE VARCHAR DEFAULT '' NOT NULL)");
			db.addBatch("INSERT INTO REFERENCIAS VALUES(0,0.0E0,0.0E0,'graus')"); // Alinhamento horizontal da cabeça
			db.addBatch("INSERT INTO REFERENCIAS VALUES(1,0.0E0,0.0E0,'graus')"); // Alinhamento horizontal dos acrômios
			db.addBatch("INSERT INTO REFERENCIAS VALUES(2,0.0E0,0.0E0,'graus')"); // Alinhamento horizontal das espinhas ilíacas ântero-superiores
			db.addBatch("INSERT INTO REFERENCIAS VALUES(3,0.0E0,0.0E0,'graus')"); // Ângulo entre os dois acrômios e as duas espinhas ilíacas ântero-superiores
			db.addBatch("INSERT INTO REFERENCIAS VALUES(4,1E300,0.0E0,'graus')"); // Ângulo frontal do membro inferior direito
			db.addBatch("INSERT INTO REFERENCIAS VALUES(5,1E300,0.0E0,'graus')"); // Ângulo frontal do membro inferior esquerdo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(6,0.0E0,0.0E0,'cm')");     // Diferença no comprimento dos membros inferiores (D-E)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(7,0.0E0,0.0E0,'graus')"); // Alinhamento horizontal das tuberosidades das tíbias
			db.addBatch("INSERT INTO REFERENCIAS VALUES(8,15.0E0,0.0E0,'graus')");// Ângulo Q direito
			db.addBatch("INSERT INTO REFERENCIAS VALUES(9,15.0E0,0.0E0,'graus')");// Ângulo Q esquerdo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(10,0.0E0,0.0E0,'%')");    // Assimetria horizontal da escápula em relação à T3
			db.addBatch("INSERT INTO REFERENCIAS VALUES(11,1E300,0.0E0,'graus')");// Ângulo perna/retropé direito
			db.addBatch("INSERT INTO REFERENCIAS VALUES(12,1E300,0.0E0,'graus')");// Ângulo perna/retropé esquerdo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(13,1E300,0.0E0,'graus')");// Alinhamento horizontal da cabeça (C7)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(14,0.0E0,0.0E0,'graus')");// Alinhamento vertical da cabeça (acrômio)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(15,1E300,0.0E0,'graus')");// Alinhamento vertical do tronco
			db.addBatch("INSERT INTO REFERENCIAS VALUES(16,1E300,0.0E0,'graus')");// Ângulo do quadril (tronco e membro inferior)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(17,1E300,0.0E0,'graus')");// Alinhamento vertical do corpo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(18,1E300,0.0E0,'graus')");// Alinhamento horizontal da pélvis
			db.addBatch("INSERT INTO REFERENCIAS VALUES(19,1E300,0.0E0,'graus')");// Ângulo do joelho
			db.addBatch("INSERT INTO REFERENCIAS VALUES(20,1E300,0.0E0,'graus')");// Ângulo do tornozelo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(21,1E300,0.0E0,'graus')");// Alinhamento horizontal da cabeça (C7)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(22,0.0E0,0.0E0,'graus')");// Alinhamento vertical da cabeça (acrômio)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(23,1E300,0.0E0,'graus')");// Alinhamento vertical do tronco
			db.addBatch("INSERT INTO REFERENCIAS VALUES(24,1E300,0.0E0,'graus')");// Ângulo do quadril (tronco e membro inferior)
			db.addBatch("INSERT INTO REFERENCIAS VALUES(25,1E300,0.0E0,'graus')");// Alinhamento vertical do corpo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(26,1E300,0.0E0,'graus')");// Alinhamento horizontal da pélvis
			db.addBatch("INSERT INTO REFERENCIAS VALUES(27,1E300,0.0E0,'graus')");// Ângulo do joelho
			db.addBatch("INSERT INTO REFERENCIAS VALUES(28,1E300,0.0E0,'graus')");// Ângulo do tornozelo
			db.addBatch("INSERT INTO REFERENCIAS VALUES(29,1E300,0.0E0,'graus')");// Centro de gravidade frontal X
			db.addBatch("INSERT INTO REFERENCIAS VALUES(30,1E300,0.0E0,'graus')");// Centro de gravidade frontal Y
			db.addBatch("INSERT INTO REFERENCIAS VALUES(31,1E300,0.0E0,'graus')");// Centro de gravidade Lateral Direita X
			db.addBatch("INSERT INTO REFERENCIAS VALUES(32,1E300,0.0E0,'graus')");// Centro de gravidade Lateral Direita Y
			db.addBatch("INSERT INTO REFERENCIAS VALUES(33,1E300,0.0E0,'graus')");// Centro de gravidade Lateral Esquerda X
			db.addBatch("INSERT INTO REFERENCIAS VALUES(34,1E300,0.0E0,'graus')");// Centro de gravidade Lateral Esquerda Y
			db.addBatch("INSERT INTO REFERENCIAS VALUES(35,1E300,0.0E0,'graus')");// Média do centro de gravidade lateral X
			db.addBatch("INSERT INTO REFERENCIAS VALUES(36,1E300,0.0E0,'graus')");// Média do centro de gravidade lateral Y
			db.addBatch("INSERT INTO REFERENCIAS VALUES(37,0.0E0,0.0E0,'graus')");// Assimetria no plano frontal
			db.addBatch("INSERT INTO REFERENCIAS VALUES(38,1E300,0.0E0,'graus')");// Origem do centro de gravidade X
			db.addBatch("INSERT INTO REFERENCIAS VALUES(39,1E300,0.0E0,'graus')");// Assimetria no plano sagital
			db.addBatch("INSERT INTO REFERENCIAS VALUES(40,1E300,0.0E0,'graus')");// Origem do centro de gravidade Y
			sucesso1 = db.executeBatch();
		}
		
		// obtém metadata da tabela paciente
		sqlPreparado = "SELECT * FROM dpaciente";
		rs = db.getPreparaInsereEnvia(sqlPreparado).executaQuery();
		boolean temStatus = false;
		boolean temHandedness = false;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Atualiza tabelas dpaciente antigas para conter a coluna "publicado"
		int i=1; 
		try {
			if (rsmd != null) 
				while (i <= rsmd.getColumnCount()) {
					if (rsmd.getColumnName(i).equalsIgnoreCase("publicado")) 
						temStatus = true;
					i++;
				}
		} catch (SQLException e) { e.printStackTrace();	}
		boolean sucesso2;
		if (!temStatus) {
			db.addBatch("ALTER TABLE dpaciente ADD COLUMN publicado BOOLEAN DEFAULT false NOT NULL");
			sucesso2 = db.executeBatch();
		}
		else sucesso2 = true;
		return (sucesso1 && sucesso2);
	}
	
}
