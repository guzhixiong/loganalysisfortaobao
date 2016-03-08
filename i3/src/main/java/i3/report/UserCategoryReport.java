package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.UserCategoryDao;
import i3.entity.UserCategory;
import i3.report.page.UserCategoryAvgTimeFiller;
import i3.report.page.UserCategoryPvUvFiller;

import java.io.IOException;

public class UserCategoryReport extends Report<UserCategory, UserCategoryDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(UserCategoryReport.class);

	public UserCategoryReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new UserCategoryPvUvFiller(dao));
		fill(new UserCategoryAvgTimeFiller(dao));
	}

	@Override
	protected UserCategoryDao getDao() {
		return new UserCategoryDao();
	}

	@Override
	protected String getNick(UserCategory entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_USER_CATEGORY_DAY(Date,UserID,Category_URL,Category_TITLE,PV,UV,AvgTime,EntranceRate,BounceRate) values";
	}

	@Override
	protected String value(Usr user, UserCategory entity) {
		UserCategory.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder();
		builder.append("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(DbUtils.sqlEscape(key.getCategoryURL()));
		builder.append(",");
		builder.append(DbUtils.sqlEscape(entity.getCategoryTitle()));
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
		UserCategoryReport report = new UserCategoryReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
