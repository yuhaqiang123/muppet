package cn.bronzeware.muppet.util.autogenerate;

import cn.bronzeware.muppet.core.InitException;

 class GenerateException extends RuntimeException{

	public GenerateException(String message){
		this.message = message;
	}
	private String message;
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
