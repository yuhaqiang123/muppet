package cn.bronzeware.test1;

public class System_Book{

	private java.util.Date update;
	private java.lang.String author;
	private java.lang.Integer bookid;
	private java.lang.String bookname;
	private java.util.Date date;
	private java.lang.String pub;

	public void setAuthor(java.lang.String obj1){
		this.author=obj1;
	}
	public java.lang.String getAuthor(){
		return this.author;
	}
	public java.lang.String getBookname(){
		return this.bookname;
	}
	public void setDate(java.util.Date obj1){
		this.date=obj1;
	}
	public void setBookid(java.lang.Integer obj1){
		this.bookid=obj1;
	}
	public void setBookname(java.lang.String obj1){
		this.bookname=obj1;
	}
	public java.lang.Integer getBookid(){
		return this.bookid;
	}
	public void setUpdate(java.util.Date obj1){
		this.update=obj1;
	}
	public void setPub(java.lang.String obj1){
		this.pub=obj1;
	}
	public java.util.Date getUpdate(){
		return this.update;
	}
	public java.lang.String getPub(){
		return this.pub;
	}
	public java.util.Date getDate(){
		return this.date;
	}

}