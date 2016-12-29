package cn.bronzeware.muppet.core;
public class ResourceLoadException extends InitException{

	public static void main(String[] args) {

	}
	
	private String message = "";
	
	public ResourceLoadException(String message){
		this.message = message;
	}

	public ResourceLoadException(){
		
	}
	
	@Override
	public String message() {
		
		return "加载实体类时是出错->"+message;
	}

}
