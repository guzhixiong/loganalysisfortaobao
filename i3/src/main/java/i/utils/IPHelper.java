package i.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPHelper {

	private static final Logger LOG = LoggerFactory.getLogger(IPHelper.class);

	public static final String IP_PATH = "/opt/i/ip";
	public static final String IP_DATA_FILE_NAME = "ip.txt";
	public static final String IP_META_FILE_NAME = "_" + IP_DATA_FILE_NAME;
	public static final String IP_DATA_FILE = IP_PATH + File.separatorChar
			+ IP_DATA_FILE_NAME;
	public static final String IP_META_FILE = IP_PATH + File.separatorChar
			+ IP_META_FILE_NAME;

	public static final String UNKNOW_AREA = "29,249";

	public static void buildCache(String driver, String url, String user,
			String pass, String path, String data, String meta)
			throws ClassNotFoundException, SQLException, IOException {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt
				.executeQuery("select id,start_ip,end_ip,province_id,city_id from SYS_IP");

		new File(path).mkdirs();

		int lines = 0;
		int bigest = 0;

		File metaFile = new File(path + File.separator + meta);
		FileChannel channel = new FileOutputStream(metaFile).getChannel();
		ByteBuffer buffer = null;
		while (rs.next()) {
			long id = rs.getLong(1);
			long startIp = rs.getLong(2);
			long endIp = rs.getLong(3);
			String province = rs.getString(4);
			String city = rs.getString(5);

			String line = "" + id + " " + startIp + " " + endIp + " "
					+ province + " " + city;

			int tmp = line.length();
			bigest = tmp > bigest ? tmp : bigest;
			lines++;

			buffer = ByteBuffer.wrap((line + "\r\n").getBytes());
			channel.write(buffer);
		}
		channel.close();

		rs.close();
		stmt.close();

		File dataFile = new File(path + File.separator + data);
		channel = new FileOutputStream(dataFile).getChannel();
		BufferedReader reader = new BufferedReader(new FileReader(metaFile));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = StringUtils.rightPad(line, bigest, ' ');
			channel.write(ByteBuffer.wrap(line.getBytes()));
		}
		reader.close();
		channel.close();

		channel = new FileOutputStream(metaFile).getChannel();
		channel.write(ByteBuffer.wrap(("" + lines + " " + bigest).getBytes()));
		channel.close();
	}

	public static void buildCache(String path, String data, String meta)
			throws ClassNotFoundException, SQLException, IOException {

		String url = "jdbc:mysql://192.168.1.181:3306/NBDR?useUnicode=true&characterEncoding=utf8";
		buildCache("com.mysql.jdbc.Driver", url, "dc", "dc", path,
				data, meta);
	}

	public static void buildCache() throws ClassNotFoundException,
			SQLException, IOException {
		buildCache(IP_PATH, IP_DATA_FILE_NAME, IP_META_FILE_NAME);
	}

	public static final IPHelper initCache(String data, String meta,
			RandomAccessFile file) throws IOException {
		System.out.println("initializing...");

		IPHelper helper = new IPHelper();
		helper.file = file;

		File ipFile = new File(data);
		File metaFile = new File(meta);
		if (ipFile.exists() && metaFile.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(metaFile));
			String[] tmp = reader.readLine().trim().split(" ");
			reader.close();

			helper.lines = Integer.parseInt(tmp[0]);
			helper.bigest = Integer.parseInt(tmp[1]);
		} else {
			throw new FileNotFoundException("Could not found the ip files: ["
					+ data + "] and [" + meta + "]");
		}

		return helper;
	}

	public static final IPHelper initCache(RandomAccessFile file)
			throws IOException {
		String data = IP_PATH + File.separator + IP_DATA_FILE_NAME;
		String meta = IP_PATH + File.separator + IP_META_FILE_NAME;
		return initCache(data, meta, file);
	}

	private RandomAccessFile file = null;

	private int lines = 0;
	private int bigest = 0;

	private IPHelper() {

	}

	public int getLines() {
		return lines;
	}

	public int getBigest() {
		return bigest;
	}

	public final String getGeo(String ip) throws IOException {
		long intIp = ipToInt(ip);
		return search(intIp);
	}

	private final long ipToInt(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		String[] fields = new String[4];
		fields[0] = st.nextToken();
		fields[1] = st.nextToken();
		fields[2] = st.nextToken();
		fields[3] = st.nextToken();

		long value = (Long.parseLong(fields[0]) << 24)
				+ (Integer.parseInt(fields[1]) << 16)
				+ (Short.parseShort(fields[2]) << 8)
				+ Short.parseShort(fields[3]);

		st = null;
		fields[0] = null;
		fields[1] = null;
		fields[2] = null;
		fields[3] = null;
		fields = null;

		return value;
	}

	public String search(long key) {
		try {
			int low = 0;
			int high = lines - 1;
			while (low <= high) {
				int mid = (low + high) / 2;

				String[] values = get(mid, file);

				long key1 = Long.valueOf(values[1]);
				long key2 = Long.valueOf(values[2]);

				if (key >= key1 && key <= key2) {
					return StringUtils.join(
							new String[] { values[3], values[4] }, ",");
				} else if (key < key1) {
					high = mid - 1;
				} else {
					low = mid + 1;
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}

		return UNKNOW_AREA;
	}

	private String[] get(int line, RandomAccessFile file) throws IOException {
		int pos = line * bigest;
		file.seek(pos);

		byte[] buf = new byte[bigest];
		if (file.read(buf) == bigest) {
			String values = new String(buf, 0, bigest);

			// System.out.println(values);
			return values.split(" ");
		}

		return new String[] { "-1", "-1" };
	}

	public static void main(String args[]) throws SQLException,
			ClassNotFoundException, IOException, InterruptedException {
		if (args.length == 0) {
			System.out.println("build [path]");
			System.out.println("search [path] ip");
			System.out.println("    default path are all current;");
		} else if (args[0].equalsIgnoreCase("build")) {
			String path = ".";
			if (args.length == 2) {
				path = args[1];
			}

			IPHelper.buildCache(path, IP_DATA_FILE_NAME, IP_META_FILE_NAME);
		} else if (args[0].equalsIgnoreCase("search")) {
			String path = IP_PATH;
			String ip = "";
			if (args.length == 2) {
				ip = args[1];
			} else if (args.length == 3) {
				path = args[1];
				ip = args[2];
			}

			if (args.length == 3 || args.length == 2) {
				path = path + File.separatorChar + IP_DATA_FILE_NAME;
				RandomAccessFile file = new RandomAccessFile(path, "r");

				IPHelper helper = IPHelper.initCache(file);
				System.out.println("lines: " + helper.getLines() + ", bigest:"
						+ helper.getBigest());

				String geo = helper.getGeo(ip);
				System.out.println(ip + " [" + geo + "]");

				file.close();
			}
		}

		System.out.println("Done.");
	}

}
