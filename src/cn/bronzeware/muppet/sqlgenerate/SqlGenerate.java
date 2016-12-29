package cn.bronzeware.muppet.sqlgenerate;


public interface SqlGenerate {

	public String getSql(Sql sql) throws 
	ParamCanNotBeNullException, 
	Exception;
	public static int INSERT = 1;
	public static int UPDATE = 2;
	public static int DELETE = 3;
	public static int SELECT = 4;
	
	
}
