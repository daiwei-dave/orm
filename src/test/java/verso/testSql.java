package verso;

import pojo.Book;
import pojo.User;
import dao.BookDao;
import dao.UserDao;
import verso.session.VSession;
import verso.session.VSessionFactory;

public class testSql 
{
	public static void main(String args[]) {	
		VSessionFactory factory = VSessionFactory.getFactoryInstance("verso-config.xml");
		VSession session = factory.openSession();
		UserDao dao = (UserDao) session.getBean("userDao");
		try {
			dao.insert("test", "password");
			for (User i : dao.display()) {
				System.out.printf("[id=%d] : name=%s,email=%s,password=%s\n",
						i.getId(), i.getName(), i.getEmail(), i.getPassword());
			}
			BookDao bookDao = (BookDao) session.getBean("bookDao");
			Book ans = bookDao.findByName("Harold Abelson");
			System.out.println(ans.getName());
		} catch (Throwable e) {
			session.rollback();
		} finally {
			session.finish();
		}
	}
}
