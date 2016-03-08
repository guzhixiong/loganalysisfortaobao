package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.IndexFromDao;
import i3.entity.IndexFrom;
import i3.report.page.IndexFromPvUvFiller;

import java.io.IOException;

public class IndexFromReport extends Report<IndexFrom, IndexFromDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(IndexFromReport.class);

	public IndexFromReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new IndexFromPvUvFiller(dao));
	}

	@Override
	protected IndexFromDao getDao() {
		return new IndexFromDao();
	}

	@Override
	protected String getNick(IndexFrom entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_INDEX_FROM_DAY(Date,UserID,From_ID,PV,UV) values";
	}

	@Override
	protected String value(Usr user, IndexFrom entity) {
		IndexFrom.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getFromID());
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
		IndexFromReport report = new IndexFromReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
