package verso.mapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import verso.session.VSession;

public class MapperProxy<T> implements InvocationHandler {

	private VSession session;
	private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

	public static <T> Object newInstance(Class<T> clazz, VSession session) {
		MapperProxy<T> proxy = new MapperProxy<T>();
		proxy.session = session;
		return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, proxy);
	}

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
  {
	if (!methodCache.containsKey(method)) {
		methodCache.put(method, new MapperMethod(method, session));
	}
	return methodCache.get(method).invoke(args);
  }

}