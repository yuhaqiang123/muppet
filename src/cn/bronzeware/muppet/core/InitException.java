package cn.bronzeware.muppet.core;
public abstract class InitException extends RuntimeException {

	@Override
	public final String getMessage() {
		
		return "初始化出现错误->"+message();
	}

	
	public abstract String message();
	
}
