package i3.market.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.market.entity.MarketFlux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.phprpc.util.PHPSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class MarketFluxDao extends SecKeyNickDao<MarketFlux.Key, MarketFlux> {

	private static final Logger LOG = LoggerFactory
			.getLogger(MarketFluxDao.class);

	public MarketFluxDao(String date) {
		setPath("market_flux/" + date);
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(MarketFlux.Key.class, MarketFlux.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<MarketFlux>() {
			@Override
			public void execute(MarketFlux entity) {
				MarketFlux.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getIp(),
						entity.getFroms().toString(),
						entity.getKeywords().toString() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

	public static void main(String[] args) throws ParseException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IOException {
		// 20110212 七月舞华 龙藤通讯
		// 20110215 太子鞋坊
		// follower("龟龟lovezhuzhu", "20110212");

		// 20110212 龙购 七格格吉祥
		// 20110215 太平鸟官方专营店
		// for (int i = 0; i < 1; i++) {
		// }
		// someone("�'b8�刀玉蝴蝶", "20110212", 1);

		LOG.info("{}", Integer.MAX_VALUE);
	}

	public static void someone(final String nick, final String date, int repeat)
			throws ParseException, IllegalAccessException,
			InvocationTargetException, FileNotFoundException, IOException {
		MarketFluxDao dao = new MarketFluxDao(date);
		dao.open();

		final Date current = new SimpleDateFormat("yyyyMMdd").parse(date);
		final List<XiaoAi.Moudle.Flux.MarketFlux> fluxList = new ArrayList<XiaoAi.Moudle.Flux.MarketFlux>();

		LOG.info("nick: {}", nick);

		final AtomicInteger count = new AtomicInteger(0);
		Iterator<MarketFlux> it = dao.iterateByNick(nick);
		while (it.hasNext()) {
			MarketFlux entity = it.next();
			// LOG.info("{} {} {}", new String[] { entity.getKey().getIp(),
			// entity.getKeywords().toString(),
			// entity.getFroms().toString() });

			XiaoAi.Moudle.Flux.MarketFlux flux = new XiaoAi.Moudle.Flux.MarketFlux();
			flux.setIP(entity.getKey().getIp());
			flux.setTime(current);
			flux.setListFrom(entity.getFroms());
			flux.setListKeyWord(entity.getKeywords());
			fluxList.add(flux);

			count.incrementAndGet();
		}
		LOG.info("count: {}", count.get());

		FileOutputStream out = null;
		try {
			byte[] php = new PHPSerializer().serialize(fluxList);
			LOG.info("bytes: {}", php.length);

			String path = "d:/data/someone/" + nick + "/" + date;
			File p = new File(path);
			p.mkdirs();

			out = new FileOutputStream(path + "/" + repeat);
			out.write(php);

			php = null;
		} finally {
			if (out != null) {
				out.close();
			}
		}

		dao.close();
	}

	public static void follower(final String nick, String date)
			throws ParseException {
		MarketFluxDao dao = new MarketFluxDao(date);
		dao.open();

		Iterator<MarketFlux> itByNick = dao.iterateByNick(nick);
		if (!itByNick.hasNext()) {
			LOG.info("{} is not exists", nick);
			return;
		}

		final AtomicInteger count = new AtomicInteger(0);

		String _nick = "";

		boolean found = false;
		Iterator<MarketFlux> it = dao.iterator();
		while (it.hasNext()) {
			count.incrementAndGet();

			MarketFlux entity = it.next();
			_nick = entity.getNick();
			LOG.info("{}: {}, {}", new String[] { "" + count, _nick,
					entity.getKey().getIp() });

			if (!_nick.equals(nick)) {
				if (!found) {
					continue;
				} else {
					break;
				}
			} else {
				found = true;
			}
		}
		LOG.info("count: {}; found: {}", count.get(), found);

		LOG.info("{} follow {}", nick, _nick);

		dao.close();
	}

}
