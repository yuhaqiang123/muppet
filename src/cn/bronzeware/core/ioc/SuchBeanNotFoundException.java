package cn.bronzeware.core.ioc;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class SuchBeanNotFoundException extends RuntimeException{

    private Object beanName = null;
    public SuchBeanNotFoundException(String  beanName){
        this.beanName = beanName;
    }

    private Class<?> beanClazz;
    public <T> SuchBeanNotFoundException(Class<T> clazz){
        this.beanClazz = clazz;
    }

    public <T> SuchBeanNotFoundException(String beanName, Class<T> clazz){
        this.beanName = beanName;
        this.beanClazz = beanClazz;
    }

    @Override
    public String getMessage() {

        String msg = "";
        if(beanClazz != null && this.beanName == null){
            msg = "can not found the bean which className is :" +beanClazz.getName();
        }else if(this.beanName != null && this.beanClazz == null){
            msg = "can not found the bean which  mapping to this key :" + beanName;
        }
        else{
            msg = "can not found the bean which bean's className : " + beanClazz.getName() + " and beanName is :" + beanName;
        }
        return msg;
    }
}
