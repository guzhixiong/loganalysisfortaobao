package com.wangcheng.dc.monitor;

public class MemeryMonitor extends Thread {
	private long clearGCTime = 30*60*1000;

	public void run() {
		while (true) {
			try {
				sleep(clearGCTime);
				System.gc();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
