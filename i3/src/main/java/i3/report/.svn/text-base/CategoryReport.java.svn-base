package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.CategoryDao;
import i3.entity.Category;
import i3.report.page.CategoryAvgTimeFiller;
import i3.report.page.CategoryPvUvFiller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryReport extends Report<Category, CategoryDao> {

	private static final Logger LOG = LoggerFactory
			.getLogger(CategoryReport.class);

	public CategoryReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new CategoryPvUvFiller(dao));
		fill(new CategoryAvgTimeFiller(dao));
	}

	@Override
	protected CategoryDao getDao() {
		return new CategoryDao();
	}

	@Override
	protected String getNick(Category entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_CATEGORY_DAY(Date,UserID,CategoryID,PV,UV,AvgTime,EntranceRate,BounceRate) values";
	}

	protected String value(Usr user, Category entity) {
		Category.Key key = entity.getKey();
		if (key.getCategoryID().equalsIgnoreCase("-")) {
			return null;
		}

		if (key.getCategoryID() == null) {
			LOG.error("null CategoryID " + key.getNick());
			return null;
		}

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getCategoryID());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getAvgTime());
		builder.append(",");
		builder.append(entity.getEntranceRate());
		builder.append(",");
		builder.append(entity.getBounceRate());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		CategoryReport report = new CategoryReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
