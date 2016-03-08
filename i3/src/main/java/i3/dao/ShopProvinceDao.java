package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.ShopProvince;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class ShopProvinceDao extends
		SecKeyNickDao<ShopProvince.Key, ShopProvince> {

	private static final Logger LOG = LoggerFactory
			.getLogger(ShopProvinceDao.class);

	public ShopProvinceDao() {
		setStoreName("shop_province");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(ShopProvince.Key.class, ShopProvince.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<ShopProvince>() {
			@Override
			public void execute(ShopProvince entity) {
				ShopProvince.Key key = entity.getKey();
				String[] tmp = { key.getNick(), "" + key.getProvince(),
						"" + entity.getPv(), "" + entity.getUv(),
						"" + entity.getAvgTime(), "" + entity.getAvgPage() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
