package cn.bronzeware.muppet.core;

import cn.bronzeware.muppet.resource.ResourceInfo;

/**
 * 解析指定的实体类下的所有注解，以及对应的默认处理
 * @version 1.4
 * @since 1.4
 * 2016年8月8日 下午3:26:04
 * @author 梁莹莹
 *
 */
public interface ResourceResolve {

	/**
	 * 解析指定Class的注解
	 * @return {@link ResourceInfo} 返回ResourceInfo子类的对象，可以解析
	 * Table,可以解析View，不同的结果内部的数据结构是不同的，但是对于解析
	 * {@link ResourceResolve}的接口是一样的
	 */
	public ResourceInfo resolve(Class<?> clazz) throws ResourceResolveException ;
}
