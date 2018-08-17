package com.github.basic.session;

/**
 *session工厂
 * <P>
 *      1.初始化数据源
 *      2.创建一个session
 * </P>
 */
public interface SessionFactory {
    Session openSession();

    VSessionFactory getFactoryInstance(String resource);
}
