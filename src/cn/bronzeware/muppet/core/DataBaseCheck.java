package cn.bronzeware.muppet.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.ConnectionRecord;
import cn.bronzeware.muppet.datasource.DataSourceEvent;
import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceNotFoundException;
import cn.bronzeware.muppet.sql.SqlMetaData;
import cn.bronzeware.muppet.sql.SqlType;
import cn.bronzeware.muppet.sql.TypeConvertMapper;
import cn.bronzeware.muppet.util.CloseUtil;
import cn.bronzeware.muppet.util.StringUtil;


/**
 * 数据库检验类负责与数据库进行交互
 * 主要通过DataBaseMetaData元数据获取表与列的信息
 * 实例化前需要先将DataSourceUtil中设置数据库连接
 * @author 刘仁鹏
 *
 */
public class DataBaseCheck {

	private Connection connection;
	private static String[] TABLE_TYPE = new String[]{"TABLE"};
	private static String[] VIEW_TYPE = new String[]{"VIEW"};
	DatabaseMetaData databaseMetaData = null;
	DataSourceUtil dataSourceUtil ;
	ApplicationContext applicationContext;
	public DataBaseCheck(ApplicationContext context){
		applicationContext  = context;
		dataSourceUtil = applicationContext.getBean(DataSourceManager.class).getDefaultDataSource();
	}
	
	private DatabaseMetaData getDataSourceMetaData()throws SQLException{
		if(databaseMetaData == null){
			try{
				connection = dataSourceUtil.getConnection();
				databaseMetaData = connection.getMetaData();
				return databaseMetaData;
			}catch(SQLException e){
				throw e;
			}
		}
		return databaseMetaData;
	}
	
	public DataBaseCheck(ApplicationContext context, DataSourceUtil dataSourceUtil){
		applicationContext  = context;
		this.dataSourceUtil = dataSourceUtil;
	}
	
	public DataSourceUtil getDataSourceUtil(){
		return this.dataSourceUtil;
	}
	
	private class SqlTypeAndLength{
		String typeName;
		int length;
		SqlTypeAndLength(String typeName,int length){
			this.typeName = typeName;
			this.length = length;
		}
	}
	
