package cn.bronzeware.muppet.resource;

import cn.bronzeware.muppet.core.InitException;

public class ResourceNotFoundException extends InitException{

	@Override
	public String message() {
		return "没有找到相关的资源->"+message;
	}
	
	public String message = "";
	
	public ResourceNotFoundException(){
		
	}

	
	public ResourceNotFoundException(String message){
		this.message = message;
	}
}
