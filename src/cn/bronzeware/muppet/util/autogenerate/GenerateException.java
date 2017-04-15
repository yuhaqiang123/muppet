package cn.bronzeware.muppet.util.autogenerate;

import cn.bronzeware.muppet.core.InitException;
import cn.bronzeware.muppet.sqlgenerate.Generate;

 class GenerateException extends RuntimeException{

	public GenerateException(Throwable throwable){
		super(throwable);
	}
	 
	public GenerateException(String message){
		this.message = message;
	}
	private String message;
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
