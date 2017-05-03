package cn.bronzeware.muppet.test;

import java.sql.SQLException;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestDeleteSupport extends TestSuper{
	
	/**
	 * 
	 * 测试 Session.delete(Class, whereCondition, conditionValue)
	 * 
	 */
	@Test
	public void testDelete(){
		Session session = sessionFactory.getSession();
		
		String deleteContidition = " value = ? ";
		Object[] conditionValue = new Object[]{"2"};
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			Logger.println(session.query(Note.class, deleteContidition, conditionValue));
			
			session.delete(Note.class, deleteContidition, conditionValue);
			
			Logger.println(session.query(Note.class, deleteContidition, conditionValue));
			transaction.rollback();
		}catch(Exception e){
			try {
				transaction.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		session.close();
	}
	
	/**
	 * 测试Session.delete(Class, whereCondition, conditionValue)
	 */
	@Test
	public void testDeleteByPrimaryKeyOnTransactionExecute(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			@Override
			public void execute(Session session, Transaction transaction) {
				
				String deleteContidition = " value = ? ";
				Object[] conditionValue = new Object[]{"2"};
				Logger.println(session.query(Note.class, deleteContidition, conditionValue));
				
				session.delete(Note.class, deleteContidition, conditionValue);
				
				Logger.println(session.query(Note.class, deleteContidition, conditionValue));
			}
		});
	}
	
	/**
	 * 测试Session.deleteByPrimaryKey(Class, primaryKeyValue)
	 */
	@Test
	public void testDeleteByPrimaryKey(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Logger.println(session.queryById(Note.class, 2));
				session.deleteByPrimaryKey(Note.class, 2);
				Logger.println(session.queryById(Note.class, 2));
			}
		});
	}
	
	public static void main(String[] args){
		TestDeleteSupport s = new TestDeleteSupport();
		s.testDeleteByPrimaryKey();
	}
	

}
