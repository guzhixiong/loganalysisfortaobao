package i3.dao;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import i3.entity.Index;

import com.sleepycat.je.DatabaseException;

import dao.bdb.BdbDao;
import dao.bdb.Exec;

public class IndexDao extends BdbDao<String, Index> {

	private static final Logger LOG = LoggerFactory.getLogger(IndexDao.class);

	public IndexDao() {
		setStoreName("index");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(String.class, Index.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Index>() {
			@Override
			public void execute(Index entity) {
				String[] tmp = { entity.getNick(), "" + entity.getPv(),
						"" + entity.getUv(), "" + entity.getAvgTime(),
						"" + entity.getEntranceRate(),
						"" + entity.getBounceRate() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

	public void listHourly() {
		this.setStoreName("index_hourly");
		this.open();
		this.each(new Exec<Index>() {
			@Override
			public void execute(Index entity) {
				String[] tmp = { entity.getNick(), "" + entity.getPv(),
						"" + entity.getUv(), "" + entity.getAvgTime(),
						"" + entity.getAvgPage(),
						"" + entity.getEntranceRate(),
						"" + entity.getBounceRate(), entity.getRelevance() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
