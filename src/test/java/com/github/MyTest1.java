package com.github;


import com.github.basic.VDataSource;
import com.github.basic.dao.Dao;
import com.github.basic.dao.UserDao;
import com.github.basic.pojo.User;
import com.github.basic.session.Session;
import com.github.basic.session.SessionFactory;
import com.github.basic.session.VSession;
import com.github.basic.session.VSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest1 {
    private static String driverClassName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/orm?useUnicode=true&characterEncoding=utf8";

    /**
     * 不通过xml配置文件加载数据源
     * @throws Exception
     */
    @Test
    public  void test1() throws Exception {
        DataSource data = new VDataSource(driverClassName, url, "root", "123456");
        Session session = new VSession(data);
        session.exec("select * from t_task where id=8");
    }


    @Test
    public  void test2() throws Exception {
        SessionFactory factory = new VSessionFactory("config.xml");
        Session session = factory.openSession();
        Dao dao = session.getMapper(Dao.class);
        dao.findById(8);
    }




    /**
     *insert
     * @throws Exception
     */
    @Test
    public  void testInsert() throws Exception {
        VSessionFactory vSessionFactory=new VSessionFactory();
        VSessionFactory factory = vSessionFactory.getFactoryInstance("config.xml");
        VSession session = factory.openSession();
        try {
            UserDao dao =  session.getMapper(UserDao.class);
            User user = new User();
            user.setName("test");
            user.setPassword("password");
            user.setEmail("test@verso.com");
            dao.insert(user);
            session.commit();
        } catch (Exception e) {
            session.rollback();
        }
    }



}
