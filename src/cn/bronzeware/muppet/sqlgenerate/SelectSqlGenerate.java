package cn.bronzeware.muppet.sqlgenerate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * 单表查询语句生成，我们知道用户的单表操作，可以很方便的查询
 * 不用每一次都指定 返回哪些数据，但是用户的查询条件是必须自己输入的
 * @author 于海强
 * 2016-6-27  下午6:14:07
 */
public class SelectSqlGenerate implements SqlGenerate{

	/**
	 * 1.2版本废弃
	 */
	/*private static Map<String, String> factory = 
			new HashMap<String, String>();*/
	
	/**
	 * 获取查询相关的sql语句
	 * 单表查询语句生成，我们知道用户的单表操作，可以很方便的查询
	 * 不用每一次都指定 返回哪些数据，但是用户的查询条件是必须自己输入的
	 * @author 于海强
	 * 2016-6-27  下午6:14:59
	 */
	public String getSql(Sql sql) throws ParamCanNotBeNullException{
		String tableName = sql.getTableName();
		if(tableName==null){
			throw new ParamCanNotBeNullException("tableName");
		}
		
		/**
		 * 查询语句不能缓存，查询结果不同
		 */
		/*if(factory.containsKey(tableName)){
			return factory.get(tableName);
		}*/
		
		Field[] objectkeys = sql.getObjectkeys();
		if(objectkeys==null){
			throw new ParamCanNotBeNullException("objectkeys");
		}
		
		
		String wheres = sql.getWheres();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ");
		
		for(Field s:objectkeys){
			String columnName = sql.getColumnNames().get(s);
			stringBuffer.append(columnName+",");
		}
		if(objectkeys.length>0){
			stringBuffer.delete(stringBuffer.length()-1, stringBuffer.length());
		}
		
		stringBuffer.append(" from "+tableName);
		if(isAddWheres(wheres)){
			/**
			 * @version 1.5 如果wheres 为null或者空串,那么将不设置where
			 */
			
				//stringBuffer.append(" where ");
				stringBuffer.append(wheres);
		}
		
		String sqlString = stringBuffer.toString();
		/*factory.put(tableName,sqlString);*/
		return sqlString;
		
		
	}
	
	private boolean isAddWheres(String wheres){
		if(wheres==null||wheres.length()==0){
			return false;
		}
		
		/*if(wheres.indexOf(" and ")==-1
			&&wheres.indexOf(" or ")==-1
			&&wheres.indexOf('<')==-1
			&&wheres.indexOf('>')==-1
			&&wheres.indexOf('=')==-1
			&&wheres.indexOf("like")==-1
			&&wheres.indexOf("in")==-1){

			return false;
		}*/
		return true;
		
	}
	
	
	public static void main(String[] args) {

		/*SqlGenerate sqlGenerate = new SelectSqlGenerate();
		Sql sql = new Sql();
		sql.setTableName("t_user");
		sql.setWheres("id = 1");
		String[] objectkeys = new String[2];
		objectkeys[0] = "username";
		objectkeys[1] = "password";
		sql.setObjectkeys(objectkeys);
		try {
			String sqlString = sqlGenerate.getSql(sql);
			System.out.println(sqlString);
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
		}
		*/
	}

}
