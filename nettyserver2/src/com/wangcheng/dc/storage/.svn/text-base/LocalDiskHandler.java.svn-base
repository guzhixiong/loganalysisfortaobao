package com.wangcheng.dc.storage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.wangcheng.dc.IConstants;
import com.wangcheng.http.NettyServer;

public class LocalDiskHandler implements StorageHandler<String> {
	private static final Logger log = Logger.getLogger(LocalDiskHandler.class);

	private String localDiskDir = ".";

	private String lastOutputPath = null;

	private OutputStream out = null;

	@Override
	public void initialize(Properties properties) {
		localDiskDir = properties.getProperty(
				NettyServer.LOCAL_DISK_STORAGE_DIR_NAME, localDiskDir);
	}

	
	@Override
	public boolean exsits(String path) throws Exception {
		String outputPath = localDiskDir + File.separator + path;		
		return new File(outputPath).exists();		
	}


	@Override
	public void create(String path) throws Exception {
		String outputPath = localDiskDir + File.separator + path;		
		
		File file = new File(outputPath);		
		
		file.createNewFile();
		
		if(out==null){
			out = new BufferedOutputStream(new FileOutputStream(outputPath,
					true));
			File lockFile = new File(outputPath + ".lock");
			if (!lockFile.exists()) {
				lockFile.createNewFile();
			}			
			lastOutputPath = outputPath;
		}		  
	}


	@Override
	public void flush() throws Exception { 
		
		if(out!=null){
			out.flush();
		}else{
			log.warn("输出流为空");
		}
		
	}

	@Override
	public void close() throws Exception {
		if (out != null) {
			try {
				out.flush();

			} finally {			
				try {
					out.close();
				} catch (Exception e) {
					log.error("关闭文件错误:"+e.getMessage(), e);
				}
				
				File lockFile = new File(lastOutputPath + ".lock");

				if (lockFile.exists()) {
					lockFile.delete();
				}

				out = null;
				lastOutputPath = null;
			}
		}

	}

	@Override
	public final void handle(String path, String content) throws Exception {

		String outputPath = localDiskDir + File.separator + path;

		if (out != null) {

			if (!outputPath.equals(lastOutputPath)) {

				File lockFile = new File(lastOutputPath + ".lock");

				if (lockFile.exists()) {
					lockFile.delete();
				}

				IOUtils.closeStream(out);

				out = null;

				out = new BufferedOutputStream(new FileOutputStream(outputPath,
						true));

				lockFile = new File(outputPath + ".lock");

				if (!lockFile.exists()) {
					lockFile.createNewFile();
				}

				lastOutputPath = null;

				lastOutputPath = outputPath;
			}
		} else {
			out = new BufferedOutputStream(new FileOutputStream(outputPath,
					true));
			File lockFile = new File(outputPath + ".lock");
			if (!lockFile.exists()) {
				lockFile.createNewFile();
			}
			
			lastOutputPath = outputPath;
		}

		out.write(content.getBytes());
		out.write(IConstants.LogNames.ROW_DELIMIER_STRING.getBytes());
	}

}
