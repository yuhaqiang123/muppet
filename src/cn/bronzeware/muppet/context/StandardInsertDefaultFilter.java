package cn.bronzeware.muppet.context;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import cn.bronzeware.muppet.converter.ObjectConvertor;
import cn.bronzeware.muppet.filters.FilterChain;
import cn.bronzeware.muppet.filters.SqlRequest;
import cn.bronzeware.muppet.filters.SqlResponse;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerate;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateException;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateHelper;
import cn.bronzeware.muppet.transaction.Transaction;


public class StandardInsertDefaultFilter implements DefaultFilter{

	
	private Container<String, ResourceInfo> container;
	private SqlGenerateHelper sqlGenerateHelper ;
	private PreparedStatement ps;
	
	public StandardInsertDefaultFilter(Container<String,ResourceInfo> container
			,SqlGenerateHelper sqlGenerateHelper
			,PreparedStatement ps){
		this.container = container;
		this.sqlGenerateHelper = sqlGenerateHelper;
		this.ps = ps;
	}
	

	@Override
	public void doFilter(FilterChain chain, SqlContext context) {
		SqlRequest request  = context.getSqlRequest();
		SqlResponse response = context.getSqlResponse();
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		Boolean success = false;
		/*
		try {
			Transaction transaction = request.getTransaction();
			connection = transaction.getConnection();
			

			
			Map<Field, Object> map = sql.getValues();
			ps = connection.prepareStatement(sqlString,Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			for(Entry<Field, Object> e:map.entrySet()){
				ps.setObject(i,e.getValue());
				//System.out.println(e.getKey().getName()+e.getValue());
				i++;
			}
			System.out.println("Time:"+
					new Date(System.currentTimeMillis()).toLocaleString()
					+"  "+sqlString);
			int rows = ps.executeUpdate();
			success= (rows== 1 ?true:false);
			results = ps.getGeneratedKeys();
			int num = -1;
			if(results.next())
	          {
	             num = results.getInt(1);
	          }
			ObjectConvertor.loadField(sql.getPrimarykey(), object, num);

		} catch (SQLException e) {
			// 
			e.printStackTrace();
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
		} catch (SqlGenerateException e1) {
			// 
			throw new SqlGenerateContextException(e1.getMessage());
		}
		finally{
			try {
				if(results!=null){
					results.close();
				}
				
				if(ps!=null){
					ps.close();
				}
				
				if(connection!=null){
					connection.close();
				}
				
			} catch (SQLException e) {
				// 
				e.printStackTrace();
			}
		}
		//return success;
		*/
		
		
	}

}
