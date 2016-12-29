package cn.bronzeware.muppet.sqlgenerate;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 生成删除语句，删除语句 delete from table where 查询条件
 * 不支持缓存特性，1.0版本没有缓存，在1.1版本中加入了缓存，后发现这是错误的
 * 在1.2版本中即放弃缓存
 * 
 * @author 于海强 
 * 2016-6-27  下午4:36:11
 * 
 */
class DeleteSqlGenerate implements SqlGenerate {

	private static Map<String, String> factory = 
			new HashMap<String, String>();
	
	/**
	 * 获取删除操作的的Sql相关信息
	 */
	public String getSql(Sql sql) throws ParamCanNotBeNullException{
		/**
		 * 获取表名称
		 */
		String tableName =  sql.getTableName();
		if(tableName==null){
			throw new ParamCanNotBeNullException("tableName");
		}
		//查找factoy中是否有相关表的删除语句
		//删除，因为where子句不同
		/*if(factory.containsKey(tableName)){
			return factory.get(tableName);
		}*/
		
		//获取删除子句中的where子句
		String wheres = sql.getWheres();
		
		StringBuffer stringBuffer = new StringBuffer(); 
		stringBuffer.append("delete from ");
		stringBuffer.append(tableName);
		if(sql.getWheres()!=null){
			stringBuffer.append(" where ");
			stringBuffer.append(sql.getWheres());
		}
		String sqlString = stringBuffer.toString();
		
		//缓存sql语句,where子句不同，不能缓存
		//factory.put(tableName, sqlString);
		return sqlString;
	}
	public static void main(String[] args) throws ParamCanNotBeNullException {

		SqlGenerate sqlGenerate = new  DeleteSqlGenerate();
		Sql sql = new Sql();
		sql.setTableName("t_user");
		sql.setWheres(" username = username");
		String sqlString;
		try {
			sqlString = sqlGenerate.getSql(sql);
			System.out.println(sqlString);
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
		
	}

}
