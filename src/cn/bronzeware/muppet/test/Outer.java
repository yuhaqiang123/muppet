package cn.bronzeware.muppet.test;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



public interface Outer {

	int outer();
	
	interface Inner{
		int innnr();
	}
}
class OuterImplement implements Outer{
	

	@Override
	public int outer() {
		
		return 0;
	}
	public static void main(String[] args){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("http://202.206.249.66:3306/itcast0721project", "root", "network@centos");
			String string  = "select FULL_MSG_ from ACT_HI_COMMENT";
			PreparedStatement ps = (PreparedStatement) connection.prepareCall(string);
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				byte[] bytes = rs.getBytes(1);
				System.out.println(new String(bytes,"utf-8"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//StaticClass.Inner inner =  new StaticClass.Inner();
		//Map.Entry<String, String> entry = new HashMap.Entry<String,String>();
		//Outer.Inner
	}
}
