package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.IndexDao;
import i3.entity.Index;
import i3.report.index.IndexAvgTimeFiller;
import i3.report.index.IndexPvUvAvgPageFiller;

import java.io.IOException;

public class HourlyIndexReport extends Report<Index, IndexDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(HourlyIndexReport.class);

	public HourlyIndexReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new IndexPvUvAvgPageFiller(dao));
		fill(new IndexAvgTimeFiller(dao));
	}

	@Override
	protected IndexDao getDao() {
		IndexDao dao = new IndexDao();
		dao.setStoreName("index_hourly");
		return dao;
	}

	@Override
	protected String getNick(Index entity) {
		return entity.getNick();
	}

	@Override
	protected String getTimestamp() {
		return ymdh();
	}

	@Override
	protected String insert() {
		return "insert into A_INDEX_HOUR(Time,UserID,PV,UV,AvgTime,AvgPage,EntranceRate,BounceRate,Relevance) values";
	}

	@Override
	protected String value(Usr user, Index entity) {
		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getAvgTime());
		builder.append(",");
		builder.append(entity.getAvgPage());
		builder.append(",");
		builder.append(entity.getEntranceRate());
		builder.append(",");
		builder.append(entity.getBounceRate());
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
		HourlyIndexReport report = new HourlyIndexReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
