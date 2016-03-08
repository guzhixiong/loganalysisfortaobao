package i3.report.page;

import i.report.FieldFiller;
import i3.dao.IndexFromDao;
import i3.entity.IndexFrom;

import java.io.BufferedReader;
import java.io.IOException;

public class IndexFromPvUvFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(IndexFromPvUvFiller.class);

	private IndexFromDao dao;

	public IndexFromPvUvFiller(IndexFromDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "index_from_pv_uv";
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

			long pv = Long.parseLong(tmp[2]);
			long uv = Long.parseLong(tmp[3]);

			IndexFrom.Key key = new IndexFrom.Key();
			key.setNick(tmp[0]);
			key.setFromID(tmp[1]);

			IndexFrom entity = dao.find(key);
			if (null == entity) {
				entity = new IndexFrom();
				entity.setKey(key);
			}
			entity.setPv(pv);
			entity.setUv(uv);

			dao.add(entity);
		}

		dao.close();
	}

}
