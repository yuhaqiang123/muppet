package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestQueryById extends TestSuper{

	public TestQueryById() {
		System.out.println("哈哈哈哈哈");
	}
	
	@Test
	public void testQueryById(){
		session = sessionFactory.getSession();
		Logger.println("testQueryById");
		session.close();
	}
}
