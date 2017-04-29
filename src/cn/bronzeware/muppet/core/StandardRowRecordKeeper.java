package cn.bronzeware.muppet.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.glass.ui.Application;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.annotations.AnnotationException;
import cn.bronzeware.muppet.annotations.RowRecord;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.TableInfo;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerate;
import cn.bronzeware.util.reflect.ReflectUtil;

/**
 * 作为{@link RowsRecordKeeper }的子类，StandardRecordkeeper很有争议，一开始这样设计
 * 完全出于一种妥协，RowsRecordKeeper是1.3版本的一个比较复杂的类型，他是一个工具类，负责解析注解
 * 负责生成共Sql生成器{@link SqlGenerate}需要使用的{@link RowRecord}<br/><br/>
 * <h2>1.4版本采用了容器的启动方式，需要将系统所有的数据结构进行整合，有专门的注解解析器，也会有专门的中间
 * 数据结构生成器，本类将作为中间结构的生成器，即实现{@link ResourceInfo}向{@link RowRecord}之间
 * 的适配器</h2>
 * 但是后来缓存的问题暴露出来，出现了对各线程对缓存对象进行操作出现错误的情况，最后在{@link RowRecordKeeper}中
 * 封装了线程安全的getValue，和SetValue方法，子类可以继承
 * @author 于海强
 *
 *2016年8月18日 下午3:10:30
 */
public class StandardRowRecordKeeper extends RowsRecordKeeper{

	private Container<String, ResourceInfo> container;
	
	private ApplicationContext applicationContext;
	
	
	public StandardRowRecordKeeper(Container<String, ResourceInfo> container
			,ApplicationContext applicationContext) {
		this.container = container;
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 获取已经缓存的类
	 * @param clazz
	 * @param object
	 * @return
	 */
	private RowRecord getExisted(Class<?> clazz,Object object){
		
		RowRecord info = null;
		//获取时锁定map
		//如果内部有Class的键值，那么还需要检查是否有info，也就是当前线程是否有
		info = getValue(clazz);
				try {
			//如果可以获取到，那么则获取info，对象，同时获取到Field的map
					if(info!=null){
						Map<Field, Object> map = info.getMap();
						//Map<Field, Object> newmap = new LinkedHashMap<Field, Object>(map.size());
						for(Entry<Field,Object> e:map.entrySet()){
							Object value = null;
							if(object != null){
								value = e.getKey().get(object);
							}
							map.put(e.getKey(), value);
						}
						//setValue(clazz, record);
						return info;
					}
					else{
						
						return null;
					}
				} catch (IllegalArgumentException e1) {
					
				} catch (IllegalAccessException e1) {
					
				}
				return null;
		
	}
	
	
	private RowRecord getUnExisted(Class<?> clazz,Object object){

		ResourceInfo resourceInfo = container.get(clazz.getName());
		final Class<?> clazzFinal = clazz;
		if(resourceInfo==null){
			throw new InitException() {
				
				@Override
				public String message() {
					
					return "没有找到与"+clazzFinal.getName()+"相关的表";
				}
			};
		}
		
		TableInfo tableInfo = null;
		if(resourceInfo instanceof TableInfo){
			tableInfo  = (TableInfo) resourceInfo;
		}
		
		ColumnInfo[] columnInfos = tableInfo.getColumns();
		RowRecord info = new RowRecord();
		Map<Field, Object> map = new LinkedHashMap<>(columnInfos.length);
		Map<Field, String> columnNames = new HashMap<>(tableInfo.getColumns().length);
		for(ColumnInfo columnInfo:columnInfos){
			Field field = columnInfo.getField();
			if(columnInfo.isIsprivarykey()){
				info.setPrimarykey(field);
				info.setPrimaryKeyName(columnInfo.getName());
			}
			Object value = null;
			if(object != null){
				value = ReflectUtil.getValue(field, object);
			}
			//如果object == null 那么 value == null
			map.put(columnInfo.getField(), value);
			columnNames.put(columnInfo.getField(), columnInfo.getName());
		}
		info.setTableName(tableInfo.getTableName());
		info.setMap(map);
		info.setColumnNames(columnNames);
		setValue(clazz, info);
		return info;
	}
	
	
	@Override
	public RowRecord execute(Object object)
			throws AnnotationException
	{
		Class<?> clazz = object.getClass();
		return execute(clazz, object);
	}
	
	public RowRecord execute(Class clazz){
		return execute(clazz, null);
	}
	
	public RowRecord execute(Class clazz, Object object){
		RowRecord record = getExisted(clazz, object);
		if(record==null){
			//如果没有获取到Info，说明是第一次 解析
			return getUnExisted(clazz, object);
		}else{
			return record;
		}
	}
	
	
}
