package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.util.ArrayUtil;

public class TestDataSourceSession extends TestSuper{
	
	public void test1(){
		session = factory.getSession(false, "slave");
		ArrayUtil.println(session.query("select * from t_note", null));
	}
	
	public static void main(String[] args){
		TestDataSourceSession test = new TestDataSourceSession();
		test.test1();
	}

}
