package cn.bronzeware.core.ioc.aop;

public class InterceptorInvokeExeception extends RuntimeException{


	public InterceptorInvokeExeception(Throwable throwable){
		super(throwable);
	}
	
	
	public InterceptorInvokeExeception(String msg){
		super(msg);
	}
	
	public InterceptorInvokeExeception(){
		
	}
}
