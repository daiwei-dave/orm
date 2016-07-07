package verso;

import model.User;

import dao.UserDao;
import verso.session.VSession;
import verso.session.VSessionFactory;

public class testSql 
{
	public static void main(String args[]) {
		VSessionFactory factory = VSessionFactory.getFactoryInstance("verso-config.xml");
		VSession session = factory.openSession();
		UserDao dao = (UserDao) session.getBean("userDao");
		for (User i : dao.display()) {
			System.out.printf("id=%d,name=%s,email=%s,password=%s\n",
					i.getId(), i.getName(), i.getEmail(), i.getPassword());
		}
	}
}
