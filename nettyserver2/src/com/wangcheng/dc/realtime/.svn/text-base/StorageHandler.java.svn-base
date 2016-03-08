package com.wangcheng.dc.realtime;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wangcheng.dc.IConstants;
import com.wangcheng.dc.RealtimeLog;
import com.wangcheng.dc.realtime.db.DBHelper;
import com.wangcheng.dc.realtime.db.DBManager;

public final class StorageHandler {
	 
	private static final Logger log = Logger.getLogger(StorageHandler.class);
	
	private Properties properties = null;
	
	private String storeSql = "insert into a_track (T_USERID,T_UV,T_VISITURL,T_ITEMID,T_VISITTIME,T_IP,T_PROVINCE,T_CITY,T_ADDRESS,T_TITLE,T_REFERER) values (?,?,?,?,now(),?,?,?,?,?,?)";
	
	private PreparedStatement storeStatment = null;
	
	private DBManager dbManager = null; 
	
	public StorageHandler(Properties properties) throws SQLException, ClassNotFoundException{
		this.properties = properties; 
		storeSql = properties.getProperty(IConstants.SQL.STORE_REALTIME_LOG_SQL,storeSql);
		
		dbManager = DBManager.getInstance();
		
		Connection conn = getConnection(); 
		
		storeStatment = conn.prepareStatement(storeSql);  
		
	}
	
	private final Connection getConnection() throws SQLException{
		
		return dbManager.getConnection();
	}
	
	private final Connection getConnection(String uid) throws SQLException{
		Connection conn = UserCenterHelper.getUserConnection(uid);
		
		if(conn==null){
			return dbManager.getConnection();
		}
		
		return conn;
	}
	
	private final PreparedStatement getStatement(String uid) throws SQLException{
		
		Connection conn = getConnection(uid); 
		 
		PreparedStatement storeStatment = dbManager.prepareStatement(conn,storeSql);
		 
		return storeStatment;
	}
	
	public final void flush() throws SQLException{
		 Iterator <PreparedStatement> iterator = dbManager.getAllPrepareStatements().iterator();
		 while(iterator.hasNext()){
			 PreparedStatement stmt = iterator.next();
			 try{
				 if(!stmt.isClosed()){
					 DBHelper.flush(stmt);
				 }else{
					 dbManager.checkConnections();
				 }
			 }catch(Exception e){
				 log.error(e.getMessage(),e);
			 }			
		 }		  
	}
	
	public final int store(RealtimeLog data) throws SQLException{ 
		return DBHelper.execute(getStatement(data.getUid()),data.getUid(),data.getAgent(),data.getUrl(),data.getPid(),data.getIp(),data.getProvince(),data.getCity(),data.getAddress(),data.getTitle()); 
	}
}
