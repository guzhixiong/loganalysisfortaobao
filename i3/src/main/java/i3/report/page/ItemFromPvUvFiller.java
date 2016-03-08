package i3.report.page;

import i.report.FieldFiller;
import i3.dao.ItemFromDao;
import i3.entity.ItemFrom;

import java.io.BufferedReader;
import java.io.IOException;

public class ItemFromPvUvFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(ItemFromPvUvFiller.class);

	private ItemFromDao dao;

	public ItemFromPvUvFiller(ItemFromDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "item_from_pv_uv";
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

			long pv = Long.parseLong(tmp[3]);
			long uv = Long.parseLong(tmp[4]);

			ItemFrom.Key key = new ItemFrom.Key();
			key.setNick(tmp[0]);
			key.setNumIID(tmp[1]);
			key.setFromID(tmp[2]);

			ItemFrom entity = dao.find(key);
			if (null == entity) {
				entity = new ItemFrom();
				entity.setKey(key);
			}
			entity.setPv(pv);
			entity.setUv(uv);

			dao.add(entity);
		}

		dao.close();
	}

}
