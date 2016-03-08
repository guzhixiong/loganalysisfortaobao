package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.phprpc.util.PHPSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ControlPanelDataOp.CommInfo;
import ControlPanelDataOp.FromView;
import ControlPanelDataOp.SecNode;
import ControlPanelDataOp.Source;
import ControlPanelDataOp.Top;

public class App10 {

	private static final Logger LOG = LoggerFactory.getLogger(App10.class);

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException {
		CommInfo commInfo2 = new CommInfo();
		commInfo2.setUrl("i.tb.fenxi001.com");
		commInfo2.setTitle("小艾分析");
		commInfo2.setFromType("28");
		commInfo2.setPv("99");
		commInfo2.setUv("30");

		SecNode secNode = new SecNode();
		secNode.setSecNodeID("991");
		secNode.setCommInfo(commInfo2);

		List<SecNode> secNodeList = new ArrayList<SecNode>();
		secNodeList.add(secNode);

		CommInfo commInfo = new CommInfo();
		commInfo.setUrl("i.tb.fenxi001.com");
		commInfo.setTitle("小艾分析体验版");
		commInfo.setFromType("9");
		commInfo.setPv("69");
		commInfo.setUv("55");

		Top top = new Top();
		top.setTopID("hi");
		top.setCommInfo(commInfo);
		top.setSecNodeList(secNodeList);

		List<Top> topList = new ArrayList<Top>();
		topList.add(top);

		Source source = new Source();
		source.setSourceID("001");
		source.setFrom_Name("自然访问");
		source.setPv("200");
		source.setUv("100");
		source.setTopList(topList);

		List<Source> sourceList = new ArrayList<Source>();
		sourceList.add(source);

		FromView fromView = new FromView();
		fromView.setFromViewDate(new Date());
		fromView.setSourceList(sourceList);

		PHPSerializer serializer = new PHPSerializer();
		byte[] php = serializer.serialize(fromView);
		LOG.info(new String(php));

		FileOutputStream out = new FileOutputStream("test");
		out.write(php);
		out.close();

		// String php =
		// "O:22:\"ControlPanelDataOp_Top\":3:{s:11:\"SecNodeList\";N;s:8:\"CommInfo\";N;s:5:\"TopID\";s:2:\"hi\";}";
		// Object obj = serial.unserialize(php.getBytes());
		// LOG.info(obj.getClass());
		//
		// Top top = (Top) Cast.cast(obj, Top.class);
		//
		// LOG.info(top.getTopID());
		// LOG.info(top.getSecNodeList());
		// LOG.info(top.getCommInfo());

	}

	public static void showBytes(byte[] php) {
		int len = php.length;
		Integer[] arr = new Integer[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) php[i];
		}
		LOG.info(StringUtils.join(arr, ", "));
	}

}
