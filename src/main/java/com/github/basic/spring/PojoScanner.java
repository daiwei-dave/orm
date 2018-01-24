package com.github.basic.spring;

import com.github.basic.annotation.Column;
import com.github.basic.annotation.Table;
import com.github.basic.config.Environment;
import com.github.basic.maper.impl.MappedBeanResult;

import com.github.basic.session.DefaultSqlSessionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * 扫描bean并进行处理
 */
public class PojoScanner extends ClassPathBeanDefinitionScanner{

    private DefaultSqlSessionFactory sessionFactory;

    public PojoScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }


    
    public void registerFilters() {
        this.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                    throws IOException {
                String anno = Table.class.getCanonicalName();
                return metadataReader.getAnnotationMetadata().hasAnnotation(anno);
            }
        });
    }

    /**
     * 对扫描的包下的类进行处理
     * @param basePackages
     * @return
     */
    @Override
	public int scan(String... basePackages) {
        Environment environment = sessionFactory.getEnvironment();
        int count = 0;
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                Class<?> clazz;
                try {
                    clazz = Class.forName(candidate.getBeanClassName());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Class not found");
                }
                String key = clazz.getAnnotation(Table.class).value();
                
                MappedBeanResult mppedResult = new MappedBeanResult(clazz);
                for (Field field : clazz.getDeclaredFields()) {
                    Column column = field.getAnnotation(Column.class);
                    String name = column==null ? field.getName() : column.value();
                    mppedResult.put(name, field);
                }
                environment.putResult(key, mppedResult);
                count++;
            }
        }
        return count;
    }


    public DefaultSqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(DefaultSqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
