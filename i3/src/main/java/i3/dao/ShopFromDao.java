package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.ShopFrom;

import com.sleepycat.je.DatabaseException;

public class ShopFromDao extends SecKeyNickDao<ShopFrom.Key, ShopFrom> {

	public ShopFromDao() {
		setStoreName("shop_from");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(ShopFrom.Key.class, ShopFrom.class);
	}

}
