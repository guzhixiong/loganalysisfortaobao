package com.wangcheng.dc.handler.realtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.wangcheng.dc.cache.RequestQueue;

public class BufferMonitor extends Thread implements BufferMonitorMBean{
	private final Object signal = new Object();
	
	private RequestQueue<String> buf = null;
	
	int totalSend = 0;
	 
	
	private volatile int packetBatchSize = 3;
	
	private volatile int packetSize = 5;
	
	private RealtimeSender sendHandler ;
	
	
	public BufferMonitor(RealtimeSender sendHandler,RequestQueue<String> buf,int packetSize){
		this.buf = buf;
		this.sendHandler = sendHandler; 
		this.packetSize = packetSize;
	}
	
	void notifyFull(){
		synchronized(signal){
			signal.notify();
		}
	}
	public void run(){
	 
		while(true){
			 
			
			try { 
				
				if(buf.size()>0){
					for(int i=0;i<packetBatchSize;i++){
						if(buf.size()>0){
							processBuffer(); 
						}else{
							break;
						} 
					} 
				} 
				
				synchronized(signal){
					signal.wait(300);
				}
			} catch (Exception e) { 
				e.printStackTrace();
			} 
		}
		
	}
	
	private void processBuffer() throws IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
		String content = buf.pull();
		int count = 0;
		while(content!=null){
			count++; 
			bout.write(content.getBytes());
			content = null;
			if(count<packetSize){
				content = buf.pull();	 
			}else{
				break;
			}
		}
		 
		if(count>0){			
			totalSend += count;
			sendHandler.sendData(bout.toByteArray());  
		}  
		
		bout.close();
	}

	@Override
	public int getBufferSize() {
		return buf.size();
	}

	@Override
	public int getSendedSize() {
		return totalSend;
	}
}