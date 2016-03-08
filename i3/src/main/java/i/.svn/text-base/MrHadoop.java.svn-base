package i;

import i.utils.Nick2UID;
import i.utils.Utils;
import i3.dao.PathFromDao;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MrHadoop extends Configured implements Tool {

	private static final String TIMESTAMP = "timestamp";

	public static class M extends
			Mapper<LongWritable, Text, LongWritable, Text> {

		// private static final Log LOG = LogFactory.getLog(M.class);

		// private String base = "/tmp";
		// private FileChannel channel = null;

		private PathFromDao dao;

		private Nick2UID n2u;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			// File file = new File(base);
			// file.mkdirs();
			//
			// channel = new FileInputStream(base + File.separator
			// + "sync_time.log").getChannel();

			dao = new PathFromDao();
			dao.setPath(context.getConfiguration().get(TIMESTAMP) + "_path");
			Utils.openDaoInMap(context, dao);

			n2u = new Nick2UID();
			Utils.openDaoInMap(context, n2u);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			// if (channel != null) {
			// channel.close();
			// }

			dao.close();

			n2u.close();
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// ByteBuffer buffer = ByteBuffer.allocate(1024);
			// int size = 0;
			// while ((size = channel.read(buffer)) > -1) {
			// buffer.flip();
			// LOG.info(":p "
			// + new String(Arrays.copyOf(buffer.array(), size)));
			// buffer.clear();
			// }
			context.write(key, value);

		}
	}

	@Override
	public int run(String[] args) throws Exception {
		String date = args[1].substring(args[1].indexOf("/") + 1);
		Configuration conf = getConf();
		getConf().set(TIMESTAMP, date);

		Job job = new Job(conf, date + "MrHadoop");
		job.setJarByClass(MrHadoop.class);

		job.setMapperClass(M.class);

		TextInputFormat.addInputPaths(job, args[0]);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "_mr"));

		boolean successful = job.waitForCompletion(true);

		System.out.println(job.getJobName()
				+ (successful ? " :successful" : " :failed"));

		return successful ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("MrHadoop Start...");
		System.exit(ToolRunner.run(new Configuration(), new MrHadoop(), args));
	}

}
