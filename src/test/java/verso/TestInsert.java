package verso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import pojo.Book;
import pojo.User;
import dao.BookDao;
import dao.UserDao;
import verso.session.VSession;
import verso.session.VSessionFactory;

/**
 * @author DELL
 * Operating System : Windows 10
 * insert size=2000 items with three property :
 * Version 1 Normal : 15991~18000ms
 * Version 2 PreparedStatement : first 45K;  later 13K, 14K
 * Version 3 Field Cache & Module: 16K, 17K
 * 
 */

public class TestInsert 
{
	static VSessionFactory factory = VSessionFactory.getFactoryInstance("verso-config.xml");
	// 线程完成数
	static AtomicInteger count = new AtomicInteger(0);
	static int size = 2000;
	
	static class TestRunnable implements Runnable {
		public void run() {
			VSession session = factory.openSession();
			UserDao dao = (UserDao) session.getBean("userDao");
			try {
				User user = new User();
				user.setName("test");
				user.setPassword("password");
				user.setEmail("test@verso.com");
				dao.insert(user);
			} catch (Throwable e) {
				e.printStackTrace();
				session.rollback();
			} finally {
				session.finish();			
			}
			// 每完成一百个线程打印提示一下
			int value = count.incrementAndGet();
			if (value % 100 == 0) System.out.println(value);
		}
	}
	
	public static void main(String args[])
	{
		long startTime = System.currentTimeMillis();
		
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i=0; i<size; i++) {
			exec.execute(new TestRunnable());
		}
		exec.shutdown();
		while (count.get() < size) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.printf("total ms: %d\n", endTime - startTime);
	}
}