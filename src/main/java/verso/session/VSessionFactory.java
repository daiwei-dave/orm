package verso.session;

import java.io.IOException;

import verso.config.Environment;
import verso.config.XMLConfigBuilder;

public class VSessionFactory
{
	Environment config;
	
	public static VSessionFactory getFactoryInstance(String resource) {
		try {
			VSessionFactory factory = new VSessionFactory();
			factory.config = XMLConfigBuilder.build(resource);
			return factory;
		} catch (IOException e) {
			System.err.println("Can't find resource " + resource);
		}
		return null;
	}
	
	public VSession openSession() {
		return new VSession(config);
	}
}