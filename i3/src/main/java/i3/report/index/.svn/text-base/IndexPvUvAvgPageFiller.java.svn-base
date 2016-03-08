package i3.report.index;

import i.report.FieldFiller;
import i3.dao.IndexDao;
import i3.entity.Index;

import java.io.BufferedReader;
import java.io.IOException;

public class IndexPvUvAvgPageFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(IndexPvUvAvgPageFiller.class);

	private IndexDao dao;

	public IndexPvUvAvgPageFiller(IndexDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "index_pv_uv";
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

			long pv = Long.parseLong(tmp[1]);
			long uv = Long.parseLong(tmp[2]);

			Index index = dao.find(tmp[0]);
			if (null == index) {
				index = new Index();
				index.setNick(tmp[0]);
			}
			index.setPv(pv);
			index.setUv(uv);
			index.setAvgPage(((float) pv) / uv);

			dao.add(index);
		}

		dao.close();
	}

}
