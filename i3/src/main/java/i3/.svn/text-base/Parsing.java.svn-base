package i3;

import i.TextComparator;
import i.utils.IPHelper;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parsing extends Configured implements Tool {

	private static final Logger LOG = LoggerFactory.getLogger(Parsing.class);

	public static class M extends
			Mapper<LongWritable, Text, Text, NullWritable> {

		private static final Logger LOG = LoggerFactory.getLogger(M.class);

		private RandomAccessFile file = null;
		private IPHelper helper = null;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			file = new RandomAccessFile(IPHelper.IP_DATA_FILE, "r");
			helper = IPHelper.initCache(file);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			if (file != null) {
				file.close();
			}
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 i3 = new I3();
			try {
				i3.parse(helper, value.toString());
			} catch (RuntimeException e) {
				LOG.info(e.getMessage());
				return;
			}
			context.write(new Text(i3.toString()), NullWritable.get());
		}

	}

	public static class ShopPartitioner<T> extends Partitioner<Text, T> {

		@Override
		public int getPartition(Text key, T value, int parts) {
			I3 k = new I3();
			k.fromString(key.toString());
			int hash = k.getNick().hashCode();
			return (hash & Integer.MAX_VALUE) % parts;
		}

	}

	/**
	 * one certain page is identified by shop, page_type, page_id and refer.
	 * when page_type is 0, refer need to be compared.
	 * 
	 */
	public static class PageAndTimeComparator extends TextComparator {

		@Override
		public int compare(Text o1, Text o2) {
			I3 k1 = new I3();
			I3 k2 = new I3();
			try {
				k1.fromString(o1.toString());
				k2.fromString(o2.toString());
			} catch (Exception e) {
				LOG.error("\n" + o1.toString() + "\n" + o2.toString(), e);
				return 0;
			}

			int cmp = k1.getIp().compareTo(k2.getIp());
			if (cmp != 0)
				return cmp;
			cmp = k1.getNick().compareTo(k2.getNick());
			if (cmp != 0)
				return cmp;
			if (!"0".equalsIgnoreCase(k1.getPageType())
					&& !"0".equalsIgnoreCase(k2.getPageType())) {
				cmp = k1.getPageType().compareTo(k2.getPageType());
				if (cmp != 0)
					return cmp;
				cmp = k1.getPageID().compareTo(k2.getPageID());
				if (cmp != 0)
					return cmp;
			} else {
				cmp = k1.getLandingPage().compareTo(k2.getLandingPage());
				if (cmp != 0)
					return cmp;
			}
			cmp = k1.getHour() - k2.getHour();
			if (cmp != 0)
				return cmp;
			cmp = k1.getMinute() - k2.getMinute();
			if (cmp != 0)
				return cmp;
			cmp = k1.getSecond() - k2.getSecond();
			return cmp;
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "Parsing");
		job.setJarByClass(Parsing.class);

		job.setMapperClass(M.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(ShopPartitioner.class);
		job.setSortComparatorClass(PageAndTimeComparator.class);

		String[] file = args[0].split(",");
		Path[] path = new Path[file.length];
		for (int i = 0; i < file.length; i++) {
			path[i] = new Path(file[i]);
		}
		TextInputFormat.setInputPaths(job, path);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "_a"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Hello World!");
		System.exit(ToolRunner.run(new Configuration(), new Parsing(), args));
	}

}
