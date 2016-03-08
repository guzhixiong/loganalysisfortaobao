package i3.mr;

import i.TextComparator;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Path1 extends Configured implements Tool {

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] tmp = value.toString().split("\\s");

			String k = tmp[0] + " " + tmp[1] + " " + tmp[2] + " " + tmp[4]
					+ " " + tmp[5];
			context.write(new Text(k), new Text(tmp[6] + " " + tmp[7]));
		}

	}

	public static class R extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Iterator<Text> it = values.iterator();

			long pv = 0;
			long uv = 0;
			while (it.hasNext()) {
				String[] tmp = it.next().toString().split(" ");
				pv += Long.parseLong(tmp[0]);
				uv += Long.parseLong(tmp[1]);
			}

			context.write(key, new Text(pv + " " + uv));
		}

	}

	public static class P extends Partitioner<Text, Text> {

		@Override
		public int getPartition(Text k, Text v, int parts) {
			String[] tmp = k.toString().split(" ");
			int hash = tmp[0].hashCode();

			return (hash & Integer.MAX_VALUE) % parts;
		}

	}

	public static class Grouping extends TextComparator {

		@Override
		public int compare(Text o1, Text o2) {
			String[] a = o1.toString().split(" ");
			String[] b = o2.toString().split(" ");
			int cmp = a[0].compareTo(b[0]);
			if (cmp != 0)
				return cmp;
			cmp = a[1].compareTo(b[1]);
			if (cmp != 0)
				return cmp;
			return a[2].compareTo(b[2]);
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[0].substring(args[0].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "Path1");
		job.setJarByClass(Path1.class);

		job.setMapperClass(M.class);
		job.setCombinerClass(R.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setGroupingComparatorClass(Grouping.class);

		TextInputFormat.addInputPaths(job, args[0] + "_path_1_pv_uv");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[0] + "_path_1"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Path1 Start...");
		System.exit(ToolRunner.run(new Configuration(), new Path1(), args));
	}

}
