package cn.bronzeware.core.ioc.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InterceptorMetaData {

	private Class targetAnnotationType;
	
	private String  fullyQualifiedClassNamePrefix  = null;
	
	private Class interceptorClazz = null;
	
	private Method interceptorMethod = null;
	
	private Annotation interceptorType = null;
	
	private Object interceptorObject;

	public Object getInterceptorObject() {
		return interceptorObject;
	}

	public void setInterceptorObject(Object interceptorObject) {
		this.interceptorObject = interceptorObject;
	}

	

	public Class getTargetAnnotationType() {
		return targetAnnotationType;
	}

	public void setTargetAnnotationType(Class targetAnnotationType) {
		this.targetAnnotationType = targetAnnotationType;
	}

	public String getFullyQualifiedClassNamePrefix() {
		return fullyQualifiedClassNamePrefix;
	}

	public void setFullyQualifiedClassNamePrefix(String fullyQualifiedClassNamePrefix) {
		this.fullyQualifiedClassNamePrefix = fullyQualifiedClassNamePrefix;
	}

	public Class getInterceptorClazz() {
		return interceptorClazz;
	}

	public void setInterceptorClazz(Class interceptorClazz) {
		this.interceptorClazz = interceptorClazz;
	}

	public Method getInterceptorMethod() {
		return interceptorMethod;
	}

	public void setInterceptorMethod(Method interceptorMethod) {
		this.interceptorMethod = interceptorMethod;
	}

	public Annotation getInterceptorType() {
		return interceptorType;
	}

	public void setInterceptorType(Annotation interceptorType) {
		this.interceptorType = interceptorType;
	}
	
}
