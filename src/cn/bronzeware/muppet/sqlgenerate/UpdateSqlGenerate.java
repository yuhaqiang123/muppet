package cn.bronzeware.muppet.sqlgenerate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.bronzeware.muppet.util.log.Logger;


/**
 * 生成相应的更新语句，更新语句同样由于查询条件不同，不能缓存，所以之前1.1
 * 版本的缓存功能必须去掉 <br>
 * <br>
 * 值得一说的是，这个更新语句生成，有一个小小的问题，例如用户通过实体类，更新<br><br>
 * 数据表，但是如果实体类有十个属性，但是他只想更新其中两个属性，那么其他八个怎么办<br><br>
 * 当然理想的结果是用户，是自己写个sql语句，但是我们想为他们做一些事情，目前可以分为<br><br>
 * 三个方向，
 * <br>第一是如果属性域为默认值，那么即不更改
 * <br>第二 属性域所有值，不论是否设置均更新到数据库
 * <br>第三  用户自己输入sql语句。
 * <br> 我们可以为第一二中情况设置两种 模式一种是
 * UPDATE_WITHOUT_DEFAULT 即默认值不更新<br>
 * UPDATE_WITH_DEFAULT 即默认值更新 <br>
 * 
 * 
 * @author 于海强
 * 2016-6-27  下午6:34:39
 */
class UpdateSqlGenerate implements SqlGenerate{

	/*private static Map<String, String> factory = 
			new HashMap<String, String>();*/
	
	public String getSql(Sql sql) throws Exception{
	
		StringBuffer s = new StringBuffer();
		String tableName = sql.getTableName();
		
		if(tableName==null){
			throw new ParamCanNotBeNullException("tableName");
		}
		/**
		 * 删除缓存功能
		 */
		/*if(factory.containsKey(tableName)){
			return factory.get(tableName);
		}*/
		
		Field[] values = sql.getObjectkeys();
		Map<Field, Object> map = sql.getValues();
		
		s.append("update ");
		s.append(tableName);
		
			s.append(" set ");
		
		List<Field> list = new LinkedList<Field>();
		for(Field e:values){
			Object object = map.get(e);
			Object defaultvalue = TypeDefaultValueValid.valid(e.getGenericType());
			//Logger.println(e.getName()+" "+object+" default "+defaultvalue);
			
			if(object ==null||object.equals(defaultvalue)){
				
				continue;
			}
			list.add(e);
			String columnName = sql.getColumnNames().get(e);
			s.append(columnName);
			s.append(" = ");
			s.append("? ,");
		}
		 sql.setObjectkeys(list.toArray(new Field[list.size()]));
		if(values.length>0){
			s.delete(s.length()-1, s.length());
		}
		if(values.length<=0){
			throw new UpdateException();
		}
		
		if(sql.getWheres()!=null){
			s.append(" where ");
			s.append(sql.getWheres());
		}
		String sqlString = s.toString();
		/*factory.put(tableName, sqlString);*/
		return sqlString;
		
		
		
	}
	public static void main(String[] args) throws Exception {

		UpdateSqlGenerate updateSqlGenerate = 
				new UpdateSqlGenerate();
		Sql sql = new Sql();
		String[] map = new String[2];
		map[0] = "usernfefaamafefe";
		map[1] = "passwofaefwrd";
		
		sql.setTableName("t_user");
		sql.setWheres(" id = 3");
		String sqlString = updateSqlGenerate.getSql(sql);
		System.out.println(sqlString);
	}

}
