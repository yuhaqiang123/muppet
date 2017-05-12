package cn.bronzeware.core.ioc;

/**
 * <p>当BeanFactroy,或者应用上下文无法找到对应的bean时,抛出此异常.
 * 
 * 
 * @see BeanFactory
 * @see ApplicationContext
 * @author yuhaiqiang  email: yuhaiqiangvip@sina.com
 * @time 17/2/12.
 */
public class SuchBeanNotFoundException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5420547096676377878L;
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
        this.beanClazz = clazz;
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
