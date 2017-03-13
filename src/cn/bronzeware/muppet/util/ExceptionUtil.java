package cn.bronzeware.muppet.util;

import cn.bronzeware.core.ioc.InitializeException;

public class ExceptionUtil {
	
	/**
	 * 把继承Exception的异常转化为RuntimeException异常，避免编译时检查　
	 * @param e
	 * @return　RuntimeException
	 */
	public static RuntimeException getRuntimeException(Exception e){
		RuntimeException r = new InitializeException(e.getMessage());
		r.setStackTrace(e.getStackTrace());
		r.initCause(e.getCause());
		return r;
	}

	public  static RuntimeException getRuntimeException(){
		return new RuntimeException();
	}
	
	public static RuntimeException getRuntimeException(String msg){
		return new RuntimeException(msg);
	}
	
	
}
