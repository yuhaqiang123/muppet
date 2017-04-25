package cn.bronzeware.muppet.context;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.converter.ObjectConvertor;
import cn.bronzeware.muppet.core.ThreadLocalTransaction;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.entities.User;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerate;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateException;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateHelper;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.util.log.Logger;

public class DeleteContext  extends AbstractContext{

	public DeleteContext(Container<String, ResourceInfo> container, ApplicationContext applicationContext){
		this.container = container;
		this.sqlGenerateHelper = new SqlGenerateHelper(container, applicationContext);
	}
	private Container<String, ResourceInfo> container;
	private SqlGenerateHelper sqlGenerateHelper ;
	private ApplicationContext applicationContext;
	
	public Object executeByPrimaryKey(Object object) throws ContextException{
		Sql sql = new Sql();
		try {
			sql = sqlGenerateHelper.execute(object, sql, SqlGenerate.DELETE);
			Field primaryKeyField = sql.getPrimarykey();
			Object value = ObjectConvertor.getValue(object, primaryKeyField);
			StringBuffer wheres = new StringBuffer();
			wheres.append(primaryKeyField.getName());
			wheres.append(" = ? ");
			return this.execute(object, wheres.toString(), new Object[]{value});
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
		} catch (SqlGenerateException e) {
			// 
			e.printStackTrace();
		} catch (ContextException e) {
			// 
			throw e;
		}
		return false;
		
	}
	
	public Object execute(Object object,String wheres ,Object[] wherevalues)
	throws ContextException{
		
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			Transaction transaction = ThreadLocalTransaction.get();
			connection = transaction.getConnection();
			Sql sql = new Sql();
			sql.setWheres(wheres);
			sql = sqlGenerateHelper.execute(object,sql, SqlGenerate.DELETE);
			
			String sqlString = sql.getSql();
			Map<Field, Object> map = sql.getValues();
			
			ps = connection.prepareStatement(sqlString);
			/*System.out.println("Time:"+
					new Date(System.currentTimeMillis()).toLocaleString()
					+"  "+sqlString);*/
			Logger.println(sqlString);
			int i = 1;
			/* 
			for(Field field:sql.getObjectkeys()){
				ps.setObject(i, map.get(field));
				i++;
			}*/
			
			for(Object object2:wherevalues){
				ps.setObject(i, object2);
				i++;
			}
			
			int success = ps.executeUpdate();
			return success > 0 ? true:false;
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
		} catch (SqlGenerateException e) {
			// 
			throw new SqlGenerateContextException(e.getMessage());
		}finally
		{
			try {
				/*if(results!=null){
					results.close();
				}*/
				
				if(ps!=null){
					ps.close();
				}
				
			/*	if(connection!=null){
					connection.close();
				}*/
				
			} catch (SQLException e) {
				// 
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public static void main(String[] args) {

		/*DeleteContext deleteContext = new DeleteContext();
		User testEntity = new User(); 
		//testEntity.setPassword("yg");
		//testEntity.setUsername("passssssssswod");
		//Date date = new Date(System.currentTimeMillis());
		//testEntity.setDate(date);
		String wheres = "username = ?";*/
		//testEntity.setDate(new Date(System.currentTimeMillis()));
		//deleteContext.execute(testEntity,wheres,new Object[]{"username"});
	}
}
