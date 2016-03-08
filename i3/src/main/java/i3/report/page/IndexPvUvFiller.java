package i3.report.page;

import i.report.FieldFiller;
import i3.dao.IndexDao;
import i3.entity.Index;

import java.io.BufferedReader;
import java.io.IOException;

public class IndexPvUvFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(IndexPvUvFiller.class);

	private IndexDao dao;

	public IndexPvUvFiller(IndexDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "page_pv_uv";
	}

	@Override
	public void fill(BufferedReader reader) throws IOException {
		dao.open(false);

		String line;
		while (null != (line = reader.readLine())) {
			int pos = line.lastIndexOf("\t");
			String[] tmp = line.substring(0, pos).split(" ");
			if (tmp[0].equals("-") || !tmp[1].equals("1") || tmp[2].equals("-")) {
				continue;
			}

			Index entity = dao.find(tmp[0]);
			if (null == entity) {
				entity = new Index();
				entity.setNick(tmp[0]);
			}

			tmp = line.substring(pos + 1).split(" ");
			long pv = Long.parseLong(tmp[0]);
			long uv = Long.parseLong(tmp[1]);
			entity.setPv(pv);
			entity.setUv(uv);

			dao.add(entity);
		}

		dao.close();
	}

}
