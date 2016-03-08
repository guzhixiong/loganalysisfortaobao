package com.wangcheng.udp;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import uc.UcException;
import uc.UserCenterClient; 

import com.wangcheng.dc.cache.RequestQueue;
import com.wangcheng.dc.monitor.RealTimeQueueMonitor;
import com.wangcheng.dc.realtime.IPHelper;
import com.wangcheng.dc.realtime.KeywordBaseURLCategoryHelper;
import com.wangcheng.dc.realtime.StorageHandler;
import com.wangcheng.dc.realtime.URLCategoryHelper;
import com.wangcheng.dc.realtime.UserCenterHelper;
import com.wangcheng.dc.realtime.db.DBHelper;
import com.wangcheng.dc.realtime.db.DBManager;

public class UDPServer {
	
	private static final Logger log = Logger.getLogger(UDPServer.class); 
	/**
	 * 默认配置文件
	 */
	public static final String DEFAULT_CONFIG_FILE = "/realtime.properties";
	
	/**
	 * 日志配置文件
	 */
	public static final String DEFAULT_LOG4J_FILE = "/log4j-rta.properties";
	
	/**
	 * udp监听端口
	 */
	public static final String REALTIME_UDP_PORT_NAME = "realtime.udp.port";   
	
	/**
	 * 最大处理队列
	 */
	public static final String MAX_REALTIME_QUEUE_NAME = "realtime.queue.max";
	
	/**
	 * 队列处理间隔
	 */
	public static final String CHECK_REALTIME_QUEUE_INTERVAL_NAME = "realtime.queue.checkInterval"; 
	 
	
	private static final UDPServer instance = new UDPServer();
	/**
	 * jmx server
	 */
	private MBeanServer mbeanServer = null; 

	
	/**
	 * 请求队列
	 */
	private RequestQueue<String> requestQueue = null;
	
	private UDPRequestHandler requestHandler = null;
	
	private StorageHandler storageHandler = null;
	
	private RealTimeQueueMonitor realTimeQueueMonitor = null;
	
	private Properties properties = new Properties();

	/**
	 * @param args
	 * @throws NullPointerException 
	 * @throws NotCompliantMBeanException 
	 * @throws MBeanRegistrationException 
	 * @throws InstanceAlreadyExistsException 
	 * @throws MalformedObjectNameException 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configure(UDPServer.class
				.getResource(DEFAULT_LOG4J_FILE));
		UDPServer server = getInstance();
		server.init();
		server.start();
		
		
	}
	
	public static UDPServer getInstance(){
		return instance;
	}
 
	private void init() throws Exception {
		 
		loadConfig();
		
		DBManager.initInstance(properties);
		DBHelper.initDBHelper(Integer.valueOf(properties.getProperty(DBManager.JDBC_BATCHSIZE_NAME,"100")));
		IPHelper.initCache();
		KeywordBaseURLCategoryHelper.loadConfig();
		
		initUC();
		
		mbeanServer = ManagementFactory.getPlatformMBeanServer();
		
		requestQueue = new RequestQueue<String>(Integer.valueOf(properties.getProperty(MAX_REALTIME_QUEUE_NAME,"1000")));
		
		requestHandler = new UDPRequestHandler(requestQueue);
		
		storageHandler = new StorageHandler(properties);
		
		realTimeQueueMonitor = new RealTimeQueueMonitor(storageHandler);
		realTimeQueueMonitor.initialize(properties, requestQueue);
		
		ObjectName objectName = new ObjectName("netty:name=realtime");
		
		mbeanServer.registerMBean(requestHandler, objectName); 
		
		ObjectName queueObjectName = new ObjectName("netty:name=realtimeQueueMonitor");
		
		mbeanServer.registerMBean(realTimeQueueMonitor, queueObjectName); 
	}
	 
	private void loadConfig() throws IOException{
		properties.load(UDPServer.class.getResourceAsStream(DEFAULT_CONFIG_FILE));
	}
	
	private void initUC(){		
		try {
			UserCenterHelper.initUC(properties);
		} catch (Exception e) {
			log.error("init UserCenterHelper failture",e);
		}  
	}
	private void start(){
		/*DatagramChannelFactory factory = new OioDatagramChannelFactory(Executors.newCachedThreadPool());
		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);

		ChannelPipeline pipeline = bootstrap.getPipeline();
		//pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("decoder", new StringDecoder()); 
		pipeline.addLast("handler",requestHandler);
		

		int port = Integer.valueOf(properties.getProperty(REALTIME_UDP_PORT_NAME,"18881"));
		
		log.info("starting UDP server at port="+port);
		
		bootstrap.setOption(
                "receiveBufferSizePredictorFactory",
                new FixedReceiveBufferSizePredictorFactory(2048));
		bootstrap.setOption("broadcast", "false");
		
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port)); */
		
		int port = Integer.valueOf(properties.getProperty(REALTIME_UDP_PORT_NAME,"18881"));
		
		MiniUDPServer miniServer = new MiniUDPServer(port);
		
		miniServer.addHandler(new MiniUDPRequestHandler() {
			
			@Override
			public void onMessageRecieved(String message) {
				try {
					 
					requestQueue.add(message);
					if(requestQueue.isFull()){
						realTimeQueueMonitor.notifyQueueFull();
					} 
				} catch (IOException e) {
					log.error(e.getMessage(),e);
				} 
				
			}
		});
		
		log.info("starting UDP server at port="+port);
		
		miniServer.start();
		 
		
		realTimeQueueMonitor.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				realTimeQueueMonitor.destory();
			}
		}); 
		
	}

	public RealTimeQueueMonitor getRealTimeQueueMonitor() {
		return realTimeQueueMonitor;
	}

}
