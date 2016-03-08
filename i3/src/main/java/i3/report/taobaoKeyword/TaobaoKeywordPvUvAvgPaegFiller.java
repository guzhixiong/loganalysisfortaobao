package i3.report.taobaoKeyword;

import i.report.FieldFiller;
import i.utils.Utils;
import i3.dao.TaobaoKeywordDao;
import i3.entity.TaobaoKeyword;

import java.io.BufferedReader;
import java.io.IOException;

public class TaobaoKeywordPvUvAvgPaegFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(TaobaoKeywordPvUvAvgPaegFiller.class);

	private TaobaoKeywordDao dao;

	public TaobaoKeywordPvUvAvgPaegFiller(TaobaoKeywordDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "taobao_keyword_pv_uv";
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

			String keyword = Utils.urlDecode(tmp[1]);
			if (keyword.equalsIgnoreCase("-") || keyword.trim().equals("")) {
				continue;
			}

			TaobaoKeyword.Key key = new TaobaoKeyword.Key();
			key.setNick(tmp[0]);
			key.setKeyword(keyword);

			TaobaoKeyword entity = dao.find(key);
			if (null == entity) {
				entity = new TaobaoKeyword();
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
