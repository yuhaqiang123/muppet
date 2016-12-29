package cn.bronzeware.muppet.util.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggerInvocationHandler implements InvocationHandler{

	
	/*public LoggerInvocationHandler(LoggerProxy logger){
		this.logger = logger;
	}
	private LoggerProxy logger;*/
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		boolean isexe = false;
		if(Logger.isStart==true){
			
			if(Logger.isDebug==true&&method.getName().startsWith("debug")){
				isexe = true;
			}
			
			if(Logger.isDebug==true&&method.getName().startsWith("print")){
				isexe = true;
			}
			
			if(Logger.isInfo==true&&method.getName().startsWith("info")){
				isexe = true;
			}
			
			if(Logger.isError==true&&method.getName().startsWith("error")){
				isexe = true;
			}
			
		}
		if(isexe == true){
			return method.invoke(proxy, args);
		}else{
			return null;
		}
		
		
	}
	
}
