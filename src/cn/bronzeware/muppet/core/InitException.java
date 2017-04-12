package cn.bronzeware.muppet.core;


public abstract class InitException extends RuntimeException {

	
	public InitException() {
	}
	private String msg = null;
	
	public InitException(Throwable throwable, String msg){
		super(throwable);
		this.msg = msg;
	}
	
	public InitException(String msg){
		this.msg = msg;
	}
	

	public InitException(Throwable throwable){
		super(throwable);
	}
	
	@Override
	public final String getMessage() {
		
		return msg == null ? "初始化出现错误->" : msg +message();
	}

	
	public abstract String message();
	
}
