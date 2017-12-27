package com.github.verso.session;



import com.github.verso.config.XMLConfigBuilder;

import javax.sql.DataSource;

public class VSessionFactory implements SessionFactory {

    private DataSource config;
	
	public VSessionFactory(String resource) {
		config = XMLConfigBuilder.build(resource);
	}
	
	public VSession openSession() {
		return new VSession(config);
	}
}
