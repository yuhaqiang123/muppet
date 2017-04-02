package cn.bronzeware.muppet.test;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.core.ioc.test.B;
import cn.bronzeware.core.ioc.test.A;
import cn.bronzeware.core.ioc.test.C;
import cn.bronzeware.core.ioc.test.TestInterceptor;

public class TestInterceptorApplication {

	public static void main(String[] args){
		ApplicationContext applicationContext = new AutowiredApplicationContext();
		C c = applicationContext.getBean(C.class);
		c.test();
	}
}
