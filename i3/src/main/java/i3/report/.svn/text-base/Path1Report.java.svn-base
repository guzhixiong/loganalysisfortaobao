package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.PathDao;
import i3.entity.Path;
import i3.report.path.Path1Filler;

import java.io.IOException;

public class Path1Report extends Report<Path, PathDao> {

	// private static final Log LOG = LogFactory.getLogger(Path1Report.class);

	public Path1Report(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new Path1Filler(dao));
	}

	@Override
	protected PathDao getDao() {
		return new PathDao();
	}

	@Override
	protected String getNick(Path entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_PATH_DAY(Date,UserID,From_Url,From_Title,From_Type,Source_ID) values";
	}

	@Override
	protected String value(Usr user, Path entity) {
		Path.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(DbUtils.sqlEscape(entity.getUrl()));
		builder.append(",");
		builder.append(DbUtils.sqlEscape(entity.getTitle()));
		builder.append(",'");
		builder.append(key.getType());
		builder.append("',0");
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Path1Report report = new Path1Report(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
