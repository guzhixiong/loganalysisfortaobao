package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.ShopFromDao;
import i3.entity.ShopFrom;
import i3.report.from.ShopFromPvUvFiller;

import java.io.IOException;

public class ShopFromReport extends Report<ShopFrom, ShopFromDao> {

	// private static final Log LOG = LogFactory.getLogger(ShopReport.class);

	public ShopFromReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new ShopFromPvUvFiller(dao));
	}

	@Override
	protected ShopFromDao getDao() {
		return new ShopFromDao();
	}

	@Override
	protected String getNick(ShopFrom entity) {
		return entity.getNick();
	}

	@Override
	public String insert() {
		return "insert into A_SHOP_FROM_DAY(Date,UserID,From_ID,PV,UV,Percentage) values";
	}

	@Override
	protected String value(Usr user, ShopFrom entity) {
		ShopFrom.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getFromID());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getPercentage());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ShopFromReport report = new ShopFromReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
