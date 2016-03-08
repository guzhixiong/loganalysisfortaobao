package i3.business;

import i.DataReader;
import i.utils.Nick2UID;

import java.io.IOException;

import dao.bdb.pair.Pair;

public class UserItemUv extends DataReader {

	private Nick2UID n2u;

	public UserItemUv(String date) {
		super(date);
	}

	@Override
	protected String getSrc() {
		return "page_pv_uv";
	}

	@Override
	protected String getDes() {
		return "A_ITEM_DAY";
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
		int pos = line.lastIndexOf("\t");
		String[] tmp = line.substring(0, pos).split(" ");
		if (!tmp[1].equals("3") || tmp[0].equals("-") || tmp[2].equals("-")) {
			return "";
		}

		Pair pair = n2u.find(tmp[0]);
		if (pair == null) {
			return "";
		}
		return pair.getValue() + " " + tmp[2] + " "
				+ line.substring(pos + 1).split(" ")[1] + "\n";
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
		System.out.println(args[0]);
		new UserItemUv(args[0]).process();
	}

}
