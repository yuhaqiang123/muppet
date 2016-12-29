package cn.bronzeware.util.reflect;

public class ReflectException extends RuntimeException{
	public ReflectException(String message){
		
	}
	
	protected ReflectException(){
		
	}
	private String message = "";

	
	@Override
	public String getMessage() {
		return "reflect error"+message;
	}
	
}
