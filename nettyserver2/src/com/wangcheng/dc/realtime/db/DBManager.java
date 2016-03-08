package com.wangcheng.dc.realtime.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mysql.jdbc.JDBC4Connection;

public class DBManager implements Runnable {

	private static final Logger log = Logger.getLogger(DBManager.class);
	
	private Connection conn = null;

	private Map<String, Connection> connPool = new HashMap<String, Connection>();

	private Map<Connection, PreparedStatement> stmtPool = new HashMap<Connection, PreparedStatement>();

	private Map<PreparedStatement, String> stmtSqlMap = new HashMap<PreparedStatement, String>();
	
	private Map<String, String> dbPasswords = new HashMap<String, String>();

	public final static String DRIVER_CLASS_NAME = "realtime.jdbc.driverClass";

	public final static String JDBC_URL_NAME = "realtime.jdbc.url";

	public final static String JDBC_USERNAME_NAME = "realtime.jdbc.username";

	public final static String JDBC_PASSWORD_NAME = "realtime.jdbc.password";

	public final static String JDBC_BATCHSIZE_NAME = "realtime.jdbc.batchSize";

	private Class driverClass = com.mysql.jdbc.Driver.class;

	private String url = "jdbc:mysql://localhost:3306/dc?useUnicode=true&characterEncoding=utf8";

	private String username = "root";

	private String password = "dc";

	private static DBManager instance = null;
	
	private static Map<DBEvent,DBEventListener> listeners = new  HashMap<DBEvent,DBEventListener>();

	private DBManager(Properties properties) throws ClassNotFoundException {

		driverClass = Class.forName(properties.getProperty(DRIVER_CLASS_NAME));

		url = properties.getProperty(JDBC_URL_NAME);

		username = properties.getProperty(JDBC_USERNAME_NAME);

		password = properties.getProperty(JDBC_PASSWORD_NAME);
		
		dbPasswords.put(url,password);
	}

	private DBManager() throws ClassNotFoundException {

	}

	public static DBManager initInstance() throws ClassNotFoundException {
		instance = new DBManager();
		Thread thread = new Thread(instance);
		thread.setDaemon(true);
		thread.setName("数据连接管理线程");
		thread.start();

		return instance;
	}

	public static DBManager initInstance(Properties properties)
			throws ClassNotFoundException {
		instance = new DBManager(properties);

		Thread thread = new Thread(instance);
		thread.setDaemon(true);
		thread.setName("数据连接管理线程");
		thread.start();

		return instance;
	}

	public static DBManager getInstance() {
		return instance;
	}

	public void run() {
		while (true) {
			checkConnections();
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public final void checkConnections() {
		try {

			
			boolean hasReconnectEvent = Boolean.FALSE;
			for (Entry<String, Connection> entry : connPool.entrySet()) {

				JDBC4Connection conn = (JDBC4Connection) entry.getValue();

				try {
					if (conn.isClosed()) {
						
						log.warn(conn.getURL()+" is closed!!! try to reconnect with"+conn.getUser()+"/"+dbPasswords.get(conn.getURL())); 

						PreparedStatement stmt = stmtPool.get(conn);

						conn = (JDBC4Connection) newConnection(conn.getURL(),
								conn.getUser(), dbPasswords.get(conn.getURL()));

						entry.setValue(conn);
   
						hasReconnectEvent = Boolean.TRUE;
						
						if (stmt != null) { 
							String sql = stmtSqlMap.get(stmt);
							
							log.info("reset statement :"+sql); 

							stmtSqlMap.remove(stmt);

							stmt = conn.prepareStatement(sql);

							stmtPool.put(conn, stmt);

							stmtSqlMap.put(stmt, sql); 
							
						}
					}
				} catch (Exception e) {
					log.error("checkConnection error:"+e.getMessage(),e);
				}
			}
			if(hasReconnectEvent){
				DBEventListener listener = listeners.get(DBEvent.RECONNECT);
				
				if(listener!=null){
					listener.onEvent(DBEvent.RECONNECT);
				}
			}
			
			Thread.sleep(3000);
		} catch (Exception e) {
			log.error("checkConnections error:"+e.getMessage(),e);
		}
	}
	
	public final static synchronized boolean registerListener(DBEvent event,DBEventListener listener){
		return listeners.put(event,listener)!=null;
	}
	

	public final Connection getConnection() throws SQLException {

		if (conn == null || conn.isClosed()) {
			synchronized (this) {
				if (conn == null || conn.isClosed()) {
					conn = DriverManager.getConnection(url, username, password);
					conn.setAutoCommit(false);
					connPool.put(url, conn);
					dbPasswords.put(url, password);
				}
			}
		}
		return conn;
	}

	public final PreparedStatement prepareStatement(Connection conn,
			String storeSql) throws SQLException {

		PreparedStatement storeStatment = stmtPool.get(conn);

		if (storeStatment == null) {
			storeStatment = conn.prepareStatement(storeSql);
			stmtPool.put(conn, storeStatment);
			stmtSqlMap.put(storeStatment, storeSql);
		}

		return storeStatment;
	}

	public final Collection<PreparedStatement> getAllPrepareStatements()
			throws SQLException {

		return stmtPool.values();
	}

	public final Connection getConnection(String url, String username,
			String password) throws SQLException {
		Connection conn = connPool.get(url);
		if (conn == null || conn.isClosed()) {
			synchronized (connPool) {

				conn = connPool.get(url);

				if (conn == null || conn.isClosed()) {
					conn = DriverManager.getConnection(url, username, password);
					conn.setAutoCommit(false);
					connPool.put(url, conn);
					dbPasswords.put(url, password);
				}
			}
		}
		return conn;
	}

	public final Connection newConnection(String url, String username,
			String password) throws SQLException {

		return DriverManager.getConnection(url, username, password);

	}

	public static void main(String args[]) throws Exception {

		Properties props = new Properties();
		props.put(JDBC_URL_NAME,
				"jdbc:mysql://192.168.2.181:3306/dc?useUnicode=true&characterEncoding=utf8");
		props.put(JDBC_USERNAME_NAME, "dc");
		props.put(JDBC_PASSWORD_NAME, "dc");
		props.put(DRIVER_CLASS_NAME, "com.mysql.jdbc.Driver");
		DBManager mysql = new DBManager(props);

		Connection mysqlConn = mysql.getConnection();

		mysqlConn.close();

	}
}
