package dao;

import model.Book;
import verso.annotation.Operation;

public interface BookDao {
	@Operation(sql="select * from book b left join person p on b.author=p.id where p.name={0}",
			result="book")
	Book findByName(String name);
}
