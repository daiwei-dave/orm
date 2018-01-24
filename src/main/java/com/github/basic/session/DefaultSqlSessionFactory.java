package com.github.basic.session;
import com.github.basic.config.Configuration;
import com.github.basic.config.Environment;
import com.github.basic.config.XMLConfigBuilder;

import javax.sql.DataSource;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private DataSource config;

	private Configuration configuration;


	private Environment environment;


	public DefaultSqlSessionFactory() {
	}

	public DefaultSqlSessionFactory(String resource) {
		config = XMLConfigBuilder.build(resource);
	}
	
	@Override
	public DefaultSqlSession openSession() {
		return new DefaultSqlSession(environment);
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
