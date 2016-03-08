package i.utils;

import org.apache.commons.lang.StringUtils;

public class DbUtils {

	// 10.10.1.2 NBDR_DA DA2e4S6z
	// 192.168.1.171 NBDR NBDR

	private DbUtils() {
	}

	public static String buildUrl(String protocol, String host, String port,
			String db, String options) {
		return protocol + "://" + host + ":" + port + "/" + db + "?" + options;
	}

	public static String sqlEscape(String field) {
		if (field == null)
			return "null";
		return "'" + field.replace("'", "''").replace("\\", "\\\\") + "'";
	}

	public static String sqlDate(String ymdhms) {
		String ymd = StringUtils.rightPad(ymdhms, 14, '0');
		ymd = ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-"
				+ ymd.substring(6, 8) + " " + ymd.substring(8, 10) + ":"
				+ ymd.substring(10, 12) + ":" + ymd.substring(12, 14);
		return ymd;
	}

}