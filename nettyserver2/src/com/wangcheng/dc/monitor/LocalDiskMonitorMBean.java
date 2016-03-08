package com.wangcheng.dc.monitor;

public interface LocalDiskMonitorMBean {
	
	public long getUploadTotalSize();
	public long getUploadFileCount();
	public boolean getProcessing();
}
