package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.PathRelationDao;
import i3.entity.PathRelation;
import i3.report.path.Path1RelationPvUvFiller;

import java.io.IOException;

public class Path1RelationReport extends Report<PathRelation, PathRelationDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(Path1RelationReport.class);

	public Path1RelationReport(String date) {
		super(date, 1000);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new Path1RelationPvUvFiller(dao));
	}

	@Override
	protected PathRelationDao getDao() {
		return new PathRelationDao();
	}

	@Override
	protected String getNick(PathRelation entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_PATH_DAY_RELATION(View_ID,From_ID,PV,UV,BounceRate,Percentage,Relevance) values";
	}

	@Override
	protected String value(Usr user, PathRelation entity) {
		PathRelation.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("(");
		builder.append("(select View_ID from A_PATH_DAY where Date='");
		builder.append(dateTime);
		builder.append("' and UserID=");
		builder.append(user.getUid());
		builder.append(" and From_Url=");
		builder.append(DbUtils.sqlEscape(entity.getUrl()));
		builder.append(" limit 1),");
		builder.append(key.getFromID());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getBounceRate());
		builder.append(",");
		builder.append(entity.getPercentage());
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
		Path1RelationReport report = new Path1RelationReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
