package verso.config;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import verso.io.ClassLoaderWrapper;

/**
 * @author JingGe Tang
 */
public class XMLConfigBuilder 
{
	private static ClassLoaderWrapper loader = ClassLoaderWrapper.getInstance();
	private static Environment config = new Environment();
	/**
	 * 读取xml信息并处理
	 * @return Environment
	 */
	public static Environment build(String resource) throws IOException
	{
		InputStream stream = loader.loadResource(resource);
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			Element root = document.getRootElement();
			evalRoot(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
	
    public static void evalRoot(Element node) throws Exception {  
        if (!node.getName().equals("config")) {
        	throw new Exception("Root should be <config>");
        }
        evalDataSource(node.element("database"));
        for (Object item : node.elements("bean")) evalBean((Element) item);
      
    }
    
	public static void evalBean(Element node) {
    	String value = node.hasContent() ? node.getText() : node.attributeValue("value");
    	try {
			Class<?> clazz = loader.loadClass(value);
			String name = node.attributeValue("name");
			if (name == null) name = clazz.getSimpleName();
			config.putClass(name, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static void evalDataSource(Element node) throws Exception {
		DataSource data = new DataSource(); 
		for (Object item : node.elements("property")) {
			Element i = (Element) item;			
			String value = i.hasContent() ? i.getText() : i.attributeValue("value");
			String name = i.attributeValue("name");
			if (name == null || value == null) 
				throw new Exception("<property> should contain name and value");
			
			switch (name) {
				case "url" : data.setUrl(value); break;
				case "username" : data.setUsername(value); break;
				case "password" : data.setPassword(value); break;
				case "driverClassName" : data.setDriverClassName(value); break; 
				default : throw new Exception("<property> unknown name"); 
			}
		}
		config.setDataSource(data);
	}
}