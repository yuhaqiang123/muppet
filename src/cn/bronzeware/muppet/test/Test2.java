package cn.bronzeware.muppet.test;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.util.testframework.Test;

@Test
@Component
public class Test2 {

	@Test
	public int test1(){
		System.out.println("test1");
		return 344;
	}
	
	public static void main(String[] args){
		Integer i1 = 128;
		Integer i2 = 128;
		if(i1.equals(i2)){
			System.out.println("fefaes");
		}
	}
}
