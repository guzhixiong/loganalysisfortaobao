package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App2 implements Exec {

	private static final Logger LOG = LoggerFactory.getLogger(App2.class);

	public void exec(Connection conn, int num) throws SQLException {
		Date start = null;
		Date end = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			start = f.parse("2000-1-01 00:00:00");
			end = f.parse("2011-01-01 00:00:00");
		} catch (ParseException e) {
			LOG.error("", e);
			System.exit(-1);
		}

		Calendar curr = Calendar.getInstance();
		curr.setTime(start);

		StringBuilder str = new StringBuilder("");
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		while (curr.getTime().before(end)) {
			str.append("('");
			str.append(f.format(curr.getTime()));
			str.append("',");
			str.append(num);
			str.append(",0,");
			str.append(r.nextInt(99999));
			str.append(",0,0,0),");

			curr.add(Calendar.DAY_OF_MONTH, 1);
		}

		String sql = str.toString();
		if ("".equalsIgnoreCase(sql)) {
			LOG.info("no sql");
			return;
		}
		sql = "insert into A_SHOP_DAY(Date,UserID,PV,UV,AvgTime,AvgPage,ExpandRate) values"
				+ sql.substring(0, sql.length() - 1);
		LOG.info(sql);

		Statement st = conn.createStatement();
		st.execute(sql);
		st.close();

	}

	public void insert() {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("");
			conn = DriverManager
					.getConnection(
							"jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8",
							"", "");
			for (int i = 1; i < 100; i++) {
				this.exec(conn, i);

				Thread.sleep(1000 * 3);
			}
		} catch (ClassNotFoundException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			LOG.error("", e);
			throw new RuntimeException(e);
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

	public static void main(String[] args) {
		new App2().insert();

	}

}

interface Exec {
	public void exec(Connection conn, int num) throws SQLException;
}
