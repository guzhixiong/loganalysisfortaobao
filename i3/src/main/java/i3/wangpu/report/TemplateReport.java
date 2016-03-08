package i3.wangpu.report;

import i.report.FieldFiller;
import i.report.Report;
import i.uc.entity.Usr;
import i.utils.DbUtils;
import i3.dao.TemplateDao;
import i3.entity.Template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateReport extends Report<Template, TemplateDao> {

	private static final Logger LOG = LoggerFactory
			.getLogger(TemplateReport.class);

	public TemplateReport(String date) {
		super(date);
	}

	@Override
	public void fillAll() throws IOException {
		fill(new TemplateFiller(dao));
	}

	@Override
	protected TemplateDao getDao() {
		return new TemplateDao();
	}

	@Override
	protected String getNick(Template entity) {
		return entity.getNick();
	}

	@Override
	protected void fill(FieldFiller filler) throws IOException,
			RuntimeException {
		String field = filler.getDataFile();
		if (null == field) {
			filler.fill(null);
			return;
		}

		String base = conf.getString("da.file.path", "/opt/i");
		new File(base).mkdirs();

		File path = new File(base + File.separator + "template"
				+ File.separator + getTimestamp() + "_" + field);
		if (!path.exists()) {
			throw new RuntimeException(path.getAbsolutePath() + " not exist");
		}

		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().startsWith("part-r-");
			}
		});

		for (File file : files) {
			LOG.info(file.getAbsolutePath());

			BufferedReader reader = new BufferedReader(new FileReader(file));
			filler.fill(reader);
			reader.close();
		}
	}

	public String insert() {
		return "insert into A_TEMPLATE_DAY(DATE,UserID,TEMPLATE) values";
	}

	protected String value(Usr user, Template entity) {
		StringBuilder builder = new StringBuilder("('");
		builder.append(dateTime);
		builder.append("',");
		builder.append(user.getUid());
		builder.append(",");
		builder.append(DbUtils.sqlEscape(entity.his2json()));
		builder.append(")");

		return builder.toString();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		TemplateReport report = new TemplateReport(args[0]);
		report.fillAll();
		report.doInsert();

	}

}
