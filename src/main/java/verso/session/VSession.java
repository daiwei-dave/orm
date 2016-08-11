package verso.session;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import verso.annotation.Operation;
import verso.config.DataSource;
import verso.config.Environment;
import verso.config.ResultMapper;
import verso.mapper.MapperProxy;

public class VSession {
	private Environment env;
	Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

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
		if (!cache.containsKey(name)) {
			Class<?> clazz = env.getDao(name);
			cache.put(name, MapperProxy.newInstance(clazz, this));
		}
		return cache.get(name);
	}

	public Object select(Operation anno, Object[] args, Class<?> returnType)
			throws Exception {
		String sql = anno.sql();
		Pattern p = Pattern.compile("\\{[0-9]+\\}");
		Matcher m = p.matcher(sql);
		if (m.find()) {
			StringBuffer sb = new StringBuffer();
			while (true) {
				String s = m.group();
				Object arg = args[Integer
						.valueOf(s.substring(1, s.length() - 1))];
				// 补上字符串的标识
				if (arg instanceof String)
					arg = "'" + arg + "'";
				m.appendReplacement(sb, arg.toString());
				if (!m.find())
					break;
			}
			sql = sb.toString();
		}

		ResultMapper mapper = env.getResult(anno.result());

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		List<String> columns = new ArrayList<>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String name = rsmd.getColumnLabel(i);
			if (mapper.get(name) != null)
				columns.add(name);
		}
		if (returnType == List.class || returnType.isArray()) {
			List<Object> ans = new ArrayList<>();
			while (rs.next()) {
				Object obj = mapper.getClassInstance();
				for (String name : columns) {
					Field field = mapper.get(name);
					field.setAccessible(true);
					field.set(obj, rs.getObject(name));
				}
				ans.add(obj);
			}
			if (returnType.isArray())
				return ans.toArray();
			return ans;
		} else {
			while (rs.next()) {
				Object obj = mapper.getClassInstance();
				for (String name : columns) {
					Field field = mapper.get(name);
					field.setAccessible(true);
					field.set(obj, rs.getObject(name));
				}
				return obj;
			}
			return null;
		}
	}

	public Object other(Operation anno, Object[] args, Class<?> returnType)
			throws Exception {
		String sql = anno.sql();
		Pattern p = Pattern.compile("\\{[0-9]+\\}");
		Matcher m = p.matcher(sql);
		if (m.find()) {
			StringBuffer sb = new StringBuffer();
			while (true) {
				String s = m.group();
				Object arg = args[Integer
						.valueOf(s.substring(1, s.length() - 1))];
				// 补上字符串的标识
				if (arg instanceof String)
					arg = "'" + arg + "'";
				m.appendReplacement(sb, arg.toString());
				if (!m.find())
					break;
			}
			sql = m.appendTail(sb).toString();
		}
		System.err.println(sql);

		ResultMapper mapper = env.getResult(anno.result());
		//PreparedStatement stmt = conn.prepareStatement(sql);
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		Integer result = stmt.executeUpdate();
		System.out.println(result);
		return result;
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
