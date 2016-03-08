package i3.report.area;

import i.report.FieldFiller;
import i3.dao.ShopProvinceDao;
import i3.entity.ShopProvince;

import java.io.BufferedReader;
import java.io.IOException;

public class ShopProvincePvUvAvgPageFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(ShopProvincePvUvAvgPageFiller.class);

	private ShopProvinceDao dao;

	public ShopProvincePvUvAvgPageFiller(ShopProvinceDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "shop_area_pv_uv";
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
			long pv = Long.parseLong(tmp[2]);
			long uv = Long.parseLong(tmp[3]);

			ShopProvince.Key key = new ShopProvince.Key();
			key.setNick(nick);
			key.setProvince(Integer.parseInt(tmp[1]));

			ShopProvince entity = dao.find(key);
			if (null == entity) {
				entity = new ShopProvince();
				entity.setKey(key);
			}
			entity.setPv(pv);
			entity.setUv(uv);
			entity.setAvgPage(((float) pv) / uv);

			dao.add(entity);
		}

		dao.close();
	}

}
