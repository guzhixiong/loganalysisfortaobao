package i3.dao.ex;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.SecondaryIndex;

import dao.bdb.BdbDao;

public abstract class SecKeyNickDao<K, E> extends BdbDao<K, E> {

	private static final Logger LOG = LoggerFactory
			.getLogger(SecKeyNickDao.class);

	protected SecondaryIndex<String, K, E> nickKey;

	protected EntityCursor<E> nickCursor = null;

	@Override
	protected void init() throws DatabaseException {
		nickKey = store.getSecondaryIndex(pk, String.class, "nick");
	}

	public Iterator<E> iterateByNick(String nick) {
		try {
			nickCursor = nickKey.entities(nick, true, nick, true);
		} catch (DatabaseException e) {
			LOG.error("", e);
			throw e;
		}

		return nickCursor.iterator();
	}

	@Override
	public void fin() {
		if (nickCursor != null) {
			nickCursor.close();
		}
	}

}
