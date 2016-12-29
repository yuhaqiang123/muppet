package cn.bronzeware.muppet.test;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaticClass extends A implements Serializable{

	 
public static void main(String[] args){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://202.206.249.66:3306/itcast0721project", "root", "network@centos");
			String string  = "select FULL_MSG_ from ACT_HI_COMMENT";
			PreparedStatement ps = (PreparedStatement) connection.prepareCall(string);
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				byte[] bytes = rs.getBytes(1);
				System.out.println(new String(bytes,"gbk"));
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

