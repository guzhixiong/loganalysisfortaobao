package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.IndexFrom;

import com.sleepycat.je.DatabaseException;

public class IndexFromDao extends SecKeyNickDao<IndexFrom.Key, IndexFrom> {

	public IndexFromDao() {
		setStoreName("index_from");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(IndexFrom.Key.class, IndexFrom.class);
	}

}
