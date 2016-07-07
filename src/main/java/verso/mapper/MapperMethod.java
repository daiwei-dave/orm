package verso.mapper;

import java.lang.reflect.Method;
import java.util.Arrays;

import verso.annotation.Operation;
import verso.session.VSession;

public class MapperMethod 
{
	Operation anno;
	SqlCommandType sqlType;
	VSession session;
	Class<?> returnType;
	  	
	public MapperMethod(Method method, VSession session) {
		this.anno = method.getAnnotation(Operation.class);
		this.sqlType = SqlCommandType.UNKNOWN;
		if (anno == null) return;
		
		this.returnType = method.getReturnType();
		
		this.session = session;
		String sql = anno.sql();
		int index = sql.indexOf(' ');
		String firstWord = index==-1 ? sql : sql.substring(0, index);
		try {
			this.sqlType = SqlCommandType.valueOf(firstWord.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println("Unknown sql statement in Annotation of Method " + method.getName());
		}
	}
	
	public Object invoke(Object args[]) {
		System.out.println("invoke : " + anno.sql());
		System.out.println("args : " + Arrays.toString(args));

		System.out.println(sqlType);
		switch (sqlType) {
		case INSERT: case UPDATE: case DELETE:
			System.out.println("[Begin transaction]");
			System.out.println(anno.sql());
			System.out.println("[Commit transaction]");
			break;
		case SELECT:
			return session.select(anno, args, returnType);
		case FLUSH:
			session.test();
			//result = sqlSession.flushStatements();
			break;
		default:
			
		}
		return null;
	}
}
