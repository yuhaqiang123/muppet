package cn.bronzeware.muppet.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.context.ContextException;
import cn.bronzeware.muppet.context.DeleteContext;
import cn.bronzeware.muppet.context.InsertContext;
import cn.bronzeware.muppet.context.SelectContext;
import cn.bronzeware.muppet.context.SqlGenerateContextException;
import cn.bronzeware.muppet.context.UpdateContext;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.transaction.Transaction;

/**
 * 
 */
class StandardSession implements Session,Closed{

	protected ApplicationContext applicationContext = null;
	
	private static final String Boolean = null;
	
	private ResourceContext context ;
	
	private  InsertContext insertContext ;
	private  SelectContext selectContext ;
	

	private  UpdateContext updateContext ;
	private  DeleteContext deleteContext ;
	
	private  boolean close = false;
	
	private Transaction transaction;
	
	private  Container<String,ResourceInfo> container;
	public StandardSession(Transaction transaction,ApplicationContext applicationContext){
		this.transaction = transaction;
		this.applicationContext = applicationContext;
	}
	
	
	
	
	public boolean close(){
		if(close==true){
			return true;
		}
		try {
			close = true;
			commit();//如果是自动提交就默认不执行
			transaction.close();
			ThreadLocalTransaction.remove();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 启动前将默认提交之前数据，并且将关闭事务自动提交。
	 * 除非手动提交事务，或者调用close,关闭Session.
	 */
	public Transaction beginTransaction() throws SQLException{
		commit();
		setAutoCommit(false);
		return this.transaction;
	}
	
	
	private void setAutoCommit(boolean autoCommit){
		Connection conn = transaction.getConnection();
		try {
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private  void commit() {
		Connection conn = transaction.getConnection();
		try {
			if(!conn.getAutoCommit()){
				transaction.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/*public  void initial(Properties properties){
		new DataSourceUtil(properties);
		 = true;
	}*/
	
	
	public  boolean update(Object object,String wheres ,Object[] wherevalues){
		
		Object success = null;
		try {
			success = updateContext.execute(object, wheres, wherevalues);
		} catch (SqlGenerateContextException e) {
			e.printStackTrace();
		}
		
		return (Boolean)success;
				
	}
	
	public boolean updateByPrimaryKey(Object object){
		return (Boolean)updateContext.executeByPrimaryKey(object);
	}
	
	
	public boolean insert(Object object)
	{
		boolean success = false;
		try {
			success = (Boolean)insertContext.execute(object, null, null);
		} catch (ContextException e) {
			// 
			e.printStackTrace();
		}
		return success;
	}
	
	public boolean delete(Object object,String wheres ,Object[] wherevalues)
	{
		Boolean success = false;
		try {
			success = (Boolean)deleteContext.execute(object, wheres, wherevalues);
		} catch (ContextException e) {
			// 
			e.printStackTrace();
		}
		return success;
	}
	
	public boolean deleteByPrimaryKey(Object object){
		Boolean success = false;
		try {
			success = (Boolean)deleteContext.executeByPrimaryKey(object);
		} catch (ContextException e) {
			// 
			e.printStackTrace();
		}
		return success;
	}
	/*public boolean delete(int id,Object object){
		Object object 
		delete(id, clazz)
	}*/
	
	/**
	 * 单表查询
	 */
	public <T> List<T>  query(Class<T> clazz
			,String wheres 
			,Object[] wherevalues){
		List<T> list = null;
		try {
			list = selectContext.execute(clazz, wheres, wherevalues);
		} catch (ContextException e) {
			// 
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 多表查询
	 */
	public <T> List<T>  query(String queryString,Object[] values,Class<T> clazz){
		List<T> list = selectContext.execute(queryString, values, clazz);
		return list;
	}
	
	
	
	@Override
	public Map<String, Object> queryOne(String sql, Object[] values) {
		return this.selectContext.executeToMap(sql, values);
	}




	@Override
	public List<Map<String, Object>> query(String sql, Object[] values) {
		return this.selectContext.executeToList(sql, values);
	}


	public <T> T queryById(Class<T> clazz, Object primaryKeyValue){
		return this.selectContext.executeOnPrimaryKey(clazz, primaryKeyValue);
	}


	public Container<String, ResourceInfo> getContainer() {
		return container;
	}
	public void setContainer(Container<String, ResourceInfo> container) {
		this.container = container;
	}
	
	public InsertContext getInsertContext() {
		return insertContext;
	}
	public void setInsertContext(InsertContext insertContext) {
		this.insertContext = insertContext;
	}
	public SelectContext getSelectContext() {
		return selectContext;
	}
	public void setSelectContext(SelectContext selectContext) {
		this.selectContext = selectContext;
	}
	public UpdateContext getUpdateContext() {
		return updateContext;
	}
	public void setUpdateContext(UpdateContext updateContext) {
		this.updateContext = updateContext;
	}
	public DeleteContext getDeleteContext() {
		return deleteContext;
	}
	public void setDeleteContext(DeleteContext deleteContext) {
		this.deleteContext = deleteContext;
	}
	@Override
	public boolean hasClosed() throws IllealInvokeException {
		return close;
	}
	@Override
	public <T> Criteria<T> createCriteria(Class<T> clazz) {
		
		Criteria<T> criteria =
				new CriteriaBuilder<T>(container, selectContext,clazz);
		return criteria;
	}

}
