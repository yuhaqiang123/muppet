package cn.bronzeware.muppet.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyInvote implements InvocationHandler{

	private Object target;
	
	public <T> T bind(Class<T> targetInterface,Object target){
		this.target = target;
		Object object = Proxy.newProxyInstance(target.getClass().getClassLoader()
				, new Class[]{targetInterface},this);
		return (T)object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		System.out.println("invoke");
		return method.invoke(target, args);
	}

	
	
}
