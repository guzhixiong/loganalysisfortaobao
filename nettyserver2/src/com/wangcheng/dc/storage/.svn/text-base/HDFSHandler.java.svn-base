package com.wangcheng.dc.storage;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.wangcheng.dc.IConstants;
import com.wangcheng.http.NettyServer;

/**
 * 线程不安全的，所以必须是在单线程里面运行
 * 
 * @author Administrator
 * 
 */
public class HDFSHandler implements StorageHandler<String> {
	
	private static final Logger log = Logger.getLogger(HDFSHandler.class);
	
	private int maxWriteBufferSize = 1024*1024;
	
	private int maxBufferSize = 10000;
	
	private int bufferSize = 0;

	private Configuration conf = null;

	private String uri = "hdfs://localhost:9000/log";

	private String lastOutputPath = null;

	private FileSystem fs = null;

	private OutputStream out = null; 

	public HDFSHandler() {
		conf = new Configuration();
		conf.set("dfs.support.append", "true"); 
	}

	@Override
	public void initialize(Properties properties) throws IOException {
		uri = properties.getProperty(NettyServer.HDFS_STORAGE_DIR_NAME, uri);
		maxWriteBufferSize = Integer.valueOf(properties.getProperty(NettyServer.MAX_WRITE_BUFFER_SIZE_NAME, "1024000"));
		try{
			fs = FileSystem.get(URI.create(uri), conf); 
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
	}
	
	

	@Override
	public boolean exsits(String path) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void create(String path) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void handle(String path, String content) throws Exception {

		String outputPath = uri + "/" + path;
		 
		if (out != null) {
			if (!outputPath.equals(lastOutputPath)) {
				close();
				bufferSize = 0;
				out = getOutputStream(outputPath);
				lastOutputPath = outputPath;
			} 
		} else {
			out = getOutputStream(outputPath);
		}

		/* try { */
		out.write(content.getBytes()); 
		out.write(IConstants.LogNames.ROW_DELIMIER_STRING.getBytes()); 		
		
		bufferSize++;
		 
		
		if(bufferSize>=maxBufferSize){			
			close();
			bufferSize = 0;
		}
	}

	@Override
	public void flush() throws Exception {
		if (out != null) {
			out.flush();
		}
		
	}
	
	@Override
	public void close() throws Exception {
		if (out != null) {
			IOUtils.closeStream(out);
			out = null;
			lastOutputPath = null;
		}
		
	}

	protected OutputStream getOutputStream(String outputPath)
			throws IOException {
		Path fsPath = new Path(outputPath);
		
		if(fs==null){
			fs = FileSystem.get(URI.create(uri), conf); 
		} 
		
		if (fs.exists(fsPath)) {
			out = new BufferedOutputStream(fs.append(fsPath),maxWriteBufferSize);
		} else {
			out = new BufferedOutputStream(fs.create(fsPath, false),maxWriteBufferSize);
		}

		return out;
	}
	
	

}
