package cn.bronzeware.core.ioc;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public interface BeanFactory {

    <T> T getBean(Class<T> clazz);

    <T> T getBean(String beanName, Class<T> clazz);

    Object getBean(String beanName);

    Object[] getBeans();
}


