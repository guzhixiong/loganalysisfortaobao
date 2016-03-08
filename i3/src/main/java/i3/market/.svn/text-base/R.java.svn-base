package i3.market;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class R extends Reducer<Text, NullWritable, Text, NullWritable> {

	@Override
	protected void reduce(Text key, Iterable<NullWritable> values,
			Context context) throws IOException, InterruptedException {
		context.write(key, NullWritable.get());
	}

}
