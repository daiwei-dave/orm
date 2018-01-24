package com.github.basic.maper;

import java.sql.ResultSet;

public interface MappedResult {
	enum Type {
		MAP, BEAN, PRIMITIVE
	}
	
	Type getResultType();
	Object getResult(ResultSet rs, Class<?> returnType) throws Exception;
}
