package cn.bronzeware.muppet.core;


public class ResourceResolveException extends InitException{

	public static void main(String[] args) {

	}

	@Override
	public String message() {
		
		return "解析实体类注解时出错->"+message;
	}
	
	private String message = "";
	public ResourceResolveException(String message){
		this.message = message;
	}

	public ResourceResolveException(){
		
	}
}
