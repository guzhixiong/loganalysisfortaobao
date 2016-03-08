package i3.report.page;

import i.report.FieldFiller;
import i3.dao.CategoryDao;
import i3.entity.Category;

import java.io.BufferedReader;
import java.io.IOException;

public class CategoryAvgTimeFiller implements FieldFiller {

	// private static final Log LOG = LogFactory
	// .getLogger(CategoryAvgTimeFiller.class);

	private CategoryDao dao;

	public CategoryAvgTimeFiller(CategoryDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "category_stay";
	}

	@Override
	public void fill(BufferedReader reader) throws IOException {
		dao.open(false);

		String line;
		while (null != (line = reader.readLine())) {
			String[] tmp = line.split("\\s");
			if (tmp[0].equals("-") || !tmp[1].equals("2") || tmp[2].equals("-")) {
				continue;
			}

			long timeLong = Long.parseLong(tmp[2]);
			long persons = Long.parseLong(tmp[3]);

			Category.Key key = new Category.Key();
			key.setNick(tmp[0]);
			key.setCategoryID(tmp[1]);

			Category entity = dao.find(key);
			if (null == entity) {
				entity = new Category();
				entity.setKey(key);
			}
			entity.setAvgTime(timeLong / persons);

			dao.add(entity);
		}

		dao.close();
	}

}
