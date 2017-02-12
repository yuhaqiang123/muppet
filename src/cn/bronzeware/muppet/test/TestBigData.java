package cn.bronzeware.muppet.test;

import java.util.List;

import cn.bronzeware.muppet.core.Criteria;
import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.BlogHouse;
import cn.bronzeware.muppet.util.ArrayUtil;

public class TestBigData {
	//static SessionFactory factroy = new SessionFactory("muppet.xml");
	public static Session muppet ;
	/*
	static
	{
		muppet = factroy.getSession(true);
	}
	*/
	public static void test1(){
		long start = System.currentTimeMillis();
		
		Criteria criteria = muppet.createCriteria(BlogHouse.class);
		//criteria.andPropEqual("source", "http://www.cnblogs.com/wangtao_20/p/3539960.html");
		criteria.andPropEqual("title", "《数据库系统实现》读书笔记 - 王滔 - 博客园");
		
		List<BlogHouse> list = criteria.list();
		//ArrayUtil.println(list);
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
	
	public static void test2(){
		muppet.query("SHOW CREATE TABLE tb_blog", null, null);
	}
	
	
	public static void main(String[] args){
		test2();
	}
	
	
}
