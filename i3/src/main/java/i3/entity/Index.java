package i3.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Index {

	@PrimaryKey
	private String nick;

	private long pv;
	private long uv;
	private long avgTime;
	private double entranceRate;
	private double bounceRate;

	private float avgPage;
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

	public double getEntranceRate() {
		return entranceRate;
	}

	public void setEntranceRate(double entranceRate) {
		this.entranceRate = entranceRate;
	}

	public double getBounceRate() {
		return bounceRate;
	}

	public void setBounceRate(double bounceRate) {
		this.bounceRate = bounceRate;
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
