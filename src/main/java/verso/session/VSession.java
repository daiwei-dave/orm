package verso.session;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import verso.config.DataSource;
import verso.config.Environment;
import verso.config.ResultMapper;
import verso.mapper.MapperProxy;

public class VSession 
{
	private Environment env;
	Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
	
    private Connection conn = null;
    
    public VSession(Environment env) {
		this.env = env;
        try {
        	DataSource data = env.getDataSource();
			conn = DriverManager.getConnection(data.getUrl(), data.getUsername(), data.getPassword());
			conn.setAutoCommit(false);//禁止自动提交，设置回滚点 
		} catch (SQLException e) {
			System.err.println(e);
		}
    }
    public void rollback() {
    	try {
			conn.rollback();
		} catch (SQLException e) {
			System.err.println(e);
		}
    }
    
	public Statement createStatement() {
		try {
			if (conn != null) return conn.createStatement();
		} catch (SQLException e) {
			System.err.println(e);
		}
		return null;
	}
	
	@Override
	public void finalize() {
       	if (conn == null) return;
        try {
        	conn.commit(); //事务提交 
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
	
	public Object select(String sql) 
	{
		try {
			ResultMapper mapper = env.getResult("user");
			List<Object> ans = new ArrayList<>();
			
			Statement stmt = createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			List<String> columns = new ArrayList<>();
			for (int i=1; i<=rsmd.getColumnCount(); i++) {
				String name = rsmd.getColumnName(i);
				if (mapper.get(name) != null) columns.add(name);
			}
			
			while (rs.next()) {
				Object obj = mapper.getClassInstance();
				for (String name : columns) {
					Field field = mapper.get(name);					
					field.setAccessible(true);
					field.set(obj, rs.getObject(name));
				}
				ans.add(obj);
			}
			return ans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
}
