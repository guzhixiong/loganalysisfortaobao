package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.ItemFromDao;
import i3.entity.ItemFrom;
import i3.report.page.ItemFromPvUvFiller;

import java.io.IOException;

public class ItemFromReport extends Report<ItemFrom, ItemFromDao> {

	// private static final Log LOG =
	// LogFactory.getLogger(ItemFromReport.class);

	public ItemFromReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new ItemFromPvUvFiller(dao));
	}

	@Override
	protected ItemFromDao getDao() {
		return new ItemFromDao();
	}

	@Override
	protected String getNick(ItemFrom entity) {
		return entity.getNick();
	}

	@Override
	protected String insert() {
		return "insert into A_ITEM_FROM_DAY(Date,UserID,Num_IID,From_ID,PV,UV,AvgTime,EntranceRate,BounceRate) values";
	}

	@Override
	protected String value(Usr user, ItemFrom entity) {
		ItemFrom.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getNumIID());
		builder.append(",");
		builder.append(key.getFromID());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getAvgTime());
		builder.append(",");
		builder.append(entity.getEntranceRate());
		builder.append(",");
		builder.append(entity.getBounceRate());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ItemFromReport report = new ItemFromReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
