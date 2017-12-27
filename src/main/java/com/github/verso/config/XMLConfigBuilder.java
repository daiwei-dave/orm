package com.github.verso.config;

import com.github.verso.VDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import javax.sql.DataSource;
import java.io.InputStream;

/**
 * 读取与解析配置信息，并返回处理后的Environment
 * @author tjg
 */
public class XMLConfigBuilder 
{
	private static ClassLoader loader = ClassLoader.getSystemClassLoader();

	/**
	 * 读取xml信息并处理
	 */
	public static DataSource build(String resource)
	{
	    try {
	        InputStream stream = loader.getResourceAsStream(resource);
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			Element root = document.getRootElement();
			return evalDataSource(root);
		} catch (Exception e) {
			throw new RuntimeException("error occured while evaling xml " + resource);
		}
	}
	
	public static DataSource evalDataSource(Element node) throws ClassNotFoundException 
	{
        if (!node.getName().equals("database")) {
        	throw new RuntimeException("root should be <database>");
        }
		String driverClassName = null;
		String url = null;
		String username = null;
		String password = null;
		for (Object item : node.elements("property")) {
			Element i = (Element) item;			
			String value = getValue(i);
			String name = i.attributeValue("name");
			if (name == null || value == null) 
				throw new RuntimeException("[database]: <property> should contain name and value");
			
			switch (name) {
				case "url" : url = value; break;
				case "username" : username = value; break;
				case "password" : password = value; break;
				case "driverClassName" : driverClassName = value; break; 
				default : throw new RuntimeException("[database]: <property> unknown name"); 
			}
		}
		return new VDataSource(driverClassName, url, username, password);
	}
	
	private static String getValue(Element node) {
		return node.hasContent() ? node.getText() : node.attributeValue("value");
	}
}
