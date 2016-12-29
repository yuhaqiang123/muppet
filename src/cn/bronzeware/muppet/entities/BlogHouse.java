package cn.bronzeware.muppet.entities;

import cn.bronzeware.muppet.annotations.Table;

@Table(tablename="tb_bloghouse")
public class BlogHouse{

	private java.lang.Integer id;
	private java.lang.String content;
	private java.util.Date time;
	private java.lang.String title;
	@Override
	public String toString() {
		return "BlogHouse [id=" + id + ", content=" + content + ", time="
				+ time + ", title=" + title + ", source=" + source
				+ ", browse_times=" + browse_times + ", category1=" + category1
				+ ", category2=" + category2 + "]";
	}
	private java.lang.String source;
	private java.lang.Integer browse_times;
	private java.lang.String category1;
	private java.lang.String category2;

	public java.lang.String getContent(){
		return this.content;
	}
	public java.util.Date getTime(){
		return this.time;
	}
	public void setSource(java.lang.String obj1){
		this.source=obj1;
	}
	public void setContent(java.lang.String obj1){
		this.content=obj1;
	}
	public void setBrowse_times(java.lang.Integer obj1){
		this.browse_times=obj1;
	}
	public void setTime(java.util.Date obj1){
		this.time=obj1;
	}
	public java.lang.String getCategory1(){
		return this.category1;
	}
	public java.lang.String getCategory2(){
		return this.category2;
	}
	public java.lang.Integer getId(){
		return this.id;
	}
	public java.lang.Integer getBrowse_times(){
		return this.browse_times;
	}
	public void setId(java.lang.Integer obj1){
		this.id=obj1;
	}
	public void setCategory2(java.lang.String obj1){
		this.category2=obj1;
	}
	public void setTitle(java.lang.String obj1){
		this.title=obj1;
	}
	public java.lang.String getSource(){
		return this.source;
	}
	public java.lang.String getTitle(){
		return this.title;
	}
	public void setCategory1(java.lang.String obj1){
		this.category1=obj1;
	}

}