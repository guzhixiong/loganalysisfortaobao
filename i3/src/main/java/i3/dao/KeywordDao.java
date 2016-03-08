package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.Keyword;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class KeywordDao extends SecKeyNickDao<Keyword.Key, Keyword> {

	private static final Logger LOG = LoggerFactory.getLogger(KeywordDao.class);

	public KeywordDao() {
		setStoreName("keyword");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(Keyword.Key.class, Keyword.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Keyword>() {
			@Override
			public void execute(Keyword entity) {
				Keyword.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getKeyword(),
						"" + entity.getPv(), "" + entity.getUv() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
