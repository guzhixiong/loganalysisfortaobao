package i3.entity;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Template {

	@PrimaryKey
	private String nick;

	private List<String[]> his = new ArrayList<String[]>();

	@NotPersistent
	private String json = null;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public synchronized String his2json() {
		if (json == null) {
			StringBuffer buffer = new StringBuffer("[");
			int size = his.size();
			for (int i = 0; i < size; i++) {
				String[] tmp = his.get(i);
				buffer.append("{\"ID\":");
				buffer.append(tmp[0]);
				buffer.append(",\"Name\":\"");
				buffer.append(tmp[1]);
				buffer.append("\"}");
				if (i < size - 1) {
					buffer.append(",");
				}
			}
			buffer.append("]");

			json = buffer.toString();
		}
		return json;
	}

	public List<String[]> getHis() {
		return his;
	}

	public void setHis(List<String[]> his) {
		this.his = his;
	}

}
