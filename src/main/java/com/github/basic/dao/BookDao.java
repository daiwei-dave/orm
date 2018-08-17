package com.github.basic.dao;

import com.github.basic.annotation.Sql;
import com.github.basic.pojo.Book;


public interface BookDao {
	@Sql(value="select * from book b left join person p on b.author=p.id where p.name={0}",
			result="book")
	Book findByName(String name);
}
