package com.wangcheng.dc.realtime;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.wangcheng.dc.IConstants;
import com.wangcheng.dc.realtime.db.DBManager;

public final class IPHelper {
	
	private static final Logger log = Logger.getLogger(IPHelper.class); 

	private static final List<long[]> ipList = new ArrayList<long[]>();
	private static final List<String[]> ipAddressList = new ArrayList<String[]>();
 
	private static int ipCount = 0;
	
	public static final String [] EMPTY_ADDRESS = new String[]{"-1","-1","-1"};
	
	public static final void initCache() throws SQLException {
		 
		Connection conn = DBManager.getInstance().getConnection();
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt
				.executeQuery("select id,start_ip,end_ip,province_id,city_id,full_address  from SYS_IP");
		
		while (rs.next()) {
			long id = rs.getLong(1); 
			long startIp = rs.getLong(2);
			long endIp = rs.getLong(3); 
			String province = rs.getString(4); 
			String city = rs.getString(5); 
			String full_address = rs.getString(6); 

			ipList.add(new long[] { startIp, endIp,id});
			ipAddressList.add(new String[] { province, city,full_address});
		}
		
		ipCount = ipList.size();
		
		log.info("LOAD IP ADDRESS COUNT:"+ipCount);
		
		rs.close();
		stmt.close();
		
		System.gc();
	}
	
	

	public static final String[] getGeo(String ip) throws SQLException {
		  
		long intIp = ipToInt(ip);
 
		return search(intIp); 
	}

	public static final long ipToInt(String ip) {
		StringTokenizer st = new StringTokenizer(ip, IConstants.IP_DELIMETER);
		String[] fields = new String[4];
		fields[0] = st.nextToken();
		fields[1] = st.nextToken();
		fields[2] = st.nextToken();
		fields[3] = st.nextToken();

		long value = (Long.parseLong(fields[0]) <<24)
				+ (Integer.parseInt(fields[1])<<16) + (Short.parseShort(fields[2])<<8) + Short.parseShort(fields[3]);
		
		st = null;
		fields[0] = null;
		fields[1] = null;
		fields[2] = null;
		fields[3] = null;
		fields = null;

		return value;
	}
	
	public static final String[] search(long key) {	
		int low = 0;		
		int high =  ipCount-1;
		
		while(low<=high){
			int mid = (low + high)/2;
			
			long[] values = ipList.get(mid);

			long key1 = (long) values[0];
			long key2 = (long) values[1]; 

			if (key>=key1&&key<=key2) {	
				
				return ipAddressList.get(mid);		
				
			} else if (key < key1) {	
				
				high = mid-1;
			} else {			
				low = mid+1;
			}
		}
		
		return EMPTY_ADDRESS;
	} 

	public static void main(String args[]) throws SQLException,
			ClassNotFoundException, IOException, InterruptedException {
		
		Properties props = new Properties();
		props.put(DBManager.JDBC_URL_NAME,"jdbc:mysql://192.168.1.181:3306/NBDR?useUnicode=true&characterEncoding=utf8");
		props.put(DBManager.JDBC_USERNAME_NAME,"dc");
		props.put(DBManager.JDBC_PASSWORD_NAME,"dc");
		props.put(DBManager.DRIVER_CLASS_NAME,"com.mysql.jdbc.Driver");
		
		DBManager.initInstance(props);
		
		initCache();
		String [] result = null;
		//long begin =System.currentTimeMillis();
		
		//for(int i=0;i<1000000;i++){
			result = getGeo("113.90.45.42"); 
		//}			 
						
			System.out.println(getGeo("119.176.109.233")[2]);
			System.out.println(getGeo("182.61.130.80")[2]);
			System.out.println(getGeo("59.104.108.189")[2]);
			System.out.println(getGeo("121.10.138.176")[2]);
			System.out.println(getGeo("182.150.153.70")[2]);
			System.out.println(getGeo("222.132.94.118")[2]);
			System.out.println(getGeo("218.7.209.35")[2]);
			System.out.println(getGeo("125.84.85.127")[2]);
			System.out.println(getGeo("115.152.195.92")[2]);
			System.out.println(getGeo("222.89.114.18")[2]);
			System.out.println(getGeo("121.205.204.100")[2]);
			System.out.println(getGeo("116.115.223.68")[2]);
			System.out.println(getGeo("125.70.30.209")[2]);
			System.out.println(getGeo("122.94.24.160")[2]);
			System.out.println(getGeo("112.237.131.171")[2]);
			System.out.println(getGeo("58.213.141.134")[2]);
			System.out.println(getGeo("58.48.232.183")[2]);
			System.out.println(getGeo("116.55.249.10")[2]);
			System.out.println(getGeo("116.205.150.196")[2]);
			System.out.println(getGeo("120.134.1.39")[2]);
			System.out.println(getGeo("123.11.59.182")[2]);
			System.out.println(getGeo("119.180.202.127")[2]);
			System.out.println(getGeo("183.66.124.163")[2]);
			System.out.println(getGeo("123.185.43.48")[2]);
			System.out.println(getGeo("113.75.237.114")[2]);
			System.out.println(getGeo("125.76.72.0")[2]);
			System.out.println(getGeo("125.37.126.94")[2]);
			System.out.println(getGeo("58.240.100.82")[2]);
			System.out.println(getGeo("111.172.130.0")[2]);
			System.out.println(getGeo("61.146.231.192")[2]);
			System.out.println(getGeo("60.167.122.113")[2]);
			
			System.out.println();
			
			System.out.println(getGeo("183.15.135.0")[2]);
			System.out.println(getGeo("113.57.44.53")[2]);
			System.out.println(getGeo("180.117.1.28")[2]);
			System.out.println(getGeo("112.93.2.80")[2]);
			System.out.println(getGeo("188.186.237.62")[2]);
			System.out.println(getGeo("59.61.134.119")[2]); 
			System.out.println(getGeo("182.149.105.238")[2]); 
			System.out.println(getGeo("182.149.52.57")[2]); 
			
			
		
		//long end =System.currentTimeMillis();
		
		//System.out.println("cost :"+1000000*1000/(end-begin)+","+result[0]);
	}
}
