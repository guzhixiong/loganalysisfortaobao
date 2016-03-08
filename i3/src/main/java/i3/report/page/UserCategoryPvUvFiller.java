package i3.report.page;

import i.report.FieldFiller;
import i3.dao.UserCategoryDao;
import i3.entity.UserCategory;

import java.io.BufferedReader;
import java.io.IOException;

public class UserCategoryPvUvFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(UserCategoryPvUvFiller.class);

	private UserCategoryDao dao;

	public UserCategoryPvUvFiller(UserCategoryDao dao) {
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
			if (!tmp[1].equals("4") || tmp[2].equals("-") || tmp[0].equals("-")) {
				continue;
			}

			String url = tmp[3];
			int pos2 = url.indexOf(".htm?");
			pos2 = pos2 == -1 ? url.length() : pos2 + 5;
			url = url.substring(0, pos2);

			UserCategory.Key key = new UserCategory.Key();
			key.setNick(tmp[0]);
			key.setCategoryURL(url);

			UserCategory entity = dao.find(key);
			if (null == entity) {
				entity = new UserCategory();
				entity.setKey(key);
			}
			entity.setCategoryTitle(tmp[4]);

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
