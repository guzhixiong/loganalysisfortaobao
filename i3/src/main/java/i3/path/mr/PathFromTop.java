package i3.path.mr;

import i.NoSplitInputFormat;
import i.TextComparator;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class PathFromTop extends Configured implements Tool {

	private static final Log LOG = LogFactory.getLog(PathFromTop.class);

	public static class M extends
			Mapper<LongWritable, Text, Text, NullWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			context.write(value, NullWritable.get());
		}

	}

	public static class P extends Partitioner<Text, NullWritable> {

		@Override
		public int getPartition(Text k, NullWritable v, int parts) {
			String[] tmp = k.toString().split("\\s");
			int hash = tmp[0].hashCode();
			return (hash & Integer.MAX_VALUE) % parts;
		}

	}

	public static class Sort extends TextComparator {

		@Override
		public int compare(Text o1, Text o2) {
			String[] a = o1.toString().split("\\s");
			String[] b = o2.toString().split("\\s");

			if (a.length < 4) {
				LOG.info("G o1: " + o1.toString());
				return 1;
			}
			if (b.length < 4) {
				LOG.info(" o2: " + o2.toString());
				return -1;
			}

			int cmp = a[0].compareTo(b[0]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(b[2]) - Integer.parseInt(a[2]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(b[3]) - Integer.parseInt(a[3]);
			return cmp;
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[0].substring(args[0].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "PathFromTop");
		job.setJarByClass(PathFromTop.class);

		job.setMapperClass(M.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setSortComparatorClass(Sort.class);

		NoSplitInputFormat.addInputPaths(job, args[0] + "_path_from");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileOutputFormat.setOutputPath(job,
				new Path(args[0] + "_path_from_top"));

		boolean successful = job.waitForCompletion(true);
		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("PathFromTop Start...");
		System.exit(ToolRunner
				.run(new Configuration(), new PathFromTop(), args));
	}

}
