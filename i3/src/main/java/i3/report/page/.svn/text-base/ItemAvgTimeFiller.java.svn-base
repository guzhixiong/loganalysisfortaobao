package i3.report.page;

import i.report.FieldFiller;
import i3.dao.ItemDao;
import i3.entity.Item;

import java.io.BufferedReader;
import java.io.IOException;

public class ItemAvgTimeFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(ItemAvgTimeFiller.class);

	private ItemDao dao;

	public ItemAvgTimeFiller(ItemDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "item_stay";
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

			long timeLong = Long.parseLong(tmp[2]);
			long persons = Long.parseLong(tmp[3]);

			Item.Key key = new Item.Key();
			key.setNick(tmp[0]);
			key.setNumIID(tmp[1]);

			Item entity = dao.find(key);
			if (null == entity) {
				entity = new Item();
				entity.setKey(key);
			}
			entity.setAvgTime(timeLong / persons);

			dao.add(entity);
		}

		dao.close();
	}

}
