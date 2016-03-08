package i3.dao;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import i3.entity.Shop;

import com.sleepycat.je.DatabaseException;

import dao.bdb.BdbDao;
import dao.bdb.Exec;

public class ShopDao extends BdbDao<String, Shop> {

	private static final Logger LOG = LoggerFactory.getLogger(ShopDao.class);

	public ShopDao() {
		setStoreName("shop");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(String.class, Shop.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Shop>() {
			@Override
			public void execute(Shop entity) {
				String[] tmp = { entity.getNick(), "" + entity.getPv(),
						"" + entity.getUv(), "" + entity.getAvgTime(),
						"" + entity.getAvgPage(), "" + entity.getExpandRate() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

	public void listHourly() {
		this.setStoreName("shop_hourly");
		this.open();
		this.each(new Exec<Shop>() {
			@Override
			public void execute(Shop shop) {
				String[] tmp = { shop.getNick(), "" + shop.getPv(),
						"" + shop.getUv(), "" + shop.getAvgTime(),
						"" + shop.getAvgPage(), "" + shop.getExpandRate(),
						shop.getRelevance() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}
}
