package com.wangcheng.utils;

import java.rmi.registry.LocateRegistry;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXMonitor {
	private MBeanServerConnection mbsc = null;

	private ObjectName objectName = null;

	public JMXMonitor(String ip, String port, String mbeanName)
			throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + ip
				+ "/jndi/rmi://" + ip + ":" + port + "/jmxrmi");

		final JMXConnector jmxc = JMXConnectorFactory.connect(url);

		mbsc = jmxc.getMBeanServerConnection();

		objectName = new ObjectName(mbeanName);

	}

	public void start() throws Exception {
		for (ObjectName subObjectName : mbsc.queryNames(objectName, null)) {
			MBeanInfo mbeanInfo = mbsc.getMBeanInfo(subObjectName);

			System.out.println(mbeanInfo.getClassName());

			MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();

			String attributeName = null;
			Object attributeValue = null;

			for (MBeanAttributeInfo attributeInfo : attributes) {
				attributeName = attributeInfo.getName();
				attributeValue = mbsc
						.getAttribute(subObjectName, attributeName);
				System.out.println("\t" + attributeName + "\t=\t"
						+ attributeValue);
			}
		}
	}

	public Object getAttribute(String mbeanName, String attributeName) {
		Object value = null;
		try {
			ObjectName objectName = new ObjectName(mbeanName);

			value = mbsc.getAttribute(objectName, attributeName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public static void main(String args[]) throws Exception {
		String ip = "127.0.0.1";
		String port = "8903";
		String mbeanName = "netty:name=queueMonitor";
		String attributeName = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h")) {
				ip = args[++i];
			} else if (args[i].equals("-p")) {
				port = args[++i];
			} else if (args[i].equals("-m")) {
				mbeanName = args[++i];
			}else if (args[i].equals("-attr")) {
				attributeName = args[++i];
			}
		}
		
		//LocateRegistry.createRegistry(Integer.valueOf(port));


		JMXMonitor monitor = new JMXMonitor(ip, port, mbeanName);
		
		if(attributeName!=null){
			 System.out.println(monitor.getAttribute(mbeanName,attributeName));
		}else{
			monitor.start();
		}

	}
	 
}
