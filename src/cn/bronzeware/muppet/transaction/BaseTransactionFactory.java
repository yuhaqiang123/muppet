package cn.bronzeware.muppet.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseTransactionFactory implements TransactionFactory{

	
	
	
	@Override
	public Transaction newTransaction(Connection conn, boolean autoCommit) {
		try {
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Transaction transaction = new BaseTransaction(conn);
		
		return transaction;
	}

}
