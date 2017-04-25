package cn.bronzeware.muppet.sqlgenerate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * sql类，是数据操作上下文 InsertContext,UpdateContex，等与
 * SqlGenerate之间交互的桥梁，context通过向sqlgenerate传过去一个
 * sql对象，即可以获得相关的sql语句，sqlgenerate可能返回string对象，
 * 也可能把结果返回到sql对象中
 * @author 于海强
 * 2016-6-27  下午6:25:33
 */
public class Sql {

	/**
	 * sql语句，查询条件中没有值
	 */
	private String sql ; 

	/**
	 * 表名
	 */
	private String tableName = "";
	
	private Map<Field, String> columnNames;
	
	
	public Map<Field, String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(Map<Field, String> columnNames) {
		this.columnNames = columnNames;
	}

	/*
	 * 实体对象中的属性域，某一类的实体对象的
	 * 属性集合损耗比较大，应该缓存
	 */
	private Field[] objectkeys;
	
	private Field primarykey;

	private String primaryKeyName;

	
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	/**
	 * where子句
	 */
	private String wheres;
	
	/**
	 * 对应对象相关属性域中的值 的Map，可以反射生成一个对象
	 */
	private Map<Field, Object> values;
	
	/**
	 * 这个字段废弃
	 * @deprecated
	 */
	private int valid_value_length = 0;
	
	

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	

	public String getWheres() {
		return wheres;
	}

	public void setWheres(String wheres) {
		this.wheres = wheres;
	}

	public Field[] getObjectkeys() {
		return objectkeys;
	}

	public void setObjectkeys(Field[] objectkeys) {
		this.objectkeys = objectkeys;
	}

	public Map<Field, Object> getValues() {
		return values;
	}

	public void setValues(Map<Field, Object> values) {
		this.values = values;
	}

	/**
	 * @deprecated
	 */
	public int getValid_value_length() {
		return valid_value_length;
	}

	/**
	 * @deprecated
	 */
	public void setValid_value_length(int valid_value_length) {
		this.valid_value_length = valid_value_length;
	}

	public Field getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(Field primarykey) {
		this.primarykey = primarykey;
	}

	
	
}