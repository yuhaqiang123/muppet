package cn.bronzeware.muppet.listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import cn.bronzeware.muppet.util.log.Logger;

public class ListernInvocationHandler implements InvocationHandler{

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		return method.invoke(proxy, args);
		//return null;
	}

}
