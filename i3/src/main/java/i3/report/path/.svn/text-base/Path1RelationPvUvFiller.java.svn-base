package i3.report.path;

import i.report.FieldFiller;
import i3.dao.PathDao;
import i3.dao.PathRelationDao;
import i3.entity.Path;
import i3.entity.PathRelation;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Path1RelationPvUvFiller implements FieldFiller {

	private static final Logger LOG = LoggerFactory
			.getLogger(Path1RelationPvUvFiller.class);

	private PathRelationDao dao;

	public Path1RelationPvUvFiller(PathRelationDao dao) {
		this.dao = dao;
	}

	@Override
	public String getDataFile() {
		return "path_1_pv_uv";
	}

	@Override
	public void fill(BufferedReader reader) throws IOException {
		dao.open(false);

		PathDao path = new PathDao();
		path.setPath(dao.getPath());
		path.open();

		Set<String> unknowUser = new HashSet<String>();

		String line;
		while (null != (line = reader.readLine())) {
			String[] tmp = line.split("\\s");
			if (tmp[0].equals("-")) {
				continue;
			}

			Path.Key k = new Path.Key();
			k.setNick(tmp[0]);
			k.setType(tmp[1]);
			k.setId(tmp[2]);
			k.setSrcType("");
			k.setSrcId("");

			Path p = path.find(k);
			if (p == null) {
				unknowUser.add(tmp[0]);
				continue;
			}

			PathRelation.Key key = new PathRelation.Key();
			key.setNick(tmp[0]);
			key.setFromID(tmp[3]);
			key.setType(tmp[1]);
			key.setId(tmp[2]);

			PathRelation entity = dao.find(key);
			if (null == entity) {
				entity = new PathRelation();
				entity.setKey(key);
			}
			entity.setUrl(p.getUrl());
			entity.setPv(Long.parseLong(tmp[6]));
			entity.setUv(Long.parseLong(tmp[7]));
			entity.setBounceRate(0);
			entity.setPercentage(0);
			entity.setRelevance("");

			dao.add(entity);
		}

		Iterator<String> it = unknowUser.iterator();
		while (it.hasNext()) {
			String user = it.next();
			LOG.info("unknow: ", user);
		}

		path.close();

		dao.close();
	}

}
