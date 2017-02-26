package cn.bronzeware.core.ioc.annotation;

import java.lang.reflect.Field;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.BeanInitializationException;
import cn.bronzeware.core.ioc.InitializeException;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.util.reflect.ReflectUtil;


public class AutowiredExecutor {
	
	
	private ApplicationContext context = null;
	
	public AutowiredExecutor(ApplicationContext context){
		this.context = context;
	}
	
	public void execute(Field field, Object target){
		Autowired autowired = field.getAnnotation(Autowired.class);
		if ( Utils.notEmpty(autowired) ){
			String beanName = autowired.beanName();
			Class beanClazz = autowired.getClass();
			boolean required = autowired.required();
			if( !beanName.equals(Autowired.BEAN_NAME_DEFAULT)){
				Object object = null;
				//如果获取不到相关bean,检查是否时必须的，如果是报错，否则返回即可
				try{
					object = this.context.getBean(beanName);
				}catch (InitializeException e) {
					if(required){
						throw new BeanInitializationException(
								String.format("the bean which type is %s has the property named  %s without mapping bean ,autowired failed "
										,target.getClass(), field.getName()));
					}else{
						return ;
					}
				}
				//检查获取到的bean 是否是相关属性的子类型
				if(field.getType().isAssignableFrom(object.getClass())){
					ReflectUtil.setValue(field, target, object);
				}else{
					throw new BeanInitializationException(
							String.format("the bean named %s is not the child type of %s", beanName, field.getType().getName()));
				}
				
			}else{
				if( !beanClazz.equals(Autowired.TYPE_DEFAULT)){
					Object object = null;
					try{
						object = this.context.getBean(beanClazz);
					}catch (InitializeException e) {
						if(required){
							throw new BeanInitializationException(
									String.format("the bean which type is %s has the property named  %s without mapping bean ,autowired failed "
											,target.getClass(), field.getName()));
						}else{
							return;
						}
					}
					if(field.getType().isAssignableFrom( object.getClass())){
						ReflectUtil.setValue(field, target, object);
					}else{
						throw new BeanInitializationException(
								String.format("bean type of %s initialization error happend ,the bean which type is %s is not the child type of property named %s "
										, target.getClass()
										, object.getClass().getName()
										, field.getName()));
					}
					
				}
			}
		}else{
			//如果没有Autowired注释，直接返回
			return;
		}
		
	}
}
