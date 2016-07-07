package verso.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ResultMapper {
	private Class<?> clazz;
	private Map<String, Field> propMap = new HashMap<>();
	
	public ResultMapper(Class<?> clazz) {
		this.clazz = clazz;
	}
	public void put(String key, Field value) {
		propMap.put(key, value);
	}
	
	public Object getClassInstance() throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	public Field get(String key) {
		return propMap.get(key);
	}
}
