package cn.bronzeware.util.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 标准绑定代理加强器，继承自{@link DefaultBindInvocationHandler }
 * 这个绑定代理加强器，重写了invoke方法，intercept方法，同时接受一个参数
 * {@InvocationHandler}类型参数，这个代理加强是jdk的接口，但是我们通过一个巧妙地替换将
 * intercept方法调用委托给了这个接口的invoke方法，其实DefaultBindInvocationHandler类
 * 完全可以实现代理类的增强，为什么定义这个类呢？因为在DefaultBindInvocationoHandler类
 * 中只是进行了前后置增强，并没有拦截方法调用的执行，而且灵活性太弱，需要提供一个接口灵活的对代理类进行
 * 增强
 * 
 * 
 * @author 于海强
 *
 */
class StandardBindInvocationHandler extends DefaultBindInvocationHandler{

	private InvocationHandler handler;
	public StandardBindInvocationHandler(InvocationHandler handler){
		this.handler = handler;
	}

	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		before(proxy,method,args,null);
		Object result = handler.invoke(target, method, args);
		after(proxy,method,args,null);
		return result;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] params,
			MethodProxy methodProxy) throws Throwable {
		//methodProxy.
		before(proxy,method,params,methodProxy);
		Object object = handler.invoke(target, method, params);
		after(proxy,method,params,methodProxy);
		return object;
	}
	
}
