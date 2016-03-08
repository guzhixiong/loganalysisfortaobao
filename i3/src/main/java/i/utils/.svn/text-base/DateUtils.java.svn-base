package i.utils;

public class DateUtils {

	public static final String TIMESTAMP = "timestamp";

	private DateUtils() {
	}

	public static String format(String date, int hour, int minute, int second) {
		String timestamp = hour < 10 ? date + "0" : date;
		timestamp += hour;
		timestamp = minute < 10 ? timestamp + "0" : timestamp;
		timestamp += minute;
		timestamp = second < 10 ? timestamp + "0" : timestamp;
		timestamp += second;

		return timestamp;
	}

}
