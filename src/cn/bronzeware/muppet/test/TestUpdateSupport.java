package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.util.testframework.Test;

@Test
public class TestUpdateSupport extends TestSuper{

	public void testUpdate(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public void execute(Session session, Transaction transaction) {
				
				//session.update(object, wheres, wherevalues);
				
			}
		});
	}
	
}
