package i3.business;

import i.DataReader;
import i.utils.Nick2UID;

import java.io.IOException;

import dao.bdb.pair.Pair;

public class UserUv extends DataReader {

	private Nick2UID n2u;

	public UserUv(String date) {
		super(date);
	}

	@Override
	protected String getSrc() {
		return "shop_pv_uv";
	}

	@Override
	protected String getDes() {
		return "A_SHOP_DAY";
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
		if (tmp[0].equals("-")) {
			return "";
		}
		Pair pair = n2u.find(tmp[0]);
		if (pair == null) {
			return "";
		}
		return pair.getValue() + " " + tmp[2] + "\n";
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
		new UserUv(args[0]).process();
	}

}
