package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;

import static java.lang.System.out;
public class TestContext {

	public static void testContext1(){
		SessionFactory factory = new SessionFactory("muppet.xml");
		Session session = null;
		session = factory.getSession();
		Note note = new Note();
		note.setId(1);
		ArrayUtil.println(session.query(String.format("select * from t_note"), null, Note.class));
	}
	
	public static void main(String[] args){
		testContext1();
	}
	
}
