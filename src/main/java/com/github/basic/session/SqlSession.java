package com.github.basic.session;

import com.github.basic.maper.MappedStatement;

import java.sql.SQLException;

/**
 * <P>
 *     1.获取mapper接口（dao）的代理
 *     2.负责sql的执行
 * </P>
 */
public interface SqlSession {
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
     * @return a mapper bound to this SqlSession
     */
    public <T> T getMapper(Class<T> clazz);

    /**
     *select语句
     * @param mappedStmt
     * @param args
     * @return
     * @throws Exception
     */
    Object select(MappedStatement mappedStmt, Object[] args)
            throws Exception;
}
