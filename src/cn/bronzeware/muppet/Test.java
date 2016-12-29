package cn.bronzeware.muppet;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import cn.bronzeware.muppet.datasource.DataSourceUtil;


public class Test {

	public static void main(String[] args) {

		Connection connection =null;
		PreparedStatement ps = null;
		try {
			 connection = new DataSourceUtil().getConnection();
			String sql = "insert into t_user" +
					"(username,password,create_time) values (?,?,?)";
			String sqlquery = "select username,password from t_user";
			ps = connection.prepareStatement(sql);
			
			ps.setObject(1, "username");
			ps.setObject(2, "password");
			ps.setObject(3, "fae");
			ParameterMetaData data = ps.getParameterMetaData();
			
			System.out.println(data.getParameterCount());
			for (int i = 1; i <=data.getParameterCount(); i++) {
				 	System.out.print(data.getParameterClassName(i)+"  ");
	                System.out.print(data.getParameterMode(i)+"  ");
	                System.out.print(data.getParameterType(i)+"  ");
	                System.out.println(data.getParameterTypeName(i));
			}
			
			ps.execute();
			
			
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		}
		finally{
			try {
				if(ps!=null){
					System.out.println("ps close");
					ps.close();
				}
			
				if(connection!=null&&connection.isClosed()==false)
				{
					System.out.println("connection close");
					connection.close();
				}
			}
			 catch (SQLException e) {
					// 
					e.printStackTrace();
				}
		}
		
	}

}
