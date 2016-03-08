package trade;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.BdbDao;
import dao.bdb.Exec;

public class TradeDao extends BdbDao<String, Trade> {

	private static final Logger LOG = LoggerFactory.getLogger(TradeDao.class);

	public TradeDao() {
		setPath("trade");
		setStoreName("trade");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(String.class, Trade.class);
	}

	public void list() {
		this.open();
		this.each(new Exec<Trade>() {
			@Override
			public void execute(Trade entity) {
				String from = entity.getFrom();
				if (from == null || from.equals("")) {
					return;
				}

				String[] tmp = { entity.getTid(), entity.getShop(),
						entity.getIp(), "" + entity.getPayment(),
						"" + entity.getNum(), entity.getFrom(),
						entity.getRefer() };

				LOG.info(StringUtils.join(tmp, ", "));
			}
		});
		this.close();
	}

	public static void main(String[] args) {
		TradeDao dao = new TradeDao();
		dao.setPath("20101026_trade_2");
		dao.list();
	}

}
