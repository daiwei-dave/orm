package com.github.basic.session;

import java.sql.SQLException;

/**
 * <P>
 *     1.获取mapper接口（dao）的代理
 *     2.负责sql的执行
 * </P>
 */
public interface Session {
    /**
     * 执行sql
     * @param sql
     * @throws SQLException
     */
    void exec(String sql) throws SQLException;

    /**
     * 获取mapper接口（dao）的代理
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> clazz);
}
