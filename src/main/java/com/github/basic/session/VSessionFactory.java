package com.github.basic.session;
import com.github.basic.config.XMLConfigBuilder;

import javax.sql.DataSource;
import java.io.IOException;

public class VSessionFactory implements SessionFactory {

    private DataSource config;


	public VSessionFactory() {
	}

	@Override
	public  VSessionFactory getFactoryInstance(String resource) {
		try {
			VSessionFactory factory = new VSessionFactory();
			factory.config = XMLConfigBuilder.build(resource);
			return factory;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Can't find resource " + resource);
			//throw e;
		}
		return null;
	}
	
	public VSessionFactory(String resource) {
		config = XMLConfigBuilder.build(resource);
	}
	
	@Override
	public VSession openSession() {
		return new VSession(config);
	}
}
