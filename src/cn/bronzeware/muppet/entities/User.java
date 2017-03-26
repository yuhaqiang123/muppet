package cn.bronzeware.muppet.entities;

import java.lang.reflect.Field;
import java.util.Date;

import cn.bronzeware.muppet.annotations.Column;
import cn.bronzeware.muppet.annotations.PrimaryKey;
import cn.bronzeware.muppet.annotations.Table;
import cn.bronzeware.muppet.annotations.Type;
import cn.bronzeware.muppet.sql.SqlType;

@Table(tablename="t_user")
public class User {

	private String username;
	private String password;
	public  static void main(String[] args) throws SecurityException, NoSuchFieldException{
		Class clazz = User.class;
		Field field = clazz.getDeclaredField("date");
		System.out.println(field.getGenericType().equals(Date.class));
		field.getGenericType().equals(int.class);
	}
	
	
	@PrimaryKey()
	private int id;
	
	@Column(type=@Type(type=SqlType.VARCHAR)
	,valuein={"",""},columnname="date",cannull=true,index=true,unique=true,defaultvalue="")
	private Date date;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
