package com.wangcheng.dc.cache;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestQueue<T> {	
	 
	private int maxQueueSize = 100000;//10m
	 
	private Queue<T> queue = null;
	
	private transient volatile int size = 0;
	
	public RequestQueue(int maxQueueSize) throws IOException{
		queue = new ConcurrentLinkedQueue<T>();
		this.maxQueueSize = maxQueueSize;
	}
	
	public final void add(T content) throws IOException{
		size++; 
		queue.add(content);
	}  
	
	public final boolean isFull(){
		return size >=maxQueueSize;
	}
	
	public final T pull(){
		if(size>0){
			size--;
		}
		
		return queue.poll();
	}
	
	public final int size(){
		return size;
	}
}
