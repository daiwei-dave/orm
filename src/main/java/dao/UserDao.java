package dao;

import java.util.List;

import pojo.User;
import verso.annotation.Operation;

public interface UserDao {
	@Operation(sql="insert into user (name, password) values({0}, {1})", result="void")
	void insert(String name, String password);
	void update(User t);
	@Operation(sql="select * from user", result="user")
	List<User> display();
	User findByFlag(String flag);
	User findByEmail(String email);
	@Operation(sql="flush", result="void")
	void test();
}
