package com.wangcheng.dc.monitor;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.wangcheng.dc.IConstants;
import com.wangcheng.dc.RealtimeLog;
import com.wangcheng.dc.cache.RequestQueue;
import com.wangcheng.dc.realtime.IPHelper;
import com.wangcheng.dc.realtime.KeywordBaseURLCategoryHelper;
import com.wangcheng.dc.realtime.StorageHandler;
import com.wangcheng.dc.realtime.URLCategoryHelper;
import com.wangcheng.dc.utils.StringUtils;
import com.wangcheng.udp.UDPServer;

public class RealTimeQueueMonitor extends Thread implements
		RealTimeQueueMonitorMBean {

	private static final Logger log = Logger
			.getLogger(RealTimeQueueMonitor.class);

	private Properties props = null;

	private RequestQueue<String> requestQueue;

	private long checkQueueInterval = 1000;

	private long maxQueueSize = 1000;

	private long abandonSize = 200000;

	private AtomicBoolean running = new AtomicBoolean(false);

	private AtomicLong processedCounter = new AtomicLong(0);

	private AtomicLong errorCounter = new AtomicLong(0);

	private int maxHistoryCounter = 0;

	private StorageHandler storageHandler = null;

	private final Object fullSignal = new Object();

	public RealTimeQueueMonitor(StorageHandler storageHandler)
			throws UnknownHostException {
		this.storageHandler = storageHandler;
	}

	public void initialize(Properties properties,
			RequestQueue<String> requestQueue) throws UnknownHostException {
		this.props = properties;
		this.requestQueue = requestQueue;
		checkQueueInterval = Long.valueOf(properties.getProperty(
				UDPServer.CHECK_REALTIME_QUEUE_INTERVAL_NAME, "1000"));
		maxQueueSize = Long.valueOf(properties.getProperty(
				UDPServer.MAX_REALTIME_QUEUE_NAME, "1000"));

		this.setName("实时队列处理线程");
	}

	public void start() {
		running.set(true);
		super.start();
	}

	public void destory() {
		log.info("关闭实时数据处理线程...");

		running.set(false);

		while (requestQueue.size() > 0) {
			processQueue();
		}

		try {
			storageHandler.flush();
		} catch (Exception e) {
			log.error("刷新存储缓存失败:" + e.getMessage());
		}
	}

	public final void notifyQueueFull() {
		synchronized (fullSignal) {
			fullSignal.notify();
		}
	}

	public void run() {
		int count = 0;
		while (running.get()) {

			try {
				count = processQueue();

				if (count > 0) {
					onFlush();
				}

				synchronized (fullSignal) {

					fullSignal.wait(checkQueueInterval);
				}

			} catch (Throwable e) {
				log.error("处理实时队列错误:" + e.getMessage(), e);
			}

		}
	}

	private final int processQueue() {

		String contents = requestQueue.pull();

		RealtimeLog data = new RealtimeLog();

		int count = 0;

		String content = null;

		while (contents != null) {

			StringTokenizer stringTokenizer = new StringTokenizer(contents,
					IConstants.LogNames.ROW_DELIMIER_STRING);

			while (stringTokenizer.hasMoreTokens()) {

				count++;

				content = stringTokenizer.nextToken();

				data = parseLogInfo(data, content);

				if (onProcess(data) > 0) {
					processedCounter.incrementAndGet();
				} else {
					onError(data);
					errorCounter.incrementAndGet();
				}
			}

			// System.out.println(processedCounter);

			stringTokenizer = null;

			contents = null;

			contents = requestQueue.pull();
		}

		return count;

	}

	private final RealtimeLog parseLogInfo(RealtimeLog data, String content) {

		if (log.isDebugEnabled()) {
			log.debug(content);
		}

		StringTokenizer st = new StringTokenizer(content,IConstants.LogNames.LOG_DELIMETER_STRING);

		String logDate = st.nextToken();
		String logTime = st.nextToken();
		String uid = st.nextToken();
		String ip = st.nextToken();
		String agent = st.nextToken();
		String url = st.nextToken();  

		String[] geo = IPHelper.EMPTY_ADDRESS;
		try {
			geo = IPHelper.getGeo(ip);
		} catch (SQLException e) {
			log.error(ip, e);
		}
		String province = geo[0];
		String city = geo[1];
		String address = geo[2];
 
		String title = KeywordBaseURLCategoryHelper.parse(url); 
 

		agent = StringUtils.md5(ip + agent);
 
		String pid = null;
		
		if (IConstants.ITEM_KEYWORD.equals(title)) {
			pid = null;
			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
			Map<String, List<String>> params = queryStringDecoder.getParameters();

			List<String> pidParams = params.get(IConstants.LogNames.PARAM_PID); 

			if (pidParams != null && !pidParams.isEmpty()) {
				pid = pidParams.get(0);
			} else {				
				pidParams = params.get(IConstants.LogNames.PARAM_PID2);				
				if (pidParams != null && !pidParams.isEmpty()) {
					pid = pidParams.get(0);
				}
			}			

			params.clear();
			params = null;
			queryStringDecoder = null;
			
		}

		if (pid != null && !org.apache.commons.lang.StringUtils.isNumeric(pid)) {
			pid = null;
			log.warn("item_id of " + url + " is not integer");
		}

		if (log.isDebugEnabled()) {
			log.debug("pid=" + pid);
		}

		data.fill(ip, logTime, url, agent, uid, pid, province, city,
				address, title);

		st = null;
		content = null;
		return data;
	}

	private final int onProcess(RealtimeLog data) {

		try {
			storageHandler.store(data);// hdfsStorageHandler.handle(path,
										// content);
		} catch (Exception e) {
			log.error("主存储失败:" + data + ",原因:" + e.getMessage(), e);
			return -1;
		}

		return 1;
	}

	private final void onFlush() {

		try {
			storageHandler.flush();
		} catch (Exception e) {
			log.error("刷新存储缓存失败:" + e.getMessage());
		}

	}

	private final void onError(RealtimeLog data) {
		try {
			if (requestQueue.size() < abandonSize) {
				Thread.sleep(10000);
			}
			storageHandler.store(data);
		} catch (Exception e) {
			log.error("备存储失败:" + data);
		}

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

}
