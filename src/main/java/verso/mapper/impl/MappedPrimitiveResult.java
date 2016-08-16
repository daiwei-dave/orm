package verso.mapper.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import verso.mapper.MappedResult;

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
