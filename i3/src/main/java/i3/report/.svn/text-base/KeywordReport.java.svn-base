package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.KeywordDao;
import i3.entity.Keyword;
import i3.report.keyword.KeywordPvUvFiller;

import java.io.IOException;

public class KeywordReport extends Report<Keyword, KeywordDao> {

	// private static final Log LOG = LogFactory.getLogger(ShopReport.class);

	public KeywordReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new KeywordPvUvFiller(dao));
	}

	@Override
	protected KeywordDao getDao() {
		return new KeywordDao();
	}

	@Override
	protected String getNick(Keyword entity) {
		return entity.getNick();
	}

	@Override
	public String insert() {
		return "insert into A_KEYWORD_DAY(Date,UserID,Keyword,PV,UV) values";
	}

	@Override
	protected String value(Usr user, Keyword entity) {
		Keyword.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(DbUtils.sqlEscape(key.getKeyword()));
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		KeywordReport report = new KeywordReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
