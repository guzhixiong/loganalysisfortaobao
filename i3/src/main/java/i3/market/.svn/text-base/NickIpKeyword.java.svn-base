package i3.market;

import i.utils.DateUtils;
import i.utils.Utils;
import i3.I3;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class NickIpKeyword extends Configured implements Tool {

	public static class M extends
			Mapper<LongWritable, Text, Text, NullWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 log = new I3();
			log.fromString(value.toString());

			String nick = log.getNick();
			String ip = log.getIp();
			String keyword = log.getKeyword();
			keyword = Utils.urlDecode(keyword).trim().replace(" ", "+");
			int hour = log.getHour();
			int minute = log.getMinute();
			int second = log.getSecond();

			String date = context.getConfiguration().get(DateUtils.TIMESTAMP);
			date = DateUtils.format(date, hour, minute, second);

			if (!log.getSearchType().equals("1") || nick.equals("-")
					|| keyword.equals("-") || keyword.equals("")) {
				return;
			}

			String[] tmp = new String[] { nick, ip, keyword, date };
			Text k = new Text(StringUtils.join(tmp, " "));

			context.write(k, NullWritable.get());
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Configuration conf = getConf();
		getConf().set(DateUtils.TIMESTAMP, date);

		Job job = new Job(conf, date + "NickIpKeyword");
		job.setJarByClass(NickIpKeyword.class);

		job.setMapperClass(M.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setSortComparatorClass(Sort.class);

		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1]
				+ "_nick_ip_keyword"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("NickIpKeyword Start...");
		System.exit(ToolRunner.run(new Configuration(), new NickIpKeyword(),
				args));
	}

}
