package com.wangcheng.dc.realtime;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import uc.UcException;
import uc.UserCenterClient;
import uc.model.Sys;
import uc.model.Usr;

import com.wangcheng.dc.realtime.db.DBEvent;
import com.wangcheng.dc.realtime.db.DBEventListener;
import com.wangcheng.dc.realtime.db.DBManager;

public class UserCenterHelper {

	private static final Logger log = Logger.getLogger(UserCenterHelper.class); 

	private static final String SYSTEM_TYPE_DB = "Write";

	public static final String JDBC_URL = "conn_java";

	public static final String JDBC_DB_SERVER = "IP";

	public static final String JDBC_DBNAME = "DataBaseName";

	public static final String JDBC_USERNAME = "DbUserName";

	public static final String JDBC_PASSWORD = "DbPwd";

	private static final int DEFAULT_JDBC_PORT = 3306;

	public static final String UC_IP = "uc.ip";

	public static final String UC_PORT = "uc.port";
	
	public static final String UC_WSDL = "uc.wsdl"; 
	
	public static final String UC_ENABLED = "uc.enabled";
	
	private static final String UC_ATTEMPT_INTEVERAL = "uc.attemptIntervals";
	
	public static final String UC_TYPE = "rta"; 

	private static Map<String, String> userDBNames = new HashMap<String, String>();

	private static Map<String, Map<String, String>> systemsMap = new HashMap<String, Map<String, String>>();

	private static Map<String, Connection> systemDBConns = new HashMap<String, Connection>();
	
	private static Map<String, Long> lastFailedUserMap = new HashMap<String, Long>();

	private Properties properties = new Properties();

	private static UserCenterClient ucClient = null;
	
	private static transient boolean enabled = true;
	
	private static long attemptIntervals = 10*60*1000 ;

	public final synchronized static void initUC(Properties properties)
			throws IOException, UcException {

		// loadTestData();

		log.info("init uc ...........");

		String wsdl = properties.getProperty(UC_WSDL); 
		
		enabled =  Boolean.valueOf(properties.getProperty(UC_ENABLED,"true"));
		
		attemptIntervals = Long.valueOf(properties.getProperty(UC_ATTEMPT_INTEVERAL,String.valueOf(attemptIntervals)));
		
		log.info("uc enabled = "+enabled);
		 

		ucClient = new UserCenterClient(wsdl); 

		loadSystems();
		loadUsers();

		
		DBManager.registerListener(DBEvent.RECONNECT, new DBEventListener() {
			
			@Override
			public void onEvent(DBEvent event) {
				try {
					loadSystems();
				} catch (Exception e) {
					log.error(DBEvent.RECONNECT+" event error:"+e.getMessage(),e);
				}  
			}
		});
		System.gc();
	}

	public final synchronized static void loadUsers() throws IOException,
			UcException {

		log.info("loading users ...........");

		Map<String, String> parameters = new HashMap<String, String>();

		Collection<Usr> users = null;

		for (String exsitingSysId : systemsMap.keySet()) {
			parameters.put("sysid", exsitingSysId);

			users = ucClient.getUsers();

			for (Usr user : users) {
				onLoadUsr(ucClient, user);
			}

			users.clear();
		}

		users = null;

		log.info("loaded users count :" + userDBNames.size());
		
		if(log.isDebugEnabled()){
			log.debug("loaded users  :" + userDBNames);
		}
	}

	private final static void onLoadUsr(UserCenterClient ucClient, Usr user)
			throws IOException, UcException {

		String sysId = null;

		label: for (String userSysId : user.getSystems()) {
			for (String exsitingSysId : systemsMap.keySet()) {
				if (userSysId.equals(exsitingSysId)) {
					sysId = exsitingSysId;
					break label;
				}
			}
		}

		if (sysId != null) {
			userDBNames.put(user.getUid(), sysId);
		}

	}

	public final synchronized static void loadSystems() throws IOException,
			UcException {

		log.info("loading systems ...........");

		// Map<String, String> parameters = new HashMap<String, String>();
		Collection<Sys> systems = ucClient.getSystems(UC_TYPE);
		for (Sys system : systems) {
			//if (SYSTEM_TYPE_DB.equals(system.getType())) {
				onLoadSystem(system);
			//}
		}

		log.info("loaded systems:" + systemsMap);
		
		if(log.isDebugEnabled()){
			log.info("loaded systems connections :" + systemDBConns);
		}
	}

	private final static void onLoadSystem(Sys system) {
		String username = system.getParameters().get(JDBC_USERNAME);
		String password = system.getParameters().get(JDBC_PASSWORD);

		String url = system.getParameters().get(JDBC_URL);

		if (url == null) {
			String ip = system.getParameters().get(JDBC_DB_SERVER);

			Integer port = system.getPort();
			if (port == null) {
				port = DEFAULT_JDBC_PORT;
			}
			String database = system.getParameters().get(JDBC_DBNAME);
			url = "jdbc:mysql://"
					+ ip
					+ ":"
					+ port
					+ "/"
					+ database
					+ "?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&cachePrepStmts=true";
		}

		systemsMap.put(system.getId(), system.getParameters());

		try {
			DBManager dbManager = DBManager.getInstance();
			Connection conn = dbManager.getConnection(url, username, password);
			systemDBConns.put(system.getId(),conn);
			
			
		} catch (SQLException e) {
			log.error(" load connection error : "+system.getName()+",url="+url+",username="+username+",password="+password, e);
		}
	}

	public final synchronized static Connection getUserConnection(String uid) {
		String systemId = userDBNames.get(uid); 

		if (systemId == null&&enabled) {
			
			Long lastAttemptIntervalsTime = lastFailedUserMap.get(uid);
			
			long now = System.currentTimeMillis();
			
			if(lastAttemptIntervalsTime!=null){		
				
				if(now-lastAttemptIntervalsTime<attemptIntervals){
					return null;
				}else{
					lastFailedUserMap.put(uid, now);
				}				
			} 
			
			try {
				Usr user = ucClient.getUserById(uid);
				
				if (user != null) {
					onLoadUsr(ucClient, user);
					systemId = userDBNames.get(uid);
					lastFailedUserMap.remove(uid);
				} else {					
					lastFailedUserMap.put(uid, System.currentTimeMillis());					
					return null;
				}

			} catch (UcException e) {
				lastFailedUserMap.put(uid, now);
				log.error("getUserConnection error UID="+uid+",error="+e.getMessage());
			} catch (Exception e) {
				lastFailedUserMap.put(uid,now);
				log.error("getUserConnection error UID="+uid+",error="+e.getMessage(),e);
			}
		}

		return systemDBConns.get(systemId);
	}

	public static void main(String args[]) {
		try {
			DBManager.initInstance();

			Properties props = new Properties();

			props.load(UserCenterHelper.class
					.getResourceAsStream("/realtime.properties"));

			UserCenterHelper.initUC(props);
			
			//System.out.println(UserCenterHelper.userDBNames);

			Connection conn008 = UserCenterHelper.getUserConnection("萤火虫之光1");

			System.out.println(conn008);
			
			int count = 1;
			long begin = System.currentTimeMillis();

			for(int i=0;i<count;i++){
				UserCenterHelper.getUserConnection("萤火虫之光1");
			}					
			
			long end = System.currentTimeMillis(); 
			
			//System.out.println("cost="+(end-begin)+",tps="+count*1000/(end-begin));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
