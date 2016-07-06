package dao;

import java.util.List;

import verso.annotation.Operation;
import model.User;

public interface UserDao {
	@Operation("insert into user where id=#id")
	void insert(User t);
	void update(User t);
	@Operation("select * from user")
	List<User> display();
	@Operation("test somthing")
	User findByFlag(String flag);
	User findByEmail(String email);
}
