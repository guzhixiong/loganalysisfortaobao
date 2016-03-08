package i3.entity;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class ItemKeyword {

	@PrimaryKey
	private Key key;

	@SecondaryKey(name = "nick", relate = Relationship.MANY_TO_ONE, onRelatedEntityDelete = DeleteAction.CASCADE)
	private String nick;

	private long pv1;
	private long pv2;
	private long pv3;

	@Persistent
	public static class Key {

		@KeyField(1)
		private String nick;
		@KeyField(2)
		private String numIID;
		@KeyField(3)
		private String keyword;

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getNumIID() {
			return numIID;
		}

		public void setNumIID(String numIID) {
			this.numIID = numIID;
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

	public long getPv1() {
		return pv1;
	}

	public void setPv1(long pv1) {
		this.pv1 = pv1;
	}

	public long getPv2() {
		return pv2;
	}

	public void setPv2(long pv2) {
		this.pv2 = pv2;
	}

	public long getPv3() {
		return pv3;
	}

	public void setPv3(long pv3) {
		this.pv3 = pv3;
	}

}
