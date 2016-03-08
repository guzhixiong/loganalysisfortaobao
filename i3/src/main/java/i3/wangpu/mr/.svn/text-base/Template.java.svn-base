package i3.wangpu.mr;

import i.TextComparator;
import i.utils.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Template extends Configured implements Tool {

	private static final Logger LOG = LoggerFactory.getLogger(Template.class);

	private static final String TIMESTAMP = "timestamp";

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] log = value.toString().split(" ");
			if (log.length < 11) {
				LOG.info("have no template in this line \n {}",
						value.toString());
				return;
			}

			String nick = log[2].toLowerCase();
			int hour = -1, minute = -1, second = -1;
			String datetime = log[0] + " " + log[1];
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date d = f.parse(datetime);
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				hour = c.get(Calendar.HOUR_OF_DAY);
				minute = c.get(Calendar.MINUTE);
				second = c.get(Calendar.SECOND);
			} catch (ParseException e) {
				LOG.info("wrong date \n {}", datetime);
			}
			String tID = log[log.length - 2].trim();
			String tName = log[log.length - 1].trim();

			String date = context.getConfiguration().get(TIMESTAMP);
			date = DateUtils.format(date, hour, minute, second);

			if (nick.equals("-") || tID.equals("-") || tID.equals("")) {
				return;
			}
			context.write(new Text(nick + " " + date), new Text(tID + " "
					+ tName));
		}
	}

	public static class R extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Text k = new Text(key.toString().split(" ")[0]);
			String value = "";

			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				String _value = it.next().toString();
				if (!value.equals(_value)) {
					if (!value.equals("")) {
						context.write(k, new Text(value));
					}

					value = _value;
				}
			}
			context.write(k, new Text(value));
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
			cmp = b[1].compareTo(a[1]);
			return cmp;
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Configuration conf = getConf();
		getConf().set(TIMESTAMP, date);

		Job job = new Job(conf, date + "Template");
		job.setJarByClass(Template.class);

		job.setMapperClass(M.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(1);

		job.setPartitionerClass(P.class);
		job.setGroupingComparatorClass(Grouping.class);
		job.setSortComparatorClass(Sort.class);

		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "_template"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Template Start...");
		System.exit(ToolRunner.run(new Configuration(), new Template(), args));
	}

}
