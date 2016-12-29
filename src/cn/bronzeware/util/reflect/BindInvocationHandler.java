package cn.bronzeware.util.reflect;

import java.lang.reflect.InvocationHandler;
interface BindInvocationHandler extends InvocationHandler{

	public Object bind(Object target)throws NullPointerException,IllegalArgumentException;
	public <T> T bind(Object target,Class<T> targetInterface)throws NullPointerException,IllegalArgumentException;
}
