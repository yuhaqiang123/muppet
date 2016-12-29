package cn.bronzeware.muppet.context;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cn.bronzeware.muppet.filters.FilterChain;
import cn.bronzeware.muppet.filters.FilterChainWrapper;
import cn.bronzeware.muppet.filters.StandardFilterChain;

public abstract class AbstractContext implements Context{

	private StandardFilterChain standardFilterChain ;
	
	
	
	public AbstractContext(){
		standardFilterChain =  new StandardFilterChain(); 
		
	}
	
	
/*	public void doChain(SqlContext context){
		standardFilterChain.doChain(context);
	}*/
	
	public String[] getColumnNames(ResultSet set){
		try{
			ResultSetMetaData data = set.getMetaData();
			int count = data.getColumnCount();
			String[] results = new String[count];
			for(int i = 0;i<count;i++){
				results[i] = data.getColumnName(i);
			}
			return results;
		}catch(SQLException e){
			/**
			 * ignore
			 */
		}
		throw new RuntimeException("获取结果集元数据出错");
	}
	
	/*public int getRSCounts(ResultSet rs){
		ResultSetMetaData data = rs.getMetaData();
		
	}*/
	
	
}
