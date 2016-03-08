package i3.report.keyword;

import i.report.FieldFiller;
import i.utils.Utils;
import i3.dao.KeywordDao;
import i3.entity.Keyword;

import java.io.BufferedReader;
import java.io.IOException;

public class KeywordPvUvFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(KeywordPvUvFiller.class);

	private KeywordDao dao;

	public KeywordPvUvFiller(KeywordDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "keyword_pv_uv";
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

			Keyword.Key key = new Keyword.Key();
			key.setNick(tmp[0]);
			key.setKeyword(keyword);

			Keyword entity = dao.find(key);
			if (null == entity) {
				entity = new Keyword();
				entity.setKey(key);
			}
			entity.setPv(pv);
			entity.setUv(uv);

			dao.add(entity);
		}

		dao.close();
	}

}
