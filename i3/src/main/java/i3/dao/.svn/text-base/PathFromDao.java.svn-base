package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.ShopFrom;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class PathFromDao extends SecKeyNickDao<ShopFrom.Key, ShopFrom> {

	private static final Logger LOG = LoggerFactory
			.getLogger(PathFromDao.class);

	public PathFromDao() {
		setStoreName("path_from");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(ShopFrom.Key.class, ShopFrom.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<ShopFrom>() {
			@Override
			public void execute(ShopFrom entity) {
				ShopFrom.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getFromID(),
						"" + entity.getPv(), "" + entity.getUv(),
						"" + entity.getPercentage() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
