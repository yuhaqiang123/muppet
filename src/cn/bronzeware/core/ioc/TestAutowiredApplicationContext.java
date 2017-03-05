package cn.bronzeware.core.ioc;

import java.nio.channels.Selector;

import cn.bronzeware.core.ioc.annotation.Autowired;
import cn.bronzeware.core.ioc.test.A;
import cn.bronzeware.core.ioc.test.B;
import cn.bronzeware.core.ioc.test.C;
import cn.bronzeware.muppet.util.autogenerate.Field;
import cn.bronzeware.muppet.util.log.Logger;

public class TestAutowiredApplicationContext {

	
	public static void test1(){
		ApplicationContext context = new AutowiredApplicationContext();
		//context.getBean(Selector.class);
		context.getBean(ApplicationContext.class);
		ApplicationContextAware applicationContextAware = context.getBean(ApplicationContextAware.class);
		if(applicationContextAware instanceof C){
			Logger.println("ok");
		}
		B b = context.getBean(B.class);
		Logger.println(b.a);
		if(b.a !=null && b.c!=null && b.applicationContext!=null ){
			Logger.println("autowried ok");
		}
		
	}
	
	public static void main(String[] args){
		test1();
		
	}
	
}
