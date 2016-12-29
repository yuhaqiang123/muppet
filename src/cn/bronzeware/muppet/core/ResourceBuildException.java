package cn.bronzeware.muppet.core;
public class ResourceBuildException extends InitException{

	public ResourceBuildException(String message) {
		this.message = message;
	}

	public ResourceBuildException(){
		
	}
	
	
	
	private String message = "";
	public static void main(String[] args) {

	}

	@Override
	public String message() {
		
		return "生成表结构时出错->"+message;
	}

}
