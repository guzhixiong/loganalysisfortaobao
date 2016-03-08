package com.wangcheng.http;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.wangcheng.dc.LogInfo;
import com.wangcheng.dc.cache.RequestQueue;
import com.wangcheng.dc.handler.RequestHandler;
import com.wangcheng.dc.monitor.QueueMonitor;
import com.wangcheng.dc.storage.StorageHandler;

public class NettyServer {
	
	private static final Logger log = Logger.getLogger(NettyServer.class);
	/**
	 * 默认配置文件
	 */
	public static final String DEFAULT_CONFIG_FILE = "/netty.properties";
	
	/**
	 * 日志配置文件
	 */
	public static final String DEFAULT_LOG4J_FILE = "/log4j-netty.properties";
	
	/**
	 * http监听端口
	 */
	public static final String SERVER_PORT_NAME = "server.port";
	
	/**
	 * http监听端口
	 */
	public static final String SERVER_ID_NAME = "server.id";
	
	
	public static final String DOCUMENT_ROOT_NAME = "document.root";
	
	
	public static final String REQUEST_REDIRECT_NAME = "request.redirect";
	
	
	/**
	 * 请求处理类
	 */
	public static final String REQUEST_HANDLERS_NAME = "request.handlers";
	
	/**
	 * 最大处理队列
	 */
	public static final String MAX_REQUEST_QUEUE_NAME = "request.queue.max";
	
	/**
	 * 最大实时处理队列
	 */
	public static final String MAX_REALTIME_REQUEST_QUEUE_NAME = "realtime.queue.max";
	
	/**
	 * 队列处理间隔
	 */
	public static final String CHECK_REQUEST_QUEUE_INTERVAL_NAME = "request.queue.checkInterval";
	
	/**
	 * 磁盘数据同步间隔
	 */
	public static final String CHECK_LOCAL_DISK_INTERVAL_NAME = "storage.checkLocalDiskInterval";
	
	
	/**
	 * hdfs 目录地址
	 */
	public static final String HDFS_STORAGE_DIR_NAME = "storage.hdfs.dir"; 	
	
	/**
	 * 存储缓存大小
	 */
	public static final String MAX_WRITE_BUFFER_SIZE_NAME = "storage.maxWriteBufferSize"; 	
	
	/**
	 * 数据采集的本地目录
	 */
	public static final String LOCAL_DISK_STORAGE_DIR_NAME = "storage.local.dir"; 	
	
	/**
	 * 记录文件名种的日期的格式
	 */
	public static final String STORAGE_DATE_FORMAT_NAME = "storage.dateformat"; 	
	
	/**
	 * 记录文件时间间隔
	 */
	public static final String LOG_INTERVAL_NAME = "storage.logInterval"; 	
	
	/**
	 * 数据存储主接口
	 */
	public static final String STORAGE_HANDLER_MAIN_NAME = "storage.handlers.main"; 	
	
	/**
	 * 数据处理备接口
	 */
	public static final String STORAGE_HANDLER_SLAVE_NAME = "storage.handlers.slave"; 	

	/**
	 * 默认的http监听端口
	 */
	public static final String DEFAULT_SERVER_PORT = "8080";

	/**
	 * 服务器实例
	 */
	private static NettyServer instance = new NettyServer();

	/**
	 * 配置属性
	 */
	private Properties props = new Properties();
	
	
	

	private String documentRoot = "html";
	
	/**
	 * 请求队列
	 */
	private RequestQueue<LogInfo> requestQueue = null;
	
	/**
	 * 实时请求队列
	 */
	private RequestQueue<String> realtimeRequestQueue = null;
	
	/**
	 * 情况处理接口
	 */
	private List<RequestHandler> requestHandlers = null;
	
	/**
	 * 队列处理监听器
	 */
	private QueueMonitor queueMonitor = null; 

	/**
	 * 主存储接口
	 */
	private StorageHandler<String> mainStorageHandler = null;
	
	/**
	 * 被存储接口
	 */
	private StorageHandler<String> slaveStorageHandler = null;
	
	/**
	 * jmx server
	 */
	private MBeanServer mbeanServer = null;
	
	
	

	/**
	 * 主程序入口
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		NettyServer server = getServerInstance();
		// init server
		server.init();
		// start the server
		server.start();
	}

	/**
	 * 获取服务器实例
	 * @return
	 */
	public static NettyServer getServerInstance() {
		return instance;
	}

	/**
	 * 初始化服务器
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private final void init() throws Exception {
		
		PropertyConfigurator.configure(NettyServer.class.getResource(DEFAULT_LOG4J_FILE));
		
		mbeanServer = ManagementFactory.getPlatformMBeanServer();	
		
		loadConfig();
		
		this.documentRoot = this.props.getProperty(NettyServer.DOCUMENT_ROOT_NAME,"html");
		
		initRequestQueue();				 
		initStorageHandlers();
		initQueueMonitor();		
		initRequestHandlers();
		//initDiskMonitor();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				queueMonitor.destory();
			}
		}); 
	}
	
	/**
	 * 加载配置文件
	 * 
	 * @return netty config properties
	 * @throws IOException
	 *             if config resource is not available
	 */
	private void loadConfig() throws IOException {
		props.load(NettyServer.class.getResourceAsStream(DEFAULT_CONFIG_FILE));
	} 
	
