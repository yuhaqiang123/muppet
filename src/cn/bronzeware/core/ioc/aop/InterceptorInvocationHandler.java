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
import cn.bronzeware.util.reflect.ProxyInvocationHandler;
import net.sf.cglib.proxy.MethodProxy;

public class InterceptorInvocationHandler implements ProxyInvocationHandler{

	private ApplicationContext applicationContext;
	
	private List<InterceptorMetaData> interceptorMetaDatas;
	
	private InterceptorMetaData[] preInterceptorArray = null;
	
	private InterceptorMetaData[] postInterceptorArray = null;
	
	private InterceptorMetaData aroundMetaData = null;
	
	private String interceptorInvokeExeceptionOnIllealArgumentMsg =  
				String.format("拦截方法应该包括 %s　参数"
				, PointCut.class.getName());
	
	public InterceptorInvocationHandler(ApplicationContext applicationContext, List<InterceptorMetaData> list) {
		this.applicationContext = applicationContext;
		this.interceptorMetaDatas = list;
		List<InterceptorMetaData> preMethods = new ArrayList<>();
		
		List<InterceptorMetaData> postMethods = new ArrayList<>();
		for(InterceptorMetaData metaData:list){
			Annotation annotation = metaData.getInterceptorType();
			this.annotationType = metaData.getTargetAnnotationType();
			if(Before.class.isAssignableFrom(annotation.annotationType())){
				preMethods.add(metaData);
			}
			if(After.class.isAssignableFrom(annotation.annotationType())){
				postMethods.add(metaData);
			}
			if(Around.class.isAssignableFrom(annotation.annotationType())){
				if(aroundMetaData!=null){
					throw new InterceptorInvokeExeception(
							String.format("%s 类的 %s　方法所环绕的方法，被多次环绕拦截"
									, aroundMetaData.getInterceptorClazz().getName()
									, aroundMetaData.getInterceptorMethod().getName()));
				}
				aroundMetaData = metaData;
			}
			
		}
		preInterceptorArray = preMethods.toArray(new InterceptorMetaData[preMethods.size()]);
		postInterceptorArray = postMethods.toArray(new InterceptorMetaData[postMethods.size()]);
	}
	
	private Class annotationType = null;
	private boolean isIntercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy){
		/*System.out.println(String.format("%s.%s.%s"
				, method.getDeclaringClass().getName()
				, method.getName()
				,annotationType));*/
		Annotation targetAnnotation = method.getDeclaredAnnotation(annotationType);
		if(targetAnnotation == null){
			return false;
		}else{
			return true;
		}
	}
	
	protected void before(Object proxy, Method method, Object[] args, MethodProxy methodProxy){
		if(preInterceptorArray == null){
			return;
		}
		
		for(InterceptorMetaData preInterceptorMetaData:preInterceptorArray){
			Method preMethod = preInterceptorMetaData.getInterceptorMethod();
			Object interceptorObject = preInterceptorMetaData.getInterceptorObject();
			PointCut pointCut = initializePointCut(proxy, method, args, methodProxy);
			try {
				preMethod.invoke(interceptorObject, new Object[]{pointCut});
			} catch (IllegalAccessException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (IllegalArgumentException e) {
				throw new InterceptorInvokeExeception(interceptorInvokeExeceptionOnIllealArgumentMsg);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new InterceptorInvokeExeception(e.getCause());
			}
		}
	}
	
	
	
	protected void after(Object proxy, Method method, Object[] args,MethodProxy methodProxy,Object result){
		if(postInterceptorArray == null){
			return;
		}
		
		for(InterceptorMetaData preInterceptorMetaData:postInterceptorArray){
			Method postMethod = preInterceptorMetaData.getInterceptorMethod();
			Object interceptorObject = preInterceptorMetaData.getInterceptorObject();
			PointCut pointCut = initializePointCut(proxy, method, args, methodProxy);
			pointCut.setReturnValue(result);
			try {
				postMethod.invoke(interceptorObject, pointCut);
			} catch (IllegalAccessException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			} catch (IllegalArgumentException e) {
				throw new InterceptorInvokeExeception(interceptorInvokeExeceptionOnIllealArgumentMsg);
			} catch (InvocationTargetException e) {
				throw new InterceptorInvokeExeception(e.getCause());
			}
		}
	}
	
	private PointCut initializePointCut(Object proxy, Method method, Object[] args, MethodProxy methodProxy){
		PointCut pointCut = new PointCut();
		pointCut.setParam(args);
		pointCut.setTargetMethod(method);
		pointCut.setMethodProxy(methodProxy);
		pointCut.setTargetObject(proxy);
		return pointCut;
	}
	
	protected Object around(Object proxy, Method method, Object[] args, MethodProxy methodProxy){
		Object result = null;
		if(aroundMetaData == null){
			try {
				result = methodProxy.invokeSuper(proxy, args);
			} catch (Throwable e) {
				throw new InterceptorInvokeExeception(e.getCause());
			}
			return result;
		}
		Method aroundMethod = aroundMetaData.getInterceptorMethod();
		Object interceptorObject = aroundMetaData.getInterceptorObject();
		PointCut pointCut = initializePointCut(proxy, method, args, methodProxy);
		try {
			result = aroundMethod.invoke(interceptorObject, pointCut);
		} catch (IllegalAccessException e) {
			throw new InterceptorInvokeExeception(e.getCause());
		} catch (IllegalArgumentException e) {
			throw new InterceptorInvokeExeception(interceptorInvokeExeceptionOnIllealArgumentMsg);
		} catch (InvocationTargetException e) {
			throw new InterceptorInvokeExeception(e.getCause());
		}
		return result;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		before(proxy, method, args, null);
		Object result = around(proxy, method, args, null);
		after(proxy, method, args, null, null);
		return result;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		if(!isIntercept(proxy, method, params, methodProxy)){
			try {
				Object result = methodProxy.invokeSuper(proxy, params);
				return result;
			} catch (Throwable e) {
				throw new InterceptorInvokeExeception(e.getCause());
			}
		}
		before(proxy, method, params, methodProxy);
		Object result = around(proxy, method, params, methodProxy);
		after(proxy, method, params, methodProxy, result);
		return result;
	}
}
