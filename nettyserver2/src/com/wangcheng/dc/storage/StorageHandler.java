package com.wangcheng.dc.storage;

import java.io.IOException;
import java.util.Properties;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public interface StorageHandler<T> {
 
	public void initialize(Properties properties)throws IOException; 
	
	public boolean exsits(String path)throws Exception;
	
	public void create(String path)throws Exception;
	
	public void handle(String path,T content)throws Exception;
	
	public void flush()throws Exception;
	
	public void close()throws Exception;
}
