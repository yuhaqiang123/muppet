package cn.bronzeware.muppet.test;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.core.Session
;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

import static java.lang.System.out;

@Test
@Component
public class TestContext extends TestSuper{

	@Test
	public  void testContext1(){
		session = factory.getSession();
		Note note = new Note();
		note.setId(1);
		System.out.println(session.query(String.format("select * from t_note"), null, Note.class).size());
	}
	
	
}
