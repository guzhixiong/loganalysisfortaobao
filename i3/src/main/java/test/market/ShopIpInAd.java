package test.market;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trade.ShopIp2Trades.Key;
import trade.ShopIp2Trades.ShopIpAndTrades;

public class ShopIpInAd {

	private static final Logger LOG = LoggerFactory.getLogger(ShopIpInAd.class);

	private String uri = "hdfs://10.10.1.9:9000/";

	private String path = "/user/root/tradeHistory/";

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	// @SuppressWarnings("unchecked")
	// private void add(String shop, String date, InputStream xml) {
	// Document doc = null;
	// SAXBuilder builder = new SAXBuilder();
	// try {
	// doc = builder.build(xml);
	// XPath xpath = XPath.newInstance("/ArrayOfTrade/Trade");
	// List<Element> trades = (List<Element>) xpath.selectNodes(doc
	// .getRootElement());
	// LOG.debug(trades.size() + " trades");
	//
	// for (Element trade : trades) {
	// String created = trade.getChildText("created");
	// if (!created.replace("-", "").startsWith(date)) {
	// continue;
	// }
	//
	// String buyerIp = trade.getChildText("buyer_ip");
	// String ip = buyerIp;
	// if (!buyerIp
	// .matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
	// ip = new String(Base64.decodeBase64(ip.getBytes()));
	// }
	// LOG.debug(buyerIp + " " + ip);
	//
	// String tid = trade.getChildText("tid");
	//
	// ShopIpAndTrades sit = si2t.find(new Key(shop, ip));
	// if (sit == null) {
	// sit = new ShopIpAndTrades();
	// sit.setKey(shop, ip);
	// }
	// sit.addTrade(tid);
	//
	// si2t.add(sit);
	//
	// float payment = 0;
	// int num = 0;
	// try {
	// payment = Float.parseFloat(trade.getChildText("payment"));
	// List<Element> orders = trade.getChild("orders")
	// .getChildren("order");
	// for (Element order : orders) {
	// num += Integer.parseInt(order.getChildText("num"));
	// }
	// } catch (Exception e) {
	// LOG.error("", e);
	// continue;
	// }
	// Trade entity = new Trade();
	// entity.setTid(tid);
	// entity.setShop(shop);
	// entity.setIp(ip);
	// entity.setPayment(payment);
	// entity.setNum(num);
	//
	// all.add(entity);
	// String status = trade.getChildText("status").toUpperCase();
	// if (status.equals("WAIT_BUYER_PAY")) {
	// ordered.add(entity);
	// } else if (status.equals("WAIT_SELLER_SEND_GOODS")
	// || status.equals("WAIT_BUYER_CONFIRM_GOODS")
	// || status.equals("TRADE_BUYER_SIGNED")
	// || status.equals("TRADE_FINISHED")) {
	// payed.add(entity);
	// }
	// }
	// } catch (JDOMException e) {
	// LOG.error("", e);
	// throw new RuntimeException(e);
	// } catch (IOException e) {
	// LOG.error("", e);
	// throw new RuntimeException(e);
	// }
	// }

	// public void addAll(final String date) throws IOException {
	// ordered = new TradeDao();
	// ordered.setPath(date + "_trade_order");
	// payed = new TradeDao();
	// payed.setPath(date + "_trade_pay");
	// all = new TradeDao();
	// all.setPath(date + "_trade_all");
	//
	// ordered.open();
	// payed.open();
	// all.open();
	//
	// si2t = new ShopIp2Trades();
	// si2t.setPath(date + "_" + si2t.getPath());
	//
	// si2t.open();
	//
	// Configuration conf = new Configuration();
	// FileSystem fs = FileSystem.get(URI.create(uri), conf);
	//
	// Path p = new Path(path);
	// FileStatus[] shops = fs.listStatus(p);
	// for (FileStatus shop : shops) {
	// Path p1 = shop.getPath();
	//
	// LOG.debug(p1.getName());
	//
	// FileStatus[] trades = fs.listStatus(p1, new PathFilter() {
	// @Override
	// public boolean accept(Path path) {
	// String name = path.getName().replace("-", "");
	// return name.indexOf(date) > -1;
	// }
	// });
	//
	// for (FileStatus trade : trades) {
	// Path p2 = trade.getPath();
	// LOG.debug("  " + p2.toString());
	//
	// InputStream in = fs.open(p2);
	// add(p1.getName(), date, in);
	// IOUtils.closeStream(in);
	// }
	// }
	//
	// ordered.close();
	// payed.close();
	// all.close();
	// si2t.close();
	// }

	// public static void main(String[] args) {
	// try {
	// ShopIpInAd at = new ShopIpInAd();
	// at.addAll(args[0]);
	// } catch (Exception e) {
	// System.exit(-1);
	// }
	// }

}
