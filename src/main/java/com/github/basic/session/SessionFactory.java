package com.github.basic.session;

import com.github.basic.config.Configuration;
import com.github.basic.config.Environment;

/**
 *session工厂
 * <P>
 *      1.初始化数据源
 *      2.创建一个session
 * </P>
 */
public interface SessionFactory {
    /**
     *开启一个SqlSession回话
     * @return
     */
    SqlSession openSession();
    Configuration getConfiguration();
    Configuration getConfiguration(Environment environment);
}
