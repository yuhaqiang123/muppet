package cn.bronzeware.muppet.filters;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SqlResponse {

	private ResultSet resultSet;
	
	private ResultSetMetaData resultSetMetaData;
	
	private long respTime;

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}

	public long getRespTime() {
		return respTime;
	}

	public void setRespTime(long respTime) {
		this.respTime = respTime;
	}
	
}
