package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.Item;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class ItemDao extends SecKeyNickDao<Item.Key, Item> {

	private static final Logger LOG = LoggerFactory.getLogger(ItemDao.class);

	public ItemDao() {
		setStoreName("item");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(Item.Key.class, Item.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Item>() {
			@Override
			public void execute(Item entity) {
				Item.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getNumIID(),
						"" + entity.getPv(), "" + entity.getUv(),
						"" + entity.getAvgTime(),
						"" + entity.getEntranceRate(),
						"" + entity.getBounceRate() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
