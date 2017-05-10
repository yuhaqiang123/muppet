package cn.bronzeware.muppet.sqlgenerate;



/**
 *	抛出此异常的方法要求参数不能为空
 * @author 于海强
 * 2016-6-16  上午10:12:42
 */
public class ParamCanNotBeNullException extends Exception{

	private static final long serialVersionUID = 55020266797353347L;
	private String[] params;
	
	
	
	public ParamCanNotBeNullException(Throwable e){
		super(e);
	}
	
	/**
	 * @param 要求不能为空的参数名
	 * @author 于海强
	 * 2016-6-16  上午10:13:13
	 * 
	 */
	public ParamCanNotBeNullException(String... params){
		this.params = params;
	}

	@Override
	public String getMessage() {
		
		StringBuffer paramlist = new StringBuffer();
		for(String param:params){
			paramlist.append(param+",");
		}
		return "参数"+paramlist+"不能为空";
	}
	

}
