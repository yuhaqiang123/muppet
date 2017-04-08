package cn.bronzeware.test;

import java.lang.reflect.Constructor;

import javax.annotation.PostConstruct;

import cn.bronzeware.muppet.datasource.DataSourceUtil;


public class TestConstruct {

	
	public static void main(String[] args){
		TestConstruct test = new TestConstruct();
		test.test();
	}
	
	public TestConstruct(){
		System.out.println("构造方法");
	}
	public void test(){
		
	}
	
	@PostConstruct
	public void post(){
		System.out.println("post");
	}
	
	
}
