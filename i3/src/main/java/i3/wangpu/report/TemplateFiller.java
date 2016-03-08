package i3.wangpu.report;

import i.report.FieldFiller;
import i3.dao.TemplateDao;
import i3.entity.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateFiller implements FieldFiller {

	private static final Logger LOG = LoggerFactory
			.getLogger(TemplateFiller.class);

	private TemplateDao dao;

	public TemplateFiller(TemplateDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "template";
	}

	@Override
	public void fill(BufferedReader reader) throws IOException {
		List<String[]> his = new ArrayList<String[]>();
		String nick = "";

		dao.open(false);

		String line;
		while (null != (line = reader.readLine())) {
			String[] tmp = line.split("\\s");
			if (tmp.length < 3 || tmp[0].equals("-") || tmp[1].equals("-")) {
				continue;
			}

			try {
				Double.parseDouble(tmp[1]);
			} catch (NumberFormatException e) {
				LOG.error("incorrect template id [{}]", tmp[1]);
				continue;
			}

			String _nick = tmp[0];
			if (!_nick.equalsIgnoreCase(nick) && his.size() > 0) {
				Template entity = new Template();
				entity.setNick(nick);
				entity.setHis(his);

				dao.add(entity);
				his.clear();
			}

			String name = tmp[2];
			name = name.equals("-") ? "" : name;
			his.add(new String[] { tmp[1], name });
			nick = _nick;
		}
		if (his.size() > 0) {
			Template entity = new Template();
			entity.setNick(nick);
			entity.setHis(his);

			dao.add(entity);
			his.clear();
		}

		dao.close();
	}

}
