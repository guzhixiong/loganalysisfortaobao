package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.IndexProvinceDao;
import i3.entity.IndexProvince;
import i3.report.page.IndexProvincePvUvFiller;

import java.io.IOException;

public class IndexProvinceReport extends
		Report<IndexProvince, IndexProvinceDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(IndexProvinceReport.class);

	public IndexProvinceReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new IndexProvincePvUvFiller(dao));
	}

	@Override
	protected IndexProvinceDao getDao() {
		return new IndexProvinceDao();
	}

	@Override
	protected String getNick(IndexProvince entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_INDEX_PROVINCE_DAY(Date,UserID,Province,PV,UV) values";
	}

	@Override
	protected String value(Usr user, IndexProvince entity) {
		IndexProvince.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getProvince());
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
		IndexProvinceReport report = new IndexProvinceReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
