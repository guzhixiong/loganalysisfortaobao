package i3.market;

import i.TextComparator;

import org.apache.hadoop.io.Text;

public class Sort extends TextComparator {

	@Override
	public int compare(Text o1, Text o2) {
		String[] a = o1.toString().split(" ");
		String[] b = o2.toString().split(" ");
		int cmp = a[0].compareTo(b[0]);
		if (cmp != 0)
			return cmp;
		cmp = a[1].compareTo(b[1]);
		if (cmp != 0)
			return cmp;
		cmp = a[3].compareTo(b[3]);
		return cmp;
	}

}
