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


public class UpdateContext  extends AbstractContext{

	public UpdateContext(Container<String, ResourceInfo> container, ApplicationContext applicationContext)
	{
		this.container = container;
		this.sqlGenerateHelper = new SqlGenerateHelper(container, applicationContext);
	}
	
	private Container<String, ResourceInfo> container;
	private SqlGenerateHelper sqlGenerateHelper;
	
	
	
	public Object executeByPrimaryKey(Object object){
		Sql sql = new Sql();
		try {
			sql = sqlGenerateHelper.execute(object,sql, SqlGenerate.UPDATE);
			Field primarykeyField = sql.getPrimarykey();
			String wheres = primarykeyField.getName() + " = ?";
			Object value = ObjectConvertor.getValue(object, primarykeyField);
			return this.execute(object, wheres, new Object[]{value});
				
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
		} catch (SqlGenerateException e) {
			// 
			e.printStackTrace();
		} catch (SqlGenerateContextException e) {
			// 
			e.printStackTrace();
		}
		return false;
	}

	
	public Object execute(Object object
			,String wheres 
			,Object[] wherevalues) 
		throws SqlGenerateContextException
		{
			Connection connection = null;
					PreparedStatement ps = null;
				try {
					Transaction transaction = ThreadLocalTransaction.get();
					connection = transaction.getConnection();
					Sql sql = new Sql();
					sql.setWheres(wheres);
					sql = sqlGenerateHelper.execute(object,sql, SqlGenerate.UPDATE);
					
					String sqlString = sql.getSql();
					Map<Field, Object> map = sql.getValues();
					
					ps = connection.prepareStatement(sqlString);
					//Logger.println(sqlString);
					int i = 1; 
					for(Field field:sql.getObjectkeys()){
						ps.setObject(i, map.get(field));
						i++;
					}
					
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
				}
				try {
					/*if(results!=null){
						results.close();
					}*/
					
					if(ps!=null){
						ps.close();
					}
					
					/*if(connection!=null){
						connection.close();
					}*/
					
				} catch (SQLException e) {
					// 
					e.printStackTrace();
				}
				return false;
	}
	
	/*private void execute(String sql,Object[] values){

		int select_index = sql.indexOf("sql");
		int from_index = sql.indexOf("from");
		String string = new StringBuffer(sql).substring(select_index+6, from_index);
		System.out.println(string);
	}*/
	

}
