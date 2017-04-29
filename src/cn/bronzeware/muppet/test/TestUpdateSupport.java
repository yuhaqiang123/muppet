package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestUpdateSupport extends TestSuper{

	@Test
	public void testUpdate(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Note note = session.query("select * from t_note where pk = ?", new Object[]{2}, Note.class).get(0);
				Logger.println("查询前记录");
				Logger.println(note);
				note.setUser_id("修改");
				session.update(note, " pk = ?", new Object[]{2});
				
				note = session.query("select * from t_note where pk = ?", new Object[]{2}, Note.class).get(0);
				
				Logger.println(note);
			}
		});
	}
	
	@Test
	public void testUpdatePriamryKey(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				
				Note note = session.queryById(Note.class, 2);
				Logger.println("查询前结果" + note);
				note.setUsername("修改username");
				note.setId(90);
				
				Logger.println("按主键更新：" + session.updateByPrimaryKey(note));
				
				note = session.queryById(Note.class, 2);
				Logger.println("修改后结果" + note);
			}
		});
	}
	
	
	public static void main(String[] args){
		TestUpdateSupport test = new TestUpdateSupport();
		test.testUpdatePriamryKey();
	}
	
	
}
