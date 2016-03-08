package ControlPanelDataOp;

import java.io.Serializable;
import java.util.List;

public class Source implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Top> TopList;

	private String SourceID;

	private String From_Name;

	private String IsPay;

	private String IsOutSide;

	private String Pv;

	private String Uv;

	private String Percentage;

	public List<Top> getTopList() {
		return TopList;
	}

	public void setTopList(List<Top> topList) {
		TopList = topList;
	}

	public String getSourceID() {
		return SourceID;
	}

	public void setSourceID(String sourceID) {
		SourceID = sourceID;
	}

	public String getFrom_Name() {
		return From_Name;
	}

	public void setFrom_Name(String from_Name) {
		From_Name = from_Name;
	}

	public String getIsPay() {
		return IsPay;
	}

	public void setIsPay(String isPay) {
		IsPay = isPay;
	}

	public String getIsOutSide() {
		return IsOutSide;
	}

	public void setIsOutSide(String isOutSide) {
		IsOutSide = isOutSide;
	}

	public String getPv() {
		return Pv;
	}

	public void setPv(String pv) {
		Pv = pv;
	}

	public String getUv() {
		return Uv;
	}

	public void setUv(String uv) {
		Uv = uv;
	}

	public String getPercentage() {
		return Percentage;
	}

	public void setPercentage(String percentage) {
		Percentage = percentage;
	}

}
