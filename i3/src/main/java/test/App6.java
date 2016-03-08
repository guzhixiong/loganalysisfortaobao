package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.commons.lang.StringUtils;

public class App6 {

	public static void main(String[] args) throws XMLStreamException,
			IOException {
		System.out.println("hi");

		String xml = "res/2010-10-1--2010-10-3 9.38.16.xml";
		InputStream in = new FileInputStream(xml);

		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

		XMLStreamReader reader = factory.createXMLStreamReader(in);

		XMLStreamReader r = new StreamReaderDelegate(reader) {
			public int next() throws XMLStreamException {
				while (true) {
					int event = super.next();
					switch (event) {
					case XMLStreamConstants.COMMENT:
					case XMLStreamConstants.PROCESSING_INSTRUCTION:
						continue;
					default:
						return event;
					}
				}
			}
		};

		String s;
		Stack<String> stack = new Stack<String>();
		while (r.hasNext()) {
			int event = r.getEventType();
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				QName tag = r.getName();

				s = tag.toString();
				s = StringUtils.leftPad(s, s.length() + stack.size(), "  ");
				System.out.print("\n" + s);

				stack.push(tag.toString());

				break;
			case XMLStreamConstants.CHARACTERS:
				if (!r.isWhiteSpace()) {
					s = " :" + r.getText();
					System.out.print(s);
				}

				break;
			case XMLStreamConstants.END_ELEMENT:
				stack.pop();
				break;
			case XMLStreamConstants.START_DOCUMENT:

				break;
			case XMLStreamConstants.END_DOCUMENT:

				break;
			}

			event = r.next();
		}

		r.close();
		in.close();

	}
}
