package verso;

import dao.UserDao;
import verso.session.VSession;
import verso.session.VSessionFactory;

public class testSql 
{
	public static void main(String args[]) {
		VSessionFactory factory = VSessionFactory.getFactoryInstance("verso-config.xml");
		VSession session = factory.openSession();
		UserDao dao = (UserDao) session.getBean("userDao");
		dao.display();
	}
}
