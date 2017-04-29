package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestQuery extends TestSuper{

	public TestQuery() {
	}
	
	@Test
	public void testQueryById(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Logger.println("根据主键查询：");
				Logger.println(session.queryById(Note.class, 2));
			}
		});
	}
	
	@Test
	public void testQueryByExecute(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Logger.println("查询user_id 2,4 间隔 Note ");
				Logger.println(session.query(Note.class, " user_id > ? and user_id < ?", new Object[]{2,4}));				
			}
		});
	}
	
	@Test
	public void testMutiQueryList(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Logger.println("测试简单sql查询");
				ArrayUtil.println(session.query("select * from t_note ", null));
			}
		});
	}
	
	@Test
	public void testSimpleOneTableQuery(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Logger.println("单表简单查询");
				ArrayUtil.println(session.query(Note.class, " username = ? or username = ?", new Object[]{2, 4}));
			}
		});
	}
	
	@Test
	public void testQueryOne(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				Logger.println("查询单个结果");
				Logger.println(
						session.queryOne("select * from t_note where password = ? or user_id = ?"
								, new Object[]{3,4}));
			
			}
		});
	}
	
	
	public static void main(String[] args){
		TestQuery id = new TestQuery();
		id.testQueryOne();
	}
}
