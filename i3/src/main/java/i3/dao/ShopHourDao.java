package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.ShopHour;

import com.sleepycat.je.DatabaseException;

public class ShopHourDao extends SecKeyNickDao<ShopHour.Key, ShopHour> {

	public ShopHourDao() {
		setStoreName("shop_hour");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(ShopHour.Key.class, ShopHour.class);
	}

}
