package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class PathDao extends SecKeyNickDao<Path.Key, Path> {

	private static final Logger LOG = LoggerFactory.getLogger(PathDao.class);

	public PathDao() {
		setStoreName("path");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(Path.Key.class, Path.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Path>() {
			@Override
			public void execute(Path entity) {
				Path.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getType(), key.getId(),
						key.getSrcType(), key.getSrcId(), entity.getUrl(),
						entity.getTitle() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
