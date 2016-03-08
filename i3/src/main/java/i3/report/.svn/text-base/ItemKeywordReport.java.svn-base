package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.ItemKeywordDao;
import i3.entity.ItemKeyword;
import i3.report.page.ItemKeywordPvFiller;

import java.io.IOException;

public class ItemKeywordReport extends Report<ItemKeyword, ItemKeywordDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(ItemKeywordReport.class);

	public ItemKeywordReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new ItemKeywordPvFiller(dao));
	}

	@Override
	protected ItemKeywordDao getDao() {
		return new ItemKeywordDao();
	}

	@Override
	protected String getNick(ItemKeyword entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_ITEM_KEYWORD_DAY(Date,UserID,Num_IID,Keyword,PV_1,PV_2,PV_3) values";
	}

	@Override
	protected String value(Usr user, ItemKeyword entity) {
		ItemKeyword.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getNumIID());
		builder.append(",");
		builder.append(DbUtils.sqlEscape(key.getKeyword()));
		builder.append(",");
		builder.append(entity.getPv1());
		builder.append(",");
		builder.append(entity.getPv2());
		builder.append(",");
		builder.append(entity.getPv3());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ItemKeywordReport report = new ItemKeywordReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
