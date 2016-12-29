package cn.bronzeware.muppet.core;

import java.util.Map;


/**
 * 
 * 加载包下的所有类
 * @since 1.4
 * @version 1.4
 * 2016年8月8日 下午3:23:43
 * @author 杨开
 *
 */
public interface ResourceLoad {

	
	/**
	 * 
	 * 加载这个每个包下的所有类包括子包
	 * 返回的Map中，包名作为key,包名下的Class是数组
	 * 但是子包的包名也作为key单独保存在Map中。
	 */
	public Map<String, Class<?>[]> loadClass(String[] packetname);
	
	
}
