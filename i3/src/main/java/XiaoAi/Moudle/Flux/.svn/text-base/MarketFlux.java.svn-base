package XiaoAi.Moudle.Flux;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.phprpc.util.PHPSerializer;

import XiaoAi.Moudle.XiaoAiObject;

public class MarketFlux extends XiaoAiObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String IP;
	private List<String> ListKeyWord;
	private List<Integer> ListFrom;
	private Date Time;

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public List<String> getListKeyWord() {
		return ListKeyWord;
	}

	public void setListKeyWord(List<String> listKeyWord) {
		ListKeyWord = listKeyWord;
	}

	public List<Integer> getListFrom() {
		return ListFrom;
	}

	public void setListFrom(List<Integer> listFrom) {
		ListFrom = listFrom;
	}

	public Date getTime() {
		return Time;
	}

	public void setTime(Date time) {
		Time = time;
	}

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException {
		List<MarketFlux> list = new ArrayList<MarketFlux>();

		MarketFlux flux = new MarketFlux();
		list.add(flux);

		byte[] php = new PHPSerializer().serialize(list);

		FileOutputStream out = new FileOutputStream("flux.php");
		out.write(php);
		out.close();
	}

}
