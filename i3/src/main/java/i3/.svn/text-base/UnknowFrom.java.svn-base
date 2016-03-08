package i3;

import i.TextComparator;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknowFrom extends Configured implements Tool {

	private static final Logger LOG = LoggerFactory.getLogger(UnknowFrom.class);

	public static class M extends
			Mapper<LongWritable, Text, LongWritable, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String url = value.toString();
			int start = url.indexOf("http://");
			int end = url.indexOf(" ", start);
			if (start == -1 || end == -1 || start > end) {
				LOG.info("[{}] {}", key.get(), value.toString());
				return;
			}

			url = url.substring(start, end);
			if (!url.startsWith("http://click.simba.taobao.com")) {
				return;
			}

			context.write(key, value);
		}

	}

	// public static class R extends Reducer<Text, Text, Text, NullWritable> {
	//
	// @Override
	// protected void reduce(Text key, Iterable<Text> values, Context context)
	// throws IOException, InterruptedException {
	// Iterator<Text> it = values.iterator();
	// while (it.hasNext()) {
	// Text v = it.next();
	// context.write(v, NullWritable.get());
	// }
	// }
	//
	// }

	// public static class P extends Partitioner<Text, Text> {
	//
	// @Override
	// public int getPartition(Text k, Text v, int parts) {
	// String[] tmp = k.toString().split(" ");
	// int hash = tmp[0].hashCode();
	//
	// return (hash & Integer.MAX_VALUE) % parts;
	// }
	//
	// }
	//
	// public static class Grouping extends TextComparator {
	//
	// @Override
	// public int compare(Text o1, Text o2) {
	// String[] a = o1.toString().split(" ");
	// String[] b = o2.toString().split(" ");
	// return a[0].compareTo(b[0]);
	// }
	//
	// }
	//
	// public static class Sort extends TextComparator {
	//
	// @Override
	// public int compare(Text o1, Text o2) {
	// String[] a = o1.toString().split(" ");
	// String[] b = o2.toString().split(" ");
	// int cmp = a[0].compareTo(b[0]);
	// if (cmp != 0)
	// return cmp;
	// if (!"0".equalsIgnoreCase(a[1]) && !"0".equalsIgnoreCase(b[1])) {
	// cmp = a[1].compareTo(b[1]);
	// if (cmp != 0)
	// return cmp;
	// cmp = a[2].compareTo(b[2]);
	// if (cmp != 0)
	// return cmp;
	// } else {
	// cmp = a[3].compareTo(b[3]);
	// if (cmp != 0)
	// return cmp;
	// }
	// cmp = Integer.parseInt(a[4]) - Integer.parseInt(b[4]);
	// if (cmp != 0)
	// return cmp;
	// cmp = Integer.parseInt(a[5]) - Integer.parseInt(b[5]);
	// if (cmp != 0)
	// return cmp;
	// cmp = Integer.parseInt(a[6]) - Integer.parseInt(b[6]);
	// return cmp;
	// }
	//
	// }

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "UnknowFrom");
		job.setJarByClass(UnknowFrom.class);

		job.setMapperClass(M.class);
		// job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		// job.setPartitionerClass(P.class);
		// job.setSortComparatorClass(Sort.class);
		// job.setGroupingComparatorClass(Grouping.class);

		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "_unknow_from"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("UnknowFrom Start...");
		System.exit(ToolRunner.run(new Configuration(), new UnknowFrom(), args));
	}

}
