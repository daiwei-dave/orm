package verso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pojo.Book;
import pojo.User;
import dao.BookDao;
import dao.UserDao;
import verso.session.VSession;
import verso.session.VSessionFactory;

public class testSql 
{
	static VSessionFactory factory = VSessionFactory.getFactoryInstance("verso-config.xml");	
	
	static class TestRunnable implements Runnable {
		static int count = 0;
		private int id;
		
		public TestRunnable() {
			id = count++;
		}
		public void run() {
			VSession session = factory.openSession();
			UserDao dao = (UserDao) session.getBean("userDao");
			try {
				dao.insert("test", "password");
				System.out.println(id+":"+dao.display().size());
				/*
				for (User i : dao.display()) {
					System.out.printf("[id=%d] : name=%s,email=%s,password=%s\n",
							i.getId(), i.getName(), i.getEmail(), i.getPassword());
				}
				
				BookDao bookDao = (BookDao) session.getBean("bookDao");
				Book ans = bookDao.findByName("Harold Abelson");
				System.out.println(ans.getName());*/
			} catch (Throwable e) {
				e.printStackTrace();
				session.rollback();
			} finally {
				session.finish();
			}
		}
	}
	
	public static void main(String args[])
	{
		long startTime = System.currentTimeMillis();
		
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i=0; i<1000; i++) {
			exec.execute(new TestRunnable());
		}
		exec.shutdown();
		while (!exec.isTerminated()) {
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
