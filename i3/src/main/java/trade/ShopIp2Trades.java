package trade;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trade.ShopIp2Trades.Key;
import trade.ShopIp2Trades.ShopIpAndTrades;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;

import dao.bdb.BdbDao;
import dao.bdb.Exec;

public class ShopIp2Trades extends BdbDao<Key, ShopIpAndTrades> {

	private static final Logger LOG = LoggerFactory.getLogger(ShopIp2Trades.class);

	public ShopIp2Trades() {
		setPath("trades");
		setStoreName("trades");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(Key.class, ShopIpAndTrades.class);
	}

	@Persistent
	public static class Key {

		@KeyField(1)
		private String shop;

		@KeyField(2)
		private String ip;

		public Key() {

		}

		public Key(String shop, String ip) {
			this.shop = shop;
			this.ip = ip;
		}

		public String getShop() {
			return shop;
		}

		public void setShop(String shop) {
			this.shop = shop;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

	}

	@Entity
	public static class ShopIpAndTrades {

		@PrimaryKey
		private Key key;

		private Set<String> trades;

		public ShopIpAndTrades() {
			trades = new HashSet<String>();
		}

		public Key getKey() {
			return key;
		}

		public void setKey(Key key) {
			this.key = key;
		}

		public void setKey(String shop, String ip) {
			this.key = new Key(shop, ip);
		}

		public Set<String> getTrades() {
			return trades;
		}

		public void setTrades(Set<String> trades) {
			this.trades = trades;
		}

		public void addTrade(String tid) {
			this.trades.add(tid);
		}

	}

	public void list() {
		this.open();
		this.each(new Exec<ShopIpAndTrades>() {
			@Override
			public void execute(ShopIpAndTrades entity) {
				Key k = entity.getKey();
				LOG.info(k.getShop() + ", " + k.getIp());
				Set<String> trades = entity.getTrades();
				Iterator<String> it = trades.iterator();
				while (it.hasNext()) {
					LOG.info("  " + it.next());
				}
			}
		});
		this.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShopIp2Trades dao = new ShopIp2Trades();
		dao.setPath("20101026_trades");
		dao.list();

	}

}
