package cn.bronzeware.core.ioc.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.util.reflect.ProxyInvocationHandler;
import net.sf.cglib.proxy.MethodProxy;

public class StandardBeanInterceptorInvocationHandler implements ProxyInvocationHandler{

	private ApplicationContext applicationContext;
	
	public StandardBeanInterceptorInvocationHandler(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	
		return method.invoke(proxy, args);
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		
		return methodProxy.invokeSuper(proxy, params);
	}

}
