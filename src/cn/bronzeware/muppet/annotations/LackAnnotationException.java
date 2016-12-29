package cn.bronzeware.muppet.annotations;
public class LackAnnotationException extends AnnotationException{

	public static void main(String[] args) {

	}

	private String message;
	
	public LackAnnotationException(String message){
		this.message = message;
	}
	
	
	@Override
	public String Message() {
		
		return "没有发现注解定义->"+message;
	}

}
