package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.IndexProvince;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.Exec;

public class IndexProvinceDao extends
		SecKeyNickDao<IndexProvince.Key, IndexProvince> {

	private static final Logger LOG = LoggerFactory.getLogger(IndexProvinceDao.class);

	public IndexProvinceDao() {
		setStoreName("index_province");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store
				.getPrimaryIndex(IndexProvince.Key.class, IndexProvince.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<IndexProvince>() {
			@Override
			public void execute(IndexProvince entity) {
				IndexProvince.Key key = entity.getKey();
				String[] tmp = { key.getNick(), "" + key.getProvince(),
						"" + entity.getPv(), "" + entity.getUv() };
				LOG.info(StringUtils.join(tmp, " "));
			}
		});
		this.close();
	}

}
