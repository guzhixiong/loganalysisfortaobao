package i3.report;

import i.report.Report;
import i.uc.dao.SysDao;
import i.uc.entity.Sys;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.PathFromDao;
import i3.entity.ShopFrom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathFromReport extends Report<ShopFrom, PathFromDao> {

	private static final Logger LOG = LoggerFactory
			.getLogger(PathFromReport.class);

	public PathFromReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		dao.open(false);

		Set<Sys> allSys = new SysDao().getAllSystems();
		Iterator<Sys> _sys = allSys.iterator();
		while (_sys.hasNext()) {
			Sys system = _sys.next();

			Map<String, String> param = system.getParameters();
			String host = param.get("IP");
			String port = param.get("Port");
			String db = param.get("DataBaseName");
			String user = param.get("DbUserName");
			String pass = param.get("DbPwd");

			String sql = "select * from A_SHOP_FROM_DAY where From_ID <> 31 and From_ID <> 30 and From_ID <> -1 and Date = '"
					+ DbUtils.sqlDate(dateTime) + "' order by UserID,PV desc";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(DbUtils.buildUrl(
						"jdbc:mysql", host, port, db,
						"useUnicode=true&characterEncoding=utf8"), user, pass);
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				int countDown = 5;
				String userID = "";
				while (rs.next()) {
					String uid = rs.getString("UserID");
					if (uid.equalsIgnoreCase(userID)) {
						if (countDown == 0) {
							continue;
						}
					} else {
						userID = uid;
						countDown = 5;
					}

					ShopFrom.Key key = new ShopFrom.Key();
					key.setNick(userID);
					key.setFromID(rs.getString("From_ID"));

					ShopFrom entity = new ShopFrom();
					entity.setKey(key);
					entity.setPv(rs.getInt("PV"));
					entity.setUv(rs.getInt("UV"));
					entity.setPercentage(rs.getDouble("Percentage"));

					dao.add(entity);

					countDown--;
				}
			} catch (ClassNotFoundException e) {
				LOG.error("", e);
				throw new RuntimeException(e);
			} catch (SQLException e) {
				LOG.error("", e);
				throw new RuntimeException(e);
			} finally {
				try {
					if (null != rs)
						rs.close();
					if (null != stmt)
						stmt.close();
					if (null != conn)
						conn.close();
				} catch (SQLException e) {
					LOG.error("", e);
					throw new RuntimeException(e);
				}
			}
		}

		dao.close();
	}

	@Override
	protected PathFromDao getDao() {
		return new PathFromDao();
	}

	@Override
	protected String getNick(ShopFrom entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_PATH_FROM_DAY(Date,UserID,From_ID,PV,UV,Percentage) values";
	}

	@Override
	protected String value(Usr user, ShopFrom entity) {
		ShopFrom.Key key = entity.getKey();

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
		builder.append(",");
		builder.append(entity.getPercentage());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		PathFromReport report = new PathFromReport(args[0]);
		report.fillAll();
		report.doInsert();

	}

}
