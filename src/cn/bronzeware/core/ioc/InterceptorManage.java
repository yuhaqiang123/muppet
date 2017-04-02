package cn.bronzeware.core.ioc;

public interface InterceptorManage {

	public Object intercept(Class targetClass, Class[] paramClazzs,Object[] params);
}
