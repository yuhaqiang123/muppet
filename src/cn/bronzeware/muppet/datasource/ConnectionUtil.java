package cn.bronzeware.muppet.datasource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtil {

	public static void closeConnection(Connection connection, DataSourceUtil dataSourceUtil){
		if(connection != null){
			try{
				connection.close();
				
				DataSourceEvent event = new DataSourceEvent();
				event.setKey(dataSourceUtil.getDataSourceKey());
				event.setType(DataSourceListener.Type.CONNECTION_CLOSED);
				ConnectionRecord connectionRecord = dataSourceUtil.getConnectionIdLocal().get();
				connectionRecord.setConnectionEndTime(System.currentTimeMillis());
				connectionRecord.setDataSourceKey(dataSourceUtil.getDataSourceKey());
				event.setConnectionRecord(connectionRecord);
				
				dataSourceUtil.getConnectionIdLocal().remove();
				dataSourceUtil.getDataSourceListener().event(event);
				
			}catch(SQLException e){
				DataSourceEvent event = new DataSourceEvent();
				event.setError(e);
				event.setKey(dataSourceUtil.getDataSourceKey());
				event.setType(DataSourceListener.Type.CONNECTION_CLOSED_ERROR);
				ConnectionRecord connectionRecord = dataSourceUtil.getConnectionIdLocal().get();
				connectionRecord.setConnectionEndTime(System.currentTimeMillis());
				connectionRecord.setDataSourceKey(dataSourceUtil.getDataSourceKey());
				event.setConnectionRecord(connectionRecord);
				
				dataSourceUtil.getConnectionIdLocal().remove();
				dataSourceUtil.getDataSourceListener().event(event);
			}
		}
	}
}
