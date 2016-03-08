package i3.entity;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class TaobaoKeyword {

	@PrimaryKey
	private Key key;

	@SecondaryKey(name = "nick", relate = Relationship.MANY_TO_ONE, onRelatedEntityDelete = DeleteAction.CASCADE)
	private String nick;

	private long pv;
	private long uv;
	private long ov;
	private long avgTime;
	private float avgPage;
	private String relevance;

	@Persistent
	public static class Key {

		@KeyField(1)
		private String nick;
		@KeyField(2)
		private String keyword;

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
		this.nick = key.getNick();
	}

	public String getNick() {
		return nick;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public long getOv() {
		return ov;
	}

	public void setOv(long ov) {
		this.ov = ov;
	}

	public long getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(long avgTime) {
		this.avgTime = avgTime;
	}

	public float getAvgPage() {
		return avgPage;
	}

	public void setAvgPage(float avgPage) {
		this.avgPage = avgPage;
	}

	public String getRelevance() {
		return relevance;
	}

	public void setRelevance(String relevance) {
		this.relevance = relevance;
	}

}
