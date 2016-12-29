package cn.bronzeware.muppet.annotations;
public class MutiPrimaryKeyException extends AnnotationException {

	private static final long serialVersionUID = 8231143224411563901L;

	public MutiPrimaryKeyException(){
		
	}
	
	public MutiPrimaryKeyException(String message)
	{
		this.message = message;
	}
	
	private String message;
	
	@Override
	public String Message() {

		return "主键重复定义"+message;
	}

	
}
