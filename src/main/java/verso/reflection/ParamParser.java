package verso.reflection;

import java.lang.reflect.Field;

/**
 * @author DELL
 * Parse parameter which like "obj.field.field"
 */
public class ParamParser {
	private String levels[];
	private Field fields[];
	private Class<?> clazz = null;
	
	public ParamParser(String param) throws Exception {
		if (!param.startsWith("{") || !param.endsWith("}")) {
			throw new Exception("Syntex wrong : {param} but not " + param);
		}
		levels = param.substring(1, param.length() - 1).split("\\.");
	}
	
	/**
	 * 
	 * @param args 计算{param}时，代入的参数
	 * @return 返回{param}的值
	 * @throws Exception
	 * 注意此处写了优化，将先前计算出的Field存了下来，供下次使用
	 * (前提：如果下一次代入的参数的Class与先前一样)
	 * @即是说如果第一次代入A {B x}而下一次代入A {C x}将出错 
	 */
	public Object calculate(Object args[]) throws Exception {
		Object arg = null;		// arg 计算第一个参数
		Integer index = 0;
		try {
			arg = args[Integer.valueOf(levels[0])];
			index++;
		} catch (Exception e) {
			arg = args[0];
		}
		// 第一次调用某方法时的初始化，由于并发所以同步一下
		if (fields == null) synchronized(this) {
			if (fields == null) {
				fields = new Field[levels.length];
				clazz = arg.getClass();
				for (; index < levels.length; index++) 
				{
					// getDeclaredField : can get private field
					String name = levels[index];
					Field field = arg.getClass().getDeclaredField(name);
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					fields[index] = field;
					arg = field.get(arg);
				}
				return arg;
			}
		}
		
		// 如果与前次是同一个类，则使用fields的缓存
		boolean flag = clazz == arg.getClass();
		for (; index < levels.length; index++) {
			if (flag) { 
				arg = fields[index].get(arg);
			} else {
				// getDeclaredField : can get private field
				String name = levels[index];
				Field field = arg.getClass().getDeclaredField(name);
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				arg = field.get(arg);
			}
		}
		return arg;
	}
}
