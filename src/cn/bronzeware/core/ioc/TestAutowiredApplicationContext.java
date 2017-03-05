package cn.bronzeware.core.ioc;

import java.nio.channels.Selector;

import cn.bronzeware.core.ioc.test.A;

public class TestAutowiredApplicationContext {

	
	public static void test1(){
		ApplicationContext context = new AutowiredApplicationContext();
		//context.getBean(Selector.class);
		context.getBean(ApplicationContext.class);
		
	}
	
	public static void main(String[] args){
		test1();
	}
	
}
