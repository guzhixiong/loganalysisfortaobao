package i3.mr;

import i.NoSplitInputFormat;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ShopFromStay extends Configured implements Tool {

	public static class M extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] tmp = value.toString().trim().split(" ");
			String shop = tmp[0];
			String from = tmp[5];

			context.write(new Text(shop + " " + from), new Text(tmp[5] + " 1"));
		}

	}

	public static class R extends Reducer<Text, Text, Text, Text> {

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
		String date = args[0].substring(args[0].indexOf("/") + 1);
		Job job = new Job(getConf(), date + "ShopFromStay");
		job.setJarByClass(ShopFromStay.class);

		job.setMapperClass(M.class);
		job.setReducerClass(R.class);
		job.setNumReduceTasks(3);

		job.setInputFormatClass(NoSplitInputFormat.class);
		NoSplitInputFormat.addInputPaths(job, args[0] + "_page_stay");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[0]
				+ "_shop_from_stay"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("ShopFromStay Start...");
		System.exit(ToolRunner.run(new Configuration(), new ShopFromStay(),
				args));
	}

}
