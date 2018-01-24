package com.github.basic.maper;

import com.github.basic.annotation.Sql;
import com.github.basic.annotation.Table;
import com.github.basic.params.ParamParser;


import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MappedStatement
 * <P>
 *
 * </P>
 */
public class MappedStatement {
	
	private String sql;
	private SqlCommandType sqlType;
	// 函数返回值
	private Class<?> returnType;
	// 注解指定的返回类型
	private String resultType;
	private List<ParamParser> params = new ArrayList<>();
	private static Map<Method, MappedStatement> map = new HashMap<>();

	/**
	 * 获取MappedStatement实例
	 * @param method
	 * @return
	 * @throws Exception
	 */
	static public MappedStatement getInstance(Method method) throws Exception {
		
	    MappedStatement mappedStmt = map.get(method);
		if (mappedStmt == null) {
		    map.put(method, mappedStmt = new MappedStatement(method));
		}
		return mappedStmt;
	}

	/**
	 *sql遇见初始化
	 * @param method
	 * @throws Exception
	 */
	private MappedStatement(Method method) throws Exception {
		Sql anno = method.getAnnotation(Sql.class);
		returnType = method.getReturnType();
		resultType = anno.result();
		sql = anno.value();
		int index = sql.indexOf(' ');
		String firstWord = index==-1 ? sql : sql.substring(0, index);
		try {
			sqlType = SqlCommandType.valueOf(firstWord.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new Exception("Unknown sql statement in Annotation of Method " + method.getName());
		}
		// 若没有指定返回类型，并且sql语句为select
		if (sqlType == SqlCommandType.SELECT && "".equals(resultType)) {
		    Table table = returnType.getAnnotation(Table.class);
		    if (table != null) {
		        resultType = table.value();
		    }
		}
		Pattern p = Pattern.compile("\\{[^\\}]+\\}");
		Matcher m = p.matcher(sql);
		/**
		 * 参数绑定
		 */
		if (m.find()) {
			StringBuffer sb = new StringBuffer();
			do {
				//获取{param}，用BeanWrapper处理后的返回值来替换
				String s = m.group();
				params.add(new ParamParser(s));
				m.appendReplacement(sb, "?");//wrapper.get(args));
			} while (m.find());

			sql = m.appendTail(sb).toString();
		}
	}
	public PreparedStatement createStatement(Connection conn, Object args[]) throws Exception {

		PreparedStatement stmt = conn.prepareStatement(sql);
		for (int i=0; i<params.size(); i++) {
			Object org = params.get(i).calculate(args);
			stmt.setObject(i+1, org);
		}
		return stmt;		
	}
	
	public String getSql() {
		return sql;
	}
	public List<ParamParser> getParams() {
		return params;
	}
	public SqlCommandType getSqlType() {
		return sqlType;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public String getResultType() {
		return resultType;
	}
}
