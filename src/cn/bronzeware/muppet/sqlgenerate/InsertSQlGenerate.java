package cn.bronzeware.muppet.sqlgenerate;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 生成插入语句，插入子句，insert into table(,,,,,) values ()
 * 插入子句的样式较为单调，所以我们可以做的更加方便，实际上 ，插入操作本来就应该很
 * 方便，1.0版本实现了主体功能，1.1版本加上了缓存，1.2版本加上了注释
 */
class InsertSQlGenerate implements SqlGenerate{


	private static Map<String, String> factory = 
			new HashMap<String, String>();
	
	public static void main(String[] args) {
		
		
	
	}

	@Override
	public String getSql(Sql sql) throws ParamCanNotBeNullException {
		StringBuffer sqlBuffer = null;
		String tableName = 	sql.getTableName();
		if(tableName==null){
			throw new ParamCanNotBeNullException("tableName");
		}
		
		/**
		 * 检查缓存
		 */
		if(factory.containsKey(tableName)){
			return factory.get(tableName);
		}
		
		//获取要插入的属性域
		Field[] map = sql.getObjectkeys();
	    sqlBuffer = new StringBuffer();
	    sqlBuffer.append("insert into ");
	    sqlBuffer.append(tableName);
	    sqlBuffer.append("(");
		StringBuffer values = new StringBuffer();
		values.append("(");
		
		
		for(Field s:map){
			String columnName = sql.getColumnNames().get(s);
			sqlBuffer.append(columnName +",");
			values.append("?"+",");
		}
		//删除多余的逗号
		if(map.length>0){
			sqlBuffer.delete(sqlBuffer.length()-1, sqlBuffer.length());
			values.delete(values.length()-1, values.length());
		}
		
		sqlBuffer.append(") values ");
		values.append(")");
		sqlBuffer.append(values);

		String sqlString = sqlBuffer.toString();
		
		//缓存sql语句
		synchronized(factory){
			factory.put(tableName, sqlString);
		}
		
		return sqlString;
	}

}
