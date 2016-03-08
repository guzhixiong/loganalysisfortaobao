package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.TaobaoKeywordDao;
import i3.entity.TaobaoKeyword;
import i3.report.taobaoKeyword.TaobaoKeywordAvgTimeFiller;
import i3.report.taobaoKeyword.TaobaoKeywordPvUvAvgPaegFiller;

import java.io.IOException;

public class TaobaoKeywordReport extends
		Report<TaobaoKeyword, TaobaoKeywordDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(TaobaoKeywordReport.class);

	public TaobaoKeywordReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new TaobaoKeywordPvUvAvgPaegFiller(dao));
		fill(new TaobaoKeywordAvgTimeFiller(dao));
	}

	@Override
	protected TaobaoKeywordDao getDao() {
		return new TaobaoKeywordDao();
	}

	@Override
	protected String getNick(TaobaoKeyword entity) {
		return entity.getNick();
	}

	@Override
	public String insert() {
		return "insert into A_TAOBAO_KEYWORD_DAY(Date,UserID,Keyword,PV,UV,OV,AvgTime,AvgPage,Relevance) values";
	}

	@Override
	protected String value(Usr user, TaobaoKeyword entity) {
		TaobaoKeyword.Key key = entity.getKey();

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
		builder.append(",");
		builder.append(entity.getOv());
		builder.append(",");
		builder.append(entity.getAvgTime());
		builder.append(",");
		builder.append(entity.getAvgPage());
		builder.append(",");
		builder.append(DbUtils.sqlEscape(entity.getRelevance()));
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		TaobaoKeywordReport report = new TaobaoKeywordReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
