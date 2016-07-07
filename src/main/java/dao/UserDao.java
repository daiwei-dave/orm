package dao;

import java.util.List;

import verso.annotation.Operation;
import model.User;

public interface UserDao {
	void insert(User t);
	void update(User t);
	@Operation(sql="select * from user", result="user")
	List<User> display();
	User findByFlag(String flag);
	User findByEmail(String email);
	
	@Operation(sql="flush", result="void")
	void test();
}
