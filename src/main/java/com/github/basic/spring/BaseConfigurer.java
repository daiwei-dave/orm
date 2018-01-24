package com.github.basic.spring;

import com.github.basic.session.VSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


/**
 * bean扫描初始化
 */
public class BaseConfigurer implements BeanDefinitionRegistryPostProcessor {


    private String pojoPackage;
    private VSessionFactory sessionFactory;


    public void setPojoPackage(String basePackage) {
        this.pojoPackage = basePackage;
    }
    public void setSessionFactory(VSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        PojoScanner pojoScanner = new PojoScanner(registry);
        pojoScanner.setSessionFactory(sessionFactory);
        pojoScanner.registerFilters();
        pojoScanner.scan(pojoPackage);
    }

}
