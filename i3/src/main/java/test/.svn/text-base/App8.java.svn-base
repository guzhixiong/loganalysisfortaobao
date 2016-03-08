package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App8 {

	private static final Logger LOG = LoggerFactory.getLogger(App8.class);

	private String url;
	private Document doc = null;
	private XPath xpath = null;

	public App8() {
		this.url = "res/2010-10-1--2010-10-3 9.38.16.xml";
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(url);
		} catch (JDOMException e) {
			LOG.error("", e);
		} catch (IOException e) {
			LOG.error("", e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Element> getElements(String path) {
		List<Element> items = new ArrayList<Element>();
		try {
			xpath = XPath.newInstance(path);
			items = (List<Element>) xpath.selectNodes(doc.getRootElement());
		} catch (JDOMException e) {
			LOG.error("", e);
		}

		return items;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		App8 app = new App8();
		List<Element> trades = app.getElements("/ArrayOfTrade/Trade");
		for (Element trade : trades) {
			String tid = trade.getChildText("tid");
			String totalFee = trade.getChildText("total_fee");
			String created = trade.getChildText("created").split(" ")[0];
			System.out.println(tid + " " + created + " " + totalFee);

			int num = 0;
			List<Element> orders = trade.getChild("orders")
					.getChildren("order");
			for (Element order : orders) {
				int n = Integer.parseInt(order.getChildText("num"));
				System.out.println(n);
				num += n;
			}

			System.out.println("sum " + num);
		}

	}
}
