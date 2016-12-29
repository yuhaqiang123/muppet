package cn.bronzeware.muppet.sqlgenerate;
public  class SqlGenerateException extends Exception{

	private static final long serialVersionUID = -2272596789720418995L;
	public static void main(String[] args) {

	}
	
	private String message = null;
	public SqlGenerateException(String message){
		this.message = message;
	}

	public SqlGenerateException(){
		
	}
	@Override
	public String getMessage() {
		String baseMessage ="Sql语句生成出错->"; 
		if(message==null){
			return baseMessage+message(); 
		}
		return baseMessage+message;
	}

	public  String message(){
		return "";
	}
	
}
