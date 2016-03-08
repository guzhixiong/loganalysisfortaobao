package test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

import trade.ShopIp2Trades;
import trade.ShopIp2Trades.Key;
import trade.ShopIp2Trades.ShopIpAndTrades;

import com.sleepycat.je.DatabaseException;

import dao.bdb.BdbDao;

import i3.dao.CategoryDao;
import i3.dao.IndexDao;
import i3.dao.ItemFromDao;
import i3.dao.ex.SecKeyNickDao;
import i3.report.CategoryReport;

public class Test {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// String path = "2010-10-01--2010-10-31 18.04.05.xml";
		// String start = path.substring(0, 4) + path.substring(5, 7)
		// + path.substring(8, 10);
		// String end = path.substring(12, 16) + path.substring(17, 19)
		// + path.substring(20, 22);
		// String date = "20101111";
		// boolean bool = start.compareTo(date) <= 0 && end.compareTo(date) >=
		// 0;
		// if (bool) {
		// System.out.println(start + " < " + date + " < " + end);
		// } else {
		// System.out.println(start + ", " + end);
		// }

		// ShopIp2Trades sit = new ShopIp2Trades();
		// sit.setPath("20101026_trades");
		// sit.open();
		// ShopIpAndTrades siat = sit.find(new Key("沉默是金7282",
		// "222.70.137.10"));
		// if (siat == null) {
		// System.out.println("O_O");
		// } else {
		// System.out.println(siat.getTrades());
		// }
		// sit.close();

		// String date = "20101102";
		// File p = new File("/tmp/" + date + "_page_pv_uv");
		// if (!p.exists()) {
		// System.exit(-1);
		// }
		// File[] ps = p.listFiles(new FileFilter() {
		// @Override
		// public boolean accept(File f) {
		// return f.isFile() && f.getName().startsWith("part-r-");
		// }
		// });
		//
		// FileChannel out = new FileOutputStream(date + ".items").getChannel();
		// for (File f : ps) {
		// FileChannel in = new FileInputStream(f).getChannel();
		//
		// ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 4);
		// while (true) {
		// buffer.clear();
		// int count = in.read(buffer);
		// if (count == -1) {
		// break;
		// }
		// buffer.flip();
		// out.write(buffer);
		// }
		//
		// in.close();
		// }
		//
		// out.close();

		// nihaowo1

		CategoryReport report = new CategoryReport("20101227");
		report.doInsert();
	}
}
