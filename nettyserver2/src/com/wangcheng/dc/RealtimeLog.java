package com.wangcheng.dc;

public class RealtimeLog {
	private String ip = null;
	private String logTime = null;
	private String url = null; 
	private String agent = null;
	private String uid = null;
	private String pid = null;
	
	private String province;
	private String city;
	private String address;
	
	private String title; 
	 
	public RealtimeLog(){
		
	}
 
	public void fill(String ip,String logTime,String url,String agent,String uid,String pid,String province,String city,String address,String title){
		this.ip = ip;
		this.logTime = logTime;
		this.url = url; 
		this.agent = agent;
		this.uid = uid;
		this.pid = pid;
		
		this.province = province;
		this.city = city;
		this.address = address;
		this.title = title; 
	}
	  
	public final  String getIp() {
		return ip;
	}
	public final void setIp(String ip) {
		this.ip = ip;
	}
	public final String getLogTime() {
		return logTime;
	}
	public final void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public final String getUrl() {
		return url;
	}
	public final void setUrl(String url) {
		this.url = url;
	}
	 
	public final String getAgent() {
		return agent;
	}
	public final void setAgent(String agent) {
		this.agent = agent;
	}
	public final String getUid() {
		return uid;
	}
	public final void setUid(String uid) {
		this.uid = uid;
	}
	public final String getPid() {
		return pid;
	}
	public final void setPid(String pid) {
		this.pid = pid;
	}

	public final String getProvince() {
		return province;
	}

	public final void setProvince(String province) {
		this.province = province;
	}

	public final String getCity() {
		return city;
	}

	public final void setCity(String city) {
		this.city = city;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}
	
	public String toString(){
		return this.uid+IConstants.LogNames.LOG_DELIMETER_STRING+url+IConstants.LogNames.LOG_DELIMETER_STRING+agent+IConstants.LogNames.LOG_DELIMETER_STRING+title+IConstants.LogNames.LOG_DELIMETER_STRING+ip;
	}
}
