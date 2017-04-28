package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;

public class TestSuper {

	protected SessionFactory factory = null;
	protected Session session = null;
	protected static  SessionFactory sessionFactory;
	
	public TestSuper(){
		if(sessionFactory == null){
			factory = new SessionFactory("muppet.xml");
			sessionFactory = factory;
		}
		factory  = sessionFactory;
	}
}
