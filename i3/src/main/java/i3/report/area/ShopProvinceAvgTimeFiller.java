package i3.report.area;

import i.report.FieldFiller;
import i3.dao.ShopProvinceDao;
import i3.entity.ShopProvince;

import java.io.BufferedReader;
import java.io.IOException;

public class ShopProvinceAvgTimeFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(ShopProvinceAvgTimeFiller.class);

	private ShopProvinceDao dao;

	public ShopProvinceAvgTimeFiller(ShopProvinceDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "shop_area_stay";
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

			String nick = tmp[0];
			long timeLong = Long.parseLong(tmp[2]);
			long persons = Long.parseLong(tmp[3]);

			ShopProvince.Key key = new ShopProvince.Key();
			key.setNick(nick);
			key.setProvince(Integer.parseInt(tmp[1]));

			ShopProvince entity = dao.find(key);
			if (null == entity) {
				entity = new ShopProvince();
				entity.setKey(key);
			}
			entity.setAvgTime(timeLong / persons);

			dao.add(entity);
		}

		dao.close();
	}

}
