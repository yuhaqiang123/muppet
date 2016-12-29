package cn.bronzeware.muppet.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.util.CloseUtil;

public class SqlMetaData {

	private static Map<SqlType, Integer> map = new HashMap<SqlType,Integer>();
	
	public static void main(String[] args){
		System.out.println(getDefaultLength(SqlType.BLOB));
	}
	
	public static boolean isAddDefault(ColumnInfo info){
		assert info!= null;
		if(SqlMetaData.isNummic(info.getType())){//数字类型
			if(!info.getDefaultValue().equals("")){
				//如果是不等于“”,则添加
				return true;
			}else{
				//否则为""不添加，因为mysql不接受数字为“”的情况
				return false;
			}
		}
		SqlType[] noDef = new SqlType[]{
				SqlType.BLOB,
				SqlType.LONGBLOB,
				SqlType.TINYBLOB,
				SqlType.MIDIUMBLOB,
				SqlType.TEXT,
				SqlType.LONGTEXT,
				SqlType.MEDUIMTEXT,
				SqlType.TINYTEXT
				,SqlType.DATE
				,SqlType.DATETIME
				,SqlType.TIMESTAMP
				,SqlType.YEAR
		};
		for(SqlType sqlType:noDef){
			if(info.getType().equals(sqlType)){
				
				return false;
			}
		}
		return true;
	
		
	}
	
	/**
	 * 返回指定SqlType类型的默认长度，如果没有找到相关类型，则返回-1
	 * 会有一些类型返回长度为0
	 * @param sqlType
	 * @return
	 */
	public static Integer getDefaultLength(SqlType sqlType){
		assert sqlType!=null;
		map = getDefaultSqlLengths();
		if(map.containsKey(sqlType)){
			return map.get(sqlType);
		}else{
			return -1;
		}
	}
	
	private static Map<SqlType, Integer> getDefaultSqlLengths(){
		if(map.size()!=0){
			return map;
		}
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtil.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			rs = metaData.getTypeInfo();
			while (rs.next()) {
				SqlType sqlType = TypeConvertMapper.stringToSqlType(rs.getString("TYPE_NAME"));
				if(sqlType!=null){
					Integer length = Integer.valueOf(rs.getString("PRECISION"));
					map.put(sqlType, length);
				}
				
			}
			
			return map;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return map;
		}
		finally {
			CloseUtil.close(connection);
			CloseUtil.close(rs);
		}
		
	}
	
	private static SqlType[] nummicSqlTypes={
			SqlType.BIGINT,
			SqlType.DOUBLE,
			SqlType.FLOAT,
			SqlType.INT,
			SqlType.TINYINT,
			SqlType.MUDIUMINT,
			SqlType.SMALLINT,
			SqlType.INTEGER

			
	};
	public static boolean isNummic(SqlType sqlType){
		
		for(SqlType type:nummicSqlTypes){
			if(type.equals(sqlType)){
				return true;
			}
		}
		return false;
	}
	
}
