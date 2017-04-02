package cn.bronzeware.core.ioc.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.sf.cglib.proxy.MethodProxy;

public class PointCut {

	private Method targetMethod;
	
	private Object targetObject;
	
	private MethodProxy methodProxy;
	
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

	private Object[] param;
	
}
