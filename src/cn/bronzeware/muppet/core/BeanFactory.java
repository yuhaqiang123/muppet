package cn.bronzeware.muppet.core;

public interface BeanFactory {

	public <T> T  getBean(Class<T> clazz) ;
	
	public Object getBean(String beanName);
	
	
}
