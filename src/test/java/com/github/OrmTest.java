package com.github;

import com.github.basic.dao.Dao;
import com.github.basic.session.*;
import com.github.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrmTest {

    private static BeanFactory factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");



    /**
     *spring-orm测试
     * @throws Exception
     */
    @Test
    public  void testWithSpring() throws Exception {
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) factory.getBean("defaultSqlSessionFactoryBean");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Dao dao = sqlSession.getMapper(Dao.class);
        Task task = dao.findById(8);
        System.out.println(task.toString());
    }





}
