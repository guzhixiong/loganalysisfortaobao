package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App7 {

	private static final Logger LOG = LoggerFactory.getLogger(App7.class);

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) {
		// String base =
		// "C:\\Documents and Settings\\Administrator\\My Documents\\";
		// FileWriter writer = new FileWriter(base + "sql.txt");
		// BufferedReader reader = new BufferedReader(new FileReader(base
		// + "i.log"));
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// int start = line.indexOf("insert into ");
		// if (start == -1) {
		// continue;
		// }
		//
		// String sql = line.substring(start, line.length());
		// writer.write(sql);
		// writer.write("\r\n");
		// LOG.info(sql);
		// }
		// reader.close();
		// writer.close();

		String[] arr = new String[3];
		arr[1] = "1";
		for (String str : arr) {
			// LOG.info(str);
		}

		// LOG.info(h(1073741824 + 7340032 + 5120));

		Runtime rt = Runtime.getRuntime();

		// long max = rt.maxMemory();
		// LOG.info(max + " bytes: " + h((int) max));
		// long total = rt.totalMemory();
		// LOG.info(total + " bytes: " + h((int) total));
		// long free = rt.freeMemory();
		// LOG.info(free + " bytes: " + h((int) free));

		try {
			// ProcessBuilder builder = new ProcessBuilder();
			// builder.directory(new File("\\"));
			// builder.command("java");
			//
			// Process p = builder.start();

			Process p = rt.exec("java App", new String[] {}, new File("\\"));

			String msg = stdin(p.getErrorStream());
			LOG.info("\n" + msg);

			msg = stdin(p.getInputStream());
			LOG.info("\n" + msg);

			LOG.info("{}", p.exitValue());

			p.waitFor();
		} catch (IOException e) {
			LOG.error("", e);
		} catch (InterruptedException e) {
			LOG.error("", e);
		}
	}

	private static String stdin(InputStream in) throws IOException {
		StringBuffer buffer = new StringBuffer();

		byte[] b = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) > -1) {
			buffer.append(new String(Arrays.copyOf(b, len)));
		}

		return (buffer.toString());
	}

	public static String h1000(int num) {
		return h(num, 1000);
	}

	public static String h(int num) {
		return h(num, 1024);
	}

	public static String h(int num, int kilo) {
		final String[] q = { "", "k", "m", "g", "t", "p" };

		int bit = num;
		List<Integer> list = new ArrayList<Integer>();
		while (bit >= kilo) {
			list.add(bit % kilo);
			bit = bit / kilo;
		}
		list.add(bit);

		String h = "";
		int size = list.size();
		for (int i = size; i > 0; i--) {
			h += list.get(i - 1);
			if (i <= q.length) {
				h += q[i - 1];
			}
			h += " ";
		}
		return h;
	}

}
