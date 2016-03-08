package i3.dao;

import i3.dao.ex.SecKeyNickDao;
import i3.entity.TaobaoKeyword;

import com.sleepycat.je.DatabaseException;

public class TaobaoKeywordDao extends
		SecKeyNickDao<TaobaoKeyword.Key, TaobaoKeyword> {

	public TaobaoKeywordDao() {
		setStoreName("taobao_keyword");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store
				.getPrimaryIndex(TaobaoKeyword.Key.class, TaobaoKeyword.class);
	}

}
