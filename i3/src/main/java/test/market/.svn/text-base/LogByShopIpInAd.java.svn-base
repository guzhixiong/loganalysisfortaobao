package test.market;

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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.mortbay.log.Log;

public class LogByShopIpInAd extends Configured implements Tool {

	public static class M extends
			Mapper<LongWritable, Text, Text, NullWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 log = new I3();
			log.fromString(value.toString());

			String from = log.getFromType();
			if (!from.equals("15")) {
				return;
			}

			String refer = log.getReferPage();
			String searchType = log.getSearchType();

			String type = "";
			String sign = "";
			if (searchType.equals("0")) {
				type = "0";
				sign = Utils
						.find("http://list\\.taobao\\.com/browse/search_auction\\.htm\\?(?:.+&)?cat=([^&]+)(?:&.+)?",
								refer);
			} else if (searchType.equals("1")) {
				type = "1";
				sign = Utils.ztcKeyword(log.getLandingPage());
			}

			if (sign == null || sign.equals("-") || sign.equals("")) {
				Log.info(refer);
				return;
			}

			String nick = log.getNick();
			String ip = log.getIp();

			String[] tmp = new String[] { nick, type, sign, ip };
			String k = StringUtils.join(tmp, " ");
			context.write(new Text(k), NullWritable.get());
		}
	}

	public static class R extends
			Reducer<Text, NullWritable, Text, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<NullWritable> values,
				Context context) throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[0].substring(args[0].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "ZtcIp");
		job.setJarByClass(LogByShopIpInAd.class);

		job.setMapperClass(M.class);
		job.setCombinerClass(R.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		TextInputFormat.addInputPaths(job, Utils.buildInputPath(3, args[0]));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileOutputFormat.setOutputPath(job, new Path("data/" + args[0]
				+ "_ztc_ip"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("ZtcIp Start...");
		System.exit(ToolRunner.run(new Configuration(), new LogByShopIpInAd(),
				args));
	}

}
