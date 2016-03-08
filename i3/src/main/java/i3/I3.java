package i3;

import i.utils.IPHelper;
import i.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I3 {

	private static final Logger LOG = LoggerFactory.getLogger(I3.class);

	static String[] AGENT = new String[] { "IE+6.0", "IE+7.0", "IE+8.0",
			"Mozilla+Firefox", "Safari", "Chrome", "Opera" };

	static String[] OS = new String[] { "Windows", "Macintosh" };

	private String nick;

	private String ip;
	private String area;

	private String broswer;
	private String os;

	private String referPage;
	private String searchType;
	private String keyword;

	private String landingPage;
	private String pageType;
	private String pageID;
	private String fromType;

	private String item;

	private String landingPageTitle;

	private int hour;
	private int minute;
	private int second;

	private String tmpID;
	private String tmpName;

	public I3() {
	}

	public I3(String shop, String ip, String area, String broswer, String os,
			String referPage, String searchType, String keyword,
			String landingPage, String pageType, String pageID,
			String fromType, String item, String landingPageTitle, int hour,
			int minute, int second, String tmpID, String tmpName) {
		this.nick = shop;
		this.ip = ip;
		this.area = area;
		this.broswer = broswer;
		this.os = os;
		this.referPage = referPage;
		this.searchType = searchType;
		this.keyword = keyword;
		this.landingPage = landingPage;
		this.pageType = pageType;
		this.pageID = pageID;
		this.fromType = fromType;
		this.item = item;
		this.landingPageTitle = landingPageTitle;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.tmpID = tmpID;
		this.tmpName = tmpName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBroswer() {
		return broswer;
	}

	public void setBroswer(String broswer) {
		this.broswer = broswer;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getReferPage() {
		return referPage;
	}

	public void setReferPage(String referPage) {
		this.referPage = referPage;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getLandingPageTitle() {
		return landingPageTitle;
	}

	public void setLandingPageTitle(String landingPageTitle) {
		this.landingPageTitle = landingPageTitle;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public String getTmpID() {
		return tmpID;
	}

	public void setTmpID(String tmpID) {
		this.tmpID = tmpID;
	}

	public String getTmpName() {
		return tmpName;
	}

	public void setTmpName(String tmpName) {
		this.tmpName = tmpName;
	}

	@Override
	public String toString() {
		String[] attrs = new String[] { nick, ip, area, broswer, os, referPage,
				searchType, keyword, landingPage, pageType, pageID, fromType,
				item, landingPageTitle, "" + hour, "" + minute, "" + second,
				tmpID, tmpName };
		return StringUtils.join(attrs, " ");
	}

	public void fromString(String i3) throws RuntimeException {
		String[] attrs = i3.split(" ");
		if (attrs.length < 17) {
			throw new RuntimeException("some data item lost\n" + i3);
		}

		nick = attrs[0];

		ip = attrs[1];
		area = attrs[2];

		broswer = attrs[3];
		os = attrs[4];

		referPage = attrs[5];
		searchType = attrs[6];
		keyword = attrs[7];

		landingPage = attrs[8];
		pageType = attrs[9];
		pageID = attrs[10];

		fromType = attrs[11];

		item = attrs[12];

		landingPageTitle = attrs[13];

		try {
			hour = Integer.parseInt(attrs[14]);
			minute = Integer.parseInt(attrs[15]);
			second = Integer.parseInt(attrs[16]);
		} catch (NumberFormatException e) {
			throw new RuntimeException("wrong time\n" + hour + ":" + minute
					+ ":" + second);
		}

		if (attrs.length >= 19) {
			tmpID = attrs[17];
			tmpName = attrs[18];
		}

		difot();
	}

	public static String nil(String str, String difot) {
		return str == null || str.equals("") ? difot : str;
	}

	public static String nil(String str) {
		return nil(str, "-");
	}

	public void parse(IPHelper ipHelper, String log) throws IOException,
			RuntimeException {
		if (log.startsWith("#")) {
			throw new RuntimeException("sharp start line");
		}
		String[] items = log.split(" ");
		if (9 > items.length) {
			throw new RuntimeException("log items not enough");
		}

		nick = items[2].toLowerCase();

		ip = items[3];
		if (ipHelper == null) {
			area = IPHelper.UNKNOW_AREA;
		} else {
			area = ipHelper.getGeo(ip);
		}

		broswer = sign(I3.AGENT, items[4]);
		os = sign(I3.OS, items[4]);

		landingPage = items[5].toLowerCase();
		String[] tmp = parseTypeAndID(landingPage);
		pageType = tmp[0];
		pageID = tmp[1];

		referPage = items[6].toLowerCase();
		parseSearch();

		fromType = parseFromType(landingPage, referPage);

		item = items[7];

		landingPageTitle = items[8];

		String datetime = items[0] + " " + items[1];
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = f.parse(datetime);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			second = c.get(Calendar.SECOND);
		} catch (ParseException e) {
			throw new RuntimeException("wrong time\n" + datetime);
		}

		if (items.length >= 11) {
			tmpID = items[9];
			tmpName = items[10];
		}

		difot();
	}

	private void difot() {
		nick = nil(nick);
		ip = nil(ip);
		area = nil(area, IPHelper.UNKNOW_AREA);
		broswer = nil(broswer);
		os = nil(os);
		referPage = nil(referPage);
		searchType = nil(searchType, "0");
		keyword = nil(keyword);
		landingPage = nil(landingPage);
		pageType = nil(pageType, "0");
		pageID = nil(pageID);
		fromType = nil(fromType, "-1");
		item = nil(item);
		landingPageTitle = nil(landingPageTitle);
		tmpID = nil(tmpID);
		tmpName = nil(tmpName);
	}

	private String sign(String[] words, String line) {
		String word = "Others";
		for (int i = 0; i < words.length; i++) {
			if (line.toLowerCase().indexOf(words[i].toLowerCase()) > -1) {
				word = words[i];
				break;
			}
		}
		return word;
	}

	/**
	 * 其它0，首页1，分类2，商品3，自定义4<br />
	 * 
	 * 商品：item.htm?id= 或者 item_detail<br />
	 * 分类：search-cat- 或者 catId<br />
	 * 自定义：view_page-
	 * 
	 * @param url
	 * @return
	 */
	public static String[] parseTypeAndID(String landing) {
		String pageType = "0";
		String pageID = "";

		String url = landing.toLowerCase();
		if (url.indexOf("item.htm?") > -1
				|| url.indexOf("item_detail.htm?") > -1) {
			pageType = "3";
			pageID = Utils
					.find(".+item(?:_detail)?\\.htm\\?(?:%20|\\+)*(?:[^&]+&)?(?:id|item_id|itemid|item_num_id)=([\\d]*)(?:[&+%].*)?",
							url);
		} else if (url.indexOf("search=y") > -1
				&& url.indexOf("queryType=cat") > -1
				&& url.indexOf("scid=") > -1) {
			pageType = "2";
			pageID = Utils.find(
					".+\\.taobao\\.com/\\?(?:.+&)?scid=([^&]+)(?:&.+)?", url);
		} else if (url.indexOf("search-cat-") > -1) {
			pageType = "2";
			pageID = Utils.find(".+/search-cat-(?:[\\d]+)-([\\d]+)-.+", url);
		} else if (url.indexOf("catid") > -1) {
			pageType = "2";
			pageID = Utils.find(
					".+\\.taobao\\.com/\\?(?:.+&)?catid=([^&]+)(?:&.+)?", url);
		} else if (Pattern.matches("http://shop([\\d]+)\\.taobao\\.com[/]?",
				url)) {
			pageType = "1";
			pageID = Utils.find("http://shop([\\d]+)\\.taobao\\.com[/]?", url);
		} else if (Pattern.matches("http://([^.]+)\\.taobao\\.com[/]?", url)) {
			pageType = "1";
			pageID = Utils.find("http://([^.]+)\\.taobao\\.com[/]?", url);
		} else if (url.indexOf("view_page-") > -1) {
			pageType = "4";
			pageID = Utils.find(".+\\.taobao\\.com/view_page\\-([\\w]+)\\.htm",
					url);
		}

		pageID = null == pageID ? "" : pageID;

		return new String[] { pageType, pageID };
	}

	public static String parseFromType(String landing, String refer) {
		String page1 = landing.toLowerCase();
		String page0 = refer.toLowerCase();

		String pageFrom = "-1";
		if ((page0.startsWith("http://search.taobao.com/") || page0
				.startsWith("http://www.taobao.com/"))
				&& page1.indexOf("ali_refid=") > -1) {
			pageFrom = "15";
		} else if (page0.startsWith("http://search.taobao.com/")
				|| page0.startsWith("http://s.taobao.com/search?")) {
			pageFrom = "2";
		} else if (page0.startsWith(" http://list.taobao.com/")) {
			pageFrom = "3";
		} else if (page0.startsWith("http://shopsearch.taobao.com/")) {
			pageFrom = "4";
		} else if (page0.startsWith("http://bbs.taobao.com/")) {
			pageFrom = "5";
		} else if (page0.startsWith("http://bangpai.taobao.com/")) {
			pageFrom = "6";
		} else if (page0.startsWith("http://poster.taobao.com")) {
			pageFrom = "7";
		} else if (page0.startsWith("http://favorite.taobao.com/")) {
			pageFrom = "8";
		} else if (page0.startsWith("http://buy.taobao.com/auction/")
				&& page1.startsWith("http://item.taobao.com/item.htm?")) {
			pageFrom = "10";
		} else if (page0.startsWith("http://trade.taobao.com/")) {
			pageFrom = "11";
		} else if (page0
				.startsWith("http://jianghu.taobao.com/admin/plugin.htm?")) {
			pageFrom = "12";
		} else if (page0.startsWith("http://fenxiang.taobao.com")) {
			pageFrom = "13";
		} else if (Utils.find("(http://[^/:]+:\\d+/auth/).+", page0) != null) {
			pageFrom = "14";
		} else if (page0.startsWith("http://s.click.taobao.com/t_js?")) {
			String ref = Utils.urlDecode(page0);
			ref = Utils.urlDecodeParamRef(ref);
			if (ref.indexOf(".taobao.com") == -1
					&& Utils.urlDecodeParamRef(page1).equalsIgnoreCase(ref)) {
				pageFrom = "16";
			}
		} else if (page0.startsWith("http://z.alimama.com/alimama.php?")
				&& page1.indexOf("ali_trackid=8_") > -1) {
			pageFrom = "17";
		} else if (page0.startsWith("http://cn2.adserver.yahoo.com")) {
			pageFrom = "18";
		} else if (page0.startsWith("http://click.mz.simba.taobao.com/cc_mt?")) {
			pageFrom = "19";
		} else if (page0.startsWith("http://www.taobao.com/go/chn/in/")) {
			pageFrom = "20";
		} else if (page0.startsWith("http://www.baidu.com/s?")) {
			pageFrom = "21";
		} else if (page0.startsWith("http://www.google.cn/products?")) {
			pageFrom = "22";
		} else if (page0.startsWith("http://www.google.com.hk/search?")) {
			pageFrom = "23";
		} else if (page0.startsWith("http://www.soso.com/q?")) {
			pageFrom = "24";
		} else if (page0.startsWith("http://one.cn.yahoo.com/s?")) {
			pageFrom = "25";
		} else if (page0.startsWith("http://www.sogou.com/web?")) {
			pageFrom = "26";
		} else if (page0.startsWith("http://www.youdao.com/search?")) {
			pageFrom = "27";
		} else if (page0.equalsIgnoreCase("-")) {
			pageFrom = "28";
		} else if (page0.indexOf(".taobao.com") == -1) {
			pageFrom = "30";
		} else if (page0.indexOf(".taobao.com") > -1) {
			if (page0.startsWith("http://item.taobao.com/")) {
				// 粗略地认为是自己店铺内的
			} else if (Utils.domain(page0)
					.equalsIgnoreCase(Utils.domain(page1))) {
				// 依旧是粗略地认为是自己店铺内的。因为，同一个店铺的domain，可能是默认的shop123，也可能是自定义的
			} else if (Utils
					.find("(http://(?:shop[\\w-]+|item|([^\\.]+\\.)?mall|store)\\.taobao\\.com)(?:/.*)?",
							page0) != null) {
				pageFrom = "9";
			} else {
				pageFrom = "31";
			}
		} else {
			if (LOG.isDebugEnabled())
				LOG.debug("unknow from: " + page1 + ", " + page0);
		}

		return pageFrom;
	}

	/**
	 * 0非搜索，1淘宝搜索，2店内搜索
	 * 
	 * @param referPage
	 * @return
	 */
	private void parseSearch() {
		boolean taobao = referPage.indexOf(".taobao.com/") > -1;
		if (!taobao || taobao && referPage.indexOf("keyword=") == -1
				&& referPage.indexOf("searchWord=") == -1
				&& referPage.indexOf("/search?") == -1
				&& referPage.indexOf("?q=") == -1
				&& referPage.indexOf("&q=") == -1) {
			searchType = "0";
			return;
		}

		if (referPage.startsWith("http://s.taobao.com")
				|| referPage.startsWith("http://search.taobao.com")
				|| referPage.startsWith("http://shopsearch.taobao.com")
				|| referPage.startsWith("http://re.taobao.com")) {
			searchType = "1";
		} else {
			searchType = "2";
		}

		keyword = Utils.find(
				".+\\?(?:.+&)?(?:q|searchWord|keyword)=([^&]*)(?:&.*)?",
				referPage);
		if (keyword == null) {
			if (LOG.isDebugEnabled())
				LOG.info("unknown search: " + referPage);
			keyword = "";
			searchType = "0";
		} else if ("".equals(keyword)) {
			if (LOG.isDebugEnabled())
				LOG.info("no keyword in search" + referPage);
			searchType = "0";
		}
	}

	public static void main(String[] args) {
		String[] urls = {
				"http://woaixiaohuamao.taobao.com/?search=y&scid=292398680&queryType=cat&scname=tffA7bC0xKY%3D",
				"http://woaixiaohuamao.taobao.com/?search=y&scid=292398670&scname=JmhlYXJ0czu7pLf0wOAgILv5sb6yvdboICZoZWFydHM7&checkedRange=true&queryType=cat",
				"http://woaixiaohuamao.taobao.com/?search=y&scid=292413397&scname=jmhlyxj0czsg1tdr%2bsag0khr%2bsamagvhcnrzow%3d%3d&checkedrange=true&querytype=cat" };
		for (String url : urls) {
			String[] typeAndID = I3.parseTypeAndID(url);
			LOG.info("[{}]", StringUtils.join(typeAndID, ", "));
		}
	}

}
