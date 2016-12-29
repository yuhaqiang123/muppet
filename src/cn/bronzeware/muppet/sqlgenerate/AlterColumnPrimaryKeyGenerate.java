package cn.bronzeware.muppet.sqlgenerate;

import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceInfo;

public class AlterColumnPrimaryKeyGenerate implements Generate{

	@Override
	public String generate(ResourceInfo resourceInfo) throws SqlGenerateException {
		if(resourceInfo instanceof ColumnInfo)
		{
			
			ColumnInfo info = (ColumnInfo) resourceInfo;
			if(info.isIsprivarykey()){
				/**
				 * 当现在注解中的是主键时，说明数据库非主键，所以生成设置主键语句
				 */
				StringBuffer buffer = new StringBuffer();
				buffer.append("alter table "+info.getTableName()+" ");
				buffer.append("add primary key("+info.getName()+")");
				return buffer.toString();
			}
			else{
				StringBuffer buffer = new StringBuffer();
				buffer.append("alter table "+info.getTableName()+" ");
				buffer.append(" drop primary key");
				return buffer.toString();
			}
		}
		
		return null;
	}
	

}
