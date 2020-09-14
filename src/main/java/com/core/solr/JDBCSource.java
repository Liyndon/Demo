package com.core.solr;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


/**
 * jdbc
 * 
 */
public class JDBCSource{
	
	private Connection m_dbcon;
	
	public JDBCSource(String driver,String url,String name,String password,String DataType){
		try {
	        	
			 	Class.forName(driver);
				
				m_dbcon = DriverManager.getConnection(url, name, password);  
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}  
	}
	

	public void destroy() {
		if (this.m_dbcon != null) {
			try {
				this.m_dbcon.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}


	public boolean beginTransaction() {
		try {
			this.m_dbcon.setAutoCommit(false);
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

	}

	public boolean commitTransaction() {
		try {
			this.m_dbcon.commit();
			this.m_dbcon.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

	}

	
	public boolean RollbackTransaction() {
		try {
			this.m_dbcon.rollback();
			this.m_dbcon.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * @param sql
	 * @return
	 */
	public PreparedStatement createPreparedStmt(String sql) {
		try {
			return this.m_dbcon.prepareStatement(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param procedoreSql
	 * @return
	 */
	public CallableStatement creatCallableStatement(String procedoreSql) {
		try {
			return this.m_dbcon.prepareCall(procedoreSql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Object[][] DoQuerry(String sql) {
		Object[][] retArry = new Object[0][0];
		Connection conn = this.m_dbcon;
		if (null == conn)
			return retArry;


		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			ResultSetMetaData rsmeta = rs.getMetaData();
			int iRow = 0;
			int iCol = rsmeta.getColumnCount();
			Vector<Object[]> tmpvt = new Vector<Object[]>();

			while (rs.next()) {
				Object[] arTmp = new Object[iCol];
				for (int i = 0; i < iCol; i++) {
					arTmp[i] = rs.getObject(i + 1);
				}
				tmpvt.add(arTmp);
				iRow++;
			}
			retArry = new Object[iRow + 1][iCol];

			for (int i = 0; i < iCol; i++) {
				retArry[0][i] = rsmeta.getColumnName(i + 1);
			}

			Object[] resTmp = tmpvt.toArray();
			System.arraycopy(resTmp, // src
					0, retArry, // dest
					1, iRow);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (null != rs)
					rs.close();
				if (null != stmt)
					stmt.close();
			} catch (SQLException e) {
				
			}

		}
		return retArry;
	}
	

	public Object[][] Querry(String sql) {
		Object[][] retArry = new Object[0][0];;
		Connection conn = this.m_dbcon;
		if (null == conn)
			return retArry;

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			ResultSetMetaData rsmeta = rs.getMetaData();
			int iRow = 0;
			int iCol = rsmeta.getColumnCount();
			Vector<Object[]> tmpvt = new Vector<Object[]>();

			while (rs.next()) {
				Object[] arTmp = new Object[iCol];
				for (int i = 0; i < iCol; i++) {
					arTmp[i] = rs.getObject(i + 1);
				}
				tmpvt.add(arTmp);
				iRow++;
			}
			retArry = new Object[iRow][iCol];
			Object[] resTmp = tmpvt.toArray();
			System.arraycopy(resTmp,0, retArry, 0, iRow);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (null != rs)
					rs.close();
				if (null != stmt)
					stmt.close();
			} catch (SQLException e) {
				
			}

		}
		return retArry;
	}

	/**
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public boolean insert(String sql) {
		Connection conn = this.m_dbcon;
		if (null == conn) {
			return false;
		}


		Statement stmt = null;

		try {
			return conn.createStatement().executeUpdate(sql) > 0;
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if (null != stmt) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @param sql
	 */
	public boolean update(String sql) {
		if (this.insert(sql)) {
			
			return true;
		} else {
			
			return false;
		}
	}

	/**
	 * 
	 * @param sql
	 */
	public boolean del(String sql) {
		return this.insert(sql);
	}
}
