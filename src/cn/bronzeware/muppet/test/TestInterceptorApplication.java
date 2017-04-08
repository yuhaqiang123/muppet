package cn.bronzeware.muppet.test;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.test.B;
import cn.bronzeware.core.ioc.test.A;
import cn.bronzeware.core.ioc.test.C;
import cn.bronzeware.core.ioc.test.TestInterceptor;
import cn.bronzeware.util.testframework.Test;

@Test
@Component
public class TestInterceptorApplication {

	@Test
	public  void main(){
		ApplicationContext applicationContext = new AutowiredApplicationContext();
		C c = applicationContext.getBean(C.class);
		c.test();
	}
	
	
	
	public static void main(String[] args){
		TestInterceptorApplication test = new TestInterceptorApplication();
		test.main();
	}
	
}
