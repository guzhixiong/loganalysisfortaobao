package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.ShopDao;
import i3.entity.Shop;
import i3.report.shop.ShopAvgTimeFiller;
import i3.report.shop.ShopPvUvAvgPageFiller;

import java.io.IOException;

public class ShopReport extends Report<Shop, ShopDao> {

	// private static final Log LOG = LogFactory.getLogger(ShopReport.class);

	public ShopReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new ShopPvUvAvgPageFiller(dao));
		fill(new ShopAvgTimeFiller(dao));
	}

	@Override
	protected ShopDao getDao() {
		return new ShopDao();
	}

	@Override
	protected String getNick(Shop entity) {
		return entity.getNick();
	}

	@Override
	public String insert() {
		return "insert into A_SHOP_DAY(Date,UserID,PV,UV,AvgTime,AvgPage,ExpandRate) values";
	}

	@Override
	protected String value(Usr user, Shop entity) {
		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getAvgTime());
		builder.append(",");
		builder.append(entity.getAvgPage());
		builder.append(",");
		builder.append(entity.getExpandRate());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ShopReport report = new ShopReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
