package verso.mapper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import verso.annotation.Operation;
import verso.session.VSession;

public class MapperMethod 
{
	String sql;
	String sqlArray[];
	SqlCommandType sqlType;
	VSession session;
	  	
	public MapperMethod(Method method, VSession session) {
		Operation opt = method.getAnnotation(Operation.class);
		this.sqlType = SqlCommandType.UNKNOWN;
		if (opt == null) return;
		
		this.session = session;
		this.sql = opt.value();
		this.sqlArray = opt.value().split(" ");
		try {
			this.sqlType = SqlCommandType.valueOf(sqlArray[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println("Unknown sql statement in Annotation of Method " + method.getName());
		}
	}
	
	public Object invoke(Object args[]) {
		System.out.println("invoke : " + Arrays.toString(sqlArray));
		System.out.println("args : " + Arrays.toString(args));

		System.out.println(sqlType);
		switch (sqlType) {
		case INSERT: case UPDATE: case DELETE:
			System.out.println("[Begin transaction]");
			System.out.println(Arrays.toString(sqlArray));
			System.out.println("[Commit transaction]");
			break;
		case SELECT:
			try {
				Statement stmt = session.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					System.out.println(rs.getString("email"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(Arrays.toString(sqlArray));
			break;
		case FLUSH:
			//result = sqlSession.flushStatements();
			break;
		default:
			
		}
		return null;
	}
}
