package cn.bronzeware.core.ioc.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PointCut {

	private Method targetMethod;
	
	private Object targetObject;
	
	public Method getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	@Override
	public String toString() {
		return "PointCut [targetMethod=" + targetMethod + ", targetObject=" + targetObject + ", param="
				+ Arrays.toString(param) + "]";
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
