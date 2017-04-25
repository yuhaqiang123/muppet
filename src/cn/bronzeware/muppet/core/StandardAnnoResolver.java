package cn.bronzeware.muppet.core;

import java.lang.reflect.Field;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.annotations.Column;
import cn.bronzeware.muppet.annotations.NotInTable;
import cn.bronzeware.muppet.annotations.PrimaryKey;
import cn.bronzeware.muppet.annotations.Table;
import cn.bronzeware.muppet.annotations.Type;
import cn.bronzeware.muppet.core.DataBaseCheck.ColumnCheck;
import cn.bronzeware.muppet.core.DataBaseCheck.TableCheck;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.TableInfo;
import cn.bronzeware.muppet.sql.SqlMetaData;
import cn.bronzeware.muppet.sql.SqlType;
import cn.bronzeware.muppet.sql.TypeConvertMapper;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.muppet.util.log.Msg;

/**
 * 解析实体类上的注解
 * 
 * @author 梁莹莹
 *
 *         2016年8月13日 下午5:51:16
 */
public class StandardAnnoResolver implements ResourceResolve {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		Class clazz = Note.class;

		clazz.getName();
		clazz.getField("lyy");
		clazz.getMethod("");
		Column column = (Column) clazz.getAnnotation(Column.class);

	}

	private ApplicationContext applicationContext;

	private DataBaseCheck check;

	private DataSourceUtil dataSourceUtil;

	public StandardAnnoResolver(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.dataSourceUtil = applicationContext.getBean(DataSourceManager.class).getDefaultDataSource();
	}

	public StandardAnnoResolver(ApplicationContext applicationContext, DataBaseCheck dataBaseCheck) {
		this.applicationContext = applicationContext;
		check = dataBaseCheck;
		this.dataSourceUtil = dataBaseCheck.getDataSourceUtil();
	}

	/**
	 * 解析 {@link Column } ，以及 {@link NotInTable}
	 * 
	 * @param clazz
	 * @param tableName
	 * @param fields
	 * @param columnInfos
	 * @return
	 * @throws ResourceResolveException
	 */
	private ColumnInfo[] resolveAnno(Class<?> clazz, String tableName, Field[] fields, ColumnInfo[] columnInfos)
			throws ResourceResolveException {
		int isExist = 0;
		for (int i = 0; fields != null && (i < fields.length); i++) {
			Column column = fields[i].getAnnotation(Column.class);

			ColumnInfo columnInfo = new ColumnInfo();
			if (column != null) {
				resolveColumnAnno(clazz, tableName, column, columnInfo, columnInfos, isExist, fields[i]);
				isExist++;
			} else {
				NotInTable notInTable = fields[i].getAnnotation(NotInTable.class);
				if (notInTable == null) {
					resolveNoColumnAnno(clazz, tableName, fields[i], columnInfo, columnInfos, isExist);
					isExist++;
				}
			}

		}
		ColumnInfo[] newColumnInfos = new ColumnInfo[isExist];
		System.arraycopy(columnInfos, 0, newColumnInfos, 0, isExist);
		return newColumnInfos;
	}

	/*
	 * private boolean isIllgal(Field field,ColumnInfo info,boolean
	 * is_have_primarykey){
	 * 
	 * //boolean is_have_primarykey = false; Field default_primary_key_field =
	 * null; if(field.getName().equals(Constant.PRIMARY_KEY)){
	 * default_primary_key_field = field; }
	 * 
	 * PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
	 * if(primaryKey!=null){ //说明此时 已经出现过一个主键了，应该报错
	 * if(is_have_primarykey==true){ throw new InitException() {
	 * 
	 * @Override public String message() { // TODO Auto-generated method stub
	 * return "请保证一个实体类一个PrimaryKey注解定义"; } }; }else{
	 * 
	 * is_have_primarykey = true; info.setIsprivarykey(is_have_primarykey); }
	 * 
	 * }
	 * 
	 * if(is_have_primarykey==false&&default_primary_key_field==null){
	 * info.setIsprivarykey(false); }
	 * if(is_have_primarykey==false&&default_primary_key_field!=null){
	 * 
	 * info.setIsprivarykey(true); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 */

	/**
	 * 解析一个列上的注解
	 * 
	 * @param clazz
	 *            字段所有Class
	 * @param tableName
	 *            Class上解析出的TableName
	 * @param column
	 *            列上的 {@link Column}注解
	 * @param columnInfo
	 *            列结构 输入参数
	 * @param columnInfos
	 *            列结构数组，需要将处理后的columnInfo存储进columnInfos
	 * @param indexColumnInfos
	 *            当前列结构数组的索引
	 * @param field
	 *            当前属性
	 * @throws ResourceResolveException
	 */
	private void resolveColumnAnno(Class<?> clazz, String tableName, Column column, ColumnInfo columnInfo,
			ColumnInfo[] columnInfos, int indexColumnInfos, Field field) throws ResourceResolveException {

		boolean cannull = column.cannull();
		boolean unique = column.unique();
		String check = column.check();
		String columnName = column.columnname();
		boolean index = column.index();
		Type type = column.type();
		SqlType sqlType = type.type();
		String[] values = column.valuein();
		String defaultvalue = column.defaultvalue();
		int length = type.length();

		PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
		if (primaryKey != null) {
			columnInfo.setIsprivarykey(true);
		}
		columnInfo.setTableName(tableName);
		columnInfo.setCanNull(cannull);
		columnInfo.setDefaultValue(defaultvalue);
		columnInfo.setUnique(unique);
		columnInfo.setName(columnName);
		columnInfo.setType(type.type());
		columnInfo.setLength(type.length());
		columnInfo.setCheck(check);
		columnInfo.setValues(values);

		columnInfo.setField(field);

		alterDefaultAnno(columnInfo);
		columnInfos[indexColumnInfos] = columnInfo;

	}

	/**
	 * 注解上存在一些默认值，需要在解析注解后，进行一些处理，例如 如果没有定义数据类型，需要将Java类型映射为数据库数据类型
	 * 以及默认的长度，默认值处理，如果是数值类型，显然不能使用空字符串
	 * 
	 * @param columnInfo
	 */
	private void alterDefaultAnno(ColumnInfo columnInfo) {
		SqlType sqlType = columnInfo.getType();
		int length = columnInfo.getLength();
		String defaultValue = columnInfo.getDefaultValue();

		if (sqlType.equals(SqlType.DEFAULT)) {
			sqlType = TypeConvertMapper.typeToSqlType(columnInfo.getField().getType());
		}

		if (length < 0) {
			length = SqlMetaData.getDefaultLength(sqlType, dataSourceUtil);
		}

		if (SqlMetaData.isNummic(sqlType)) {
			defaultValue = null;
		}

		columnInfo.setType(sqlType);
		columnInfo.setLength(length);
		columnInfo.setDefaultValue(defaultValue);
	}

	/**
	 * 解析实体类的注解 如果 类存在Table注解那么进行解析，如果不存在那么那默认这个类不是实体类，直接返回null 存在{@link Table}
	 * 注解那么将解析是否存在{@link Column}注解如果存在那么解析这个注解 如果不存在这个注解是否存在{@link NotInTable注解}
	 * 那么则需要，将这个字段摒弃，如果 也不存在这个注解那么将默认将属性名作为列名，{@link Field}的Java类型映射为数据库类型
	 * 
	 * @param clazz
	 *            需要解析的Class对象
	 */
	public ResourceInfo resolve(Class<?> clazz) throws ResourceResolveException {
		try {
			if (clazz != null) {
				Table table = clazz.getAnnotation(Table.class);
				if (table != null) {
					TableInfo tableInfo = new TableInfo();
					tableInfo.setClazz(clazz);

					String tableName = table.tablename();
					tableInfo.setTableName(tableName);
					
					/*TableCheck tableCheck = check.createTableCheck(tableName);
					String primaryKey = tableCheck.getPrimaryKey();
					ColumnCheck columnCheck = check.createColumnCheck(tableName, primaryKey);*/

					
					
					Field[] fields = clazz.getDeclaredFields();
					ColumnInfo[] columnInfos = new ColumnInfo[fields.length];

					/**
					 * 解析Column，同时解析NotInTable
					 */

					columnInfos = resolveAnno(clazz, tableName, fields, columnInfos);

					tableInfo.setColumns(columnInfos);
					return tableInfo;

				}

			}
			return null;

		} catch (Exception e) {
			throw new ResourceResolveException(e);
		}
	}

	private void resolveNoColumnAnno(Class<?> clazz, String tableName, Field field, ColumnInfo columnInfo,
			ColumnInfo[] columnInfos, int indexColumnInfos) throws ResourceResolveException {

		String fieldName = field.getName();
		columnInfo.setName(fieldName);
		columnInfo.setTableName(tableName);
		SqlType sqlType = TypeConvertMapper.typeToSqlType(field.getType());
		if (sqlType != null) {
			Integer length = SqlMetaData.getDefaultLength(sqlType, dataSourceUtil);
			if (length != -1) {
				columnInfo.setType(sqlType);
				columnInfo.setLength(length);
				columnInfo.setField(field);
				columnInfos[indexColumnInfos] = columnInfo;

			} else {
				String errorMsg = clazz.getName() + "类下的" + field.getName() + "字段 --" + Msg.DEFAULT_LENGTH_NOT_FOUND;
				Logger.println(errorMsg);
				throw new ResourceResolveException(errorMsg);
			}
		} else {
			String errorMsg = clazz.getName() + "类下的" + field.getName() + "字段 --" + Msg.DATYPE_NOT_FOUND;
			Logger.println(errorMsg);
			throw new ResourceResolveException(errorMsg);

		}

	}

}
