package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.annotation.After;
import cn.bronzeware.core.ioc.annotation.Around;
import cn.bronzeware.core.ioc.annotation.Before;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.Interceptor;
import cn.bronzeware.core.ioc.aop.PointCut;
import net.sf.cglib.proxy.MethodProxy;

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
	
	@Around(annotation = TestAop.class)
	public void around(PointCut pointCut){
		/*MethodProxy method = pointCut.getMethodProxy();
		Object proxy = pointCut.getTargetObject();
		Object[] params = pointCut.getParam();
		try {
			//method.invokeSuper(proxy, params);
		} catch (Throwable e) {
			e.printStackTrace();
		}*/
	}
	
	//@Around(annotation = TestAop.class)
	public void aroundq(PointCut pointCut){
		/*MethodProxy method = pointCut.getMethodProxy();
		Object proxy = pointCut.getTargetObject();
		Object[] params = pointCut.getParam();
		try {
			//method.invokeSuper(proxy, params);
		} catch (Throwable e) {
			e.printStackTrace();
		}*/
	}
	
	
}
