package cn.bronzeware.muppet.transaction;

import cn.bronzeware.muppet.core.Session;

public interface TransactionExecute {

	
	public void execute(Session session, Transaction transaction);
}
