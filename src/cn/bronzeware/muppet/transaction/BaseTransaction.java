package cn.bronzeware.muppet.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseTransaction implements Transaction{

	
	private Connection connection;
	
	
	public BaseTransaction(Connection connection){
		this.connection = connection;
	}


	@Override
	public Connection getConnection() {
		// TODO 自动生成的方法存根
		return connection;
	}


	@Override
	public void commit() throws SQLException {
		
		connection.commit();
	}


	@Override
	public void rollback() throws SQLException {
		
		connection.rollback();
	}


	@Override
	public void close() throws SQLException {
		connection.close();
	}
	
	
	
	
	
	
}
