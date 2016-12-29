package cn.bronzeware.muppet.entities;

import java.util.Date;

import cn.bronzeware.muppet.annotations.Table;

//@Table(tablename="t_note1")
public class QueryNote {
	
	private Integer id;
	private String title;
	private int category_id;
	private String label;
	private int status;
	private String summary;
	private int importance;
	private Date create_time;
	private int user_id;
	private byte[] content;
	private Date remind_time;
	private String send_email;
	private String category_name;
	private String name;
	

	

	public static void main(String[] args) {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getRemind_time() {
		return remind_time;
	}

	public void setRemind_time(Date remind_time) {
		this.remind_time = remind_time;
	}

	public String getSend_email() {
		return send_email;
	}

	public void setSend_email(String send_email) {
		this.send_email = send_email;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

}
