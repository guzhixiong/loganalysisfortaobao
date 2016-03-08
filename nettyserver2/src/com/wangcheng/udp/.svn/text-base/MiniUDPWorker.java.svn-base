package com.wangcheng.udp;

import java.net.DatagramPacket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

public class MiniUDPWorker implements Runnable {

	private static final Logger log = Logger.getLogger(MiniUDPWorker.class); 
	
	private MiniUDPRequestHandler handler = null;

	private Queue<DatagramPacket> taskQueue = null;

	private final Object signal = new Object();

	public MiniUDPWorker(MiniUDPRequestHandler handler) {
		taskQueue = new ConcurrentLinkedQueue<DatagramPacket>();
		this.handler = handler;
	}

	public void addTask(DatagramPacket dp) {
		taskQueue.add(dp);
		/*synchronized (signal) {
			signal.notify();
		}*/

	}

	@Override
	public void run() {
		while (true) {
			try { 
				DatagramPacket dp = taskQueue.poll();
				while (dp != null) {
					handler.onMessageRecieved(new String(dp.getData(),0,dp.getLength())); 
					dp = null;
					dp = taskQueue.poll();
				}  
				
				synchronized (signal) {
					signal.wait(500);
				}
			} catch (Throwable e) {
				log.error("接收udp数据错误",e);
			}
		}

	}

}
