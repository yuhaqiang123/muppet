package cn.bronzeware.muppet.core;

import cn.bronzeware.muppet.resource.ResourceInfo;

/**
 * 
 * 解析注解之后，拿到获取到的ResourceInfo，与数据库中的表进行比对
 * ，如果出现不同，生成该不同项的变更Sql语句，进行更新
 * @since 1.4
 * @version 1.4
 * 2016年8月8日 下午3:39:10
 * @author 于海强
 *
 */
public interface ResourceBuild {

	/**
	 * 根据输入数据信息，与数据库进行比对，生成Sql语句进行更新
	 * 是1.4版本的难点之一
	 */
	public boolean build(ResourceInfo info) throws BuildException;
}
