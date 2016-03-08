package i3.market;

import i.utils.DbUtils;
import i3.market.dao.MarketFluxDao;
import i3.market.entity.MarketFlux;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.lang.StringUtils;
import org.phprpc.util.PHPSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketFluxCollector {

	private static final Logger LOG = LoggerFactory
			.getLogger(MarketFluxCollector.class);

	protected Configuration conf = null;

	private final String DRIVER;
	private final String PROTOCOL;
	private final String HOST;
	private final String PORT;
	private final String DATABASE;
	private final String OPTIONS;
	private final String USERNAME;
	private final String PASSWORD;

	private final String DATA_TABLE;
	private final String SIGNAL_TABLE;

	private final int MAX_COUNT;

	private String dest = "/data/share/market_flux";
	private String src = "/data/share/nick_ip";

	private static final String FROM = "nick_ip_from";
	private static final String KEYWORD = "nick_ip_keyword";

	private String date;
	private Date current;

	private MarketFluxDao dao;

	public MarketFluxCollector(String date) throws IOException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ParseException {
		// String year = date.substring(0, 4);
		// String month = date.substring(4, 6);
		// month = month.startsWith("0") ? month.substring(1) : month;
		// String day = date.substring(6, 8);
		// day = day.startsWith("0") ? day.substring(1) : day;
		// date = year + "-" + month + "-" + day;

		ConfigurationFactory factory = new ConfigurationFactory("config.xml");
		try {
			conf = factory.getConfiguration();
		} catch (ConfigurationException e) {
			LOG.error("", e);
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		String db = conf.getString("market_flux.db", "mysql");
		DRIVER = conf.getString("market_flux." + db + ".driver");
		PROTOCOL = conf.getString("market_flux." + db + ".protocol");
		HOST = conf.getString("market_flux." + db + ".host");
		PORT = conf.getString("market_flux." + db + ".port", "3306");
		DATABASE = conf.getString("market_flux." + db + ".database");
		OPTIONS = conf.getString("market_flux." + db + ".options");
		USERNAME = conf.getString("market_flux." + db + ".username");
		PASSWORD = conf.getString("market_flux." + db + ".password");

		DATA_TABLE = conf.getString("market_flux.table.data",
				"MarketFluxFSIndex");
		SIGNAL_TABLE = conf.getString("market_flux.table.signal", "FluxSignal");

		MAX_COUNT = conf.getInt("market_flux.max.count", 20000);

		dest = conf.getString("market_flux.dest", dest);
		src = conf.getString("market_flux.src", src);

		if (date.length() < 8) {
			throw new RuntimeException("need a date parameter like [yyyyMMdd]");
		}
		this.date = date.substring(0, 8);
		current = new SimpleDateFormat("yyyyMMdd").parse(this.date);

		dao = new MarketFluxDao(date);
	}

	private void fillFroms() throws IOException {
		List<Integer> froms = new ArrayList<Integer>();

		File[] f = listFiles(FROM);
		for (File file : f) {
			String nick = "";
			String ip = "";

			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = in.readLine()) != null) {
					String[] tmp = line.split("\\s{1,}");
					if (tmp.length < 4) {
						continue;
					}

					String _nick = tmp[0];
					String _ip = tmp[1];

					int from = -1;
					try {
						from = Integer.parseInt(tmp[2]);
					} catch (NumberFormatException e) {
						LOG.info("{}, {}, {}", new String[] { _nick, _ip,
								tmp[2] });
						continue;
					}

					if (from == -1) {
						continue;
					}

					if (nick.equals("") || ip.equals("")) {

					} else if (nick.equals(_nick) && ip.equals(_ip)) {

					} else {
						dao.add(from(froms, nick, ip));
						froms.clear();
					}

					nick = _nick;
					ip = _ip;
					froms.add(from);

					tmp = null;
					line = null;
				}
				if (froms.size() > 0) {
					dao.add(from(froms, nick, ip));
					froms.clear();
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
	}

	private MarketFlux from(List<Integer> froms, String nick, String ip) {
		MarketFlux.Key key = new MarketFlux.Key();
		key.setNick(nick);
		key.setIp(ip);

		MarketFlux entity = dao.find(key);
		if (entity == null) {
			entity = new MarketFlux();
			entity.setKey(key);
			entity.setKeywords(new ArrayList<String>());
		}

		entity.setFroms(froms);
		return entity;
	}

	private void fillKeywords() throws IOException {
		List<String> keywords = new ArrayList<String>();
		Set<String> k = new HashSet<String>();

		File[] f = listFiles(KEYWORD);
		for (File file : f) {
			String nick = "";
			String ip = "";

			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = in.readLine()) != null) {
					String[] tmp = line.split("\\s{1,}");
					if (tmp.length < 4) {
						continue;
					}

					String _nick = tmp[0];
					String _ip = tmp[1];

					String keyword = tmp[2];
					if (keyword.equals("-")) {
						LOG.info("{}, {}, {}", new String[] { _nick, _ip,
								keyword });
						continue;
					}

					if (nick.equals("") || ip.equals("")) {

					} else if (nick.equals(_nick) && ip.equals(_ip)) {

					} else {
						dao.add(keyword(keywords, nick, ip));
						keywords.clear();
						k.clear();
					}

					nick = _nick;
					ip = _ip;
					if (!k.contains(keyword)) {
						keywords.add(keyword);
						k.add(keyword);
					}
				}
				if (keywords.size() > 0) {
					dao.add(keyword(keywords, nick, ip));
					keywords.clear();
					k.clear();
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
	}

	private MarketFlux keyword(List<String> keywords, String nick, String ip) {
		MarketFlux.Key key = new MarketFlux.Key();
		key.setNick(nick);
		key.setIp(ip);

		MarketFlux entity = dao.find(key);
		if (entity == null) {
			entity = new MarketFlux();
			entity.setKey(key);
			entity.setFroms(new ArrayList<Integer>());
		}

		entity.setKeywords(keywords);
		return entity;
	}

	private File[] listFiles(String where) throws FileNotFoundException {
		File path = new File(src + File.separator + date + "_" + where);
		if (!path.exists()) {
			throw new FileNotFoundException(path.getAbsolutePath());
		}

		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().startsWith("part-r-");
			}
		});
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return f1.getAbsoluteFile().compareTo(f2.getAbsoluteFile());
			}
		});

		LOG.info("{}: {}", where, StringUtils.join(files, ", "));

		return files;
	}

	private void persistent() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException,
			ParseException, ClassNotFoundException, SQLException {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");

		// select * from MarketFluxFSIndex where UserNick='' order by StartDate
		// desc limit 1
		//
		// insert into
		// MarketFluxFSIndex(UserNick,StartDate,EndDate,Path,Count,Modif)
		// values()
		//
		// update MarketFluxFSIndex
		// set(EndDate,Path,Count,Modif)
		// where UserNick=''
		//
		// insert into
		// FluxSignal(Task,CreateTime,IsDone)
		// values('MarketFluxFSIndex','',1)

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(
					DbUtils.buildUrl(PROTOCOL, HOST, PORT, DATABASE, OPTIONS),
					USERNAME, PASSWORD);
			stmt = conn.createStatement();

			// stmt.execute("truncate table MarketFluxFSIndex");

			boolean skip = false;
			int count = -1;

			String nick = "";

			String file0 = "";
			Date start = null;
			Date end = null;

			List<XiaoAi.Moudle.Flux.MarketFlux> fluxList = new ArrayList<XiaoAi.Moudle.Flux.MarketFlux>();

			Iterator<MarketFlux> it = dao.iterator();
			while (it.hasNext()) {
				MarketFlux mf = it.next();

				String _nick = mf.getNick();
				if (!nick.equals(_nick)) {
					if (!nick.equals("")) {
						persistent(fluxList, stmt, count, nick, start, end,
								file0);
						fluxList.clear();
					}

					nick = _nick;

					ResultSet rs = stmt.executeQuery("select * from "
							+ DATA_TABLE + " where UserNick="
							+ DbUtils.sqlEscape(nick)
							+ " order by StartDate desc limit 1");
					if (rs.next()) {
						count = rs.getInt("Count");
						Date start0 = rs.getDate("StartDate");
						Date end0 = rs.getDate("EndDate");
						if (end0.before(current) && count < MAX_COUNT) {
							skip = false;

							start = start0;
							end = current;

							file0 = rs.getString("Path") + "/" + nick + "/"
									+ f.format(start0) + f.format(end0);
							fluxList = getMarketFluxList(file0);
						} else if (end0.before(current) && count >= MAX_COUNT) {
							skip = false;

							count = 0;
							start = current;
							end = current;
						} else {
							skip = true;

							count = -1;
							start = null;
							end = null;
							continue;
						}
					} else {
						skip = false;

						count = 0;
						start = current;
						end = current;

					}
					rs.close();
				} else if (skip) {
					continue;
				}

				XiaoAi.Moudle.Flux.MarketFlux flux = new XiaoAi.Moudle.Flux.MarketFlux();
				flux.setIP(mf.getKey().getIp());
				flux.setTime(current);
				flux.setListFrom(mf.getFroms());
				flux.setListKeyWord(mf.getKeywords());

				fluxList.add(flux);
			}
			persistent(fluxList, stmt, count, nick, start, end, file0);
			fluxList.clear();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<XiaoAi.Moudle.Flux.MarketFlux> getMarketFluxList(String path)
			throws FileNotFoundException, IOException, IllegalAccessException,
			InvocationTargetException {
		File f = new File(path);

		byte[] buffer = null;
		RandomAccessFile in = null;
		try {
			in = new RandomAccessFile(f, "r");
			buffer = new byte[(int) in.length()];
			in.readFully(buffer);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return (List<XiaoAi.Moudle.Flux.MarketFlux>) new PHPSerializer()
				.unserialize(buffer, List.class);
	}

	private String d(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	private void persistent(List<XiaoAi.Moudle.Flux.MarketFlux> fluxList,
			Statement stmt, int count, String nick, Date start, Date end,
			String file0) throws FileNotFoundException, IllegalAccessException,
			InvocationTargetException, IOException, SQLException {
		if (fluxList.size() == 0) {
			return;
		}

		String path = dest + "/" + nick;
		new File(path).mkdirs();

		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		String file = path + "/" + f.format(start) + f.format(end);
		FileOutputStream out = null;
		try {
			byte[] php = new PHPSerializer().serialize(fluxList);

			out = new FileOutputStream(file);
			out.write(php);

			php = null;

			File f0 = new File(file0);
			if (f0.exists() && !f0.delete()) {
				LOG.error("failed:: delete file [{}]", file0);
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}

		String sql = "";
		if (count > 0) {
			sql = "update " + DATA_TABLE + " set EndDate='" + d(end)
					+ "',Path='" + dest + "',Count="
					+ (count + fluxList.size()) + ",Modif='" + d(new Date())
					+ "' where UserNick=" + DbUtils.sqlEscape(nick)
					+ " and StartDate='" + d(start) + "'";
		} else {
			sql = "insert into " + DATA_TABLE
					+ "(UserNick,StartDate,EndDate,Path,Count,Modif) value("
					+ DbUtils.sqlEscape(nick) + ",'" + f.format(start) + "','"
					+ f.format(end) + "','" + dest + "'," + fluxList.size()
					+ ",'" + d(new Date()) + "')";
		}

		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			LOG.error(sql, e);
		}
	}

	public void fill() throws IOException {
		dao.open(false);
		try {
			fillFroms();
			fillKeywords();
		} finally {
			dao.close();
		}
	}

	public void persist() {
		dao.open();
		LOG.info("to file");
		try {
			persistent();
		} catch (IllegalArgumentException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (ParseException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} finally {
			dao.close();
		}
	}

	public void setSignal() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(
					DbUtils.buildUrl(PROTOCOL, HOST, PORT, DATABASE, OPTIONS),
					USERNAME, PASSWORD);
			stmt = conn.createStatement();

			stmt.execute("insert into " + SIGNAL_TABLE
					+ "(Task,CreateTime,IsDone) value('" + DATA_TABLE + "','"
					+ d(current) + "',1)");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ParseException, ClassNotFoundException,
			SQLException {
		LOG.info("Hi");

		String useage = "Useage: java i3.market.MarketFluxCollector yyyyMMdd fill|persist|signal";

		if (args.length < 2) {
			System.out.println(useage);
		}

		MarketFluxCollector c = new MarketFluxCollector(args[0]);
		if (args[1].equalsIgnoreCase("fill")) {
			c.fill();
		} else if (args[1].equalsIgnoreCase("persist")) {
			c.persist();
		} else if (args[1].equalsIgnoreCase("signal")) {
			c.setSignal();
		} else {
			System.out.println("the 2nd argument " + args[1] + " is unknow");
			System.out.println(useage);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				LOG.info("shutdown");
			}

		});

		// MarketFluxDao dao = new MarketFluxDao("20110127");
		// // dao.list();
		// dao.open();
		// Iterator<MarketFlux> it = dao.iterateByNick("c测试账号152");
		// while (it.hasNext()) {
		// MarketFlux entity = it.next();
		// MarketFlux.Key key = entity.getKey();
		// LOG.info("{} {} {} {}", new String[] { key.getNick(),
		// key.getNick(), entity.getFroms().toString(),
		// entity.getKeywords().toString() });
		// }
		// dao.close();

		// SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		// try {
		// Date start = f.parse("20110208");
		// LOG.info("{}", start);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
