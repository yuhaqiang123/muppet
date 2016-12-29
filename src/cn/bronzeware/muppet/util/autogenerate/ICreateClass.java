package cn.bronzeware.muppet.util.autogenerate;
/**
 * 创建类的工具类的接口
 * @author thomas.xu
 * 2016.08.30
 * @see CreateClass
 */
public interface ICreateClass {

	/**
	 * 此方法用于向待创建类中添加字段信息
	 * @param field 要添加的字段
	 * @return 如果字段已存在（字段名称相同则认为字段已存在）则添加失败返回false，否则添加成功返回true
	 * @see Field
	 */
	public abstract boolean addField(Field field);
	
	/**
	 * 此方法用于向待创建类中添加方法信息
	 * @param method 要添加的字段
	 * @return 如果方法已存在（方法签名相同则认为方法存在）则添加失败返回false，否则添加成功返回true
	 * @see Method
	 */
	public abstract boolean addMethod(Method Method);
	
	/**
	 * 根据指定信息创建类
	 */
	public abstract void create();
}
