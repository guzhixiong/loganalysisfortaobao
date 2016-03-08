package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App9 {

	private static final Logger LOG = LoggerFactory.getLogger(App9.class);

	private String uri = "hdfs://10.10.1.9:9000/";

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void execute(String date) throws IOException {
		Configuration conf = new Configuration();
		final FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path p = new Path("/user/root/data/" + date + "_path_1");
		FileStatus[] files = fs.listStatus(p, new PathFilter() {
			@Override
			public boolean accept(Path path) {
				try {
					LOG.info("::" + path.getName());
					return !fs.getFileStatus(path).isDir();
				} catch (IOException e) {
					LOG.error("", e);
					throw new RuntimeException(e);
				}
			}
		});
		for (FileStatus file : files) {
			InputStreamReader in = new InputStreamReader(
					fs.open(file.getPath()));

			BufferedReader reader = new BufferedReader(in);
			int i = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				i++;
				if (line.split("\\s").length < 2) {
					LOG.info(line);
				}
			}
			LOG.info(file.getPath().getName() + ">>" + i);
		}
	}

	public void executeLocally(String date) throws IOException {
		File path = new File("d:/tmp/" + date + "_path_1");
		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				LOG.info("::" + file.getName());
				return file.isFile();
			}
		});
		for (File file : files) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int i = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				i++;
				if (line.split("\\s").length < 3) {
					LOG.info(line);
				}
			}
			LOG.info(file.getName() + ">>" + i);
		}
	}

	public static void main(String[] args) {
		try {
			App9 at = new App9();
			at.executeLocally("20101206");
		} catch (Exception e) {
			System.exit(-1);
		}
	}

}
