package i3.report.shop;

import i.report.FieldFiller;
import i3.dao.ShopDao;
import i3.entity.Shop;

import java.io.BufferedReader;
import java.io.IOException;

public class ShopPvUvAvgPageFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(ShopPvUvAvgPageFiller.class);

	private ShopDao dao;

	public ShopPvUvAvgPageFiller(ShopDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "shop_pv_uv";
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

			Shop entity = dao.find(tmp[0]);
			if (null == entity) {
				entity = new Shop();
				entity.setNick(tmp[0]);
			}
			entity.setPv(pv);
			entity.setUv(uv);
			entity.setAvgPage(((float) pv) / uv);

			dao.add(entity);
		}

		dao.close();
	}

}
