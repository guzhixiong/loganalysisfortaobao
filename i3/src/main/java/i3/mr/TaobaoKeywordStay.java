package i3.mr;

import i.NoSplitInputFormat;
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

public class TaobaoKeywordStay extends Configured implements Tool {

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 log = new I3();
			log.fromString(value.toString());

			if (!log.getSearchType().equalsIgnoreCase("1")) {
				return;
			}
			
			String ip = log.getIp();
			int hour = log.getHour();
			int minute = log.getMinute();
			int second = log.getSecond();

			String nick = log.getNick();
			String search = log.getSearchType();
			String keyword = log.getKeyword();

			String[] tmp = new String[] { ip, "" + hour, "" + minute,
					"" + second };
			Text k = new Text(StringUtils.join(tmp, " "));

			tmp = new String[] { nick, search, keyword, "" + hour, "" + minute,
					"" + second };
			Text v = new Text(StringUtils.join(tmp, " "));

			context.write(k, v);
		}

	}

	public static class R extends Reducer<Text, Text, Text, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			long t0 = -1;
			String[] value = null;

			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				String[] tmp = it.next().toString().split(" ");
				if (null == value && tmp[1].equalsIgnoreCase("1")) {
					t0 = Utils.second(tmp[3], tmp[4], tmp[5]);
					value = new String[] { tmp[0], tmp[1], tmp[2], "60" };
				} else if (null != value) {
					long t = Utils.second(tmp[3], tmp[4], tmp[5]);
					long stay = t - t0;
					if (stay < 1200) {
						value[3] = "" + stay;
					}
					context.write(new Text(StringUtils.join(value, " ")),
							NullWritable.get());

					if (tmp[1].equalsIgnoreCase("1")) {
						t0 = t;
						value = new String[] { tmp[0], tmp[1], tmp[2], "60" };
					} else {
						t0 = -1;
						value = null;
					}
				}

				if (!it.hasNext() && value != null) {
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

	public static class M2 extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] tmp = value.toString().trim().split(" ");
			context.write(new Text(tmp[0] + " " + tmp[2]), new Text(tmp[3]
					+ " 1"));
		}

	}

	public static class R2 extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			long stay = 0;
			long count = 0;

			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				String[] tmp = it.next().toString().split(" ");
				stay += Long.parseLong(tmp[0]);
				count += Long.parseLong(tmp[1]);
			}

			String v = "" + stay + " " + count + " " + stay / count;
			context.write(key, new Text(v));
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);

		Job job = new Job(getConf(), date + "TaobaoKeywordStay1");
		job.setJarByClass(TaobaoKeywordStay.class);

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
		FileOutputFormat.setOutputPath(job, new Path(args[1]
				+ "_taobao_keyword_stay_1"));

		boolean successful = job.waitForCompletion(true);
		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));
		if (!successful) {
			return 1;
		}

		Job job2 = new Job(getConf(), date + "TaobaoKeywordStay");
		job2.setJarByClass(TaobaoKeywordStay.class);

		job2.setMapperClass(M2.class);
		job2.setCombinerClass(R2.class);
		job2.setReducerClass(R2.class);
		job2.setNumReduceTasks(3);

		job2.setInputFormatClass(NoSplitInputFormat.class);
		NoSplitInputFormat.addInputPaths(job2, args[1]
				+ "_taobao_keyword_stay_1");
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job2, new Path(args[1]
				+ "_taobao_keyword_stay"));

		successful = job2.waitForCompletion(true);
		System.out.println(job2.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("TaobaoKeywordStay Start...");
		System.exit(ToolRunner.run(new Configuration(),
				new TaobaoKeywordStay(), args));
	}

}
