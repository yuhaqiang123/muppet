package cn.bronzeware.muppet.annotations;

import java.lang.reflect.Field;
import java.util.Map;

public class RowRecord {

	private Map<Field, Object> map;
	private Map<Field, String> columnNames;
	public Map<Field, String> getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(Map<Field, String> columnNames) {
		this.columnNames = columnNames;
	}
	private String tableName ;
	private Field primarykey;
	
	private String primaryKeyName;
	
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	public static void main(String[] args) {

	}
	public Map<Field, Object> getMap() {
		return map;
	}
	public void setMap(Map<Field, Object> map) {
		this.map = map;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Field getPrimarykey() {
		return primarykey;
	}
	public void setPrimarykey(Field primarykey) {
		this.primarykey = primarykey;
	}
	

}
