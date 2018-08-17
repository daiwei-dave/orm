package com.github.basic.dao;



import com.github.basic.annotation.Sql;
import com.github.basic.pojo.User;

import java.util.List;

public interface UserDao {
	@Sql("insert into user (name, password, email) values({name}, {password}, {email})")
	void insert(User user);
	@Sql("update user set name={name}, password={password}, email={email}, flag={flag} where id={id}")
	void update(User t);
	@Sql(value="select * from user", result="user")
	List<User> display();
	
	@Sql(value="select * from user where name={0}", result="user")
	User getByName(String name);
	@Sql(value="select name from user where flag={0}", result="string")
	String getNameByFlag(String flag);
	
	@Sql("flush")
	void test();
}
