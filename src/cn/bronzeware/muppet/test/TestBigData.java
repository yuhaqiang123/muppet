package cn.bronzeware.muppet.test;

import java.util.List;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.core.Criteria;
import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.BlogHouse;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.util.testframework.Test;

@Component
@Test
public class TestBigData {
	SessionFactory factroy =null;
	public  Session muppet ;
	/*
	static
	{
		muppet = factroy.getSession(true);
	}
	*/
	public TestBigData(){
		factroy = new SessionFactory("muppet.xml");
	}
	
	@Test
	public  void test1(){

		muppet = factroy.getSession();
		long start = System.currentTimeMillis();
		Criteria criteria = muppet.createCriteria(BlogHouse.class);
		//criteria.andPropEqual("source", "http://www.cnblogs.com/wangtao_20/p/3539960.html");
		criteria.andPropEqual("title", "《数据库系统实现》读书笔记 - 王滔 - 博客园");
		
		List<BlogHouse> list = criteria.list();
		//ArrayUtil.println(list);
		long end = System.currentTimeMillis();
		muppet.close();
	}
	@Test
	public  void test2(){
		muppet = factroy.getSession();
		ArrayUtil.println(muppet.query("SHOW CREATE TABLE tb_bloghouse", null));
		muppet.close();
	}
	
	
	public static void main(String[] args){
		TestBigData bigData = new TestBigData();
		bigData.test2();
	}
	
	
}
