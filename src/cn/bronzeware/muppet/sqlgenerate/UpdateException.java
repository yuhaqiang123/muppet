package cn.bronzeware.muppet.sqlgenerate;
public class UpdateException extends SqlGenerateException{

	public static void main(String[] args) {

	}
	public UpdateException(String message){
		super("");
		this.message = message;
	}
	public UpdateException(){
		super("");
	}
	
	private String message = "更新时出现异常，请检查参数";
	@Override
	public String getMessage() {
		
		return message;
	}

}
