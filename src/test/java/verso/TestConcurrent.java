package verso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestConcurrent extends Thread {

	private final static Map<String, Object> CACHE = new HashMap<>();

	public Object calc() {
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (Exception e) {
			
		}
		System.out.println("finish");
		return 0;
	}
	
	@Override
	public void run() {
		String key = "test";
		if (!CACHE.containsKey(key)) synchronized(CACHE) {
			if (!CACHE.containsKey(key))
				CACHE.put(key, calc());
		}
		System.out.println(CACHE.get(key));
	}
	
	public static void main(String args[]) {
		for (int i=0; i<10; i++) {
			new TestConcurrent().start();
		}
	}
}
