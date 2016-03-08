package com.wangcheng.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

public class MiniUDPServer implements Runnable{
	
	private static final Logger log = Logger.getLogger(MiniUDPServer.class); 
	
	private MiniUDPRequestHandler handler = null;
	
	private DatagramSocket ds = null;
	
	private int bufferSize = 3036;
	
	private MiniUDPWorker[] workers = null;
	
	private int workersCount = Runtime.getRuntime().availableProcessors()*2;
	
	private int bossesCount = Runtime.getRuntime().availableProcessors()*2;
	
	private volatile int  sheduleCounter = 0;
	
	public MiniUDPServer(int port) {
		try {
			ds = new DatagramSocket(port);
			workers = new MiniUDPWorker[workersCount];
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	public void start(){
		for(int i=0;i<workersCount;i++){
			workers[i] = new MiniUDPWorker(handler);
			Thread workerThread = new Thread(workers[i]);
			workerThread.setDaemon(true);
			workerThread.setName("I/O Mini worker "+i);
			workerThread.start();
		}
		
		for(int i=0;i<bossesCount;i++){
			Thread bossThread = new Thread(this);
			bossThread.setName("I/O Mini boss server "+i);
			bossThread.start();
		} 
	}
	
	private MiniUDPWorker nextWorker(){
		return workers[Math.abs((sheduleCounter++%workersCount))];
	}

	@Override
	public void run() {
		
		byte [] buffer = null;
		
		while(true){
			 
			try {
				
				buffer = new byte[bufferSize];
				
				DatagramPacket dp = new DatagramPacket(buffer,bufferSize);
				
				ds.receive(dp);
				
				nextWorker().addTask(dp);		 
				
			} catch (Throwable e) {
				log.error("接收udp数据错误",e);
			}
		}
	} 

	public void addHandler(MiniUDPRequestHandler handler ){
		this.handler = handler;
	} 
}
