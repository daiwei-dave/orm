package com.github.basic.session;

import com.github.basic.config.Configuration;
import com.github.basic.config.Environment;
import com.github.basic.maper.MappedResult;
import com.github.basic.maper.MappedStatement;
import com.github.basic.maper.MapperProxy;

import javax.sql.DataSource;
import java.sql.*;

public class DefaultSqlSession implements SqlSession {

	private Configuration configuration;
    private DataSource dataSource;
    private Connection conn;
	private Environment environment;
    
    public DefaultSqlSession(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	public DefaultSqlSession(Environment environment) {
		this.environment = environment;
	}
    
    public Connection getConnection() throws SQLException {
        if (conn == null) {
			DataSource dataSource = environment.getDataSource();
			conn = dataSource.getConnection();
        }
        return conn;
    }


    public Configuration getConfiguration() throws SQLException {
        if (configuration == null) {
      //      configuration = dataSource.getConnection();
        }
        return configuration;
    }
    @Override
    public void exec(String sql) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
		    stmt = getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println("--------------------print--------------------");
			while (rs.next()) {
			    for (int i=1; i<=rsmd.getColumnCount(); i++) {
			        System.out.println(rsmd.getColumnLabel(i)+"="+rs.getObject(i));
			    }
			    System.out.println();
			}
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
		}
    }

	@Override
	public <T> T getMapper(Class<T> clazz) {
		return MapperProxy.newInstance(clazz, this);
	}

	/**
	 *执行select语句
	 * @param mappedStmt
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object select(MappedStatement mappedStmt, Object[] args) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = mappedStmt.createStatement(getConnection(), args);
			rs = stmt.executeQuery();
			// 奖ResultSet转为注解指定的resultType类型，存入函数的返回值returnType类型
			String resultType = mappedStmt.getResultType();
			Class<?> returnType = mappedStmt.getReturnType();
			MappedResult mapper =environment.getResult(resultType);
			return mapper.getResult(rs, returnType);
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
		}
	}



    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