	/**
	 * 初始化请求队列
	 * 
	 * @throws Exception
	 */
	private void initRequestQueue() throws Exception {
		requestQueue = new RequestQueue<LogInfo>(Integer.valueOf(props.getProperty(NettyServer.MAX_REQUEST_QUEUE_NAME,"100000")));
		realtimeRequestQueue = new RequestQueue<String>(Integer.valueOf(props.getProperty(NettyServer.MAX_REALTIME_REQUEST_QUEUE_NAME,"10000")));
	} 

	/**
	 * 初始化请求处理接口
	 * @throws Exception
	 */
	private void initRequestHandlers() throws Exception {
		String[] handlerClassNames = props.getProperty(REQUEST_HANDLERS_NAME).split(",");
		 
		this.requestHandlers = new ArrayList<RequestHandler>(handlerClassNames.length);
		
		for(String handlerClassName:handlerClassNames){
			
			log.info("initializing request handler class :"+handlerClassName);
			
			Class  handlerClass = Class.forName(handlerClassName);
			RequestHandler requestHandler = (RequestHandler)handlerClass.newInstance();
			Method method = handlerClass.getMethod("initialize",props.getClass());
			method.invoke(requestHandler,props); 
			
			requestHandlers.add(requestHandler);
		}		
		
		HttpRequestHandler.initInstance(requestHandlers); 
	} 
	
	/**
	 * 初始化主存储和备存储接口
	 * @throws Exception
	 */
	private void initStorageHandlers() throws Exception {
		
		Class  mainStorageHandlerClass = Class.forName(props.getProperty(STORAGE_HANDLER_MAIN_NAME));
		mainStorageHandler = (StorageHandler)mainStorageHandlerClass.newInstance();
		Method method = mainStorageHandlerClass.getMethod("initialize",props.getClass());
		method.invoke(mainStorageHandler,props); 
		
	//	slaveStorageHandler = mainStorageHandler;
		
		Class  slaveStorageHandlerClass = Class.forName(props.getProperty(STORAGE_HANDLER_SLAVE_NAME));
		
		if(slaveStorageHandlerClass.equals(mainStorageHandlerClass)){
			slaveStorageHandler = mainStorageHandler;
		}else{
			slaveStorageHandler = (StorageHandler)slaveStorageHandlerClass.newInstance();
			method = slaveStorageHandlerClass.getMethod("initialize",props.getClass());
			method.invoke(slaveStorageHandler,props); 
		}
		
	} 
	
	public RequestQueue<String> getRealtimeRequestQueue() {
		return realtimeRequestQueue;
	}

	/**
	 * 初始化队列监听器
	 * @throws UnknownHostException 
	 * @throws NullPointerException 
	 * @throws MalformedObjectNameException 
	 * @throws NotCompliantMBeanException 
	 * @throws MBeanRegistrationException 
	 * @throws InstanceAlreadyExistsException 
	 */
	private void initQueueMonitor() throws UnknownHostException, MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException{
		queueMonitor = new QueueMonitor(mainStorageHandler,slaveStorageHandler);
		queueMonitor.initialize(props, requestQueue);
		ObjectName objectName = new ObjectName("netty:name=queueMonitor");
		mbeanServer.registerMBean(queueMonitor, objectName);
	}
	 
	/**
	 * 启动服务器
	 */
	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new HttpServerPipelineFactory());

		int port = Integer.valueOf(props.getProperty(
				SERVER_PORT_NAME, DEFAULT_SERVER_PORT));
		
		log.info("starting netty server at port="+port);
		
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port)); 
		
		log.info("starting queue monitor server ");
		
		queueMonitor.start(); 
		
		//localDiskMonitor.start();
	}

	/**
	 * 关闭服务器
	 */
	public void stop() {
		// TODO stop netty server
		
		queueMonitor.destory();
	}
	
	public void registerMBean(String objectName,Object mbean) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, NullPointerException{
		this.mbeanServer.registerMBean(mbean, new ObjectName(objectName));
	}
	
	public RequestQueue<LogInfo> getRequestQueue() {
		return requestQueue;
	}
 

	public QueueMonitor getQueueMonitor() {
		return queueMonitor;
	}

	public List<RequestHandler> getRequestHandlers() {
		return requestHandlers;
	}
	public String getDocumentRoot() {
		return documentRoot;
	}
	
	public String getRedirectUrl() {
		return this.props.getProperty(REQUEST_REDIRECT_NAME);
	}
}
