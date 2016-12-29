package cn.bronzeware.muppet.annotations;
public class LackPrimaryKeyException extends AnnotationException{

	private String message ;
	
	public LackPrimaryKeyException(){
		
	}
	
	public LackPrimaryKeyException(String message){
		this.message = message;
	}
	
	@Override
	public String Message() {
		
		return "缺少主键定义->"+message;
	}

	

}
