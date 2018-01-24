package com.github.basic.maper;

import com.github.basic.session.SqlSession;

import java.lang.reflect.Method;

/**
 * 映射的方法
 */
public class MappedMethod {
	Method method;
	MappedStatement stmt;

	public MappedMethod(Method method) throws Exception {
		this.method = method;
		this.stmt = MappedStatement.getInstance(method);
	}
	
	public Object invoke(Object args[], SqlSession sqlSession) throws Exception {
		//System.out.println("invoke : " + anno.sql());
		//System.out.println("args : " + Arrays.toString(args));

		//System.out.println(sqlType);
		switch (stmt.getSqlType()) {
		case INSERT: case UPDATE: case DELETE:
			//todo
	//		return sqlSession.other(stmt, args);
		case SELECT:
			return sqlSession.select(stmt, args);
		case FLUSH:
			// TODO
			break;
		default:
			
		}
		return null;
	}
}
