package com.wangcheng.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class WebLoader {

	private static final String USER_AGENT_NAME = "User-Agent";
	private static final String REFERER_NAME = "Referer";
	private static final String IP_NAME = "X-Real-IP";

	private static final String[] USER_AGENTS = new String[]{"Mozilla/5.0+(Windows;+U;+Windows+NT+5.2;+zh-CN;+rv:1.9.2.8)+Gecko/20100722+Firefox/3.6.8","Mozilla/5.0+(Windows;+U;+Windows+NT+5.2;+zh-CN;+rv:1.9.2.8)+Gecko/20100722+IE/7"};
	private static String[] REFERERS = new String[]{"http://item.taobao.com/item.htm?id=1697993631"};
	
	private static String[] IPS = null;
	
	private static CountDownLatch readyGate = null;
	private static CountDownLatch startGate = null;
	private static CountDownLatch endGate = null;

	//SELECT UserID.UserID, IP.IP, URL.Url, Agent.Agent, Preurl.Preurl, Title.Title
//FROM Test.dbo.Agent Agent, Test.dbo.IP IP, Test.dbo.Preurl Preurl, Test.dbo.Title Title, Test.dbo.URL URL, Test.dbo.UserID UserID
	 
	
	
	private static final AtomicLong requestCount = new AtomicLong(0);

	private static final AtomicLong successCount = new AtomicLong(0);

	private static final AtomicLong failCount = new AtomicLong(0);
	
	private static int num = 10;
	
	private static long sleepTime = 10;
	
	private static List<String> urls = new ArrayList<String>();

	public static void main(String args[]) throws InterruptedException, IOException {
		int concurrent = 200; 
		
		String url = "http://localhost:8080/c?u_name=alipublic01";

		String urlFile = null;
		
		String refererFile = null;
		
		String ipFile = null;
		 
		List<String> referers = new ArrayList<String>();
		
		List<String> ips = new ArrayList<String>();
		
		for(int i=0;i<args.length;i++){ 
			if(args[i].equals("-n")){
				num = Integer.valueOf(args[++i]);
			}else if(args[i].equals("-c")){
				concurrent = Integer.valueOf(args[++i]);
			}else if(args[i].equals("-url")){
				url = args[++i];
			}else if(args[i].equals("-file")){
				urlFile = args[++i];
			}else if(args[i].equals("-referer")){
				refererFile = args[++i];
			}else if(args[i].equals("-ip")){
				ipFile = args[++i];
			}else if(args[i].equals("-sleep")){
				sleepTime = Long.valueOf(args[++i]);
			}
		}
		
		if(urlFile!=null){
			BufferedReader reader = new BufferedReader( new FileReader(urlFile));
			url = reader.readLine();
			while(url!=null){
				urls.add(url);
				url = reader.readLine();
			}
		}else{
			urls.add(url);
		}
		
		if(refererFile!=null){
			BufferedReader reader = new BufferedReader( new FileReader(refererFile));
			String referer = reader.readLine();
			
			if(referer!=null){
				while(referer!=null){
					referers.add(referer);
					referer = reader.readLine();
				}				
				REFERERS = referers.toArray(new String[0]);
			}
			
			
		} 
		
		if(ipFile!=null){
			BufferedReader reader = new BufferedReader( new FileReader(ipFile));
			String ip = reader.readLine();
			
			if(ip!=null){
				while(ip!=null){
					ips.add(ip);
					ip = reader.readLine();
				}				
				IPS = ips.toArray(new String[0]);
				
				System.out.println("ips="+ips);
			}
		} else{
			IPS = new String[]{"211.147.254.128"};
		}
		
		System.out.println(concurrent+" clients run in "+num+" times ,invervals="+sleepTime+" on urls :"+urls); 
		readyGate = new CountDownLatch(concurrent);
		startGate = new CountDownLatch(1);
		endGate = new CountDownLatch(concurrent);
		
		for (int i = 0; i < concurrent; i++) {
			new Thread(new RequestRunner(urls.get(i%urls.size()))).start();
		} 
		
		readyGate.await();
		
		startGate.countDown();	 
		long begin = System.currentTimeMillis(); 		
		endGate.await();
		long end = System.currentTimeMillis(); 

		System.out.println("success=" + successCount + ",error=" + failCount
				+ ",in " + (end-begin)/1000 + " seconds"+",speed="+(successCount.get()*1000/(end-begin)));

	}

	private static final class RequestRunner implements Runnable {
		private String url = "";

		private HttpClient client = null;

		RequestRunner(String url) {
			this.url = url;
			this.client = new HttpClient();
		}

		@Override
		public void run() { 
			
			readyGate.countDown();			 
			
			try {
				startGate.await();
			} catch (InterruptedException e1) {
				throw new RuntimeException(e1);
			}
			
			while(requestCount.getAndIncrement()<num){
				try { 
					
					url = urls.get((int)(Math.random()*urls.size())%urls.size());
					
					GetMethod method = new GetMethod(url);

					method.setRequestHeader(USER_AGENT_NAME, USER_AGENTS[(int)(Math.random()*USER_AGENTS.length)%USER_AGENTS.length]);
					method.setRequestHeader(REFERER_NAME, REFERERS[(int)(Math.random()*REFERERS.length)%REFERERS.length]);					 
					
					if(IPS!=null){
						method.setRequestHeader(IP_NAME, IPS[(int)(Math.random()*IPS.length)%IPS.length]);
					}
					
					client.executeMethod(method);
					method.getResponseBody();		
					//System.out.println(requestCount);
					successCount.incrementAndGet();
				} catch (Exception e) { 
					System.err.println(e);
					failCount.incrementAndGet();
				}finally{
					//method.releaseConnection();
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				
			} 
			
			endGate.countDown();
		}

	}
}
