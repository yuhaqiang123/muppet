package cn.bronzeware.muppet.transaction;

import cn.bronzeware.muppet.core.Session;

public interface TransactionExecute {

	
	public Object execute(Session session, Transaction transaction);
}
