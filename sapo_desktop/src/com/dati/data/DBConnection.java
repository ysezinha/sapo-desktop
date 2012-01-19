/*
 * DBConnection.java
 *
 * Created on 30 de Janeiro de 2005, 21:58
 */

/**
 *
 * @author  Edison Puig Maldonado
 */

package com.dati.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DBConnection {
	
	public Connection con;
	private Statement stmt;
	
	public boolean open(String driver,String url,String username,String password) {
		boolean retorno = true;
		try {
			Class.forName(driver);
		} catch (java.lang.ClassNotFoundException e) {
			System.out.print("ClassNotFoundException: ");
			System.out.println(e.getMessage());
		}
		try {
			con = DriverManager.getConnection(url,username,password);
			if(con==null) retorno = false;
			else stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		} catch(SQLException e) { 
			e.printStackTrace(); 
			return false; 
		}
		return retorno;
	}
	
	public void close() {
		try { 
			addBatch("SHUTDOWN COMPACT");
			executeBatch();
			con.close();
		} catch(Exception e) {}
	}
	
	public void addBatch (String sql) {
		try { stmt.addBatch(sql);
		} catch(Exception e) {}
	}    
	
	public boolean executeBatch () {
		try { 
			stmt.executeBatch();
			stmt.clearBatch();
			return true;
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
			return false;
		}
	}
	
	public ResultSet executeQuery (String sql) {
		ResultSet retorno = null;
		try { 
			retorno = stmt.executeQuery(sql);
		} catch(SQLException sqle) {
			System.out.println("Exceção SQL : "+ sqle.getMessage());
		}
		return retorno;
	}
	
	public PreparaInsereEnvia getPreparaInsereEnvia(String preparedSQL) {
		return new PreparaInsereEnvia(preparedSQL);
	}
	
	public class PreparaInsereEnvia {
		
		private PreparedStatement pstmt;
		
		public PreparaInsereEnvia(String preparedSQL) {
			try {
				pstmt = con.prepareStatement(preparedSQL);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public void executa() {
			try {
				pstmt.execute();
				pstmt.close();
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public ResultSet executaQuery() {
			ResultSet retorno = null;
			try {
				retorno = pstmt.executeQuery();
				//pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return retorno;
		}
		
		public void setSQL(int parameterIndex, Timestamp x) {
			try {
				pstmt.setTimestamp(parameterIndex,x);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public void setSQL(int parameterIndex, String x) {
			try {
				pstmt.setString(parameterIndex,x);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public void setSQL(int parameterIndex, double x) {
			try {
				pstmt.setDouble(parameterIndex,x);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public void setSQL(int parameterIndex, java.math.BigDecimal x) {
			try {
				pstmt.setBigDecimal(parameterIndex,x);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public void setSQL(int parameterIndex, long x) {
			try {
				pstmt.setLong(parameterIndex,x);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
		public void setSQL(int parameterIndex, boolean x) {
			try {
				pstmt.setBoolean(parameterIndex,x);
			} catch(SQLException e) {e.printStackTrace();}
		}
		
	}
}

