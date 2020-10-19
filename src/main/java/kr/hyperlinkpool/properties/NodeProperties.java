package kr.hyperlinkpool.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class NodeProperties {

	private Properties properties = new Properties();

	public static NodeProperties instance;

	private NodeProperties() {
	}

	public Properties getProperties() {
		return properties;
	}

	public static NodeProperties getInstance() {
		if (instance == null) {
			instance = new NodeProperties();
		}

		return instance;
	}

	public void loadProp(String path) throws IOException {
		File configFile = new File(path);
		FileInputStream fileInputStream = new FileInputStream(configFile);
		properties.load(fileInputStream);
		fileInputStream.close();
	}

	public void loadPropBaseClasspath(String path) throws IOException {
		InputStream inputStream = getClass().getResourceAsStream(path);
		properties.load(inputStream);
		inputStream.close();
	}

	public static String getString(String key) {
		String property = NodeProperties.getInstance().getProperties().getProperty(key);
		return property;
	}
	
	public static boolean getBoolean(String key) {
		String property = NodeProperties.getInstance().getProperties().getProperty(key);
		return Boolean.parseBoolean(property);
	}
	
	public static int getInt(String key) {
		String property = NodeProperties.getInstance().getProperties().getProperty(key);
		int parseInt = Integer.parseInt(property);
		return parseInt;
	}
	
	public static long getLong(String key) {
		String property = NodeProperties.getInstance().getProperties().getProperty(key);
		long parseInt = Long.parseLong(property);
		return parseInt;
	}
	
	public static String[] getArray(String key) {
		String property = NodeProperties.getInstance().getProperties().getProperty(key);
		String[] propertyArray = property.split(",");
		return propertyArray;
	}
}