	/**
	 * 获取指定列的长度以及数据库类型
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private SqlTypeAndLength getSqlTypeWithLength(String tableName,String columnName) throws SQLException{
		try {
			ResultSet rs = getDataSourceMetaData().getColumns(null, null, tableName,"%%");
			while(rs.next()){
				String column = rs.getString("COLUMN_NAME");
				if(column.equals(columnName)){
					String typeName = rs.getString("TYPE_NAME");
					int length = rs.getInt("COLUMN_SIZE");
					return new SqlTypeAndLength(typeName, length);
				}
			}
			return null;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	
	/**
	 * 创建表检查器
	 * @param tableName
	 * @return
	 */
	public TableCheck createTableCheck(String tableName){
		return new TableCheck(tableName);
	}
	public ColumnCheck createColumnCheck(String tableName,String columnName){
		return new ColumnCheck(tableName, columnName);
	}
	
	
	/**
	 * 是否是主键
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private boolean isPrimaryKey(String tableName,String columnName) throws SQLException{
		try {
			ResultSet rs = getDataSourceMetaData().getPrimaryKeys(null, null, tableName);
			while(rs.next()){
				String column = rs.getString("COLUMN_NAME");
				if(column.equals(columnName)){
					
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	private String getPrimaryKey(String tableName) throws SQLException{
		try {
			ResultSet rs = getDataSourceMetaData().getPrimaryKeys(null, null, tableName);
			while(rs.next()){
				String column = rs.getString("COLUMN_NAME");
				return column;
			}
			throw new SQLException(String.format("%s没有设置主键", tableName));
		} catch (SQLException e) {
			throw e;
		}
	}
	
	
	
	
	/**
	 * 获取默认值
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private String getDefaultValue(String tableName,String columnName) throws SQLException{
		ResultSet rs = null;
		try {
			rs = getDataSourceMetaData().getColumns(null, null, tableName,"%%");
			while(rs.next()){
				String column = rs.getString("COLUMN_NAME");
				if(columnName.equals(column)){
					String defaultValue = rs.getString("COLUMN_DEF");
					return defaultValue;
				}
				
			}
			return null;
		} catch (SQLException e) {
			throw e;
		}
		finally {
				CloseUtil.close(rs);
		}
	}
	
	/**
	 * 是否可以为空
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private boolean isNullable(String tableName,String columnName) throws SQLException{
		ResultSet rs = null;
		try {
			rs = getDataSourceMetaData().getColumns(null, null, tableName,"%%");
			while(rs.next()){
				String column = rs.getString("COLUMN_NAME");
				if(columnName.equals(column)){
					boolean isnullable  = rs.getInt("NULLABLE")==DatabaseMetaData.columnNullable?true:false;
					return isnullable;
				}
				
			}
			return false;
		} catch (SQLException e) {
			throw e;
		}
		finally {
				CloseUtil.close(rs);
		}
	}
	
	/**
	 * 是否是外键
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	private boolean isForeignKey(String tableName,String columnName){
		ResultSet rs = null;
		try {
			rs = getDataSourceMetaData().getImportedKeys(null, null, tableName);
			while(rs.next()){
				/**
				 * PKTABLE_NAME 主键表名称，意为被引用的表
				 * FKTABLE_NAME 外键表名称，即该表中的外键
				 * PKCOLUMN_NAME 主键表的列名称
				 * FKCOLUMN_NAME 外键的列名 
				 * 如果t_note中有一个外键列是use_id，这列对应是t_user中的主键id
				 *那么对应项是：
				 *t_user,
				 *t_note
				 *id
				 *user_id
				 */
				String factTableName = rs.getString("FKTABLE_NAME");
				String factColumnName = rs.getString("FKCOLUMN_NAME");
				/**
				 * 判断是否对应相等
				 */
				return StringUtil.equals(new String[]{
	factColumnName,
				  	factTableName},new String[]
	{columnName,
					tableName});
			
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			CloseUtil.close(rs);
		}
		
	}
	
	/**
	 * 是否是索引
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	private  boolean isIndex(String tableName,String columnName){
		ResultSet rs = null;
		try {
			 rs = getDataSourceMetaData().getIndexInfo(null, null, tableName, false, false);
			while(rs.next()){
				String indexName = rs.getString("INDEX_NAME");
				String factColumnName = rs.getString("COLUMN_NAME");
				if(indexName.indexOf(columnName)!=-1){
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			CloseUtil.close(rs);
		}
		
	}
	
	/**
	 * 是否是唯一性约束的索引
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	private  boolean isUniqueIndex(String tableName,String columnName){
		ResultSet rs = null;
		try {
			 rs = getDataSourceMetaData().getIndexInfo(null, null, tableName, true, false);
			while(rs.next()){
				String indexName = rs.getString("INDEX_NAME");
				String factColumnName = rs.getString("COLUMN_NAME");
				//mysql默认建立索引是以列名开头
				if(indexName.indexOf(columnName)!=-1){
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			CloseUtil.close(rs);
		}
		
	}
	
	public List<TableCheck> getTableChecks() throws SQLException{
		try {
			ResultSet rs = getTables();
			Set<String> set = new HashSet<>();
			while(rs.next()){
				String tableName = rs.getString("TABLE_NAME");
				set.add(tableName);
			}
			List<TableCheck> list = new ArrayList<>();
			for(String tableName:set){
				list.add(new TableCheck(tableName));
			}
			return list;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	
	
	
	
	/**
	 * 表检查器
	 * @author 于海强
	 *
	 */
	public class TableCheck{
		
		private String tableName;
		private TableCheck(String tableName) throws ResourceNotFoundException{
			this.tableName = tableName;
			/*if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表没有找到");
			}*/
		}
		
		public String getPrimaryKey(){
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+String.format("表 在数据源 :%s 中没有找到", getDataSourceUtil().getDataSourceKey()));
			}
			try{
				return DataBaseCheck.this.getPrimaryKey(tableName);
			}catch(SQLException e){
				throw new InitException(e);
			}
		}
		
		
		public String getTableName(){
			return tableName;
		}
		
		/**
		 * 这个表是否存在
		 * @return
		 */
		public boolean isExist(){
			ResultSet rs = null;
			try {
				rs = getTable(tableName);
				while(rs.next()){
					return true;
				}
				return false;
			} catch (Exception e) {
				return false;
			}
			finally{
				try {
					if(rs != null){
						rs.close();
					}
				} catch (SQLException e) {
					
				}
			}
			
		}
		
		
		public List<ColumnCheck> getColumns() throws SQLException{
			ResultSet rs = null;
			try {
				rs = DataBaseCheck.this.getColumns(tableName);
				Set<String> set = new HashSet<>();
				while(rs.next()){
					String columnName = rs.getString("COLUMN_NAME");
					set.add(columnName);
				}
				List<ColumnCheck> list = new ArrayList<>();
				for(String columnName:set){
					list.add(new ColumnCheck(tableName, columnName));
				}
				return list;
			} catch (SQLException e) {
				throw e;
			}
		}
		
		
		
	}
	 
	/**
	 * 列约束器
	 * @author 于海强
	 *
	 */
	public class ColumnCheck{
		private String tableName;
		private String columnName;
		SqlTypeAndLength sqlTypeAndLength;
		private ColumnCheck(String tableName,String columnName){
			this.tableName = tableName;
			this.columnName = columnName;
			DataBaseCheck.TableCheck tableCheck = createTableCheck(tableName);
			if(!tableCheck.isExist()){
				throw new ResourceNotFoundException(tableName+"表没有找到");
			}
		}
		
		public String getColumnName(){
			return columnName;
		}
		
		public String getTableName(){
			return tableName;
		}
		
		/**
		 * 列是否相等
		 * @param info
		 * @return
		 */
		public boolean isEquals(ColumnInfo info) throws SQLException{
			ColumnInfo columnInfo =	new ColumnInfo();
			columnInfo.setCanNull(isNullable());
			columnInfo.setDefaultValue(getDefaultValue());
			columnInfo.setIsprivarykey(isPrimaryKey());
			columnInfo.setUnique(isUnique());
			columnInfo.setLength(getLength());
			columnInfo.setType(getSqlType());
			columnInfo.setName(columnName);
			columnInfo.setTableName(tableName);
			columnInfo.setValues(new String[]{""});
			
			
			if(info.getDefaultValue()!=null&&
					info.getDefaultValue().equals("")){
				if(SqlMetaData.isNummic(info.getType())){
					info.setDefaultValue(null);
					columnInfo.setDefaultValue(null);
				}
			}
			
			return columnInfo.equals(info);
		}
		
		/**
		 * 获取sql类型
		 * @return
		 */
		public SqlType getSqlType() throws SQLException{
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			try {
				sqlTypeAndLength = getSqlTypeWithLength(tableName, columnName);
				return TypeConvertMapper.stringToSqlType(sqlTypeAndLength.typeName);
			} catch (SQLException e) {
				throw e;
			}
		}
		
		/**
		 * 是否唯一
		 * @return
		 */
		public boolean isUnique(){
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			
			
			if(isPrimaryKey()){
				return true;
			}
			return DataBaseCheck.this.isUniqueIndex(tableName, columnName);
		}
		
		
		/**
		 * 是否是外键
		 * @return
		 */
		public boolean isForeignKey(){
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			
			return DataBaseCheck.this.isForeignKey(tableName, columnName);
		}
		
		/**
		 * 是否是索引
		 * @return
		 */
		public boolean isIndex(){
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			
			
			return DataBaseCheck.this.isIndex(tableName, columnName);
		}
		
		/**
		 * 获取长度
		 * @return
		 */
		public int getLength() throws SQLException{
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			try {
				sqlTypeAndLength = getSqlTypeWithLength(tableName, columnName);
				return sqlTypeAndLength.length;
			} catch (SQLException e) {
				throw e;
			}
		}
		
		/**
		 * 是否可以为空
		 * @return
		 * @throws InitException
		 */
		public boolean isNullable() throws InitException{
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			
			try {
				return DataBaseCheck.this.isNullable(tableName, columnName);
			} catch (SQLException e) {
				throw new InitException(e);
			}
		}
		
		/**
		 * 是否是主键
		 * @return
		 * @throws InitException
		 */
		public boolean isPrimaryKey() throws InitException{
			
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			try {
				return DataBaseCheck.this.isPrimaryKey(tableName, columnName);
			} catch (SQLException e) {
				throw new InitException(e);
			}
		}
		
		
		
		
		
		/**
		 * 获取默认值
		 * @return
		 * @throws InitException
		 */
		public String getDefaultValue() throws InitException{
			if(!isExist()){
				throw new ResourceNotFoundException(tableName+"表下的"+columnName+"数据列没有找到");
			}
			try {
				return DataBaseCheck.this.getDefaultValue(tableName, columnName);
			} catch (SQLException e) {
				throw new InitException(e);
			}
		}
	


		/**
		 * 是否存在
		 * @return
		 */
		public boolean isExist(){
			ResultSet rs = null;
			try {
				rs = getColumn(tableName, columnName);
				while(rs.next()){
					return true;
				}
				return false;
			} catch (SQLException e) {
				return false;
			}
			finally{
				try {
					if(rs != null){
						rs.close();
					}
				} catch (SQLException e) {
					return false;
				}
			}
		}
		
	}
	/**
	 * 是否是对应的外键
	 * @param tableName
	 * @param columnName
	 * @param refTableName
	 * @param refColumnName
	 * @return
	 */
	public boolean equalsForeignKey(String tableName,String columnName
									,String refTableName,String refColumnName){
		ResultSet rs = null;
		try {
			rs = getDataSourceMetaData().getImportedKeys(null, null, tableName);
			while(rs.next()){
				/**
				 * PKTABLE_NAME 主键表名称，意为被引用的表
				 * FKTABLE_NAME 外键表名称，即该表中的外键
				 * PKCOLUMN_NAME 主键表的列名称
				 * FKCOLUMN_NAME 外键的列名 
				 * 如果t_note中有一个外键列是use_id，这列对应是t_user中的主键id
				 *那么对应项是：
				 *t_user,
				 *t_note
				 *id
				 *user_id
				 */
				String factRefTableName = rs.getString("PKTABLE_NAME");
				String factTableName = rs.getString("FKTABLE_NAME");
				String factRefColumnName = rs.getString("PKCOLUMN_NAME");
				String factColumnName = rs.getString("FKTABLE_NAME");
				/**
				 * 判断是否对应相等
				 */
				return StringUtil.equals(new String[]{
	factColumnName,
				    factRefColumnName,
										factRefTableName,
															 factTableName},new String[]
	{columnName,
						refColumnName,
										 refTableName,
										 					tableName});
			
			}
			return false;
		} catch (SQLException e) {
			throw new InitException(e);
		}
		finally{
			CloseUtil.close(rs);
		}
		
	}
	
	

	
	private boolean isColumnPrimaryKey(){
		return false;
	}
	
	/**
	 * 该列是否存在
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	private boolean isColumnExist(String tableName,String columnName) throws SQLException{
		ResultSet rs = null;
		try {
			rs = getColumn(tableName, columnName);
			while(rs.next()){
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw e;
		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				throw new InitException(e);
			}
		}
	}
	
	
	/**
	 * 获取所有的Table
	 * @return
	 * @throws SQLException
	 */
	private ResultSet getTables() throws SQLException {
		try {
			ResultSet rs = getDataSourceMetaData().
					getTables(null,"" , "%%",TABLE_TYPE);
			return rs;
		} catch (SQLException e) {
			throw e;
		}
		
	}
	
	/**
	 * 获取指定的table
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private ResultSet getTable(String tableName) throws SQLException {
		try {/*
			if(databaseMetaData==null){
				System.out.println("它是空指针");
			}*/
			ResultSet rs = getDataSourceMetaData().
					getTables(null,"" , tableName,TABLE_TYPE);
			return rs;
		} catch (SQLException e) {
			throw e;
		}
		
	}
	
	/**
	 * 获取所有列
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private ResultSet getColumns(String tableName) throws SQLException{
		try {
			ResultSet rs = getDataSourceMetaData().
					getColumns(null,"" , tableName,"%%");
			return rs;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * 获取指定的列
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private ResultSet getColumn(String tableName,String columnName) throws SQLException{
		try {
			ResultSet rs = getDataSourceMetaData().
					getColumns(null,"" , tableName,columnName);
			return rs;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	
	/**
	 * 是否已经关闭
	 * @return
	 */
	public boolean isClosed() throws SQLException{
		try {
			return connection.isClosed();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * 关闭连接
	 * @return
	 */
	public boolean close() throws SQLException{
		try {
			if(connection!=null&&!connection.isClosed()){
				connection.close();
				
				DataSourceEvent event = new DataSourceEvent();
				event.setKey(dataSourceUtil.getDataSourceKey());
				event.setType(DataSourceListener.Type.CONNECTION_CLOSED);
				ConnectionRecord connectionRecord = dataSourceUtil.getConnectionIdLocal().get();
				connectionRecord.setConnectionEndTime(System.currentTimeMillis());
				connectionRecord.setDataSourceKey(dataSourceUtil.getDataSourceKey());
				event.setConnectionRecord(connectionRecord);
				dataSourceUtil.getConnectionIdLocal().remove();
				dataSourceUtil.getDataSourceListener().event(event);
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			DataSourceEvent event = new DataSourceEvent();
			event.setKey(dataSourceUtil.getDataSourceKey());
			event.setType(DataSourceListener.Type.CONNECTION_CLOSED_ERROR);
			event.setError(e);
			ConnectionRecord connectionRecord = dataSourceUtil.getConnectionIdLocal().get();
			connectionRecord.setConnectionEndTime(System.currentTimeMillis());
			connectionRecord.setDataSourceKey(dataSourceUtil.getDataSourceKey());
			event.setConnectionRecord(connectionRecord);
			dataSourceUtil.getConnectionIdLocal().remove();
			dataSourceUtil.getDataSourceListener().event(event);
			throw e;
		}
	}
}
