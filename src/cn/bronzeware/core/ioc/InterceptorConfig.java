package cn.bronzeware.core.ioc;

import java.lang.reflect.Method;

import cn.bronzeware.core.ioc.annotation.After;
import cn.bronzeware.core.ioc.annotation.Around;
import cn.bronzeware.core.ioc.annotation.Before;
import cn.bronzeware.core.ioc.annotation.Interceptor;
import cn.bronzeware.muppet.util.Utils;

public class InterceptorConfig {

	
	private ApplicationContext applicationContext;
	public InterceptorConfig(ApplicationContext  applicationContext){
		this.applicationContext = applicationContext;
	}
	
	public void config(Class clazz){
		Interceptor interceptor = (Interceptor)clazz.getAnnotation(Interceptor.class);
		if(Utils.notEmpty(interceptor)){
			Method[] methods = clazz.getDeclaredMethods();
			if(Utils.empty(methods)){
				return;
			}
			for(Method method:methods){
				Around around = method.getDeclaredAnnotation(Around.class);
				Before before = method.getDeclaredAnnotation(Before.class);
				After after = method.getDeclaredAnnotation(After.class);
			}
		}else{
			
		}
	}
	
	private void interceptorMethodAnnotationCheck(Method method){
		
	}
	
	
	
}
