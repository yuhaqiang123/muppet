package cn.bronzeware.muppet.util.log;

public class Msg {

	public static String TABLE_EXIST = "已经存在，现在开始检查不同项";
	public static String COLUMN_EXIST = "已经存在，现在开始检查不同项";
	public static String COLUMN_UNEXIST = "该列不存在，现在开始创建";
	
	public static String ALTER_COLUMN = "该列（非主键属性）被修改";
	public static String ADD_COLUMN = "该列已经添加";
	public static String ADD_PRIMARYKEY ="主键已经添加";
	public static String DELETE_PRIMARYKEY = "主键已经被删除";
	public static String ALTER_PRIMARYKEY= "修改主键";
	public static String DATYPE_NOT_FOUND = "没有找到对应的数据库类型，请检查该类型是否是基础类型或其包装器类型";
	public static String DEFAULT_LENGTH_NOT_FOUND = 
			"没有找到该类型的默认长度";

	public static String COLUMN_EQUALS = "该列没有变化，不需要修改";
	public static String CREATE_TABLE = "创建数据库表";
}
