package cn.bronzeware.muppet.sqlgenerate;

import cn.bronzeware.muppet.resource.ResourceInfo;


public interface Generate {

	public String generate(ResourceInfo resourceInfo) throws SqlGenerateException;
}
