package com.github.basic.maper.impl;



import com.github.basic.maper.MappedResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MappedPrimitiveResult implements MappedResult {

	@Override
	public Type getResultType() {
		return Type.PRIMITIVE;
	}

	@Override
	public Object getResult(ResultSet rs, Class<?> returnType) throws Exception {		
		if (returnType == List.class || returnType.isArray()) {
			List<Object> ans = new ArrayList<>();
			while (rs.next()) {
				ans.add(rs.getObject(1));
			}
			if (returnType.isArray()) return ans.toArray();
			return ans;
		} else {
			while (rs.next()) {
				return rs.getObject(1);
			}
			return null;
		}
	}

}
