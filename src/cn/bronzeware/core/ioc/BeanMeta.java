package cn.bronzeware.core.ioc;


import java.lang.annotation.Annotation;

import cn.bronzeware.core.ioc.annotation.Component;

public class BeanMeta {

	
	
	private Component.Scope socpe;
	
	private String beanName;
	
	private Class<?> clazz;
	
	private Annotation annotation; 
	
	private Class type;
	
	
	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public static final String DEFAULT_BEAN_NAME_PREFIX = "cn.bronzeware.ioc.beanname#";

	

	public Component.Scope getSocpe() {
		return socpe;
	}

	public void setSocpe(Component.Scope socpe) {
		this.socpe = socpe;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	
}
