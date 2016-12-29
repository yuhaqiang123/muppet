package cn.bronzeware.muppet.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TestJDBC {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		try {
				Class.forName("com.mysql.jdbc.Driver");
				
				
			for(int i = 0;i<1;i++){
				
				Connection connection = DriverManager
						.getConnection("jdbc:mysql://localhost:3306/study?Unicode=true&characterEncoding=utf-8&generateSimpleParameterMetadata=true"
								, "root", "root");
				PreparedStatement ps = null;
				try {
					
					String sql = "SHOW CREATE TABLE tb_blog";
					 ps = connection.prepareStatement(sql);
					/*ps.setObject(1, "yuhaiqiang");
					ps.setObject(2, "username");
					ps.setObject(3, 2);*/
					ResultSet rs = ps.executeQuery();
					if(rs.next()){
						System.out.println(rs.getString("Create Table"));
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}finally{
					ps.close();
					connection.close();
				}
				
			}
			
			
			
		} 
		catch (ClassNotFoundException e) {
			// 
			e.printStackTrace();
		}
		catch (SQLException e) {
			// 
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
	}

}
