package cn.bronzeware.muppet.listener;

public class UnSupportedEventTypeException extends RuntimeException{

	
	public UnSupportedEventTypeException(){
		
	}
	private String message = "";
	public UnSupportedEventTypeException(String msg){
		this.message = msg;
	}
	@Override
	public String getMessage() {
		
		return "不支持的事件类型："+ message;
	}
	
	
	
}
