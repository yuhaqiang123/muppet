package cn.bronzeware.muppet.core;

import java.lang.reflect.Field;

import cn.bronzeware.muppet.annotations.Column;
import cn.bronzeware.muppet.annotations.NotInTable;
import cn.bronzeware.muppet.annotations.Table;
import cn.bronzeware.muppet.core.DataBaseCheck.ColumnCheck;
import cn.bronzeware.muppet.core.DataBaseCheck.TableCheck;
import cn.bronzeware.muppet.datasource.DataSourceEvent;
import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.ResourceNotFoundException;
import cn.bronzeware.muppet.resource.TableInfo;

public class StandardDBCheckResolver implements ResourceResolve{

	private DataBaseCheck check;
	
	private DataSourceListener dataSourceListener;
	
	public StandardDBCheckResolver(DataBaseCheck check, DataSourceListener dataSourceListener) {
		this.check = check;
		this.dataSourceListener = dataSourceListener;
	}
	
	@Override
	public ResourceInfo resolve(Class<?> clazz) throws ResourceResolveException {
		Table table = clazz.getAnnotation(Table.class);
		
		//存在Table注解
		if(table!=null){
			String tableName = table.tablename();
			final String tableName1 = tableName;
			final String clazzName = clazz.getName();
			TableCheck tableCheck = 
					check.createTableCheck(tableName);
			if(!tableCheck.isExist()){
				InitException e =  new InitException("没有找到与"+clazzName+"相关的表"+tableName1);
				DataSourceEvent dataSourceEvent = new DataSourceEvent();
				dataSourceEvent.setError(e);
				
				dataSourceEvent.setKey(check.getDataSourceUtil().getDataSourceKey());
				dataSourceEvent.setType(DataSourceListener.Type.TABLE_NOT_EXISTS);
				dataSourceListener.event(dataSourceEvent);
			}
			else{
				TableInfo tableInfo = checkExistedTable(tableName1, clazz);
				return tableInfo;
			}
		}
		return null;
	}
	
	private  TableInfo checkExistedTable(String tableName,Class<?> clazz){
		TableInfo info = new TableInfo();
		info.setTableName(tableName);
		info.setClazz(clazz);
		Field[] fields = clazz.getDeclaredFields();
		if(fields==null){
			return info;
		}else{
			ColumnInfo[] columnInfos = new ColumnInfo[fields.length];
			int inTableNums = 0;
			/**
			 * 解析Field域，如果不在Table中，则直接跳过循环，如果在表中不存在
			 * 则跳过，如果存在那么配置ColumnInfo然后存入columnInfos数组中
			 * 
			 */
			for(Field field:fields){
				NotInTable notInTable = field.getAnnotation(NotInTable.class);
				if(notInTable!=null){
					continue;
				}else{
					ColumnInfo columnInfo = new ColumnInfo();
					columnInfo.setTableName(tableName);
					boolean isExist = checkCoumn(columnInfo, field);
					
					if(!isExist){
						continue;
					}
					else{
						columnInfos[inTableNums] = columnInfo;
						inTableNums++;
					}
				}
			}
			
			if(inTableNums!=fields.length){
				ColumnInfo[] columnInfos2 = new ColumnInfo[inTableNums];
				
				System.arraycopy(columnInfos, 0, columnInfos2, 0, inTableNums);
				info.setColumns(columnInfos2);
			}else{
				info.setColumns(columnInfos);
			}
			
			return info;
		}
	}
	
	
	private boolean checkCoumn(ColumnInfo info,Field field) throws ResourceResolveException{
		Column column = field.getAnnotation(Column.class);
		String columnName  = null;
		if(column!=null&&(!column.columnname().equals(""))){
			columnName = column.columnname();
		}
		else{
			columnName = field.getName();
			
		}
		try
		{
			ColumnCheck columnCheck = check.createColumnCheck(info.getTableName(), columnName);
			if(columnCheck.isExist()){
				info.setField(field);
				info.setCanNull(columnCheck.isNullable());
				info.setDefaultValue(columnCheck.getDefaultValue());
				info.setIsprivarykey(columnCheck.isPrimaryKey());
				info.setType(columnCheck.getSqlType());
				info.setUnique(columnCheck.isUnique());
				info.setName(columnName);
				info.setLength(columnCheck.getLength());
				return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			throw new ResourceResolveException(e);
		}
		
		
	}

}
