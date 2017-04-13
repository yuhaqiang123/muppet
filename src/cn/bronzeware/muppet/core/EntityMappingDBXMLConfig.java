package cn.bronzeware.muppet.core;


/**
 * 
 * @author 于海强
 * 2016年8月8日 下午2:58:33
 * @version 1.4
 * @since 1.4
 * 解析配置文件接口，所有解析实现类必须实现这个接口
 */
public interface EntityMappingDBXMLConfig extends XMLConfig{

	/** 
	 * 解析配置文件，参数可以为数组，也就是可以有多个配置文件
	 * 这个方法可以考虑去掉
	 * @param configpaths
	 */
	//public void config(String[] configpaths);
	
	/**
	 * 获取实体类的的包名
	 * @return
	 */
	public String[] getResourcePackageNames();
	
	
	public boolean isBuilded();
}
