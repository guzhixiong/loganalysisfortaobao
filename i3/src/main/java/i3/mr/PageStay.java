package i3.mr;

import i.TextComparator;
import i.utils.Utils;
import i3.I3;

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

public class PageStay extends Configured implements Tool {

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 log = new I3();
			log.fromString(value.toString());

			String ip = log.getIp();
			int hour = log.getHour();
			int minute = log.getMinute();
			int second = log.getSecond();
			String nick = log.getNick();
			String pageType = log.getPageType();
			String pageID = log.getPageID();
			String landing = log.getLandingPage();
			String[] area = log.getArea().split(",");
			String province = area[0];
			// String city = area[1];

			String[] tmp = new String[] { ip, "" + hour, "" + minute,
					"" + second, province };
			Text k = new Text(StringUtils.join(tmp, " "));

			tmp = new String[] { nick, pageType, pageID, landing, "" + hour,
					"" + minute, "" + second };
			Text v = new Text(StringUtils.join(tmp, " "));

			context.write(k, v);
		}

	}

	public static class R extends Reducer<Text, Text, Text, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String[] tmp = key.toString().split(" ");
			String province = tmp[4];

			long t0 = -1;
			String[] value = null;

			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				tmp = it.next().toString().split(" ");
				if (null == value) {
					t0 = Utils.second(tmp[4], tmp[5], tmp[6]);
					value = new String[] { tmp[0], tmp[1], tmp[2], tmp[3],
							province, "60" };
				} else {
					long t = Utils.second(tmp[4], tmp[5], tmp[6]);
					long stay = t - t0;
					if (stay < 1200) {
						value[5] = "" + stay;
					}
					context.write(new Text(StringUtils.join(value, " ")),
							NullWritable.get());

					t0 = t;
					value = new String[] { tmp[0], tmp[1], tmp[2], tmp[3],
							province, "60" };
				}

				if (!it.hasNext()) {
					context.write(new Text(StringUtils.join(value, " ")),
							NullWritable.get());
				}
			}
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
			return a[0].compareTo(b[0]);
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
			cmp = Integer.parseInt(a[1]) - Integer.parseInt(b[1]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(a[2]) - Integer.parseInt(b[2]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(a[3]) - Integer.parseInt(b[3]);
			return cmp;
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "PageStay");
		job.setJarByClass(PageStay.class);

		job.setMapperClass(M.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setSortComparatorClass(Sort.class);
		job.setGroupingComparatorClass(Grouping.class);

		job.setMapOutputValueClass(Text.class);
		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "_page_stay"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("PageStay Start...");
		System.exit(ToolRunner.run(new Configuration(), new PageStay(), args));
	}

}
