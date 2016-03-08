package i3.report.from;

import i.report.FieldFiller;
import i3.dao.ShopFromDao;
import i3.entity.ShopFrom;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopFromPvUvFiller implements FieldFiller {

	private static final Logger LOG = LoggerFactory
			.getLogger(ShopFromPvUvFiller.class);

	private ShopFromDao dao;

	public ShopFromPvUvFiller(ShopFromDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "shop_from_pv_uv";
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

			String from = tmp[1];
			try {
				Integer.parseInt(from);
			} catch (NumberFormatException e) {
				LOG.info("unknown from: " + from);
				continue;
			}

			ShopFrom.Key key = new ShopFrom.Key();
			key.setNick(tmp[0]);
			key.setFromID(from);

			ShopFrom entity = dao.find(key);
			if (null == entity) {
				entity = new ShopFrom();
				entity.setKey(key);
			}
			entity.setPv(pv);
			entity.setUv(uv);

			dao.add(entity);
		}

		dao.close();
	}

}
