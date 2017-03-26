package cn.bronzeware.muppet.sqlgenerate;


import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.core.DataSourceManager;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.TableInfo;
import cn.bronzeware.muppet.sql.SqlMetaData;
import cn.bronzeware.muppet.sql.SqlType;
import cn.bronzeware.muppet.sql.TypeConvertMapper;

public class TableGenerate implements Generate{

	private ApplicationContext context;
	private DataSourceUtil dataSourceUtil;
	public TableGenerate(ApplicationContext applicationContext){
		context = applicationContext;
		dataSourceUtil = context.getBean(DataSourceManager.class)
				.getDefaultDataSource();
	}
	
	public TableGenerate(DataSourceUtil dataSourceUtil){
		this.dataSourceUtil = dataSourceUtil;
	}
	
	public String generate(ResourceInfo resourceInfo) throws
		SqlGenerateException{
		if(resourceInfo!=null && resourceInfo instanceof TableInfo)
			{
			TableInfo info = (TableInfo)resourceInfo;
			StringBuffer buffer = new StringBuffer();
			String tableName = info.getTableName();
			buffer.append("create table "+tableName+"(");
			
			ColumnInfo[] columns = info.getColumns();
			for (int i = 0; i < columns.length; i++) {
				if(columns[i]!=null){
					String columnSql = columnGenerate(columns[i]);
					buffer.append(columnSql+",");
				}
			}
			buffer = new StringBuffer(buffer.substring(0, buffer.length()-1));
			buffer.append(")");
			info.setSql(buffer.toString());
			return buffer.toString();
		}else {
			return null;
		}
	}
	
	
	private String columnGenerate(ColumnInfo columnInfo){
		String check = columnInfo.getCheck();
	
		String defaultValue = columnInfo.getDefaultValue();
		int length = columnInfo.getLength();
		String name =  columnInfo.getName();
		String[] values= columnInfo.getValues();
		boolean isPrimaryKey = columnInfo.isIsprivarykey();
		boolean isCanNull = columnInfo.isCanNull();
		boolean isUnique = columnInfo.isUnique();
		SqlType type = columnInfo.getType();

		StringBuffer buffer = new StringBuffer();
		buffer.append(name);
		buffer.append(" ");
		buffer.append(TypeConvertMapper.sqlTypeToString(type));
		if(SqlMetaData.getDefaultLength(type, dataSourceUtil)>0){
			if(length==-1||length==0){
				buffer.append(" ("+SqlMetaData.getDefaultLength(type, dataSourceUtil)+")");
			}else{
				buffer.append(" ("+length+")");
			}
		}
		
			
		
		
		if(isUnique){
			buffer.append(" unique ");
		}else{
			
		}
		
		if(isCanNull){
			buffer.append(" null ");
		}else{
			buffer.append(" not null");
		}
		
		if(isPrimaryKey){
			buffer.append(" primary key ");
		}
		
		if(defaultValue!=null){
			if(SqlMetaData.isAddDefault(columnInfo)){
					buffer.append(" default \"" + defaultValue+"\"");
			}else{
				//buffer.append(" default \"" + defaultValue+"\"");
			}	
		}
		
		
		return buffer.toString();
		
		
	}
	

}
