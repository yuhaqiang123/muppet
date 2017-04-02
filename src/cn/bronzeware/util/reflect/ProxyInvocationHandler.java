package cn.bronzeware.util.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public interface ProxyInvocationHandler extends InvocationHandler{

	
	public Object intercept(Object proxy, Method method, Object[] params,
			MethodProxy methodProxy) throws Throwable ;
}
