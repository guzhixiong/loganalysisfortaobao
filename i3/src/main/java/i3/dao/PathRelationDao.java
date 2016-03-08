package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.PathRelation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class PathRelationDao extends
		SecKeyNickDao<PathRelation.Key, PathRelation> {

	private static final Logger LOG = LoggerFactory.getLogger(PathRelationDao.class);

	public PathRelationDao() {
		setStoreName("path_relation");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(PathRelation.Key.class, PathRelation.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<PathRelation>() {
			@Override
			public void execute(PathRelation entity) {
				PathRelation.Key key = entity.getKey();
				String[] tmp = { key.getNick(), key.getFromID(), key.getType(),
						key.getId(), entity.getUrl(), "" + entity.getPv(),
						"" + entity.getUv(), "" + entity.getBounceRate(),
						"" + entity.getPercentage(), entity.getRelevance() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
