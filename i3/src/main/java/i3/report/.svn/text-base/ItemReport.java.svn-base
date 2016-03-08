package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.ItemDao;
import i3.entity.Item;
import i3.report.page.ItemAvgTimeFiller;
import i3.report.page.ItemPvUvFiller;

import java.io.IOException;

public class ItemReport extends Report<Item, ItemDao> {

	// private static final Log LOG = LogFactory.getLogger(ItemReport.class);

	public ItemReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new ItemPvUvFiller(dao));
		fill(new ItemAvgTimeFiller(dao));
	}

	@Override
	protected ItemDao getDao() {
		return new ItemDao();
	}

	@Override
	protected String getNick(Item entity) {
		return entity.getNick();
	}
	
	@Override
	protected String insert() {
		return "insert into A_ITEM_DAY(Date,UserID,Num_IID,PV,UV,AvgTime,EntranceRate,BounceRate) values";
	}

	@Override
	protected String value(Usr user, Item entity) {
		Item.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getNumIID());
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
		ItemReport report = new ItemReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
