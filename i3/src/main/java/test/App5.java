package test;

import i.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class App5 {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		// String[] items = "妖精emma  \t 60.215.84.231".split("\\s");
		// LOG.info(items.length);
		//
		// String url =
		// "http://s8.taobao.com/search?q=&cat=162103&sort=credit-desc&style=grid&atype=b&olu=yes&isprepay=1&filterfineness=2&isrse=yes&ssid=s0&fs=1&unid=61843020401&mode=86&pid=mm_12576786_0_0&s=8";
		// int t = url.indexOf(".taobao.com");
		// LOG.info(t);
		//
		// FileUtils.copyDirectory(new File("res"), new File("res2"));
		// Thread.sleep(5000);
		// FileUtils.deleteDirectory(new File("res2"));
		//
		// String[] tbks = {
		// "http://s.click.taobao.com/t_js?tu=http%3a%2f%2fs.click.taobao.com%2ft_8%3fe%3d7hz5x%252bozdsp%252flmmuf5d0xe2r9sfxtwuvhxj3f1soqldbyvre73hgsg4irswbrc%252bkpfgp9uof6oj%252b%252bb5j06quzxwsn%252ffvrq%253d%253d%26p%3dmm_17255874_0_0%26n%3d19%26u%3d12120464%26ref%3dhttp%253a%252f%252fwww.buy.com.cn%252fbaobei-7159317074.html",
		// "http://s.click.taobao.com/t_js?tu=http%3a%2f%2fs.click.taobao.com%2ft_8%3fe%3d7hz5x%252fg7mp5by0awzzh8mbqwy2pbkwtbkulvco%252fbna4cjddypd%252f3njocpl%252bkb%252fin2sbgurxqwcx1xjhmjbj%252b8pf3kbzbctu12d%252fczsm8mxihn3dawec5f1q%253d%26c%3d4ffa882ff57a7f2cab44fdc8cdd1b201%26p%3dmm_12351394_0_0%26n%3d63%26u%3d0%26ref%3dhttp%253a%252f%252fs8.taobao.com%252fsearch%253fq%253d%2525d1%2525a7%2525cf%2525b0%2525bb%2525fa%2526pspuid%253d85827210%2526v%253dproduct%2526p%253ddetail%2526cat%253d50018628%2526pid%253dmm_12351394_0_0%2526unid%253d0%2526mode%253d63%2526navlog%253dmpcombo-1-title-85827210",
		// "http://s.click.taobao.com/t_js?tu=http%3a%2f%2fs.click.taobao.com%2ft_8%3fe%3d7hz5x%252bozdsp%252flmmuf5d0ugnyxq%253d%253d%26p%3dmm_21129415_0_0%26ref%3d"
		// };
		// for (String tbk : tbks) {
		// tbk = Utils.urlDecode(tbk);
		// System.out.println(tbk);
		//
		// int start = tbk.indexOf("ref=");
		// int end = tbk.indexOf("&", start);
		// end = end == -1 ? tbk.length() : end;
		// String ref = tbk.substring(start + 4, end);
		// ref = Utils.urlDecode(ref);
		// System.out.println(ref + "\n");
		// }
		String id = UUID.randomUUID().toString();
		System.out.println(id);

		String in = buildInputPath(3, args[0]);
		System.out.println(in);
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
