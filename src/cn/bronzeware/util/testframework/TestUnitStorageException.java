package cn.bronzeware.util.testframework;

public class TestUnitStorageException extends RuntimeException{

	public TestUnitStorageException(String msg){
		super(msg);
	}
	
	public TestUnitStorageException(Throwable throwable){
		super(throwable);
	}
	private String msg ;
	
	public TestUnitStorageException(String msg, Throwable throwable){
		super(throwable);
		msg = this.msg;
	}

	@Override
	public String getMessage() {
		return msg;
	}

	
	
}
