package com.github;


import com.github.basic.VDataSource;
import com.github.basic.dao.Dao;
import com.github.basic.session.*;
import com.github.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest1 {
    private static String driverClassName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/orm?useUnicode=true&characterEncoding=utf8";


    private static BeanFactory factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    /**
     * 使用原始jdbc
     * @throws Exception
     */
    @Test
    public void testWithJdbc() throws Exception {
        DataSource data = new VDataSource(driverClassName, url, "root", "123456");
        SqlSession sqlSession = new VSession(data);
        sqlSession.exec("select * from t_task where id=8");
    }


    @Test
    public  void test2() throws Exception {
        SessionFactory factory = new VSessionFactory("config.xml");
        SqlSession sqlSession = factory.openSession();
        Dao dao = sqlSession.getMapper(Dao.class);
        dao.findById(8);
    }


    /**
     *spring-orm测试
     * @throws Exception
     */
    @Test
    public  void testWithSpring() throws Exception {
        SessionFactory sessionFactory = (SessionFactory) factory.getBean("sessionFactory");
        SqlSession sqlSession = sessionFactory.openSession();
        Dao dao = sqlSession.getMapper(Dao.class);
        Task task = dao.findById(8);
        System.out.println(task.toString());
    }





}
