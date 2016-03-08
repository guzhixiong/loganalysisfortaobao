package i3.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Shop {

	@PrimaryKey
	private String nick;

	private long pv;
	private long uv;
	private long avgTime;
	private float avgPage;
	private long expandRate;

	private long shopID;
	private String relevance;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
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

	public long getExpandRate() {
		return expandRate;
	}

	public void setExpandRate(long expandRate) {
		this.expandRate = expandRate;
	}

	protected long getShopID() {
		return shopID;
	}

	protected void setShopID(long shopID) {
		this.shopID = shopID;
	}

	public String getRelevance() {
		return relevance;
	}

	public void setRelevance(String relevance) {
		this.relevance = relevance;
	}

}
