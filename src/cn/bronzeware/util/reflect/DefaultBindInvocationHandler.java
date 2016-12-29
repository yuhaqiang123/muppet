package cn.bronzeware.util.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.bronzeware.muppet.util.log.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 默认绑定代理加强器
 * {@link BindInvocationHandler} 绑定代理加强器存在的意义是，{@link InvocationHandler} 中的
 * invoke方法，传入的参数是代理的对象，用户（客户端）获取代理对象时，<br/><br/>
 * <h2>最关心的是我要获取到这个代理对象</h2>
 * <br/><br/>
 * 而不是被代理对象如何得到增强。但是如何在被代理对象 与代理增强器间取得联系呢？
 * 我们通过在代理增强器中增加私有字段 保存被代理对象，然后在invoke中将代理对象的方法转为被代理对象的方法的调用<br/>
 * Object result = method.invoke(target, args);<br/>
 * target即是被代理的对象，这样代理对象的方法调用 转为被代理对象的方法调用，然后我们可以在上行代理上下加上 其他逻辑<br/>
 * 这样一个简单动态代理就实现了，而如何获取代理对象呢？<br/>
 * bind方法定义如下：<br/>
 * this.target = (target);<br/>
 * return Proxy.newProxyInstance(target.getClass().getClassLoader()
					, target.getClass().getInterfaces(), this);	<br/>
 * 这样我们就可以获取到代理对象，这种绑定获取代理对象是一般的 获取代理对象的套路   所以我们定义了一个接口{@link BindInvocationHandler}
 * 并且提供了默认的实现，也就是本类.
 * 值得一说的是本类同时实现了另外一个接口{@link MethodInterceptor }这个接口是cglib中的接口，和{@InvocationHandler}<br/>
 * 分别是cglib代理类，jdk代理类方法执行时回调的方法.实现这个接口目的是淡化cglib代理和jdk代理的区别。<br/>
 * 如果你阅读源码，你就会看到他们共用了bind方法绑定的对象。
 * @author 于海强
 *
 */
class DefaultBindInvocationHandler implements BindInvocationHandler
,MethodInterceptor{

	private InvocationHandler handler = null;
	public DefaultBindInvocationHandler(InvocationHandler handler){
		this.handler = handler;
	}
	
	public DefaultBindInvocationHandler(){
		
	}
	
	/**
	 * 获取指定对象的代理
	 * @param target
	 * @return
	 */
	public <T> T getClassProxy(T target,Class[] constructorArgsClazzs,Object[] constructorArgsValues){
		this.target = target;
		return  (T) createProxy(target.getClass(),constructorArgsClazzs,constructorArgsValues);
	}
	
	/**
	 * 获取指定对象的代理
	 * @param target
	 * @return
	 */
	public <T> T getClassProxy(T target){
		this.target = target;
		return  (T) createProxy(target.getClass());
	}
	
	
	
	
	protected Object target;
	
	/**
	 * 绑定被代理的对象，同时返回代理对象，代理增强默认是本类 ，如果子类没有重写此方法，调用
	 * 本方法那么代理增强将是子类
	 * 值得说明的是，这个方法不是一个好方法，因为这个方法含糊不清，因为只有你传入的对象没有实现接口的情况下才
	 * 返回对象代理，否则返回接口代理，所以获取接口代理，可以放心使用本方法，类代理可以考虑使用
	 * getClassProxy方法，简单明了是获取何种代理，避免客户端代码调用的 混淆
	 */
	@Override
	public Object bind(Object target)
			throws NullPointerException,IllegalArgumentException
	{
		this.target = (target);
		if(isHaveInterface(target)){
			return Proxy.newProxyInstance(target.getClass().getClassLoader()
					, target.getClass().getInterfaces(), this.handler==null?this:this.handler);	
		}
		else{
			return createProxy(target.getClass());
		}
		
	}
	/**
	 * 是否实现了接口
	 * @param target
	 * @return
	 */
	private boolean isHaveInterface(Object target){
		if(target.getClass().getInterfaces()!=null
				&&target.getClass().getInterfaces().length>0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 同bind(Object target)方法，本方法只不过 会返回指定接口的代理
	 */
	@Override
	public <T> T bind(Object target,Class<T> targetInterface)
			throws NullPointerException,IllegalArgumentException{
		this.target = (target);
		if(isHaveInterface(target)){
			
			if(targetInterface.isInstance(target)){
				Object object = Proxy.newProxyInstance(target.getClass().getClassLoader()
						, new Class[]{targetInterface}
						,this.handler==null?this:this.handler);
				
				return (T)object;
			}else{
				throw new IllegalArgumentException(target.getClass().getName()
						+"没有实现"+targetInterface.getName());
			}
			
		}else
		{
			return (T) createProxy(target.getClass());
		}
	}

	/**
	 * cglib调用，创建代理对象，默认回调类型是当前调用类型,也就是如果子类对象，那么当在本类中执行 
	 * 本方法时，回调函数将默认是子类的回调
	 * @param targetClass
	 * @return
	 */
	private final <T> T createProxy(Class<T> targetClass) {  
	       
	        Enhancer enhancer = new Enhancer();  
	        enhancer.setSuperclass(targetClass);// 设置代理目标  
	        
	        enhancer.setCallback(this);// 设置回调  
	        enhancer.setClassLoader(targetClass.getClassLoader());
	         
	        return (T)enhancer.create();
	    }
	
	
	/**
	 * cglib调用，创建代理对象，默认回调类型是当前调用类型,也就是如果子类对象，那么当在本类中执行 
	 * 本方法时，回调函数将默认是子类的回调
	 * @param targetClass
	 * @return
	 */
	private final <T> T createProxy(Class<T> targetClass,Class[] clazzs,Object[] objects) {  
	       
	        Enhancer enhancer = new Enhancer();  
	        enhancer.setSuperclass(targetClass);// 设置代理目标  
	        
	        enhancer.setCallback(this);// 设置回调  
	        enhancer.setClassLoader(targetClass.getClassLoader());
	         
	        return (T)enhancer.create(clazzs,objects);
	    }
	
	
	
	/**
	 * jdk代理类调用执行后，默认的回调函数
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		before(proxy,method,args,null);
		Object result = method.invoke(target, args);
		after(proxy,method,args,null);
		return result;
	}

	/**
	 * cglib代理类默认的回调函数，本类只是默认的实现
	 * 如果子类没有实现intercept可以覆盖before方法，做前置增强，也可以重写after方法,
	 * 实现后置增强，当然子类也可以重写此方法
	 * 
	 */
	@Override
	public Object intercept(Object proxy, Method method, Object[] params,  
            MethodProxy methodProxy) throws Throwable {
		
		before(proxy,method,params,methodProxy);
		Object  result = methodProxy.invokeSuper(proxy, params);
		after(proxy,method,params,methodProxy);
		return result;
	}
	
	protected Object before(Object proxy, Method method, Object[] args,MethodProxy methodProxy) {
		return null;
	}
	
	protected Object after(Object proxy, Method method, Object[] args,MethodProxy methodProxy){
		return null;
	}
		
}
