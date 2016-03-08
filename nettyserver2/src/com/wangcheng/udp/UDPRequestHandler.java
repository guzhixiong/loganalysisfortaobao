package com.wangcheng.udp;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.wangcheng.dc.cache.RequestQueue;

public class UDPRequestHandler extends SimpleChannelUpstreamHandler implements UDPRequestHandlerMBean {

	private static final Logger log = Logger.getLogger(UDPRequestHandler.class);
	
	private  volatile long counter = 0;
	
	private RequestQueue<String>  requestQueue;

	UDPRequestHandler(RequestQueue<String> requestQueue) {
		this.requestQueue = requestQueue;
	}

	@Override
	public final void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
			counter++;
			String content = (String)e.getMessage();   
			
			requestQueue.add(content);
			
			if(requestQueue.isFull()){
				UDPServer.getInstance().getRealTimeQueueMonitor().notifyQueueFull();
			} 
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception { 
		 
		log.error("error",e.getCause()); 
	}

	@Override
	public long getRecievedCount() {  
		return counter;
	}
}
