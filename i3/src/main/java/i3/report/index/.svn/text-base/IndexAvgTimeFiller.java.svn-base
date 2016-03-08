package i3.report.index;

import i.report.FieldFiller;
import i3.dao.IndexDao;
import i3.entity.Index;

import java.io.BufferedReader;
import java.io.IOException;

public class IndexAvgTimeFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(IndexAvgTimeFiller.class);

	private IndexDao dao;

	public IndexAvgTimeFiller(IndexDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "index_stay";
	}

	@Override
	public void fill(BufferedReader reader) throws IOException {
		dao.open(false);

		String line;
		while (null != (line = reader.readLine())) {
			String[] tmp = line.split("\\s");
			if (tmp[0].equals("-")) {
				continue;
			}

			long timeLong = Long.parseLong(tmp[1]);
			long persons = Long.parseLong(tmp[2]);

			Index index = dao.find(tmp[0]);
			if (null == index) {
				index = new Index();
				index.setNick(tmp[0]);
			}
			index.setAvgTime(timeLong / persons);

			dao.add(index);
		}

		dao.close();
	}

}
