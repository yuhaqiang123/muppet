package cn.bronzeware.muppet.datasource;

import java.sql.Connection;
import java.util.Map;

public class DataSourceEvent {

	private String key;
	
	private DataSourceListener.Type type;
	
	private Exception error;
	
	private ConnectionRecord connectionRecord;
	
	

	public ConnectionRecord getConnectionRecord() {
		return connectionRecord;
	}

	public void setConnectionRecord(ConnectionRecord connectionRecord) {
		this.connectionRecord = connectionRecord;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "DataSourceEvent [key=" + key + ", type=" + type + ", error=" + error + ", connectionRecord="
				+ connectionRecord + "]";
	}

	public void setKey(String key) {
		this.key = key;
	}

	public DataSourceListener.Type getType() {
		return type;
	}

	public void setType(DataSourceListener.Type type) {
		this.type = type;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}
	
	
}
