package i3.report.page;

import i.report.FieldFiller;
import i.utils.Utils;
import i3.dao.ItemKeywordDao;
import i3.entity.ItemKeyword;

import java.io.BufferedReader;
import java.io.IOException;

public class ItemKeywordPvFiller implements FieldFiller {

	// private static final Log LOG =
	// LogFactory.getLogger(ItemKeywordPvFiller.class);

	private ItemKeywordDao dao;

	public ItemKeywordPvFiller(ItemKeywordDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "item_keyword_pv";
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

			String keyword = Utils.urlDecode(tmp[2]);
			if (keyword.equals("-") || keyword.trim().equals("")
					|| tmp[0].equals("-")) {
				continue;
			}

			long pv = Long.parseLong(tmp[4]);

			ItemKeyword.Key key = new ItemKeyword.Key();
			key.setNick(tmp[0]);
			key.setNumIID(tmp[1]);
			key.setKeyword(keyword);

			ItemKeyword entity = dao.find(key);
			if (null == entity) {
				entity = new ItemKeyword();
				entity.setKey(key);
			}
			if (tmp[3].equalsIgnoreCase("1")) {
				entity.setPv1(pv);
			} else if (tmp[3].equalsIgnoreCase("2")) {
				entity.setPv2(pv);
			} else if (tmp[3].equalsIgnoreCase("3")) {
				entity.setPv3(pv);
			} else {
				continue;
			}

			dao.add(entity);
		}

		dao.close();
	}

}
