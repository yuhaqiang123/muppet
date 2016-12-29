package cn.bronzeware.muppet.sql;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class TypeConvertMapper {

	public static Type[] sqlToJava(SqlType sqlType){
		
		return map.get(sqlType);
	}
	
	public static void main(String[] args) throws SecurityException, NoSuchFieldException{
		
		
		TypeConvertMapper.isIlleall(SqlType.INT,new Integer(1).getClass());
	}
	
	
	public static boolean isIlleall(SqlType sqlType,Type type){
		Type[] types = map.get(sqlType);
		for(Type type2:types){
			if(type.equals(type2)){
				return true;
			}
		}
		return false;
	}
	
	public static String sqlTypeToString(SqlType sqlType){
		
		if(sqlTypeToStringMap.containsKey(sqlType)){
			
			return sqlTypeToStringMap.get(sqlType);
		}
		else{
			return null;
		}
		
	}
	public static SqlType stringToSqlType(String value){
		if(stringToSqlTypeMap.containsKey(value)){
			return stringToSqlTypeMap.get(value);
		}
		else{
			return null;
		}
	}
	
	
	public static SqlType typeToSqlType(Type type){
		if(javaToSqlType.containsKey(type)){
			return javaToSqlType.get(type);
		}
		else{
			return null;
		}
	}
	
	
	private static HashMap<SqlType, String> sqlTypeToStringMap
	   = new HashMap<SqlType, String>(20);
	private static HashMap<String, SqlType> stringToSqlTypeMap = 
			new HashMap<String,SqlType>(20);
	
	private static HashMap<Type, SqlType> javaToSqlType = 
			new HashMap<Type,SqlType>();
	
	
	private static HashMap<SqlType, Type[]> map = new HashMap<SqlType,Type[]>();

	
	static{
		/**
		 * 配置SQlType到Sql类型的映射
		 */
		sqlTypeToStringMap.put(SqlType.BLOB, "BLOB");
		sqlTypeToStringMap.put(SqlType.VARCHAR, "VARCHAR");
		sqlTypeToStringMap.put(SqlType.INT, "INT");
		sqlTypeToStringMap.put(SqlType.TINYINT, "TINYINT");
		sqlTypeToStringMap.put(SqlType.SMALLINT, "SMALLINT");
		sqlTypeToStringMap.put(SqlType.MUDIUMINT, "MUDIUMINT");
		sqlTypeToStringMap.put(SqlType.INTEGER, "INTEGER");
		sqlTypeToStringMap.put(SqlType.BIT, "BIT");
		sqlTypeToStringMap.put(SqlType.BIGINT, "BIGINT");
		sqlTypeToStringMap.put(SqlType.DOUBLE, "DOUBLE");
		sqlTypeToStringMap.put(SqlType.FLOAT, "FLOAT");
		sqlTypeToStringMap.put(SqlType.DECIMAL, "DECIMAL");
		sqlTypeToStringMap.put(SqlType.DATE, "DATE");
		sqlTypeToStringMap.put(SqlType.TIME, "TIME");
		sqlTypeToStringMap.put(SqlType.YEAR, "YEAR");
		sqlTypeToStringMap.put(SqlType.CHAR, "CHAR");
		sqlTypeToStringMap.put(SqlType.TIMESTAMP, "TIMESTAMP");
		sqlTypeToStringMap.put(SqlType.DATETIME, "DATETIME");
		sqlTypeToStringMap.put(SqlType.TINYBLOB, "TINYBLOB");

		sqlTypeToStringMap.put(SqlType.MEDUIMTEXT, "MEDUIMTEXT");
		sqlTypeToStringMap.put(SqlType.TINYBLOB, "TINYBLOB");

		sqlTypeToStringMap.put(SqlType.LONGBLOB, "LONGBLOB");
		sqlTypeToStringMap.put(SqlType.TEXT, "TEXT");
		sqlTypeToStringMap.put(SqlType.TINYTEXT, "TINYTEXT");

		sqlTypeToStringMap.put(SqlType.MEDUIMTEXT, "MEDUIMTEXT");
		sqlTypeToStringMap.put(SqlType.LONGTEXT, "LONGTEXT");
		sqlTypeToStringMap.put(SqlType.SET, "SET");
		sqlTypeToStringMap.put(SqlType.BINARY, "BINARY");
		sqlTypeToStringMap.put(SqlType.VARBINARY, "VARBINARY");
		
		//配置String到SqlType之间的 映射
		for(Map.Entry<SqlType,String> entry:sqlTypeToStringMap.entrySet()){
			stringToSqlTypeMap.put(entry.getValue(),entry.getKey());
		}

	}
	
	
	
	static{
		/*
		 * 
		 * 配置SqlType到Java基础类型的映射
		 */
		map.put(SqlType.BLOB, new Class<?>[]{Byte[].class});
		map.put(SqlType.INT, new Class<?>[]{Integer.class,int.class});
		map.put(SqlType.VARCHAR, new Class<?>[]{String.class,char.class});
		map.put(SqlType.TINYINT, new Class<?>[]{Integer.class,int.class});
		map.put(SqlType.SMALLINT, new Class<?>[]{Integer.class,int.class});
		map.put(SqlType.MUDIUMINT, new Class<?>[]{Integer.class,int.class});
		map.put(SqlType.INTEGER, new Class<?>[]{Integer.class,int.class});
		map.put(SqlType.BIGINT, new Class<?>[]{long.class});
		map.put(SqlType.BIT, new Class<?>[]{boolean.class,Boolean.class});
		map.put(SqlType.DOUBLE, new Class<?>[]{double.class,Double.class});
		map.put(SqlType.FLOAT, new Class<?>[]{float.class,Float.class});
		map.put(SqlType.DECIMAL, new Class<?>[]{float.class
					,Float.class
					,Double.class
					,double.class});
		map.put(SqlType.DATE,new Class<?>[]{Date.class,
				java.sql.Date.class});
		map.put(SqlType.TIME,new Class<?>[]{Date.class,
				java.sql.Date.class} );
		map.put(SqlType.YEAR, new Class<?>[]{Date.class,
				java.sql.Date.class});
		map.put(SqlType.CHAR, new Class<?>[]{char.class,
				String.class});
		map.put(SqlType.TIMESTAMP, new Class<?>[]{Date.class,
				java.sql.Date.class});
		map.put(SqlType.DATETIME, new Class<?>[]{Date.class,
				java.sql.Date.class});
		map.put(SqlType.TINYBLOB, new Class<?>[]{byte[].class});

		map.put(SqlType.MEDUIMTEXT, new Class<?>[]{byte[].class});
		map.put(SqlType.TINYBLOB, new Class<?>[]{byte[].class});
		map.put(SqlType.LONGBLOB, new Class<?>[]{byte[].class});
		map.put(SqlType.TEXT, new Class<?>[]{char[].class,char.class,String.class});
		map.put(SqlType.TINYTEXT, new Class<?>[]{char[].class,char.class,String.class});

		map.put(SqlType.MEDUIMTEXT, new Class<?>[]{char[].class,char.class,String.class});
		map.put(SqlType.LONGTEXT, new Class<?>[]{String.class,char[].class,char.class});
		map.put(SqlType.BINARY, new Class<?>[]{byte[].class});
		map.put(SqlType.VARBINARY,new Class<?>[]{byte[].class} );
		
		
		/**
		 * 配置Java基础类型到 @{SqlType}的映射
		 */
		javaToSqlType.put(Integer.class, SqlType.INTEGER);
		javaToSqlType.put(int.class, SqlType.INT);
		javaToSqlType.put(String.class,SqlType.VARCHAR);
		javaToSqlType.put(char[].class, SqlType.VARCHAR);
		javaToSqlType.put(boolean.class, SqlType.BIT);
		javaToSqlType.put(long.class, SqlType.BIGINT);
		javaToSqlType.put(float.class, SqlType.FLOAT);
		javaToSqlType.put(double.class, SqlType.DOUBLE);
		javaToSqlType.put(Double.class,SqlType.DOUBLE);
		javaToSqlType.put(Long.class,SqlType.BIGINT);
		javaToSqlType.put(Float.class, SqlType.FLOAT);
		javaToSqlType.put(Boolean.class, SqlType.BIT);
		javaToSqlType.put(byte[].class, SqlType.BLOB);
		javaToSqlType.put(char.class, SqlType.CHAR);
		javaToSqlType.put(Date.class,SqlType.DATETIME);
		javaToSqlType.put(java.sql.Date.class, SqlType.DATE);
	}
	
}
