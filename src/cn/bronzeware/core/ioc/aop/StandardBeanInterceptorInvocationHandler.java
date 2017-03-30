package cn.bronzeware.core.ioc.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import cn.bronzeware.core.ioc.ApplicationContext;

public class StandardBeanInterceptorInvocationHandler implements InvocationHandler{

	private ApplicationContext applicationContext;
	
	public StandardBeanInterceptorInvocationHandler(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	
		return method.invoke(proxy, args);
	}

}
