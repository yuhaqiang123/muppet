package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestInsert extends TestSuper{

	@Test
	public void testInsert(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Logger.println("插入前：");
				Logger.println("\t 值：" + session.queryById(Note.class, 10));
				
				Note note = new Note();
				note.setId(10);
				note.setPassword(String.valueOf(10));
				note.setUser_id(String.valueOf(10));
				note.setUsername(String.valueOf(10));
				note.setValue(String.valueOf(10));
				session.insert(note);
				Logger.println(session.queryById(Note.class, 10));
				return null;
			}
		});
	}
	
	public static void main(String[] args){
		TestInsert test = new TestInsert();
		test.testInsert();
	}
	
}
