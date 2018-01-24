package com.github.basic.maper;
import com.github.basic.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.HashMap;
import java.util.Map;


public class MapperProxy<T> implements InvocationHandler {

    private SqlSession sqlSession;
    private final Map<Method, MappedMethod> cache = new HashMap<>();

    /**
     * 生成某个接口的mapper
     * @param clazz
     * @param sqlSession
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, SqlSession sqlSession) {
        MapperProxy<T> proxy = new MapperProxy<>();
        proxy.sqlSession = sqlSession;
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, proxy);
    }

    /**
     * 调用mapper方法时实际执行的内容
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
        MappedMethod mappedMethod = cache.get(method);
        if (mappedMethod == null) {
            cache.put(method, mappedMethod = new MappedMethod(method));
        }
        return mappedMethod.invoke(args, sqlSession);
    }
}
