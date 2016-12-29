package cn.bronzeware.muppet.sqlgenerate;

import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.sql.SqlType;
import cn.bronzeware.muppet.sql.TypeConvertMapper;

public class AlterColumnGenerate implements Generate{

	@Override
	public String generate(ResourceInfo resourceInfo) throws SqlGenerateException {
	

		/*
		 * 
		 * alter table t name ,newname 数据类型（长度） null， 默认值
		 */
		
		ColumnInfo info = null;
		if(resourceInfo instanceof ColumnInfo){
			info = (ColumnInfo) resourceInfo;
			StringBuffer string = new StringBuffer();
			string.append("alter table "+info.getTableName()+" change ");
			string.append(" "+info.getName()+" "+info.getName()+" ");
			if(info.getLength()>0){
				string.append(" "+
						TypeConvertMapper.sqlTypeToString(info.getType())
						+"( "+info.getLength()+")");
			}
			
			if(info.isCanNull()){
				string.append(" null ");
			}
			else{
				string.append(" not null");
			}
			
			if(info.isUnique()){
				string.append(" unique ");
			}else{

			}
			if(info.getDefaultValue()!=null){
				string.append(" default \""+info.getDefaultValue()+"\"");
			}
			
			
			return string.toString();
		}
		
		return null;
	}
	
	public static void main(String[] args) throws SqlGenerateException{
		Generate generate = new AlterColumnGenerate();
		ColumnInfo info = new ColumnInfo();
		info.setCanNull(false);
		info.setDefaultValue("34");
		info.setName("pasd");
		info.setTableName("t_test");
		//info.setUnique(true);
		info.setType(SqlType.VARCHAR);
		info.setLength(343);
		
		System.out.println(generate.generate(info));;
	}

	
	
}
