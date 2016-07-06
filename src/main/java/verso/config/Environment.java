package verso.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {
	
	private DataSource data; 
	private Map<String, Class<?>> map = new ConcurrentHashMap<String, Class<?>>();

    
	public void putClass(String className, Class<?> clazz) {
		map.put(className, clazz);
	}
	public Class<?> getClass(String className) {
		return map.get(className);
	}
	public DataSource getDataSource() {
		return data;
	}
	public void setDataSource(DataSource dataSource) {
		this.data = dataSource;
	}
}
