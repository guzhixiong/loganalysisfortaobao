package i3.dao;

import i3.entity.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.DatabaseException;

import dao.bdb.BdbDao;
import dao.bdb.Exec;

public class TemplateDao extends BdbDao<String, Template> {

	private static final Logger LOG = LoggerFactory
			.getLogger(TemplateDao.class);

	public TemplateDao() {
		setStoreName("template");
	}

	@Override
	protected void setPk() throws DatabaseException {
		pk = store.getPrimaryIndex(String.class, Template.class);
	}

	public void list() {
		final StringBuffer buffer = new StringBuffer();
		this.open();
		this.each(new Exec<Template>() {
			@Override
			public void execute(Template entity) {
				buffer.setLength(0);
				buffer.append(entity.getNick());
				buffer.append(",");
				buffer.append(entity.his2json());

				LOG.info(buffer.toString());
			}
		});
		this.close();
	}

	public static void main(String[] args) {
		TemplateDao dao = new TemplateDao();
		dao.setPath("20110128");
		dao.list();
	}

}
