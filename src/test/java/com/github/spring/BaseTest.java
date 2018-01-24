package com.github.spring;

import com.github.basic.session.VSessionFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class BaseTest {

	private static BeanFactory factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    
	public static void main(String args[]) {
		System.out.println("start init");
		VSessionFactory sessionFactory = (VSessionFactory) factory.getBean("sessionFactory");
	}
}
