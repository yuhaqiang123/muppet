package cn.bronzeware.muppet.test;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.test.TestAop;
import cn.bronzeware.util.testframework.Test;

@Test
@Component
public class Test1 {

	public  Test1(){
		System.out.println("Test1");
	}
	
	@Test
	public int test1(){
		System.out.println("hello test framework test1");
		System.err.println("error");
		return 1;
	}
	
	@Test
	public String test2(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return "2";
	}
	
}
