package cn.bronzeware.muppet.test;

import cn.bronzeware.util.reflect.ReflectUtil;

public class TestReflectUtil {

	
	public static void main(String[] args){
		ClassLoader loader = new ClassLoader() {
			
		};
		Thread.currentThread().setContextClassLoader(loader);
		System.out.println(ReflectUtil.getClassByContextLoader("cn.bronzeware.muppet.test.A"));
	}
}
