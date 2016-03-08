package i3.mr;

import i3.I3;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ItemKeywordPv extends Configured implements Tool {

	public static class M extends
			Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			I3 log = new I3();
			log.fromString(value.toString());

			int type;

			String search = log.getSearchType();
			String from = log.getFromType();
			if (!log.getPageType().equalsIgnoreCase("3")) {
				return;
			} else if (from.equalsIgnoreCase("15")) {
				type = 3;
			} else if (search.equalsIgnoreCase("1")) {
				type = 2;
			} else if (search.equalsIgnoreCase("2")) {
				type = 1;
			} else {
				return;
			}

			String keyword = log.getKeyword();
			if (keyword.equalsIgnoreCase("-") || keyword.equalsIgnoreCase("")) {
				return;
			}

			String nick = log.getNick();
			String pageID = log.getPageID();

			String[] tmp = new String[] { nick, pageID, keyword, "" + type };
			Text k = new Text(StringUtils.join(tmp, " "));

			context.write(k, new LongWritable(1));
		}
	}

	public static class R extends
			Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			long pv = 0;
			Iterator<LongWritable> it = values.iterator();
			while (it.hasNext()) {
				pv += it.next().get();
			}

			context.write(key, new LongWritable(pv));
		}

	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "ItemKeywordPv");
		job.setJarByClass(ItemKeywordPv.class);

		job.setMapperClass(M.class);
		job.setCombinerClass(R.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1]
				+ "_item_keyword_pv"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("ItemKeywordPv Start...");
		System.exit(ToolRunner.run(new Configuration(), new ItemKeywordPv(),
				args));
	}

}
