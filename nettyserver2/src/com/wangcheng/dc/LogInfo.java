package com.wangcheng.dc;

import java.io.Serializable;
import java.util.Date;

public class LogInfo implements Serializable{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = -6212916463673247405L;

	private transient Date logTime;
	
	private String content;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LogInfo(){
		logTime = new Date();
	}
	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
}
