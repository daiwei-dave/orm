package verso.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通过多种ClassLoader尝试读取，提高改进空间
 * @author tjg
 *
 */
public class ClassLoaderWrapper 
{
	private final ClassLoader cl[] = {
		ClassLoader.getSystemClassLoader(),
		Thread.currentThread().getContextClassLoader(),
	};
	
	private static class Holder {
		static ClassLoaderWrapper wrapper = new ClassLoaderWrapper(); 
	}
	public static ClassLoaderWrapper getInstance() { 
		return Holder.wrapper;
	}
	
	public InputStream loadResource(String resource) throws IOException {
		InputStream result = null;
		for (ClassLoader c : cl) 
		{
			if ((result = c.getResourceAsStream(resource)) != null || 
				(result = c.getResourceAsStream("/"+resource)) != null) 
				return result;
		}
		throw new IOException("Could not find resource: " + resource);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		for (ClassLoader c : cl) 
		try {
			Class<?> clazz = Class.forName(className, true, c);
			if (clazz != null) return clazz;
		} catch (ClassNotFoundException e) {
			// ignore
		}
    	throw new ClassNotFoundException("Could not find class: " + className);
    }
}