package i3.business;

import i.DataReader;
import i.utils.Nick2UID;

import java.io.IOException;

import dao.bdb.pair.Pair;

public class UserProvinceUv extends DataReader {

	private Nick2UID n2u;

	public UserProvinceUv(String date) {
		super(date);
	}

	@Override
	protected String getSrc() {
		return "shop_area_pv_uv";
	}

	@Override
	protected String getDes() {
		return "A_SHOP_PROVINCE_DAY";
	}

	@Override
	protected void init() {
		n2u = new Nick2UID();
		n2u.open();
	}

	@Override
	protected void fin() {
		n2u.close();
	}

	@Override
	public String process(String line) {
		String[] tmp = line.split("\\s");
		if (tmp[0].equals("-") || tmp[1].equals("29")) {
			return "";
		}

		Pair pair = n2u.find(tmp[0]);
		if (pair == null) {
			return "";
		}
		return pair.getValue() + " " + tmp[1] + " " + tmp[3] + "\n";
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("need input a date like \"yyMMdd\"");
			System.exit(-1);
		}
		new UserProvinceUv(args[0]).process();
	}

}
