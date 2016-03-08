package i3.mr;

import i.TextComparator;
import i.utils.Nick2UID;
import i.utils.Utils;
import i3.dao.PathFromDao;
import i3.entity.ShopFrom;

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
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import dao.bdb.pair.Pair;

public class Path1PvUv extends Configured implements Tool {

	private static final String TIMESTAMP = "timestamp";

	public static class M extends
			Mapper<LongWritable, Text, Text, NullWritable> {

		private PathFromDao dao;
		private Nick2UID n2u;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			dao = new PathFromDao();
			dao.setPath(context.getConfiguration().get(TIMESTAMP) + "_path");
			Utils.openDaoInMap(context, dao);

			n2u = new Nick2UID();
			Utils.openDaoInMap(context, n2u);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			n2u.close();
			dao.close();
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] tmp = value.toString().split("\\s");
			if (tmp.length != 8) {
				return;
			}

			if (tmp[3].equals("30") || tmp[3].equals("31")
					|| tmp[3].equals("-1")) {
				return;
			}

			if (!tmp[1].equals("1") && !tmp[1].equals("2")
					&& !tmp[1].equals("3") && !tmp[1].equals("4")) {
				return;
			}

			Pair pair = n2u.find(tmp[0]);
			if (pair == null) {
				return;
			}
			ShopFrom.Key k = new ShopFrom.Key();
			k.setNick(pair.getValue());
			k.setFromID(tmp[3]);
			if (dao.find(k) == null) {
				return;
			}

			context.write(value, NullWritable.get());
		}

	}

	public static class P extends Partitioner<Text, NullWritable> {

		@Override
		public int getPartition(Text k, NullWritable v, int parts) {
			String[] tmp = k.toString().split("\\s");
			int hash = tmp[0].hashCode();

			return (hash & Integer.MAX_VALUE) % parts;
		}

	}

	public static class Sort extends TextComparator {

		@Override
		public int compare(Text o1, Text o2) {
			String[] a = o1.toString().split("\\s");
			String[] b = o2.toString().split("\\s");
			int cmp = a[0].compareTo(b[0]);
			if (cmp != 0)
				return cmp;
			cmp = a[3].compareTo(b[3]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(b[6]) - Integer.parseInt(a[6]);
			if (cmp != 0)
				return cmp;
			cmp = Integer.parseInt(b[7]) - Integer.parseInt(a[7]);
			if (cmp != 0)
				return cmp;

			if (a[1].equals("3") && !b[1].equals("3")) {
				return -1;
			} else if (!a[1].equals("3") && b[1].equals("3")) {
				return 1;
			} else if (a[1].equals("4") && !b[1].equals("4")) {
				return -1;
			} else if (!a[1].equals("4") && b[1].equals("4")) {
				return 1;
			} else if (a[1].equals("2") && !b[1].equals("2")) {
				return -1;
			} else if (!a[1].equals("2") && b[1].equals("2")) {
				return 1;
			} else if (a[1].equals("1") && !b[1].equals("1")) {
				return -1;
			} else if (!a[1].equals("1") && b[1].equals("1")) {
				return 1;
			} else {
				int len = a[2].length() > b[2].length() ? a[2].length() : b[2]
						.length();

				return StringUtils.leftPad(b[2], len, '0').compareTo(
						StringUtils.leftPad(a[2], len, '0'));
			}
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[0].substring(args[0].indexOf("/") + 1);
		Configuration conf = getConf();
		getConf().set(TIMESTAMP, date);

		Job job = new Job(conf, date + "Path1PvUv");
		job.setJarByClass(Path1PvUv.class);

		job.setMapperClass(M.class);
		job.setNumReduceTasks(3);

		job.setPartitionerClass(P.class);
		job.setSortComparatorClass(Sort.class);

		TextInputFormat.addInputPaths(job, args[0] + "_page_from_pv_uv");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileOutputFormat
				.setOutputPath(job, new Path(args[0] + "_path_1_pv_uv"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Path1PvUv Start...");
		System.exit(ToolRunner.run(new Configuration(), new Path1PvUv(), args));
	}

}
