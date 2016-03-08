package com.wangcheng.utils;

public class NettyServerMonitor {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		String port = "8903";
		String mbeanName = "netty:name=queueMonitor";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h")) {
				ip = args[++i];
			} else if (args[i].equals("-p")) {
				port = args[++i];
			} 
		}
		
		JMXMonitor jmxMonitor = new JMXMonitor(ip,port,mbeanName); 
		
		long lastRecievedCount = 0 ; 
		
		while(true){
			
			long processedCount = (Long)jmxMonitor.getAttribute(mbeanName,"ProcessedCount");
			Integer waitingCount = (Integer)jmxMonitor.getAttribute(mbeanName,"WaitingCount"); 
			
			long deltaCount = lastRecievedCount-(processedCount+waitingCount);
			
			System.out.println("speed="+deltaCount);
			
			lastRecievedCount = processedCount+waitingCount;
			
			Thread.sleep(1000);
		}

	}

}
