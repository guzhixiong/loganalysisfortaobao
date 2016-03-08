package i3.report.shop;

import i.report.FieldFiller;
import i3.dao.ShopDao;
import i3.entity.Shop;

import java.io.BufferedReader;
import java.io.IOException;

public class ShopAvgTimeFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(ShopAvgTimeFiller.class);

	private ShopDao dao;

	public ShopAvgTimeFiller(ShopDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "shop_stay";
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

			Shop entity = dao.find(tmp[0]);
			if (null == entity) {
				entity = new Shop();
				entity.setNick(tmp[0]);
			}
			entity.setAvgTime(timeLong / persons);

			dao.add(entity);
		}

		dao.close();
	}

}
