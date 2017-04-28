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
	
	
	public boolean delete(Object object,String wheres ,Object[] wherevalues);
	
	
	public boolean deleteByPrimaryKey(Object object);
	
	public <T> List<T>  query(Class<T> clazz
			,String wheres 
			,Object[] wherevalues);
	
	public <T> List<T>  query(String queryString,Object[] values,Class<T> clazz);
	
	public Map<String, Object> queryOne(String sql,Object[] values);
	
	public List<Map<String, Object>> query(String sql,Object[] values);
	
	public <T> T queryById(Class<T> clazz, Object primaryKeyValue);
}
