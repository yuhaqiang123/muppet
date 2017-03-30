package cn.bronzeware.core.ioc.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.util.reflect.ReflectUtil;

public class InterceptorManager {

	
	private ApplicationContext applicationContext;
	public InterceptorManager(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		applicationContext.registerBean(InterceptorManager.class, this);
		invocationHandler = new StandardBeanInterceptorInvocationHandler(applicationContext);
	}
	
	private List<InterceptorMetaData> interceptors = new ArrayList<>();
	
	public void addInterceptorMetaData(InterceptorMetaData interceptorMetaData){
		interceptors.add(interceptorMetaData);
	}
	
	
	private List<InterceptorMetaData> interceptorByAnnotation(Annotation annotation){
		List<InterceptorMetaData> list = new ArrayList<>();
		for(InterceptorMetaData interceptorMetaData: interceptors){
			if(interceptorMetaData.getTargetAnnotationType() != null
					&& interceptorMetaData.getTargetAnnotationType().equals(annotation.annotationType())){
				list.add(interceptorMetaData);
			}
		}
		return list;
	}
	
	private InvocationHandler invocationHandler;
	
	public Object intercept(Object target, Class[] paramClazzs,Object[] params){
		Class clazz = target.getClass();
		Annotation[] annotations = clazz.getDeclaredAnnotations();
		if(Utils.notEmpty(annotations)){
			for(Annotation annotation:annotations){
				List<InterceptorMetaData> list = interceptorByAnnotation(annotation);
				//ArrayUtil.println(list);
				if(list !=null && list.size()> 0){
					InvocationHandler invocationHandler = new InterceptorInvocationHandler(applicationContext, list);
					return ReflectUtil.getClassProxy(target, invocationHandler, paramClazzs, params);
				}
			}
		}else{
			
		}
		return ReflectUtil.getClassProxy(target, invocationHandler, paramClazzs, params);
	}
	
	
	
}
