package i3.report;

import i.report.Report;
import i.uc.entity.Usr;
import i3.dao.ShopProvinceDao;
import i3.entity.ShopProvince;
import i3.report.area.ShopProvinceAvgTimeFiller;
import i3.report.area.ShopProvincePvUvAvgPageFiller;

import java.io.IOException;

public class ShopProvinceReport extends Report<ShopProvince, ShopProvinceDao> {

	// private static final Log LOG = LogFactory.getLogger(ShopReport.class);

	public ShopProvinceReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new ShopProvincePvUvAvgPageFiller(dao));
		fill(new ShopProvinceAvgTimeFiller(dao));
	}

	@Override
	protected ShopProvinceDao getDao() {
		return new ShopProvinceDao();
	}

	@Override
	protected String getNick(ShopProvince entity) {
		return entity.getNick();
	}

	@Override
	public String insert() {
		return "insert into A_SHOP_PROVINCE_DAY(Date,UserID,Province,PV,UV,AvgTime,AvgPage) values";
	}

	@Override
	protected String value(Usr user, ShopProvince entity) {
		ShopProvince.Key key = entity.getKey();

		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(key.getProvince());
		builder.append(",");
		builder.append(entity.getPv());
		builder.append(",");
		builder.append(entity.getUv());
		builder.append(",");
		builder.append(entity.getAvgTime());
		builder.append(",");
		builder.append(entity.getAvgPage());
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ShopProvinceReport report = new ShopProvinceReport(args[0]);
		report.fillAll();
		report.doInsert();
	}

}
