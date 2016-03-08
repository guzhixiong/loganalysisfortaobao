package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.ItemKeyword;

import com.sleepycat.je.DatabaseException;

public class ItemKeywordDao extends SecKeyNickDao<ItemKeyword.Key, ItemKeyword> {

	public ItemKeywordDao() {
		setStoreName("item_keyword");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(ItemKeyword.Key.class, ItemKeyword.class);
	}

}
