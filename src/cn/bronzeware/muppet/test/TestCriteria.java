package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Criteria;
import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.Note;

public class TestCriteria {

	
//	static SessionFactory factroy = new SessionFactory("muppet.xml");
	public static Session muppet ;
/*	static
	{
		muppet = factroy.getSession(true);
	}
*/
	public static void test1(){
		Criteria criteria = muppet.createCriteria(Note.class);
		//criteria.andPropEqual("id", 37);
		//criteria.andPropGreater("id", 36);
		//criteria.andPropGreaterEq("id", 39);
		//criteria.andPropGreater("id", 36).order("user_id", false).order("id", false);
		//criteria.andPropLess("id", 37);
		criteria.andPropGreater("user_id", 30);
		criteria.andPropLike("username", "%yuhai1%");
		/*Criteria criteria1 = muppet.createCriteria(Note.class);
		criteria1.andPropEqual("user_id", 35);
		criteria.or(criteria1);*/
		System.out.print(criteria.list());
		
	}
	
	
	public static void main(String[] args){
		
		test1();
		
	}
	
}
