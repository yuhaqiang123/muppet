package cn.bronzeware.muppet.context;

public class ContextException extends RuntimeException {

	private static final long serialVersionUID = -8512898271479740046L;

	public ContextException() {

	}

	private String msg = null;

	public ContextException(Throwable throwable, String msg) {
		super(throwable);
		this.msg = msg;
	}

	public ContextException(String msg) {
		this.msg = msg;
	}

	public ContextException(Throwable throwable) {
		super(throwable);
	}

	@Override
	public final String getMessage() {

		return "数据操作上下文环境错误->" + Message();
	}

	public String Message() {
		return "";
	}

}
