package i3.market;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class P extends Partitioner<Text, NullWritable> {

	@Override
	public int getPartition(Text k, NullWritable v, int parts) {
		String[] tmp = k.toString().split(" ");
		int hash = tmp[0].hashCode();

		return (hash & Integer.MAX_VALUE) % parts;
	}

}
