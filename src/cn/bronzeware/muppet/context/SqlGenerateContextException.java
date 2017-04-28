package cn.bronzeware.muppet.context;
public class SqlGenerateContextException extends ContextException{

	private static final long serialVersionUID = 3422923846416691566L;

	public static void main(String[] args) {

	}

	public SqlGenerateContextException(String message){
		this.message = message;
	}
	public SqlGenerateContextException(Throwable throwable){
		super(throwable);
	}
	
	private String message;
	
	@Override
	public String Message() {
		
		return /*"SQL语句生成出错->"*/message;
	}

}
