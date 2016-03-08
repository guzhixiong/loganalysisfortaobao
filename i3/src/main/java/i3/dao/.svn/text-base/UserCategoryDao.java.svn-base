package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.UserCategory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class UserCategoryDao extends
		SecKeyNickDao<UserCategory.Key, UserCategory> {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserCategoryDao.class);

	public UserCategoryDao() {
		setStoreName("user_category");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(UserCategory.Key.class, UserCategory.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<UserCategory>() {
			@Override
			public void execute(UserCategory entity) {
				UserCategory.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getCategoryURL(),
						entity.getCategoryTitle(), "" + entity.getPv(),
						"" + entity.getUv(), "" + entity.getAvgTime(),
						"" + entity.getEntranceRate(),
						"" + entity.getBounceRate() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
