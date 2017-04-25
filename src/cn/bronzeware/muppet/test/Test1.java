package cn.bronzeware.muppet.test;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.test.TestAop;
import cn.bronzeware.util.testframework.Test;

@Test
@Component
public class Test1 {

	public  Test1(){
		//System.out.println("Test1");
	}
	
	@Test
	public Integer test1(){
		System.out.println("hello test framework test1");
		System.err.println("error");
		return 128;
	}
	
	@Test
	public String test2(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		//throw new NullPointerException();
		return "2";
	}
	@Test
	public String test3(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return null;
		//return "2";
	}
	
	@Test
	public String test4(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return null;
		//return "2";
	}

	@Test
	public String test7(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return null;
		//return "2";
	}
	

	@Test
	public String test11(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return "hahaha";
		//return "2";
	}
	
	@Test
	public String test31(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return "hahaha";
		//return "2";
	}

	@Test
	public String test12(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return "hahaha";
		//return "2";
	}
	
	@Test
	public String test13(){
		System.out.println("hello test framework test2");
		System.err.println("error");
		return "hha";
	}
	
}
