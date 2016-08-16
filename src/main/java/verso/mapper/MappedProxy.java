package verso.mapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import verso.session.VSession;

public class MappedProxy<T> implements InvocationHandler {

	private VSession session;
	private final Map<Method, MappedMethod> CACHE = new HashMap<>();
	
	public static <T> Object newInstance(Class<T> clazz, VSession session) {
		MappedProxy<T> proxy = new MappedProxy<T>();
		proxy.session = session;
		return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, proxy);
	}

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
  {
	if (!CACHE.containsKey(method)) synchronized(CACHE) {
		if (!CACHE.containsKey(method)) {
			CACHE.put(method, new MappedMethod(method, session));
		}
	}
	return CACHE.get(method).invoke(args);
  }

}