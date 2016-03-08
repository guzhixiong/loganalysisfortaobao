package i3.report.page;

import i.report.FieldFiller;
import i3.dao.UserCategoryDao;
import i3.entity.UserCategory;

import java.io.BufferedReader;
import java.io.IOException;

public class UserCategoryAvgTimeFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(UserCategoryAvgTimeFiller.class);

	private UserCategoryDao dao;

	public UserCategoryAvgTimeFiller(UserCategoryDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "user_category_stay";
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

			UserCategory.Key key = new UserCategory.Key();
			key.setNick(tmp[0]);
			key.setCategoryURL(tmp[1]);

			UserCategory entity = dao.find(key);
			if (null == entity) {
				entity = new UserCategory();
				entity.setKey(key);
			}
			entity.setAvgTime(timeLong / persons);

			String title = entity.getCategoryTitle();
			if (null == title) {
				entity.setCategoryTitle("");
			}

			dao.add(entity);
		}

		dao.close();
	}
}
