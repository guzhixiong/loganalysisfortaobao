package i3.entity;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class IndexProvince {

	@PrimaryKey
	private Key key;

	@SecondaryKey(name = "nick", relate = Relationship.MANY_TO_ONE, onRelatedEntityDelete = DeleteAction.CASCADE)
	private String nick;

	private long pv;
	private long uv;

	@Persistent
	public static class Key {

		@KeyField(1)
		private String nick;
		@KeyField(2)
		private String province;

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
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

}
