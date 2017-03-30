package cn.bronzeware.core.ioc.aop;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.annotation.After;
import cn.bronzeware.core.ioc.annotation.Around;
import cn.bronzeware.core.ioc.annotation.Before;

public class InterceptorInvocationHandler implements InvocationHandler{

	private ApplicationContext applicationContext;
	
	private List<InterceptorMetaData> interceptorMetaDatas;
	
	private InterceptorMetaData[] preInterceptorArray = null;
	
	private InterceptorMetaData[] postInterceptorArray = null;
	
	private InterceptorMetaData aroundMetaData = null;
	
	public InterceptorInvocationHandler(ApplicationContext applicationContext, List<InterceptorMetaData> list) {
		this.applicationContext = applicationContext;
		this.interceptorMetaDatas = list;
		List<InterceptorMetaData> preMethods = new ArrayList<>();
		
		List<InterceptorMetaData> postMethods = new ArrayList<>();
		for(InterceptorMetaData metaData:list){
			Annotation annotation = metaData.getInterceptorType();
			if(Before.class.isAssignableFrom(annotation.annotationType())){
				preMethods.add(metaData);
			}
			if(After.class.isAssignableFrom(annotation.annotationType())){
				postMethods.add(metaData);
			}
			if(Around.class.isAssignableFrom(annotation.annotationType())){
				aroundMetaData = metaData;
			}
			
		}
		preInterceptorArray = preMethods.toArray(new InterceptorMetaData[preMethods.size()]);
		postInterceptorArray = postMethods.toArray(new InterceptorMetaData[postMethods.size()]);
	}
	
	protected void before(Object proxy, Method method, Object[] args){
		if(preInterceptorArray == null){
			return;
		}
		
		for(InterceptorMetaData preInterceptorMetaData:preInterceptorArray){
			Method preMethod = preInterceptorMetaData.getInterceptorMethod();
			Object interceptorObject = preInterceptorMetaData.getInterceptorObject();
			PointCut pointCut = initializePointCut(proxy, method, args);
			try {
				preMethod.invoke(interceptorObject, new Object[]{pointCut});
			} catch (IllegalAccessException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (InvocationTargetException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			}
		}
	}
	
	
	
	protected void after(Object proxy, Method method, Object[] args){
		if(postInterceptorArray == null){
			return;
		}
		
		for(InterceptorMetaData preInterceptorMetaData:postInterceptorArray){
			Method postMethod = preInterceptorMetaData.getInterceptorMethod();
			Object interceptorObject = preInterceptorMetaData.getInterceptorObject();
			PointCut pointCut = initializePointCut(proxy, method, args);
			try {
				postMethod.invoke(interceptorObject, pointCut);
			} catch (IllegalAccessException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (IllegalArgumentException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (InvocationTargetException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			}
		}
	}
	
	private PointCut initializePointCut(Object proxy, Method method, Object[] args){
		PointCut pointCut = new PointCut();
		pointCut.setParam(args);
		pointCut.setTargetMethod(method);
		pointCut.setTargetObject(proxy);
		return pointCut;
	}
	
	protected Object around(Object proxy, Method method, Object[] args){
		Object result = null;
		if(aroundMetaData == null){
			try{
				result = method.invoke(proxy, args);
				return result;
			} catch (IllegalAccessException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (IllegalArgumentException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (InvocationTargetException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			}
		}
		Method aroundMethod = aroundMetaData.getInterceptorMethod();
		Object interceptorObject = aroundMetaData.getInterceptorObject();
		PointCut pointCut = initializePointCut(proxy, method, args);
		try {
			result = aroundMethod.invoke(interceptorObject, pointCut);
		} catch (IllegalAccessException e) {
			throw new InterceptorInvokeExeception(e.getCause());
		} catch (IllegalArgumentException e) {
			throw new InterceptorInvokeExeception(e.getCause());
		} catch (InvocationTargetException e) {
			throw new InterceptorInvokeExeception(e.getCause());
		}
		return result;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		before(proxy, method, args);
		Object result = around(proxy, method, args);
		after(proxy, method, args);
		return result;
	}

	
	
}
