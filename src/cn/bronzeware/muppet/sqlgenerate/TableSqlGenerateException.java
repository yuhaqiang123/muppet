package cn.bronzeware.muppet.sqlgenerate;
public class TableSqlGenerateException extends SqlGenerateException{

	public String message(){
		return "生成表的sql文件时出错"+message;
	}
	
	private String message = "";
	public TableSqlGenerateException(){
		
	}
	
	public TableSqlGenerateException(String message){
		this.message = message;
	}
	
	
}
