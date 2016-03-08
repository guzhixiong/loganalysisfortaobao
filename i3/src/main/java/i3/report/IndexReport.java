package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.IndexDao;
import i3.entity.Index;
import i3.report.index.IndexAvgTimeFiller;
import i3.report.page.IndexPvUvFiller;

import java.io.IOException;

public class IndexReport extends Report<Index, IndexDao> {

	// private static final Log LOG = LogFactory.getLogger(IndexReport.class);

	public IndexReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new IndexPvUvFiller(dao));
		fill(new IndexAvgTimeFiller(dao));
	}

	@Override
	protected IndexDao getDao() {
		return new IndexDao();
	}

	@Override
	protected String getNick(Index entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_INDEX_DAY(Date,UserID,PV,UV,AvgTime,EntranceRate,BounceRate) values";
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
		IndexReport report = new IndexReport(args[0]);
		report.fillAll();
		report.doInsert();

		// report.list();
	}

}
