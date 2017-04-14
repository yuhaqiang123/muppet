package cn.bronzeware.muppet.datasource;

public class ConnectionRecord {
	private String connectionId;
	private long connectionStartTime;
	private long connectionEndTime;
	private String dataSourceKey;
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public long getConnectionStartTime() {
		return connectionStartTime;
	}
	public void setConnectionStartTime(long connectionStartTime) {
		this.connectionStartTime = connectionStartTime;
	}
	public long getConnectionEndTime() {
		return connectionEndTime;
	}
	public void setConnectionEndTime(long connectionEndTime) {
		this.connectionEndTime = connectionEndTime;
	}
	public String getDataSourceKey() {
		return dataSourceKey;
	}
	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}
}
