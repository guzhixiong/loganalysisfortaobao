package i;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public abstract class DataReader {

	protected String samba = "/data/share";

	protected String local = "/opt/i/stati/";

	protected String date;

	abstract protected String getSrc();

	abstract protected String getDes();

	abstract protected String process(String line);

	public DataReader(String date) {
		this.date = date;
	}

	protected void init() {

	}

	protected void fin() {

	}

	public void process() throws IOException {
		init();

		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		month = month.startsWith("0") ? month.substring(1) : month;
		String day = date.substring(6, 8);
		day = day.startsWith("0") ? day.substring(1) : day;
		date = year + "-" + month + "-" + day;

		String file = samba + File.separator + getDes().toUpperCase()
				+ File.separator;
		new File(file).mkdirs();
		file += date;

		File lock = new File(file + ".lock");
		FileUtils.touch(lock);

		FileWriter out = new FileWriter(file);
		File[] files = listFiles();
		for (File f : files) {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line = null;
			while (null != (line = in.readLine())) {
				out.write(process(line));
			}
			out.flush();

			in.close();
		}
		out.close();

		FileUtils.deleteQuietly(lock);

		fin();
	}

	private File[] listFiles() throws FileNotFoundException {
		File path = new File(local + date + File.separator + date + "_"
				+ getSrc().toLowerCase());
		if (!path.exists()) {
			throw new FileNotFoundException(path.getAbsolutePath());
		}

		File[] files = path.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().startsWith("part-r-");
			}

		});
		return files;
	}

}
