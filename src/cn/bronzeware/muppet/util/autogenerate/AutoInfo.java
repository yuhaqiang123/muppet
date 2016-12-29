package cn.bronzeware.muppet.util.autogenerate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.bronzeware.muppet.core.XMLConfigResource;

/**
 * 自动代码生成所需数据结构
 * @author 于海强
 *
 */
 class AutoInfo extends XMLConfigResource{

	 /**
	  * 存储表名，类名映射
	  */
	private Map<String,String> map = new HashMap<>();
	
	/**
	 * 实体类自动生成所在的包
	 */
	private String generatePath;
	
	
	public void setGeneratePath(String generatePath) {
		this.generatePath = generatePath;
	}
	public void set(String tableName,String domainObjectName){
		map.put(tableName,domainObjectName);
	}
	public String getDomainObjectName(String tableName){
		return map.get(tableName);
	}
	public String getGeneratePath(){
		return generatePath; 
	}
	
	public Set<String> KeySets(){
		return map.keySet();
	}
	
}
