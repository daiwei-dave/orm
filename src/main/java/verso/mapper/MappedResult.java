package verso.mapper;

import java.sql.ResultSet;

public interface MappedResult {
	enum Type {
		MAP, BEAN, PRIMITIVE
	}
	
	Type getResultType();
	Object getResult(ResultSet rs, Class<?> returnType) throws Exception;
}
