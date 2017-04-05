package cn.bronzeware.core.ioc.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.sf.cglib.proxy.MethodProxy;

public class PointCut {

	private Method targetMethod;
	
	private Object targetObject;
	
	private MethodProxy methodProxy;
	
	private Object returnValue;
	
	private Object[] param;
	
	public Object invoke() throws Throwable{
		Object result = null;
		if(methodProxy == null){
			try {
				result = targetMethod.invoke(targetObject, param);
			} catch (IllegalAccessException e) {
				throw e;
			} catch (IllegalArgumentException e) {
				throw e;
			} catch (InvocationTargetException e) {
				throw e;
			}
		}else{
			try {
				result = methodProxy.invokeSuper(targetObject, param);
			} catch (Throwable e) {
				throw e;
			}
		}
		return result;
	}
	
	
	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public String toString() {
		
		return String.format("Class %s, Method: %s,"
				, targetObject.getClass().getSuperclass().getName()
				, targetMethod.getName());
	}

	public MethodProxy getMethodProxy() {
		return methodProxy;
	}

	public void setMethodProxy(MethodProxy methodProxy) {
		this.methodProxy = methodProxy;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public Object[] getParam() {
		return param;
	}

	public void setParam(Object[] param) {
		this.param = param;
	}

	
	
}
