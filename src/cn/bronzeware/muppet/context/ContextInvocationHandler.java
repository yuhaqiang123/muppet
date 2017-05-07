package cn.bronzeware.muppet.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * 
 * @author yuhaiqiang  yuhaiqiangvip@sina.com
 * @time 2017年5月3日 下午5:23:30
 */
@Deprecated
public class ContextInvocationHandler implements InvocationHandler{

	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = method.invoke(proxy, args);
		return result;
	}

}
