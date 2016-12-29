package cn.bronzeware.muppet.entities;

import cn.bronzeware.muppet.annotations.Column;
import cn.bronzeware.muppet.annotations.PrimaryKey;
import cn.bronzeware.muppet.annotations.Table;
import cn.bronzeware.muppet.annotations.Type;
import cn.bronzeware.muppet.sql.SqlType;

//@Table(tablename="t_note3")
public class Note {

	@PrimaryKey
	@Column(columnname="id",type=@Type(type=SqlType.INT,length=10),defaultvalue="0")
	private int id;

	
	@Override
	public String toString() {
		return "Note [id=" + id + ", value=" + value + ", username=" + username
				+ ", password=" + password + ", user_id=" + user_id + "]";
	}
	@Column(columnname="value",type=@Type(type=SqlType.VARCHAR,length=255))
	private String value;
	
	@Column(columnname="username",type=@Type(type=SqlType.VARCHAR,length=255))
	private String username;
	
	@Column(columnname="password")
	private String password;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	private String user_id;
	

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
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
	
}
