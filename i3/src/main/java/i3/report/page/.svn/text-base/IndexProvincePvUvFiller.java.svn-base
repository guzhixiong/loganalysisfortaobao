package i3.report.page;

import i.report.FieldFiller;
import i3.dao.IndexProvinceDao;
import i3.entity.IndexProvince;

import java.io.BufferedReader;
import java.io.IOException;

public class IndexProvincePvUvFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(IndexProvincePvUvFiller.class);

	private IndexProvinceDao dao;

	public IndexProvincePvUvFiller(IndexProvinceDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "index_area_pv_uv";
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

			IndexProvince.Key key = new IndexProvince.Key();
			key.setNick(tmp[0]);
			key.setProvince(tmp[1]);

			IndexProvince entity = dao.find(key);
			if (null == entity) {
				entity = new IndexProvince();
				entity.setKey(key);
			}
			entity.setPv(pv);
			entity.setUv(uv);

			dao.add(entity);
		}

		dao.close();
	}

}
