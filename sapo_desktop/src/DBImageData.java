import java.awt.Point;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dati.data.DBConnection;
import com.dati.image.AnguloMedido;
import com.dati.image.DistanciaMedida;
import com.dati.image.PontoMedido;


/*
 * DBData.java
 *
 * Created on 7 de Fevereiro de 2005, 11:17
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

public class DBImageData {
	
	public static void lerImageDatas(long nID, Paciente paciente, DBConnection db) {
		
		final DadosPaciente dados = paciente.dados;
		long nIDPaciente = nID;
		DBConnection.PreparaInsereEnvia pie;
		ResultSet rs;
		String sqlPreparado, sqlPreparado2, sqlPreparado3;
		
		sqlPreparado = "SELECT * FROM imagedata WHERE nidpaciente=?";
		pie = db.getPreparaInsereEnvia(sqlPreparado);
		pie.setSQL(1, nIDPaciente);
		rs = pie.executaQuery();
		
		int i=0;
		try {
			while (rs.next()) {
				i++;
				dados.imgData[i].nID = rs.getLong("nid");
				dados.imgData[i].setAnguloVertical(rs.getDouble("angulovertical"));
				dados.imgData[i].setEscalaX(rs.getDouble("escalax"));
				dados.imgData[i].setEscalaY(rs.getDouble("escalay"));
				dados.imgData[i].setXAnchor(rs.getInt("xanchor"));
				dados.imgData[i].setYAnchor(rs.getInt("yanchor"));
				dados.imgData[i].setImgRotate(rs.getBoolean("imgrotate"));
				dados.imgData[i].setFileImage(new File(rs.getString("fileimage")));
				dados.imgData[i].setVista(rs.getString("vista"));
			}
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		
		for (int j=1; j<=i; j++) {
			sqlPreparado2 = "SELECT * FROM pontomedido WHERE nidimagedata=?";
			pie = db.getPreparaInsereEnvia(sqlPreparado2);
			pie.setSQL(1, dados.imgData[j].nID);
			rs = pie.executaQuery();
			
			try {
				while (rs.next()) {
					dados.imgData[j].addPoint(
							(int)rs.getLong("px"),
							(int)rs.getLong("py"),
							rs.getString("nome"),
							rs.getBoolean("apresenta"));
				}
			} catch(SQLException sqle) {
				System.out.println("Exceção SQL : "+ sqle.getMessage());
			}
		}
		
		for (int j=1; j<=i; j++) {
			sqlPreparado3 = "SELECT * FROM angulomedido WHERE nidimagedata=?";
			pie = db.getPreparaInsereEnvia(sqlPreparado3);
			pie.setSQL(1, dados.imgData[j].nID);
			rs = pie.executaQuery();
			
			try {
				while (rs.next()) {
					Point p[] = new Point[5];
					p[0] = new Point((int)rs.getLong("p1x"),(int)rs.getLong("p1y"));
					p[1] = new Point((int)rs.getLong("p2x"),(int)rs.getLong("p2y"));
					p[2] = new Point((int)rs.getLong("p3x"),(int)rs.getLong("p3y"));
					p[3] = new Point((int)rs.getLong("p4x"),(int)rs.getLong("p4y"));
					p[4] = new Point((int)rs.getLong("p5x"),(int)rs.getLong("p5y"));
					dados.imgData[j].addAnguloMedido(p,
							rs.getString("nome"),
							rs.getDouble("angulo"),
							(int)rs.getLong("tipo"));
				}
			} catch(SQLException sqle) {
				System.out.println("Exceção SQL : "+ sqle.getMessage());
			}
		}
		//le distancia medidas       
		for (int j=1; j<=i; j++) {
			sqlPreparado3 = "SELECT * FROM distanciamedida WHERE nidimagedata=?";
			pie = db.getPreparaInsereEnvia(sqlPreparado3);
			pie.setSQL(1, dados.imgData[j].nID);
			rs = pie.executaQuery();
			
			try {
				while (rs.next()) {
					Point p[] = new Point[2];
					p[0] = new Point((int)rs.getLong("p1x"),(int)rs.getLong("p1y"));
					p[1] = new Point((int)rs.getLong("p2x"),(int)rs.getLong("p2y"));
					dados.imgData[j].addDistanciaMedida(p,
							rs.getString("nome"),
							rs.getDouble("distancia"),
							rs.getBoolean("apresenta"));
				}
			} catch(SQLException sqle) {
				System.out.println("Exceção SQL : "+ sqle.getMessage());
			}
		}
		
		
		
		
	}
	
	public static void escreveAtualizaImageDatas(DadosPaciente dados, DBConnection db) {
		
		long nIDPaciente = dados.nID;
		apagaImageDatas(nIDPaciente, db);
		DBConnection.PreparaInsereEnvia pie;
		String sqlPreparado, sqlPreparado2, sqlPreparado3; 
		PontoMedido pm;
		AnguloMedido am;
		DistanciaMedida dm;
		
		for (int i=0; i<(SAPO.maxImg+1); i++) {
			if ((dados.imgData[i].getFileImage() != null) && 
					(dados.imgData[i].getFileImage().getPath() != null) && 
					(dados.imgData[i].getVista() != null) && 
					(!dados.imgData[i].getVista().equals(""))) {
				sqlPreparado = "INSERT INTO imagedata (nidpaciente, angulovertical," +
				" escalax, escalay, xanchor, yanchor, imgrotate, fileimage," +
				" nomepaciente, descricao, vista) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				pie = db.getPreparaInsereEnvia(sqlPreparado);
				pie.setSQL(1, nIDPaciente);
				pie.setSQL(2, dados.imgData[i].getAnguloVertical());
				pie.setSQL(3, dados.imgData[i].getEscalaX());
				pie.setSQL(4, dados.imgData[i].getEscalaY());
				pie.setSQL(5, dados.imgData[i].getXAnchor());
				pie.setSQL(6, dados.imgData[i].getYAnchor());
				pie.setSQL(7, dados.imgData[i].isImgRotate());
				pie.setSQL(8, dados.imgData[i].getFileImage().getAbsolutePath());
				pie.setSQL(9, dados.nome);
				pie.setSQL(10,"imgData = "+String.valueOf(i));
				pie.setSQL(11,dados.imgData[i].getVista());
				pie.executa();
				
				sqlPreparado2 = "SELECT * FROM imagedata WHERE nidpaciente=? AND descricao=?";
				pie = db.getPreparaInsereEnvia(sqlPreparado2);
				pie.setSQL(1, nIDPaciente);
				pie.setSQL(2, "imgData = "+String.valueOf(i));
				ResultSet rs = pie.executaQuery();
				try {
					if (rs.next()) dados.imgData[i].nID = rs.getLong("nid");
				} catch(SQLException sqle) {
					System.out.println("Exceção SQL : "+ sqle.getMessage());
				}
				
				if (!dados.imgData[i].pontosList.isEmpty()) {
					for (int j=0; j < dados.imgData[i].pontosList.size(); j++) {
						sqlPreparado3 = "INSERT INTO pontomedido (nidimagedata, apresenta," +
						" nome, generico, px, py, nidpaciente) VALUES (?, ?, ?, ?, ?, ?, ?);";
						pie = db.getPreparaInsereEnvia(sqlPreparado3);
						pm = (PontoMedido)dados.imgData[i].pontosList.get(j);
						pie.setSQL(1, dados.imgData[i].nID);
						pie.setSQL(2, pm.apresenta);
						pie.setSQL(3, pm.nome);
						pie.setSQL(4, pm.generico);
						pie.setSQL(5, pm.p.x);
						pie.setSQL(6, pm.p.y);
						pie.setSQL(7, nIDPaciente);
						pie.executa();
					}
				}
				
				if (!dados.imgData[i].angulosList.isEmpty()) {
					for (int j=0; j < dados.imgData[i].angulosList.size(); j++) {
						sqlPreparado3 = "INSERT INTO angulomedido (nidimagedata, apresenta," +
						" tipo, nome, generico, angulo, p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y, p5x, p5y, nidpaciente)" +
						" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
						pie = db.getPreparaInsereEnvia(sqlPreparado3);
						am = (AnguloMedido)dados.imgData[i].angulosList.get(j);
						pie.setSQL(1, dados.imgData[i].nID);
						pie.setSQL(2, am.apresenta);
						pie.setSQL(3, am.tipo);
						pie.setSQL(4, am.nome);
						pie.setSQL(5, am.generico);
						pie.setSQL(6, am.angulo);
						pie.setSQL(7, am.p[0].x);
						pie.setSQL(8, am.p[0].y);
						pie.setSQL(9, am.p[1].x);
						pie.setSQL(10,am.p[1].y);
						pie.setSQL(11,am.p[2].x);
						pie.setSQL(12,am.p[2].y);
						pie.setSQL(13,am.p[3].x);
						pie.setSQL(14,am.p[3].y);
						pie.setSQL(15,am.p[4].x);
						pie.setSQL(16,am.p[4].y);
						pie.setSQL(17,nIDPaciente);
						pie.executa();
					}
				}
				//Escreve distancias medidas               
				if (!dados.imgData[i].distanciaList.isEmpty()) {
					for (int j=0; j < dados.imgData[i].distanciaList.size(); j++) {
						sqlPreparado3 = "INSERT INTO distanciamedida (nidimagedata, apresenta," +
						"nome, distancia, p1x, p1y, p2x, p2y, nidpaciente)" +
						" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
						pie = db.getPreparaInsereEnvia(sqlPreparado3);
						dm = (DistanciaMedida)dados.imgData[i].distanciaList.get(j);
						pie.setSQL(1, dados.imgData[i].nID);
						pie.setSQL(2, dm.apresentaDist);
						pie.setSQL(3, dm.nome);
						pie.setSQL(4, dm.distanciaMedida);
						pie.setSQL(5, dm.p[0].x);
						pie.setSQL(6, dm.p[0].y);
						pie.setSQL(7, dm.p[1].x);
						pie.setSQL(8, dm.p[1].y);
						pie.setSQL(9, nIDPaciente);
						pie.executa();
					}
				} //if medidaslist
			}
		}
		
	}
	
	private static void apagaImageDatas(long nIDPaciente, DBConnection db) {
		DBConnection.PreparaInsereEnvia pie;
		pie = db.getPreparaInsereEnvia("DELETE FROM imagedata WHERE nidpaciente=?");
		pie.setSQL(1, nIDPaciente);
		pie.executa();
		pie = db.getPreparaInsereEnvia("DELETE FROM pontomedido WHERE nidpaciente=?");
		pie.setSQL(1, nIDPaciente);
		pie.executa();
		pie = db.getPreparaInsereEnvia("DELETE FROM angulomedido WHERE nidpaciente=?");
		pie.setSQL(1, nIDPaciente);
		pie.executa();        
		pie = db.getPreparaInsereEnvia("DELETE FROM distanciamedida WHERE nidpaciente=?");
		pie.setSQL(1, nIDPaciente);
		pie.executa();
	}
	
}
