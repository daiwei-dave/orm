package com.github.basic.session;

import com.github.basic.config.Environment;
import org.springframework.beans.factory.FactoryBean;


import javax.sql.DataSource;

public class VSessionFactoryBean implements FactoryBean<VSessionFactory> {

    private DataSource dataSource;
    private VSessionFactory sessionFactory;

    /**
     * 获取VSessionFactory实例
     * @return
     * @throws Exception
     */
    @Override
    public VSessionFactory getObject() throws Exception {
        if (sessionFactory != null) return sessionFactory;
        sessionFactory = new VSessionFactory();
        Environment environment = new Environment();
        environment.setDataSource(dataSource);
        sessionFactory.setEnvironment(environment);
        return sessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return VSessionFactory.class;
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
