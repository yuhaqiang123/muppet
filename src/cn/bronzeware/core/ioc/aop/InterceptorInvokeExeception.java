package cn.bronzeware.core.ioc.aop;

public class InterceptorInvokeExeception extends RuntimeException{


	public InterceptorInvokeExeception(Throwable throwable){
		super(throwable);
	}
	
	public InterceptorInvokeExeception(String msg){
		super(msg);
	}
	public InterceptorInvokeExeception(String msg, Throwable throwable){
		super(msg);
		this.initCause(throwable);
	}
	
	public InterceptorInvokeExeception(){
		
	}
}
