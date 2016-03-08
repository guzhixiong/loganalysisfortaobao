package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.ItemFrom;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class ItemFromDao extends SecKeyNickDao<ItemFrom.Key, ItemFrom> {

	private static final Logger LOG = LoggerFactory.getLogger(ItemFromDao.class);

	public ItemFromDao() {
		setStoreName("item_from");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(ItemFrom.Key.class, ItemFrom.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<ItemFrom>() {
			@Override
			public void execute(ItemFrom entity) {
				ItemFrom.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getNumIID(),
						key.getFromID(), "" + entity.getPv(),
						"" + entity.getUv(), "" + entity.getAvgTime(),
						"" + entity.getEntranceRate(),
						"" + entity.getBounceRate() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
