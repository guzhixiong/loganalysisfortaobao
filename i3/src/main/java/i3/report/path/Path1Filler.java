package i3.report.path;

import i.report.FieldFiller;
import i3.dao.PathDao;
import i3.entity.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Path1Filler implements FieldFiller {

	// private static final Log LOG = LogFactory.getLogger(Path1Filler.class);

	private PathDao dao;

	public Path1Filler(PathDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "path_1";
	}

	@Override
	public void fill(BufferedReader reader) throws IOException {
		dao.open(false);

		List<String> page = new ArrayList<String>();
		String _nick = "";

		String line;
		while (null != (line = reader.readLine())) {
			String[] tmp = line.split("\\s");
			if (tmp[0].equals("-")) {
				continue;
			}

			String nick = tmp[0];
			if (!nick.equalsIgnoreCase(_nick) && page.size() > 0) {
				Collections.sort(page, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						String[] s1 = o1.split("\\s");
						String[] s2 = o2.split("\\s");
						long cmp = 0;
						cmp = Long.parseLong(s1[5]) - Long.parseLong(s2[5]);
						if (cmp != 0)
							return cmp > 0 ? -1 : 1;
						cmp = Long.parseLong(s1[6]) - Long.parseLong(s2[6]);
						if (cmp != 0)
							return cmp > 0 ? -1 : 1;
						return 0;
					}
				});

				int len = page.size();
				for (int i = 0; i < 5 && i < len; i++) {
					String[] items = page.get(i).split("\\s");

					Path.Key key = new Path.Key();
					key.setNick(_nick);
					key.setType(items[1]);
					key.setId(items[2]);
					key.setSrcType("");
					key.setSrcId("");

					Path entity = dao.find(key);
					if (null == entity) {
						entity = new Path();
						entity.setKey(key);
					}
					entity.setUrl(items[3]);
					entity.setTitle(items[4]);

					dao.add(entity);
				}

				page.clear();
			}

			page.add(line);
			_nick = nick;
		}

		dao.close();
	}

}
