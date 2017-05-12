package cn.bronzeware.core.ioc;

import cn.bronzeware.muppet.util.ArrayUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>{@link BeanFactory}接口的基本实现,提供了四种获取bean的方式.
 * <li>{@link #getBean(Class)}
 * <li>{@link #getBean(String)}
 * <li>{@link #getBean(String, Class)}
 * <li>{@link #getBeans()}
 * 具体请参考 {@link BeanFactory}接口
 * <h3>除实现BeanFactory接口外, {@link BaseBeanFactory}还提供了注册服务.
 * <p>{@link #registerBean(Class, Object)}注册Class类型的bean实例.
 * <p>{@link #registerBean(Object)} 通过获取object对象的类型,注册bean实例
 * <p> {@link #registerBean(String, Object)} 按照给定beanName注册bean实例
 * 
 * @see ApplicationContext
 * 
 * @throws SuchBeanNotFoundException
 * @author yuhaiqiang  email: yuhaiqiangvip@sina.com
 * @time 17/2/12.
 */
public class BaseBeanFactory implements BeanFactory{


    private HashMap<String,Object> beanNameMap = new HashMap<String,Object>();

    private HashMap<Class<?>,Object> clazzMap = new HashMap<>();

    @Override
    public <T> T getBean(Class<T> clazz) {
        if(clazzMap.containsKey(clazz)){
            return (T)clazzMap.get(clazz);
        }
        else{
            for(Map.Entry<Class<?>, Object> entry:clazzMap.entrySet()){
                if(entry.getValue().getClass().equals(clazz)){
                    return (T)entry.getValue();
                }
            }
        }
        throw new SuchBeanNotFoundException(clazz);
    }

    public boolean hasBean(String beanName){
        try{
            this.getBean(beanName);
            return true;
        } catch (SuchBeanNotFoundException e){
            return false;
        }
    }

    public <T> boolean hasBean(Class<T> beanClazz){
        try{
            this.getBean(beanClazz);
            return true;
        }catch (SuchBeanNotFoundException e){
            return false;
        }
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        if(this.hasBean(beanName)){
            Object bean = getBean(beanName);
            if(bean.getClass().equals(clazz)){
                return (T)bean;
            }
            else if(this.hasBean(clazz) == false){
                return (T)bean;
            }
            else{
                //如果存在beanClass的bean
                throw new SuchBeanNotFoundException(beanName, clazz);
            }
        }else{
            //如果不存在相应key值的bean但是存在相应class的bean
            if(this.hasBean(clazz)){
                T bean = this.getBean(clazz);
                return bean;
            }
            else{
                //如果均不存在则抛出异常
                throw new SuchBeanNotFoundException(beanName ,clazz);
            }
        }
    }



    @Override
    public Object getBean(String beanName) {
        if(beanNameMap.containsKey(beanName)){
            return beanNameMap.get(beanName);
        }else{
            throw new SuchBeanNotFoundException(beanName);
        }
    }

    public Object registerBean(String beanName,Object object){
        Object oldObject = null;
        if(beanNameMap.containsKey(beanName)){
            oldObject =  beanNameMap.get(beanName);
        }

        beanNameMap.put(beanName, object);
        return oldObject;
    }

    public Object registerBean(Object object){
        Class clazz = object.getClass();
       return registerBean(clazz, object);
    }

    public Object registerBean(Class clazz, Object object){
        Object oldObject = null;
        if(clazzMap.containsKey(clazz)){
            oldObject = clazzMap.get(clazz);
        }
        clazzMap.put(clazz, object);
        return oldObject;
    }

    @Override
    public Object[] getBeans() {
        Object[] beanNameObjects = beanNameMap.values().toArray();
        Object[] clazzObjects = clazzMap.values().toArray();
        Object[] result = new Object[beanNameObjects.length + clazzObjects.length];
        System.arraycopy(beanNameObjects,0,result,0,beanNameObjects.length);
        System.arraycopy(clazzObjects, 0, result, beanNameObjects.length, clazzObjects.length);
        return result ;
    }
}
