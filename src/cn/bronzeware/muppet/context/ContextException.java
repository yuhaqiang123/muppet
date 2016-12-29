package cn.bronzeware.muppet.context;
public abstract class ContextException extends Exception {

	private static final long serialVersionUID = -8512898271479740046L;

	@Override
	public final String getMessage() {
		
		return "数据操作上下文环境错误->"+Message();
	}

	public abstract String Message();

	

}
