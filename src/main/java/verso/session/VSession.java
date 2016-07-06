package verso.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import verso.config.DataSource;
import verso.config.Environment;
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
			Class<?> clazz = env.getClass(name);
			cache.put(name, MapperProxy.newInstance(clazz, this));
		}
		return cache.get(name);
	}
}
