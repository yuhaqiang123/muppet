package cn.bronzeware.muppet.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ClosedInvocationHandler implements InvocationHandler{

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(Closed.class.isInstance(proxy)){
			Closed close = (Closed)proxy;
			if(close.hasClosed()){
				throw new IllealInvokeException("已经关闭，没有权限访问");
			}else{
				return method.invoke(proxy, args);
			}
		}
		return method.invoke(proxy, args);
	}

	
	
}
