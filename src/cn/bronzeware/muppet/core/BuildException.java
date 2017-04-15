package cn.bronzeware.muppet.core;


/**
 * 构建表文件的时抛出异常
 * 继承自InitException.属于RuntimeException
 * @author 于海强
 *
 */
public class BuildException extends InitException{

	
	public BuildException(){
		
	}
	
	public BuildException(Throwable throwable){
		super(throwable);
	}
	
	
	private String message;
	
	public BuildException(String messsage){
		this.message = messsage;
	}

	@Override
	public String message() {

		return "构建资源时出错->"+message;
	}
	
	
}
