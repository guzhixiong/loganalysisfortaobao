package i3.path.mr;

import i.TextComparator;
import i3.I3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class PathPrev extends Configured implements Tool {

	private static final Log LOG = LogFactory.getLog(PathPrev.class);

	private static final String TIMESTAMP = "timestamp";

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 log = new I3();
			log.fromString(value.toString());

			String ip = log.getIp();
			int h = log.getHour();
			int m = log.getMinute();
			int s = log.getSecond();

			String nick = log.getNick();
			String type = log.getPageType();
			String id = log.getPageID();
			String landing = log.getLandingPage();
			String title = log.getLandingPageTitle();
			String from = log.getFromType();
			String refer = log.getReferPage();

			String[] tmp = { nick, ip, "" + h, "" + m, "" + s };
			Text k = new Text(StringUtils.join(tmp, " "));
			tmp = new String[] { type, id, landing, title, from, refer, "" + h,
					"" + m, "" + s };
			context.write(k, new Text(StringUtils.join(tmp, " ")));
		}

	}

	public static class R extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String[] tmp = key.toString().split(" ");
			String shop = tmp[0];
			String ip = tmp[1];

			String last = null;
			String k = null;
			List<String> path = new ArrayList<String>();
			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				tmp = it.next().toString().split(" ");

				if (k == null) {
					k = shop + " " + ip + " " + tmp[6] + ":" + tmp[7] + ":"
							+ tmp[8] + " " + tmp[4];

					path.add(tmp[5]);
				} else if (!tmp[5].equals(last)) {
					context.write(new Text(k + " " + path.size()), new Text(
							StringUtils.join(path.toArray(), " ")));

					k = shop + " " + ip + " " + tmp[6] + ":" + tmp[7] + ":"
							+ tmp[8] + " " + tmp[4];

					path.clear();
					path.add(tmp[5]);
				}

				last = tmp[2];
				path.add(tmp[0] + " " + tmp[1] + " " + tmp[2] + " " + tmp[3]);
			}
			// context.write(new Text(k + " " + path.size()),
			// new Text(StringUtils.join(path.toArray(), " ")));
		}

	}

	public static class P extends Partitioner<Text, Text> {

		@Override
		public int getPartition(Text k, Text v, int parts) {
			String[] tmp = k.toString().split("\\s");
			int hash = tmp[0].hashCode();
			return (hash & Integer.MAX_VALUE) % parts;
		}

	}

	public static class Grouping extends TextComparator {

		@Override
		public int compare(Text o1, Text o2) {
			String[] a = o1.toString().split(" ");
			String[] b = o2.toString().split(" ");

			if (a.length < 5) {
				LOG.info("G o1: " + o1.toString());
				return 1;
			}
			if (b.length < 5) {
				LOG.info(" o2: " + o2.toString());
				return -1;
			}

			int cmp = a[0].compareTo(b[0]);
			if (cmp != 0)
				return cmp;
			return a[1].compareTo(b[1]);
		}

	}

	public static class Sort extends TextComparator {

		@Override
		public int compare(Text o1, Text o2) {
			String[] a = o1.toString().split(" ");
			String[] b = o2.toString().split(" ");

			if (a.length < 5) {
				LOG.info("G o1: " + o1.toString());
				return 1;
			}
			if (b.length < 5) {
				LOG.info(" o2: " + o2.toString());
				return -1;
			}

			int cmp = a[0].compareTo(b[0]);
			if (cmp != 0)
				return cmp;
			cmp = a[1].compareTo(b[1]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(a[2]) - Integer.parseInt(b[2]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(a[3]) - Integer.parseInt(b[3]);
			if (cmp != 0)
				return cmp;
			return Integer.parseInt(a[4]) - Integer.parseInt(b[4]);
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Configuration conf = getConf();
		getConf().set(TIMESTAMP, date);

		Job job = new Job(conf, date + "PathPrev");
		job.setJarByClass(PathPrev.class);

		job.setMapperClass(M.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setGroupingComparatorClass(Grouping.class);
		job.setSortComparatorClass(Sort.class);

		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "_path_prev"));

		boolean successful = job.waitForCompletion(true);
		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("PathPrev Start...");
		System.exit(ToolRunner.run(new Configuration(), new PathPrev(), args));
	}

}
