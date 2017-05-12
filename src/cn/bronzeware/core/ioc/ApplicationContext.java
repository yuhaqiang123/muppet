package cn.bronzeware.core.ioc;


/**
 * <p>ApplicationContext继承了BeanFactory接口,对外提供获取服务.
 * 并且自定义了注册方法,判断方法.ApplicationContext提供了完整的bean
 * 存取服务
 * <p>ApplicationContext接口是IOC暴露在外的少数接口之一,你可以通过传递该实例
 * ,完成bean之间依赖关系的管理,当你想传递某个组件时就将改组件注册进容器,容器
 * 托管后,任何想获取该组件的类都可以通过ApplicationContext
 * 获取.
 * <p>因此.bean之间本来显式的bean依赖配置,通过ApplicationContext管理起来.依赖
 * 关系统统都指向了ApplicationContext
 * <h3>ApplicationContext 并不像Spring 中ApplicationContext那样作为一个只读的
 * 接口,它对外提供注册服务.
 * @see AbstractApplicationContext
 * @see BeanFactory
 * @see BaseBeanFactory
 * @time 17/2/12.
 */
public interface ApplicationContext extends BeanFactory{

    public Object registerBean(String beanName,Object object);
    public Object registerBean(Object object);
    public Object registerBean(Class clazz, Object object);
    public <T> T containsBean(Class<T> clazz); 
}