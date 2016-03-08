package com.wangcheng.dc.handler;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.wangcheng.dc.LogInfo;
import com.wangcheng.dc.cache.RequestQueue;
import com.wangcheng.dc.monitor.QueueMonitor;
import com.wangcheng.http.NettyServer;

public class LogHandler extends AbstractRequestHandler implements
		RequestHandler {

	private RequestQueue<LogInfo> requestQueue = null;

	private QueueMonitor queueMonitor = null;

	@Override
	public void initialize(Properties properties) {
		requestQueue = NettyServer.getServerInstance().getRequestQueue();
		executer = Executors.newCachedThreadPool();
		queueMonitor = NettyServer.getServerInstance().getQueueMonitor();
	}

	private void doHandle(final String ip, final HttpRequest request,LogInfo logInfo) throws IOException {

		String content =  this.getFormattedContent(ip, request);   
		
		logInfo.setContent(content);
		
		requestQueue.add(logInfo);
		if (requestQueue.isFull()) {
			queueMonitor.notifyQueueFull();
		}
	}

	@Override
	public void handle(final String ip, final HttpRequest request,LogInfo logInfo) throws Exception {
		/*
		 * executer.execute(new Runnable() { public void run() {
		 */
		doHandle(ip, request,logInfo);
		/*
		 * } });
		 */

	} 

}
