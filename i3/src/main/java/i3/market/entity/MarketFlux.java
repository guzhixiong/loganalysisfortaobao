package i3.market.entity;

import java.util.List;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class MarketFlux {

	@PrimaryKey
	private Key key;

	@SecondaryKey(name = "nick", relate = Relationship.MANY_TO_ONE, onRelatedEntityDelete = DeleteAction.CASCADE)
	private String nick;

	private List<String> keywords;
	private List<Integer> froms;

	@Persistent
	public static class Key {

		@KeyField(1)
		private String nick;
		@KeyField(2)
		private String ip;

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
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

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<Integer> getFroms() {
		return froms;
	}

	public void setFroms(List<Integer> froms) {
		this.froms = froms;
	}

}
