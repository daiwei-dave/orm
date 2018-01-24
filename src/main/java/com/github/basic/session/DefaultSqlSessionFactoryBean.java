package com.github.basic.session;

import com.github.basic.config.Environment;
import org.springframework.beans.factory.FactoryBean;


import javax.sql.DataSource;

public class DefaultSqlSessionFactoryBean implements FactoryBean<DefaultSqlSessionFactory> {

    private DataSource dataSource;
    private DefaultSqlSessionFactory defaultSqlSessionFactory;

    /**
     * 获取VSessionFactory实例
     * @return
     * @throws Exception
     */
    @Override
    public DefaultSqlSessionFactory getObject() throws Exception {
        if (defaultSqlSessionFactory != null) return defaultSqlSessionFactory;
        defaultSqlSessionFactory = new DefaultSqlSessionFactory();
        Environment environment = new Environment();
        environment.setDataSource(dataSource);
        defaultSqlSessionFactory.setEnvironment(environment);
        return defaultSqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultSqlSessionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
