package com.asti.myservlets.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.asti.myservlets.beans.Hello;

public class XmlConfigTest {

	public static void main(String[] args) {
		//Hello h = new Hello();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring_config.xml");
		
		Hello h = context.getBean("hello", Hello.class);
		System.out.println(h.greet());
	}

}
