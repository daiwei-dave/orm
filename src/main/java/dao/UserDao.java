package dao;

import java.util.List;

import pojo.User;
import verso.annotation.Operation;

public interface UserDao {
	@Operation("insert into user (name, password, email) values({name}, {password}, {email})")
	void insert(User user);
	@Operation("update user set name={name}, password={password}, email={email}, flag={flag} where id={id}")
	void update(User t);
	@Operation(value="select * from user", result="user")
	List<User> display();
	
	@Operation(value="select * from user where name={0}", result="user")
	User getByName(String name);
	@Operation(value="select name from user where flag={0}", result="string")
	String getNameByFlag(String flag);
	
	@Operation("flush")
	void test();
}
