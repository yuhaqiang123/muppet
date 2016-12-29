package cn.bronzeware.muppet.resource;

public class TableInfo extends ResourceInfo{

	private String tableName;
	private ColumnInfo columns[];
	private String sql;
	private Class<?> clazz ;
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {

		
		this.tableName = tableName;
	}
	
	@Override
	public String getName(){
		return this.tableName;
	}
	
	@Override
	public ColumnInfo[] getColumns() {
		return columns;
	}
	public void setColumns(ColumnInfo[] columns) {
		this.columns = columns;
	}
	@Override
	public String getSql() {
		
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof TableInfo)){
			return false;
		}
		
		TableInfo info = (TableInfo)obj;
		if(!(this.getTableName()==info.getTableName()&&this.getTableName()==null)
				&&!info.getTableName().equals(this.getTableName())){
			
			return false;
		}
		if(!(this.getColumns()==info.getColumns())&&this.getColumns()==null){
			if(this.getColumns().length!=info.getColumns().length)
			{
				return false;
			}
				for(int i = 0;i<this.getColumns().length;i++){
					if(!(this.getColumns())[i].equals((info.getColumns())[i])){
						return false;
					}
			}
		}
		
		return true;
		
		
		
		
	}
	
	
	

}

