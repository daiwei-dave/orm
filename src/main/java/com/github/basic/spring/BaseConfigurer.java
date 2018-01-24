package com.github.basic.spring;

import com.github.basic.session.DefaultSqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


/**
 * javabean扫描
 */
public class BaseConfigurer implements BeanDefinitionRegistryPostProcessor {


    private String pojoPackage;
    private DefaultSqlSessionFactory sqlSessionFactory;


    public void setPojoPackage(String basePackage) {
        this.pojoPackage = basePackage;
    }
    public void setSqlSessionFactory(DefaultSqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        PojoScanner pojoScanner = new PojoScanner(registry);
        pojoScanner.setSessionFactory(sqlSessionFactory);
        pojoScanner.registerFilters();
        pojoScanner.scan(pojoPackage);
    }

}
