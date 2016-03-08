package i.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.bdb.BdbDao;

public class Utils {

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	private Utils() {
	}

	public static int second(String hour, String minute, String second) {
		return Integer.parseInt(hour) * 3600 + Integer.parseInt(minute) * 60
				+ Integer.parseInt(second);
	}

	public static String urlDecode(String str, String encoding) {
		if (null == str) {
			return null;
		} else if (find(".*((?:%[0-9a-zA-Z]{2})+){2}.*", str) == null) {
			return str;
		}

		try {
			String decoded = new String(URLCodec.decodeUrl(str.getBytes()),
					encoding);
			decoded = new String(decoded.getBytes(), "utf-8");
			return decoded.trim().replaceAll("\\s{1,}", "+");
		} catch (UnsupportedEncodingException e) {
			LOG.error("", e);
			return str;
		} catch (DecoderException e) {
			LOG.error(str, e);
			return str;
		}
	}

	public static String urlDecode(String str) {
		return urlDecode(str, "gbk");
	}

	public static String urlDecodeParamRef(String url) {
		int start = url.indexOf("ref=");
		if (start == -1) {
			return null;
		}
		int end = url.indexOf("&", start);
		end = end == -1 ? url.length() : end;
		String ref = urlDecode(url.substring(start + 4, end));
		ref = ref.trim().replace(" ", "%20");
		return ref;
	}

	public static String domain(String url) {
		if (!url.startsWith("http://"))
			return null;
		int end = url.indexOf("/", 7);
		end = end == -1 ? url.length() : end;
		return url.substring(0, end);
	}

	public static String ztcKeyword(String url) {
		int start = url.indexOf("&ali_refid=");
		start = start == -1 ? url.indexOf("?ali_refid=") : start;
		if (start == -1) {
			if (LOG.isDebugEnabled())
				LOG.debug("unknown search ZTC: " + url);
			return null;
		}

		int end = url.indexOf("&", start + 11);
		end = end == -1 ? url.length() : end;

		String keyword = url.substring(start + 11, end);
		String[] tmp = keyword.split(":");
		if (tmp.length != 5) {
			if (LOG.isDebugEnabled())
				LOG.info("unknown search ZTC: " + url);
			return null;
		}
		keyword = urlDecode(tmp[3]);
		keyword = keyword.replace(" ", "+");

		return keyword;
	}

	public static String find(String re, String in) {
		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(in);
		String found = null;
		if (m.matches()) {
			found = m.group(1);
		}
		return found;
	}

	@SuppressWarnings("rawtypes")
	public static void openDao(String subPath, BdbDao dao) throws IOException {
		String base = dao.getBase();
		String path = dao.getPath();
		String f = base + File.separator + path + "_" + subPath;
		FileUtils.copyDirectory(new File(base + File.separator + path),
				new File(f));
		dao.setPath(path + "_" + subPath);
		dao.open(true);
	}

	@SuppressWarnings("rawtypes")
	public static void openDaoInMap(Context context, BdbDao dao)
			throws IOException {
		openDao(context.getTaskAttemptID().toString(), dao);
	}

	public static void main(String[] args) {
		String k = "%99C+%C6%F7%C8%CB";
		String x = Utils.urlDecode(k);
		LOG.info(x);

		String url = "http://item.taobao.com/item.htm?id=108660518&ali_refid=a3_420434_1006:1102379795:6:%CB%AB%BF%A8%CB%AB%B4%FD:9c98ed82514c5f80e772e6a201e62dfd&ali_trackid=1_9c98ed82514c5f80e772e6a201e62dfd";
		String keyword = ztcKeyword(url);
		LOG.info(keyword);
	}

	public static String buildInputPath(int days, String date) {
		List<String> abc = new ArrayList<String>();
		try {
			SimpleDateFormat p = new SimpleDateFormat("yyyyMMdd");
			Date d = p.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, -days);
			c.set(Calendar.HOUR_OF_DAY, 23);

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHH");
			for (int i = 0; i < 24 * days; i++) {
				c.add(Calendar.HOUR_OF_DAY, 1);
				abc.add("data/" + f.format(c.getTime()) + "_a");
			}

			return StringUtils.join(abc.toArray(), ",");
		} catch (ParseException e) {
			throw new RuntimeException("wrong date");
		}
	}

}
