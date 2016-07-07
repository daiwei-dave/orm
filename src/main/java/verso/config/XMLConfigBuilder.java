package verso.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

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
        	throw new Exception("[config]: root should be <config>");
        }
        evalDataSource(node.element("database"));
        for (Object item : node.elements("bean")) evalBean((Element) item);
        for (Object item : node.elements("result-map")) evalResultMap((Element) item);
      
    }
    
	public static void evalBean(Element node) {
    	String value = getValue(node);
    	try {
			Class<?> clazz = loader.loadClass(value);
			String name = node.attributeValue("name");
			if (name == null) name = clazz.getSimpleName();
			config.putDao(name, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public static void evalResultMap(Element node) throws Exception {
		// 映射表名与类名 
		String key = node.attributeValue("name");
		String type = node.attributeValue("type");
		if (key == null || type == null) 
			throw new Exception("[result-map]: should contain name and type");
		// 通过反射找到具体类，构建ResultMapper
		Class<?> clazz = loader.loadClass(type);
		ResultMapper mapper = new ResultMapper(clazz);
		// 映射数据库表属性与类属性
		for (Object item : node.elements("property")) {
			Element i = (Element) item;
			String pKey = i.attributeValue("name");
			String pValue = getValue(i);
			if (pKey == null)
				throw new Exception("[result-map]: <property> should contain name and value");
			if (pValue == null)
				pValue = pKey;
			try {
				Field field = clazz.getDeclaredField(pValue);
				mapper.put(pKey, field);
			} catch (NoSuchFieldException e) {
				throw new Exception(String.format(
						"[result-map]: <property> %s has no such field %s", type, pValue));
			}			
		}
		config.putResult(key, mapper);
	}
	
	public static void evalDataSource(Element node) throws Exception {
		DataSource data = new DataSource(); 
		for (Object item : node.elements("property")) {
			Element i = (Element) item;			
			String value = getValue(i);
			String name = i.attributeValue("name");
			if (name == null || value == null) 
				throw new Exception("[database]: <property> should contain name and value");
			
			switch (name) {
				case "url" : data.setUrl(value); break;
				case "username" : data.setUsername(value); break;
				case "password" : data.setPassword(value); break;
				case "driverClassName" : data.setDriverClassName(value); break; 
				default : throw new Exception("[database]: <property> unknown name"); 
			}
		}
		config.setDataSource(data);
	}
	
	private static String getValue(Element node) {
		return node.hasContent() ? node.getText() : node.attributeValue("value");
	}
}