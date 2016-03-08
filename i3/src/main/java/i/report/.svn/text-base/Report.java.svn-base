package i.report;

import i.uc.UcClient;
import i.uc.dao.SysDao;
import i.uc.dao.UsrDao;
import i.uc.entity.Sys;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.ex.SecKeyNickDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.bdb.BdbDao;
import dao.bdb.Exec;

@SuppressWarnings("rawtypes")
public abstract class Report<ENTITY, DAO extends BdbDao> {

	private static final Logger LOG = LoggerFactory.getLogger(Report.class);

	protected Configuration conf = null;

	private String base;

	private int limit;

	private String driver;
	private String protocol;
	private String options;

	protected String dateTime;

	protected DAO dao;

	public Report(String dateTime) {
		this.dateTime = StringUtils.rightPad(dateTime, 14, '0');

		dao = getDao();
		dao.setPath(getTimestamp());

		ConfigurationFactory factory = new ConfigurationFactory("config.xml");
		try {
			conf = factory.getConfiguration();
		} catch (ConfigurationException e) {
			LOG.error("", e);
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		// String[] actions = conf.getStringArray("uc.action");
		// LOG.debug(actions.length + " actions found:");
		// for (String action : actions) {
		// LOG.debug("  " + conf.getString(action + ".SOAPAction"));
		// }

		base = conf.getString("da.file.path", "/opt/i");
		new File(base).mkdirs();

		limit = conf.getInt("da.db.insert_values");

		String db = conf.getString("da.db");
		driver = conf.getString("da." + db + ".driver");
		protocol = conf.getString("da." + db + ".protocol");
		options = conf.getString("da." + db + ".options");
	}

	public Report(String dateTime, int limit) {
		this(dateTime);
		this.limit = limit;
	}

	abstract protected DAO getDao();

	abstract protected String getNick(ENTITY entity);

	protected String getTimestamp() {
		return ymd();
	}

	protected final String ymd() {
		return dateTime.substring(0, 8);
	}

	protected final String ymdh() {
		return dateTime.substring(0, 10);
	}

	abstract public void fillAll() throws IOException;

	protected void fill(FieldFiller filler) throws IOException,
			RuntimeException {
		String field = filler.getDataFile();
		if (null == field) {
			filler.fill(null);
			return;
		}

		File path = new File(base + File.separator + getTimestamp()
				+ File.separator + getTimestamp() + "_" + field);
		if (!path.exists()) {
			throw new RuntimeException(path.getAbsolutePath() + " not exist");
		}

		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().startsWith("part-r-");
			}
		});

		for (File file : files) {
			LOG.info(file.getAbsolutePath());

			BufferedReader reader = new BufferedReader(new FileReader(file));
			filler.fill(reader);
			reader.close();
		}
	}

	abstract protected String insert();

	abstract protected String value(Usr user, ENTITY entity);

	@SuppressWarnings("unchecked")
	public void doInsert() {
		final SysDao systemDao = new SysDao();
		systemDao.open(false);
		final UsrDao userDao = new UsrDao();
		userDao.open(false);

		final UcClient client = new UcClient();
		client.setSystemDao(systemDao);
		client.setUserDao(userDao);

		final Set<String> unknow = new HashSet<String>();

		dao.open();
		dao.each(new Exec<ENTITY>() {
			@Override
			public void execute(final ENTITY e) {
				String nick = getNick(e);
				Usr user = userDao.findByNick(nick);
				if (user == null) {
					unknow.add(nick);
				}
			}
		});
		client.getUsersByNick(unknow.toArray(new String[unknow.size()]));

		Set<Sys> allSys = systemDao.getAllSystems();
		Iterator<Sys> _sys = allSys.iterator();
		while (_sys.hasNext()) {
			Sys system = _sys.next();

			Map<String, String> param = system.getParameters();
			String host = param.get("IP");
			String port = param.get("Port");
			String db = param.get("DataBaseName");
			String user = param.get("DbUserName");
			String pass = param.get("DbPwd");

			String sysId = system.getId();
			Iterator<Usr> _usr = userDao.iterateBySystem(sysId);

			Connection conn = null;
			Statement stmt = null;
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(
						DbUtils.buildUrl(protocol, host, port, db, options),
						user, pass);
				stmt = conn.createStatement();

				if (dao instanceof SecKeyNickDao) {
					byNick(stmt, _usr);
				} else {
					directly(stmt, _usr);
				}
			} catch (ClassNotFoundException e) {
				LOG.error("", e);
				throw new RuntimeException(e);
			} catch (SQLException e) {
				LOG.error("", e);
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				LOG.error("", e);
				throw e;
			} finally {
				try {
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

		systemDao.close();
		userDao.close();
	}

	private void executeSQL(Statement stmt, StringBuilder builder)
			throws SQLException {
		String value = builder.toString();
		value = value.substring(0, value.length() - 1);
		String sql = insert() + value;
		LOG.info(sql);
		stmt.execute(sql);
	}

	private void byNick(Statement stmt, Iterator<Usr> users)
			throws SQLException {
		StringBuilder builder = new StringBuilder();

		int i = this.limit;
		while (users.hasNext()) {
			Usr user = users.next();

			@SuppressWarnings("unchecked")
			Iterator<ENTITY> it = (Iterator<ENTITY>) (((SecKeyNickDao) dao)
					.iterateByNick(user.getNick()));
			while (it.hasNext()) {
				ENTITY entity = it.next();
				if (entity == null) {
					continue;
				}
				builder.append(value(user, entity));

				if (i < 1) {
					executeSQL(stmt, builder);

					builder.setLength(0);
					i = this.limit;
				} else {
					builder.append(",");
					i--;
				}
			}

			if (i < this.limit) {
				executeSQL(stmt, builder);
			}
		}
	}

	private void directly(Statement stmt, Iterator<Usr> users)
			throws SQLException {
		StringBuilder builder = new StringBuilder();

		int i = this.limit;
		while (users.hasNext()) {
			Usr user = users.next();

			@SuppressWarnings("unchecked")
			ENTITY entity = (ENTITY) (dao.find(user.getNick()));
			if (entity == null) {
				continue;
			}
			builder.append(value(user, entity));

			if (i < 1) {
				executeSQL(stmt, builder);

				builder.setLength(0);
				i = this.limit;
			} else {
				builder.append(",");
				i--;
			}
		}

		if (i < this.limit) {
			executeSQL(stmt, builder);
		}
	}

}
