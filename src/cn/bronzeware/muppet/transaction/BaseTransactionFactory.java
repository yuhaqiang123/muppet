package cn.bronzeware.muppet.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.activation.DataSource;

import cn.bronzeware.muppet.datasource.DataSourceUtil;

public class BaseTransactionFactory implements TransactionFactory{

	public static BaseTransactionFactory newBaseTransactionFactory(DataSourceUtil dataSourceUtil) {
		return new BaseTransactionFactory(dataSourceUtil);
	}
	
	private DataSourceUtil dataSourceUtil;
	private BaseTransactionFactory(DataSourceUtil dataSourceUtil) {
		this.dataSourceUtil = dataSourceUtil;
	}
	
	@Override
	public Transaction newTransaction(Connection conn, boolean autoCommit) {
		try {
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Transaction transaction = new BaseTransaction(conn, dataSourceUtil);
		
		return transaction;
	}

}
