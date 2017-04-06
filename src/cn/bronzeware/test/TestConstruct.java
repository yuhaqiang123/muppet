package cn.bronzeware.test;

import java.lang.reflect.Constructor;

import cn.bronzeware.muppet.datasource.DataSourceUtil;

public class TestConstruct {

	
	public static void test1(){
		Class clazz = TestConstruct.class;
		Constructor[] constructs = clazz.getConstructors();
		//DataSourceUtil
		for(Constructor c:constructs){
		//	c.
		}
		
	
	}
}
