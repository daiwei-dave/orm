package com.github.basic.session;
import com.github.basic.config.XMLConfigBuilder;

import javax.sql.DataSource;

public class VSessionFactory implements SessionFactory {

    private DataSource config;
	
	public VSessionFactory(String resource) {
		config = XMLConfigBuilder.build(resource);
	}
	
	@Override
	public VSession openSession() {
		return new VSession(config);
	}
}
