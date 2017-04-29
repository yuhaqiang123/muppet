package cn.bronzeware.muppet.core;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.bronzeware.muppet.transaction.Transaction;

public interface Session {

	public boolean close();
	
	
	public Transaction beginTransaction() throws SQLException;
	
	public <T> Criteria<T> createCriteria(Class<T> clazz);
	
	public  boolean update(Object object,String wheres ,Object[] wherevalues);
	
	
	public boolean updateByPrimaryKey(Object object);
	
	public boolean insert(Object object);
	
	/**
	 * 
	 * @param clazz
	 * @param wheres
	 * @param wherevalues
	 * @return
	 */
	public boolean delete(Class clazz, String wheres ,Object[] wherevalues);
	
	
	/**
	 * 根据主键删除
	 * @param clazz 指定pojo类型
	 * @param primaryKeyValue 主键值
	 * @return
	 */
	public boolean deleteByPrimaryKey(Class clazz, Object primaryKeyValue);
	
	/**
	 * 查询 指定pojo类型的结果集，
	 * @param clazz 指定pojo
	 * @param wheres  where 查询条件 
	 * @param wherevalues  填充查询条件占位符
	 * @return
	 */
	public <T> List<T>  query(Class<T> clazz
			,String wheres 
			,Object[] wherevalues);
	
	/****
	 * 
	 * 根据sql语句，查询指定pojo列表类型
	 * @param queryString 完整的sql 语句，例如 select * from tableName where columnName = ?
	 * @param values sql 中占位符 ? 要替换的值
	 * @param clazz  结果集映射的Pojo类型
	 * @return
	 */
	public <T> List<T>  query(String queryString,Object[] values,Class<T> clazz);
	
	
	/**
	 * 
	 * 根据sql语句，查询Map<String,Object> 类型结果集
	 * 用于单个查询
	 * @param sql
	 * @param values
	 * @return
	 */
	public Map<String, Object> queryOne(String sql,Object[] values);
	
	
	/**
	 * 根据sql语句，查询List<Map<String,Object>> 类型结果集
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Map<String, Object>> query(String sql,Object[] values);
	
	
	/**
	 * 根据主键查询 
	 * @param clazz
	 * @param primaryKeyValue
	 * @return
	 */
	public <T> T queryById(Class<T> clazz, Object primaryKeyValue);
}
