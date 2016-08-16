package verso.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import verso.config.DataSource;
import verso.config.Environment;
import verso.mapper.MappedProxy;
import verso.mapper.MappedResult;
import verso.mapper.MappedStatement;
import verso.mapper.impl.MappedBeanResult;

public class VSession {
	private Environment env;
	Map<String, Object> cache = new HashMap<String, Object>();

	private Connection conn = null;

	public VSession(Environment env) {
		this.env = env;
		
		DataSource data = env.getDataSource();
		while (true) {
			try {
				conn = DriverManager.getConnection(data.getUrl(),
						data.getUsername(), data.getPassword());
				if (conn == null) {
					throw new SQLException();
				} else {
					conn.setAutoCommit(false);// 禁止自动提交，设置回滚点
					break;	//成功则结束循环
				}
			} catch (SQLException e) {
				try {
					//出现异常，睡一定时间后重连数据库
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	public void finish() {
		if (conn == null)
			return;
		try {
			conn.commit(); // 事务提交
			conn.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	public Object getBean(String name) {
		if (!cache.containsKey(name)) synchronized(cache) {
			if (!cache.containsKey(name)) {
				Class<?> clazz = env.getDao(name);
				cache.put(name, MappedProxy.newInstance(clazz, this));
			}
		}
		return cache.get(name);
	}

	public Object select(MappedStatement mappedStmt, Object[] args)
			throws Exception {
		// 获取配置好参数的sql
		PreparedStatement stmt = mappedStmt.createStatement(conn, args);
		ResultSet rs = stmt.executeQuery();
		// 奖ResultSet转为注解指定的resultType类型，存入函数的返回值returnType类型
		String resultType = mappedStmt.getResultType();
		Class<?> returnType = mappedStmt.getReturnType();
		MappedResult mapper = env.getResult(resultType);
		return mapper.getResult(rs, returnType);
	}

	public Object other(MappedStatement mappedStmt, Object[] args)
			throws Exception 
	{
		PreparedStatement stmt = mappedStmt.createStatement(conn, args);
		return stmt.executeUpdate();
	}

	public void test() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select p.id as author, p.name from book left join person p on book.author=p.id");
			ResultSetMetaData rsmd = rs.getMetaData();
			/*
			 * column name 表上的列名 column label 表上的列名或as的别名
			 */
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String name = rsmd.getColumnLabel(i);
				}
				System.out.println("-----------------------------");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
