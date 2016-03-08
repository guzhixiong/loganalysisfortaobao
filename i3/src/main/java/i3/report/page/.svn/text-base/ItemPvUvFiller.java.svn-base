package i3.report.page;

import i.report.FieldFiller;
import i3.dao.ItemDao;
import i3.entity.Item;

import java.io.BufferedReader;
import java.io.IOException;

public class ItemPvUvFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(ItemPvUvFiller.class);

	private ItemDao dao;

	public ItemPvUvFiller(ItemDao dao) {
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
			if (tmp[0].equals("-") || !tmp[1].equals("3")) {
				continue;
			}

			Item.Key key = new Item.Key();
			key.setNick(tmp[0]);
			key.setNumIID(tmp[2]);

			Item entity = dao.find(key);
			if (null == entity) {
				entity = new Item();
				entity.setKey(key);
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
