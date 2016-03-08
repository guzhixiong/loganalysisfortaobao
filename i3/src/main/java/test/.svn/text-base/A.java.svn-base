package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IOUtils;

public class A {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final String date = "20101020";

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("hdfs://10.10.1.9:9000/"),
				conf);

		Path p = new Path("/user/root/tradeHistory/");

		FileStatus[] shops = fs.listStatus(p);
		for (FileStatus shop : shops) {
			Path p1 = shop.getPath();

			System.out.println(p1.getName());

			FileStatus[] trades = fs.listStatus(p1, new PathFilter() {
				@Override
				public boolean accept(Path path) {
					String name = path.getName().replace("-", "");
					return name.indexOf(date) > -1;
				}
			});

			for (FileStatus trade : trades) {
				Path p2 = trade.getPath();
				System.out.println("  " + p2.toString());

				InputStream in = fs.open(p2);

				IOUtils.closeStream(in);
			}
		}

	}

}
