package cn.bronzeware.muppet.resource;

import java.lang.reflect.Field;

import javax.activation.DataSource;

import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.sql.SqlType;
import cn.bronzeware.muppet.util.log.Logger;

public class ColumnInfo extends ResourceInfo{
	
	private String tableName;
	public String getTableName() {
		return tableName;
	}

	private Field field;

	public Field getField() {
		return field;
	}


	public void setField(Field field) {
		this.field = field;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	private String name = "";//使用
	private boolean canNull = true;//使用
	private boolean unique = false;//使用
	private boolean isprivarykey = false;//使用
	private String defaultValue = "";//使用
	private SqlType type = SqlType.DEFAULT;//使用
	private String[] values = {""};

	
	
	
	private String check = "";
	
	public String getSql(){
		return null;
	}
	
	private int length = -1;

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	

	public boolean isIsprivarykey() {
		return isprivarykey;
	}


	public void setIsprivarykey(boolean isprivarykey) {
		this.isprivarykey = isprivarykey;
	}


	public String getDefaultValue() {
		return defaultValue;
	}


	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	public SqlType getType() {
		return type;
	}


	public void setType(SqlType type) {
		this.type = type;
	}


	public String[] getValues() {
		return values;
	}


	public void setValues(String[] values) {
		this.values = values;
	}



	public String getCheck() {
		return check;
	}


	public void setCheck(String check) {
		this.check = check;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public boolean isUnique() {
		return unique;
	}


	public void setUnique(boolean unique) {
		this.unique = unique;
	}


	public boolean isCanNull() {
		return canNull;
	}


	public void setCanNull(boolean canNull) {
		this.canNull = canNull;
	}


	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ColumnInfo)){
			return false;
		}
		
		
		ColumnInfo info =(ColumnInfo )obj;
		if(!(this.getCheck()==info.getCheck()&&this.getCheck()==null)&&
				!this.getCheck().equals(info.getCheck())){
			Logger.println("Check 属性不同需要更改");
			return false;
		}
		
		if(!(this.getDefaultValue()==info.getDefaultValue()&&this.getDefaultValue()==null)
				&&(this.getDefaultValue()!=null&&info.getDefaultValue()!=null)&&
				!this.getDefaultValue().equals(info.getDefaultValue()))
		{
			
			Logger.println("default value 属性不同需要更改");
			return false;
		}
		
		if(!(this.getName()==info.getName()&&this.getName()==null)
				&&
				!this.getName().equals(info.getName())){
			Logger.println("名字 属性不同需要更改");
			return false;
		}
		
		if(!(this.getType()==info.getType()&&this.getType()==null)
				&&
				!this.getType().equals(info.getType()))
		{
			Logger.println("type 属性不同需要更改");
			return false;
		}
		
		/*if(!(this.getValues()==info.getValues()&&this.getValues()==null)
				&&
				!this.getValues().equals(info.getValues()))
		{
			Logger.println("values 属性不同需要更改");
			return false;
		}*/
		
		if(this.getLength()!=this.getLength())
		{
			Logger.println("length 属性不同需要更改");
			return false;
		}
		
		if(this.isCanNull()!=info.isCanNull()){
			Logger.println("can null属性不同需要更改");
			return false;
		}
		
		if(this.isIsprivarykey()!=info.isIsprivarykey()){
			Logger.println("primarykey 属性不同需要更改");
			return false;
		}
		
		if(this.isUnique()!=info.isUnique()){
			Logger.println("unique 属性不同需要更改");
			return false;
		}
		
		
		return  true;
		
	}

	public static void main(String[] args){
		
		ColumnInfo info = new ColumnInfo();
		info.setCheck(" faeif");
		info.setDefaultValue("fe");
		//info.setName("ee");
		info.setValues(new String[]{"ee"});
		
		ColumnInfo info1 = new ColumnInfo();
		info1.setCheck(" faeif");
		info1.setDefaultValue("fe");
		//info1.setName("ee");
		info1.setValues(new String[]{"ee"});
		info.setType(SqlType.BIGINT);
		
		//info1.setType(SqlType.);
		System.out.println(info.equals(info1));
	}
	
}