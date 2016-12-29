package cn.bronzeware.muppet.context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.omg.CORBA.PRIVATE_MEMBER;

import cn.bronzeware.muppet.datasource.DataSourceUtil;

public class SelectContextTest {

	public static void main(String[] args) {

		String sql = "select u.username as user ,u.password from t_user u where id = 4";
		Connection connection;
		try {
			connection =  new DataSourceUtil().getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			/**
			 * 
			 */
			int select_index = sql.indexOf("sql");
			int from_index = sql.indexOf("from");
			String string = new StringBuffer(sql).substring(select_index+7, from_index);
			
			
			String[] reString = string.split(",");
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			for(int i = 0;i<reString.length;i++){
				String querykey = reString[i].trim();
				int as_index = querykey.indexOf(" as ");
				if(as_index>0){
					querykey = new String(querykey.substring(as_index + 4));
					
				}
				int dot_index = querykey.indexOf('.');
				if(dot_index>0){
					querykey = new String(querykey.substring(dot_index + 1));
				}
				reString[i] = querykey;
				System.out.println(querykey);
			}
			//
			
			while(rs.next()){
				String user = (String) rs.getObject(reString[0]);
				String password = (String) rs.getObject(reString[1]);
				System.out.println(user+"  "+password);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		
			
			
			
			
		
	}

}
