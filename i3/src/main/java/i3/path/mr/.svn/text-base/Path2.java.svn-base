package i3.path.mr;

import i.NoSplitInputFormat;
import i.TextComparator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Path2 extends Configured implements Tool {

	private static final Log LOG = LogFactory.getLog(Path2.class);

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] tmp = value.toString().split("\\s");
			if (tmp.length < 14 || tmp[0].equals("-") || tmp[3].equals("-1")
					|| tmp[3].equals("30") || tmp[3].equals("31")) {
				return;
			}

			String[] tmp2 = { tmp[0], tmp[3], tmp[6], tmp[7], tmp[8], tmp[10],
					tmp[11], tmp[12], tmp[13] };
			context.write(new Text(StringUtils.join(tmp2, " ")), new Text(
					tmp[1]));
		}
	}

	public static class R extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Set<String> set = new HashSet<String>();

			long pv = 0;
			long uv = 0;
			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				String ip = it.next().toString();
				pv++;
				if (!set.contains(ip)) {
					uv++;
					set.add(ip);
				}
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

			if (a.length < 9) {
				LOG.info("G o1: " + o1.toString());
				return 1;
			}
			if (b.length < 9) {
				LOG.info(" o2: " + o2.toString());
				return -1;
			}

			int cmp = a[0].compareTo(b[0]);
			if (cmp != 0)
				return cmp;
			cmp = a[1].compareTo(b[1]);
			if (cmp != 0)
				return cmp;
			cmp = a[2].compareTo(b[2]);
			if (cmp != 0)
				return cmp;
			else if (a[2].equals("0"))
				cmp = a[4].compareTo(b[4]);
			else
				cmp = a[3].compareTo(b[3]);
			if (cmp != 0)
				return cmp;
			cmp = a[5].compareTo(b[5]);
			if (cmp != 0)
				return cmp;
			else if (a[5].equals("0"))
				cmp = a[7].compareTo(b[7]);
			else
				cmp = a[6].compareTo(b[6]);
			return cmp;
		}

	}

	public static class Sort extends TextComparator {

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
			cmp = a[2].compareTo(b[2]);
			if (cmp != 0)
				return cmp;
			else if (a[2].equals("0"))
				cmp = a[4].compareTo(b[4]);
			else
				cmp = a[3].compareTo(b[3]);
			if (cmp != 0)
				return cmp;
			cmp = a[5].compareTo(b[5]);
			if (cmp != 0)
				return cmp;
			else if (a[5].equals("0"))
				cmp = a[7].compareTo(b[7]);
			else
				cmp = a[6].compareTo(b[6]);
			if (cmp != 0)
				return cmp;
			return a[8].compareTo(b[8]);
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[0].substring(args[0].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "Path2");
		job.setJarByClass(Path2.class);

		job.setMapperClass(M.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setGroupingComparatorClass(Grouping.class);
		job.setSortComparatorClass(Sort.class);

		NoSplitInputFormat.addInputPaths(job, args[0] + "_path_prev");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[0] + "_path_2"));

		boolean successful = job.waitForCompletion(true);
		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Path2 Start...");
		System.exit(ToolRunner.run(new Configuration(), new Path2(), args));
	}

}
