package com.wangcheng.dc.handler.realtime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.wangcheng.dc.IConstants;
import com.wangcheng.dc.LogInfo;
import com.wangcheng.dc.cache.RequestQueue;
import com.wangcheng.dc.handler.AbstractRequestHandler;
import com.wangcheng.http.NettyServer;

public class TCPRealTimeHandler extends AbstractRequestHandler implements RealtimeSender {

	private final static Logger log = Logger
			.getLogger(UDPRealTimeHandler.class);
	
	private final static String UDP_REALTIME_PORT_NAME = "realtime.udp.port";
	
	private final static String UDP_REALTIME_ADDRESS_NAME = "realtime.udp.address";
	
	public final static String UDP_REALTIME_BUFFER_SIZE_NAME = NettyServer.MAX_REALTIME_REQUEST_QUEUE_NAME;
	
	public final static String UDP_REALTIME_PACKET_SIZE_NAME = "realtime.udp.packetSize";
	
	private InetAddress address = null;;
	
	private int port = 18881;
	 
	private volatile int packetSize = 10;
	
	private DatagramSocket ds = null;
	
	private RequestQueue<String> buf = null;
	
	private BufferMonitor monitor = null;
 
	@Override
	public void initialize(Properties properties) throws Exception {
		super.initialize(properties); 
		port = Integer.valueOf(properties.getProperty(UDP_REALTIME_PORT_NAME,"18881"));
		address = InetAddress.getByName(properties.getProperty(UDP_REALTIME_ADDRESS_NAME,"127.0.0.1")); 
		packetSize = Integer.valueOf(properties.getProperty(UDP_REALTIME_PACKET_SIZE_NAME,"5"));
		ds = new DatagramSocket();
		
		buf = NettyServer.getServerInstance().getRealtimeRequestQueue();
		
		monitor = new BufferMonitor(this,buf,packetSize);
		monitor.setName("实时udp发送缓冲线程");
		monitor.start();
		
		NettyServer.getServerInstance().registerMBean("netty:name=udpBufferMonitor", monitor);
	} 

	@Override
	public void handle(final String ip, final HttpRequest request,LogInfo logInfo) throws Exception {

		try {
			
			sendData(logInfo.getContent().getBytes());
			 
			buf.add(logInfo.getContent()+IConstants.LogNames.ROW_DELIMIER_STRING);   
			
			if(buf.isFull()){
				monitor.notifyFull();
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void sendData(byte[] data) throws IOException{		
		
		DatagramPacket dp = new DatagramPacket(data,data.length,address,port);
		
		ds.send(dp);
		
		data = null;
		
		dp = null; 
	}
}
