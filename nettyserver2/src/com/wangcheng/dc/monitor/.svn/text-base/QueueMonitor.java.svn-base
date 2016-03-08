package com.wangcheng.dc.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.wangcheng.dc.LogInfo;
import com.wangcheng.dc.cache.RequestQueue;
import com.wangcheng.dc.storage.StorageHandler;
import com.wangcheng.http.NettyServer;

public final class QueueMonitor extends Thread implements QueueMonitorMBean {

	private static final Logger log = Logger.getLogger(QueueMonitor.class);

	private Properties props = null;

	private RequestQueue<LogInfo> requestQueue;

	private long checkQueueInterval = 60 * 1000;
	
	/**
	 * 记录日志时间间隔
	 */
	private int logInterval = 20;//分钟

	private long maxQueueSize = 100000;

	private AtomicBoolean running = new AtomicBoolean(false);

	private AtomicLong processedCounter = new AtomicLong(0);

	private AtomicLong errorCounter = new AtomicLong(0);

	private int maxHistoryCounter = 0;

	private StorageHandler<String> slaveStorageHandler = null;

	private StorageHandler<String> mainStorageHandler = null;

	private String storagePrefix = null;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

	private final Object fullSignal = new Object();

	public QueueMonitor(StorageHandler<String> mainStorageHandler,
			StorageHandler<String> slaveStorageHandler)
			throws UnknownHostException {
		this.mainStorageHandler = mainStorageHandler;
		this.slaveStorageHandler = slaveStorageHandler;
	}

	public void initialize(Properties properties,
			RequestQueue<LogInfo> requestQueue) throws UnknownHostException {
		this.props = properties;
		this.requestQueue = requestQueue;
		checkQueueInterval = Long.valueOf(properties.getProperty(
				NettyServer.CHECK_REQUEST_QUEUE_INTERVAL_NAME, "60000"));
		maxQueueSize = Long.valueOf(properties.getProperty(
				NettyServer.MAX_REQUEST_QUEUE_NAME, "100000"));
		sdf = new SimpleDateFormat(props.getProperty(
				NettyServer.STORAGE_DATE_FORMAT_NAME, "yyyymmddHH"));
		
		logInterval = Integer.valueOf(props.getProperty(
				NettyServer.LOG_INTERVAL_NAME,Integer.toString(logInterval)));
		
		storagePrefix = properties.getProperty(NettyServer.SERVER_ID_NAME,
				InetAddress.getLocalHost().getHostAddress());

		this.setName("队列处理线程" + storagePrefix);
	}

	public void start() {
		running.set(true);
		super.start();
	}

	public void destory() {
		log.info("开始关闭队列监听,将内存中的数据存储到本地磁盘...");

		running.set(false);

		while (requestQueue.size() > 0) {
			processQueue();
		}

		try {
			mainStorageHandler.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		try {
			slaveStorageHandler.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		log.info("完成关闭队列监听,将内存中的数据存储到本地磁盘...");
	}

	public final void notifyQueueFull() {
		synchronized (fullSignal) {
			fullSignal.notify();
		}
	}

	public final void run() {
		
		int count = 0;
		
		while (running.get()) {
			if(log.isDebugEnabled()){
				log.debug("开始检查队列...");
			}
			
			try {
				count = processQueue();

				if (requestQueue.size() >= maxQueueSize / 2) {
					count = processQueue();
				} 

				/**
				 * 防止超过当前小时时文件还没有关闭
				 */
				if(lastPath!=null){
					
					String path = getPath(new Date());
					
					if(!path.equals(lastPath)){						 
						log.info("close file expire hour time limit!!!");
						onClose();
						lastPath = null;
					}
					
				}else{					
					
					String path = getPath(new Date());
					
					lastPath = path;
					
					/**
					 * 保证每小时产生一个文件
					 */
					if(!this.mainStorageHandler.exsits(path)){
						mainStorageHandler.create(path);						
					}else{	
						/**
						 * 保证内存中的数据能被及时被存储，尽可能降低java程序崩溃后数据的丢失
						 */
						this.onFlush();
					} 
					
				}
				
				if(log.isDebugEnabled()){
					log.debug("完成检查队列");
				}
				

				synchronized (fullSignal) {
					fullSignal.wait(checkQueueInterval);
				}

			} catch (Throwable e) {
				log.error("处理队列错误:" + e.getMessage());
			}

		}
	}

	private final int processQueue() {
		int count = 0;

		LogInfo logInfo = requestQueue.pull();

		while (logInfo != null) {

			String path = getPath(logInfo);

			if (onProcess(path, logInfo.getContent()) > 0) {
				processedCounter.incrementAndGet();
			} else {
				onError(path, logInfo.getContent());
				errorCounter.incrementAndGet();
			}

			logInfo.setContent(null);
			logInfo.setLogTime(null);
			logInfo = null;

			count++;

			if (count == maxQueueSize) {
				onFlush();
				count = 0;
				break;
			}

			logInfo = requestQueue.pull();
		}

		return count;

	}
 
	private Calendar calendar = Calendar.getInstance(); 
	
	private String lastPath = null;

	private final String getPath(LogInfo logInfo) { 
		lastPath = sdf.format(logInfo.getLogTime())/*+minute*/+"-"+storagePrefix+".log";
		//calendar.setTime(logInfo.getLogTime());
		//System.out.println(calendar.get(Calendar.MINUTE));
		//int minute = (calendar.get(Calendar.MINUTE)/logInterval)*logInterval;
		return lastPath;
	}
	
	private final String getPath(Date date) { 
		return sdf.format(date)/*+minute*/+"-"+storagePrefix+".log"; 
	}

	protected final int onProcess(String path, String content) {

		try {
			mainStorageHandler.handle(path, content);// hdfsStorageHandler.handle(path,
														// content);
		} catch (Exception e) {
			log.error("主存储失败:" + path, e);
			return -1;
		}

		return 1;
	}

	protected final int onFlush() {
		try {
			mainStorageHandler.flush();
		} catch (Exception e) {
			log.error("刷新存储缓存失败:" + e.getMessage(),e);
			return -1;
		}

		return 1;
	}
	
	protected final int onClose() {

		try {
			mainStorageHandler.close();
		} catch (Exception e) {
			log.error("关闭存储缓存失败:" + e.getMessage());
			return -1;
		}

		return 1;
	}

	protected final int onError(String path, String content) {

		try {
			slaveStorageHandler.handle(path, content);
		} catch (Exception e) {
			log.error("备存储失败:" + path, e);
			return -1;
		}

		return 1;
	}

	@Override
	public long getProcessedCount() {
		return processedCounter.get();
	}

	@Override
	public int getWaitingCount() {
		return this.requestQueue.size();
	}

	@Override
	public long getErrorCount() {
		return errorCounter.get();
	}

	@Override
	public long getMaxHistoryCount() {
		return maxHistoryCounter;
	}

	public String getLastPath() {
		return lastPath;
	}

	public static void main(String args[]) {
		 
		System.out.println(TimeZone.getDefault());
	}
}
