package com.wangcheng.dc.handler;

import java.util.Properties;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.wangcheng.dc.LogInfo;

public interface RequestHandler {
	public void initialize(Properties properties)throws Exception;
	public void handle(String remoteIp,HttpRequest request,LogInfo logInfo)throws Exception; 
}
