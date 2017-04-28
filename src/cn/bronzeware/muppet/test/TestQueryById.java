package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestQueryById extends TestSuper{

	public TestQueryById() {
	}
	
	@Test
	public void testQueryById(){
		session = sessionFactory.getSession();
		Logger.println(session.queryById(Note.class, 2));
		session.close();
	}
	
	public void testQueryByExecute(){
		session = sessionFactory.getSession();
		Logger.println(session.query(Note.class, " id = ?", new Object[]{2}));
		session.close();
	}
	
	public static void main(String[] args){
		TestQueryById id = new TestQueryById();
		id.testQueryById();
		//id.testQueryByExecute();
	}
}
