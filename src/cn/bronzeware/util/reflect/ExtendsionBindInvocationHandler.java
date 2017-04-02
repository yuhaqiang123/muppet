package cn.bronzeware.util.reflect;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public class ExtendsionBindInvocationHandler extends DefaultBindInvocationHandler{

	
	private ProxyInvocationHandler handler ;
	
	public ExtendsionBindInvocationHandler(ProxyInvocationHandler handler){
		
		this.handler = handler;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		before(proxy,method,args,null);
		Object result = handler.invoke(target, method, args);
		after(proxy,method,args,null);
		return result;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		before(proxy,method,params,methodProxy);
		Object result = handler.intercept(proxy, method, params, methodProxy);
		after(proxy,method,params,methodProxy);
		return result;
	}
	
	
	
}
