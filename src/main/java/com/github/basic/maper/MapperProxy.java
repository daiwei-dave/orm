package com.github.basic.maper;
import com.github.basic.annotation.Sql;
import com.github.basic.session.Session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapperProxy<T> implements InvocationHandler {

    private Session session;

    /**
     * 生成某个接口的mapper
     * @param clazz
     * @param session
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Session session) {
        MapperProxy<T> proxy = new MapperProxy<>();
        proxy.session = session;
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
        Sql anno = method.getAnnotation(Sql.class);
        if (anno == null) {
            return null;
        }
        String sql = anno.value();
        Pattern pattern = Pattern.compile("\\{[^\\}]+\\}");
        Matcher m = pattern.matcher(sql);
        if (m.find()) {
            StringBuffer sb = new StringBuffer();
            do {
                //获取{param}，用BeanWrapper处理后的返回值来替换
                String s = m.group();
                int index = Integer.valueOf(s.substring(1, s.length() - 1));
                m.appendReplacement(sb, args[index].toString());
            } while (m.find());
            sql = m.appendTail(sb).toString();
        }
        System.out.println(sql);
        session.exec(sql);
        return null;
    }
}
