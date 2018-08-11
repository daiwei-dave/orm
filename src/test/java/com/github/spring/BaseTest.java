package com.github.spring;

import com.github.basic.session.DefaultSqlSessionFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class BaseTest {

	private static BeanFactory factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    
	public static void main(String args[]) {
		System.out.println("init start");
		DefaultSqlSessionFactory sessionFactory = (DefaultSqlSessionFactory) factory.getBean("sessionFactory");
		System.out.println("init end");
	}
}
