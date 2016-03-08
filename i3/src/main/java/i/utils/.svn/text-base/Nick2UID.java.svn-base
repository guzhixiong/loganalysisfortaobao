package i.utils;

import i.uc.dao.SysDao;
import i.uc.entity.Sys;

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

import dao.bdb.pair.Pair;
import dao.bdb.pair.PairDao;

public class Nick2UID extends PairDao {

	private static final Logger LOG = LoggerFactory.getLogger(Nick2UID.class);

	public static final String SQL = "select UserID, Nick from SYS_USER";

	public Nick2UID() {
		this.setPath("nick2uid");
		this.setStoreName("nick2uid");
	}

	public void init() {
		SysDao sysDao = new SysDao();
		sysDao.open();
		Set<Sys> all = sysDao.getAllSystems();
		sysDao.close();

		// Set<Sys> all = new HashSet<Sys>();
		//
		// Sys sys = new Sys();
		// sys.setId("8415035318c43752200c47ee93fe20ed");
		// Map<String, String> p = new HashMap<String, String>();
		// p.put("IP", "192.168.1.171");
		// p.put("Port", "3306");
		// p.put("DataBaseName", "NBDR_V3");
		// p.put("DbUserName", "NBDR");
		// p.put("DbPwd", "NBDR");
		// sys.setParameters(p);
		// all.add(sys);
		//
		// sys = new Sys();
		// sys.setId("f6ed5b03a11c8aa25ce6cea5e1386acd");
		// p = new HashMap<String, String>();
		// p.put("IP", "192.168.1.171");
		// p.put("Port", "3306");
		// p.put("DataBaseName", "NBDR_V3_02");
		// p.put("DbUserName", "NBDR");
		// p.put("DbPwd", "NBDR");
		// sys.setParameters(p);
		// all.add(sys);
		//
		// Iterator<Sys> it = all.iterator();
		// while (it.hasNext()) {
		// Sys system = it.next();
		// LOG.info("{} {}", system.getId(), system.getParameters());
		// }

		Iterator<Sys> it = all.iterator();
		while (it.hasNext()) {
			Sys system = it.next();
			Map<String, String> param = system.getParameters();
			String host = param.get("IP");
			String port = param.get("Port");
			String db = param.get("DataBaseName");
			String user = param.get("DbUserName");
			String pass = param.get("DbPwd");

			query(host, port, db, user, pass);
		}

		// UsrDao usrDao = new UsrDao();
		// usrDao.open();
		//
		// open(false);
		// int i = 0;
		// Iterator<Usr> it = usrDao.iterator();
		// while (it.hasNext()) {
		// Usr user = it.next();
		// LOG.info("{} {}", user.getUid(), user.getNick());
		// this.add(new Pair(user.getUid(), user.getNick()));
		//
		// i++;
		// }
		// close();
		//
		// usrDao.close();
		//
		// LOG.info("{}", i);
	}

	private void query(String host, String port, String db, String user,
			String pass) {
		Connection conn = null;
		Statement stmt = null;
		try {
			open();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":"
					+ port + "/" + db
					+ "?useUnicode=true&characterEncoding=utf8", user, pass);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				String nick = rs.getString(2);
				String userID = rs.getString(1);
				if (nick == null || userID == null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug(nick + ", " + userID);
					}
					continue;
				}
				this.add(new Pair(nick, userID));
			}
		} catch (ClassNotFoundException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOG.error("", e);
				throw new RuntimeException(e);
			}
			close();
		}
	}

	public static void main(String[] args) {
		try {
			new Nick2UID().init();
		} catch (RuntimeException e) {
			LOG.error("", e);
			System.exit(-1);
		}

		// new Nick2UID().list();
	}

}
