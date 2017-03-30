package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.annotation.After;
import cn.bronzeware.core.ioc.annotation.Before;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.Interceptor;
import cn.bronzeware.core.ioc.aop.PointCut;

@Component
@Interceptor
public class TestInterceptor {

	public TestInterceptor() {
		System.out.println("xxxx");
	}
	
	@Before(annotation=TestAop.class)
	public void method(PointCut pointCut){
		System.out.println(pointCut);
	}
	
	@After(annotation=TestAop.class)
	public void hello(PointCut pointCut){
		System.out.println("after");
	}
	
}
