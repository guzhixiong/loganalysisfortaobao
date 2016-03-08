package com.wangcheng.dc.monitor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.wangcheng.http.NettyServer;

public class LocalDiskMonitor extends Thread implements LocalDiskMonitorMBean {

	private static final Logger log = Logger.getLogger(QueueMonitor.class);

	private AtomicBoolean running = new AtomicBoolean(false);

	private AtomicBoolean processing = new AtomicBoolean(false);

	private AtomicLong uploadFileCounter = new AtomicLong(0);

	private AtomicLong uploadTotalBytesCounter = new AtomicLong(0);

	private Properties props = null;

	private long checkLocalDiskInterval = 5 * 60 * 1000;

	private final static int LOG_DIR_NAME_LENGTH = 8;

	private String localDiskDir = ".";

	private String uri = "hdfs://localhost:9000/log";

	private Configuration conf = null;

	private FileSystem fs = null;

	private File localFileDir = null;

	private FileFilter fileFilter = null;

	public LocalDiskMonitor() {
		conf = new Configuration();
		conf.set("dfs.support.append", "true");
		this.setName("本地文件与hadoop同步线程");
	}

	public void initialize(Properties properties) throws IOException {
		this.props = properties;
		uri = properties.getProperty(NettyServer.HDFS_STORAGE_DIR_NAME, uri);

		localDiskDir = properties.getProperty(
				NettyServer.LOCAL_DISK_STORAGE_DIR_NAME, localDiskDir);

		localFileDir = new File(localDiskDir);

		if (!localFileDir.isDirectory()) {
			throw new RuntimeException(localDiskDir + "不是目录!!!");
		}

		checkLocalDiskInterval = Long.valueOf(properties.getProperty(
				NettyServer.CHECK_LOCAL_DISK_INTERVAL_NAME, "300000"));

		try {
			fs = FileSystem.get(URI.create(uri), conf);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		fileFilter = new FileFilter() {
			public boolean accept(File file) {
				if (file.getName().endsWith(".log") && file.canRead()) {

					File lockFile = new File(file.getAbsolutePath() + ".lock");

					if (!lockFile.exists()) {						
						return true;
					}
				}

				return false;
			}
		};
	}

	public void start() {
		running.set(true);
		super.start();
	}

	@SuppressWarnings("deprecation")
	public void run() {
		while (running.get()) {

			if (fs == null) {
				try {
					fs = FileSystem.get(URI.create(uri), conf);
				} catch (IOException e) {
					try {
						sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}

			if(log.isDebugEnabled()){
				log.debug("开始检查本地磁盘文件...");
			}
			

			File[] files = localFileDir.listFiles(fileFilter);

			for (File file : files) {

				String filename = file.getName();

				Path localPath = new Path(file.getAbsolutePath());

				String logDirName = filename.substring(0, LOG_DIR_NAME_LENGTH);

				Path logDir = new Path(uri + Path.SEPARATOR + logDirName);

				try {

					if (!fs.exists(logDir)) {
						fs.mkdirs(logDir);
					}

					Path hdfsPath = new Path(logDir, filename);

					Path tmpHdfsPath = new Path(logDir, "_" + filename);

					if (fs.exists(hdfsPath)) {
						long lastModified = fs.getFileStatus(hdfsPath)
								.getModificationTime();
						if (lastModified < file.lastModified()) {
							fs.rename(hdfsPath,new Path(logDir, "bak_" + filename));
							onProcess(file, localPath, hdfsPath, tmpHdfsPath);
						} else {
							log.warn("检测到hdfs文件比本地磁盘文件版本新");
							// file.delete();
						}
					}
					if (fs.exists(tmpHdfsPath)) {
						fs.delete(tmpHdfsPath, false);
						onProcess(file, localPath, hdfsPath, tmpHdfsPath);
					} else {
						onProcess(file, localPath, hdfsPath, tmpHdfsPath);
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				} finally {
					processing.set(false);
				}
			}
			
			if(log.isDebugEnabled()){
				log.info("完成检查本地磁盘文件");
			}

			

			try {
				sleep(checkLocalDiskInterval);
			} catch (InterruptedException e) {
				log.error(e);
			}

		}
	}

	private void onProcess(File file, Path localPath, Path hdfsPath,
			Path tmpHdfsPath) throws IOException {
		processing.set(true);

		fs.copyFromLocalFile(false, true, localPath, tmpHdfsPath);

		fs.rename(tmpHdfsPath, hdfsPath);

		uploadFileCounter.incrementAndGet();
		uploadTotalBytesCounter.addAndGet(file.length());

		file.delete();
	}

	@Override
	public long getUploadTotalSize() {
		return this.uploadTotalBytesCounter.get();
	}

	@Override
	public long getUploadFileCount() {
		return uploadFileCounter.get();
	}

	public boolean getProcessing() {
		return processing.get();
	}
	
	/**
	 * 日志配置文件
	 */
	public static final String DEFAULT_LOG4J_FILE = "/log4j-diskmonitor.properties";

	public static void main(String[] args) throws IOException,
			MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException {
		
		PropertyConfigurator.configure(NettyServer.class.getResource(DEFAULT_LOG4J_FILE));
		
		Properties props = new Properties();
		props.load(NettyServer.class
				.getResourceAsStream(NettyServer.DEFAULT_CONFIG_FILE));

		LocalDiskMonitor localDiskMonitor = new LocalDiskMonitor();
		localDiskMonitor.initialize(props);

		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

		ObjectName objectName = new ObjectName("netty:name=diskMonitor");
		mbeanServer.registerMBean(localDiskMonitor, objectName);

		localDiskMonitor.start();

	}
}
