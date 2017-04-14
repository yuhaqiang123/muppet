package cn.bronzeware.muppet.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.ConnectionRecord;
import cn.bronzeware.muppet.datasource.DataSourceEvent;
import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.datasource.DataSourceUtil;

public class BaseTransaction implements Transaction{

	
	private Connection connection;
	
	private DataSourceUtil dataSourceUtil;
	
	private DataSourceListener dataSourceListener;
	
	public BaseTransaction(Connection connection,DataSourceUtil dataSourceUtil){
		this.connection = connection;
		this.dataSourceUtil = dataSourceUtil;
		this.dataSourceListener = dataSourceUtil.getDataSourceListener();
	}


	@Override
	public Connection getConnection() {
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
		DataSourceEvent event = new DataSourceEvent();
		event.setKey(dataSourceUtil.getDataSourceKey());
		try{
			connection.close();
			event.setType(DataSourceListener.Type.CONNECTION_CLOSED);
			ConnectionRecord connectionRecord = dataSourceUtil.getConnectionIdLocal().get();
			connectionRecord.setConnectionEndTime(System.currentTimeMillis());
			event.setConnectionRecord (connectionRecord);
		}catch(SQLException e){
			event.setType(DataSourceListener.Type.CONNECTION_CLOSED_ERROR);
			ConnectionRecord connectionRecord = dataSourceUtil.getConnectionIdLocal().get();
			connectionRecord.setConnectionEndTime(System.currentTimeMillis());
			event.setConnectionRecord (connectionRecord);
			event.setError(e);
			throw e;
		}
		dataSourceUtil.getConnectionIdLocal().remove();
		dataSourceListener.event(event);
	}
	
	
	
	
	
	
}
