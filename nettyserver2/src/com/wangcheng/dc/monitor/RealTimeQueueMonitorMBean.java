package com.wangcheng.dc.monitor;

public interface RealTimeQueueMonitorMBean {
	public long getProcessedCount();
	public int getWaitingCount();
	public long getErrorCount();
}
