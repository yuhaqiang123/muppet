package cn.bronzeware.core.ioc;


/**
 * Created by yuhaiqiang on 17/2/12.
 *
 * 负责启动整个容器，并对外提供ioc服务
 */
public interface ApplicationContext extends BeanFactory{

    public Object registerBean(String beanName,Object object);
    public Object registerBean(Object object);
    public Object registerBean(Class clazz, Object object);
    public <T> T containsBean(Class<T> clazz); 
}
