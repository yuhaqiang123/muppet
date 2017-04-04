package cn.bronzeware.util.testframework;

public class TestUnitInvokeException extends RuntimeException{

	private String msg = null;
	
	public TestUnitInvokeException(String msg){
		this.msg = msg;
	}
	
	public TestUnitInvokeException(Throwable throwable) {
		super(throwable);
	}
	
	public TestUnitInvokeException(String msg, Throwable throwable) {
		super(throwable);
		this.msg = msg;
	}
	
	
	@Override
	public String getMessage() {
		
		return msg;
	}
	
}
