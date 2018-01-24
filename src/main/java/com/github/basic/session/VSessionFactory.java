package com.github.basic.session;
import com.github.basic.config.Configuration;
import com.github.basic.config.Environment;
import com.github.basic.config.XMLConfigBuilder;

import javax.sql.DataSource;

public class VSessionFactory implements SessionFactory {

    private DataSource config;

	private Configuration configuration;


	private Environment environment;


	public VSessionFactory() {
	}

	public VSessionFactory(String resource) {
		config = XMLConfigBuilder.build(resource);
	}
	
	@Override
	public VSession openSession() {
		return new VSession(environment);
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public Configuration getConfiguration(Environment environment) {
		return new Configuration(environment);
	}


	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}


	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
