package com.wangcheng.dc.realtime.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.wangcheng.dc.IConstants;

public final class DBHelper {

	private static volatile int stmtSize = 0;

	private static int batchSize = 0;

	private final static Map<String, PreparedStatement> statements = new HashMap<String, PreparedStatement>();

	public final static void initDBHelper(int batchSize) {
		DBHelper.batchSize = batchSize;
	}

	public final static int execute(Connection conn, String sql, String... params)
			throws SQLException {

		PreparedStatement stmt = (PreparedStatement) statements.get(sql);

		if (stmt == null) {
			stmt = conn.prepareStatement(sql);
			statements.put(sql, stmt);
		}
 
		return execute(stmt,params);
	}

	public final static int execute(PreparedStatement stmt, String... params) throws SQLException {

		int i = 1;

		for (String param : params) {
			stmt.setString(i++, param);
		}
		// stmt.execute();
		stmt.addBatch(); 

		if (++stmtSize >= batchSize) {
			stmt.executeBatch(); 
			stmt.clearBatch(); 
			stmtSize = 0;
		}

		return 0;
	}


	public static Map<String, String> uniqueResult(Connection conn, String sql,
			Object... params) throws SQLException {

		PreparedStatement stmt = conn.prepareStatement(sql); // (PreparedStatement)statements.get(sql);
 
		int i = 1;

		for (Object param : params) {
			stmt.setObject(i++, param);
		}
		Map<String, String> result = null;
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			result = new HashMap<String, String>();
			String provice = rs.getString(1);
			String city = rs.getString(2);
			String address = rs.getString(3);

			result.put(IConstants.LogNames.PROVINCE, provice);
			result.put(IConstants.LogNames.CITY, city);
			result.put(IConstants.LogNames.ADDRESS, address);
		} else {
			result = Collections.emptyMap();
		}

		return result;
	}

	public synchronized static void flush(PreparedStatement stmt) throws SQLException {
		stmt.executeBatch();
		stmt.clearBatch(); 
		stmtSize = 0;
	}

	
	public synchronized static void flush() throws SQLException {
		if (stmtSize > 0) {
			for (PreparedStatement stmt : statements.values()) {
				if (!stmt.isClosed()) {
					stmt.executeBatch();
					stmt.clearBatch(); 
				}
			}
			stmtSize = 0;
		}
	}

	public static void main(String args[]) throws SQLException,
			ClassNotFoundException {
		Connection conn = DBManager.initInstance(null).getConnection();
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			 
			System.out.println(i);
		}
		long end = System.currentTimeMillis();

		System.out.println((end - begin) / 1000);
	}
}
