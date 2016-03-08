package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.Category;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class CategoryDao extends SecKeyNickDao<Category.Key, Category> {

	private static final Logger LOG = LoggerFactory
			.getLogger(CategoryDao.class);

	public CategoryDao() {
		setStoreName("category");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(Category.Key.class, Category.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Category>() {
			@Override
			public void execute(Category entity) {
				Category.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getCategoryID(),
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
