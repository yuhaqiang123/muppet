package cn.bronzeware.muppet.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cn.bronzeware.muppet.annotations.AnnotationException;
import cn.bronzeware.muppet.annotations.Constant;
import cn.bronzeware.muppet.annotations.LackAnnotationException;
import cn.bronzeware.muppet.annotations.LackPrimaryKeyException;
import cn.bronzeware.muppet.annotations.MutiPrimaryKeyException;
import cn.bronzeware.muppet.annotations.NotInTable;
import cn.bronzeware.muppet.annotations.PrimaryKey;
import cn.bronzeware.muppet.annotations.RowRecord;
import cn.bronzeware.muppet.annotations.Table;


/**
 * 在第一版本中，RowsRecordKeeper主要作用是解析注解，生成RowRecord
 * 但是第二版本中，采用了容器设计，需要将1.3版本之前的数据结构进行整合详细情况参考
 * {@link StandardRowRecordKeeper} 
 * 需要强调的是：RowRecordKeeper内部维护了一个 protected类型的map
 * 这个map缓存了线程局部变量，为每一个类型保存一个键值对，但是这个键值对属于一个类型，但是
 * RowRecord保存了当前线程方法操作的 用户输入的一个对象的各属性域的值，在多线程访问时如果所有的
 * 对象共享这个对象显然会出错，但是map中的值是一个线程局部变量，即为每一个线程单独分配了一个RowRecord对象
 * 这个对象只要一保存在线程局部变量里，就不会被remove，因为一个类的rowrecord是固定的，唯一不确定的就是
 * 这个对象里的一个成员变量map<Field，Object> Field是属性，Object是对应一个实体类一个属性域的值
 * 我们希望减少Map的频繁内存消耗，打算共享这个map，即每一次只需要获得这个map,然后对其进行更新对应的Value，
 * Key值不更新，map长度也不更新，这样达到了缓存的目的，同时不存在线程安全问题
 * @author 于海强
 *
 */
public class RowsRecordKeeper{

	protected static Map<Class<?>,ThreadLocal<RowRecord>> factory 
		= new ConcurrentHashMap<Class<?>,ThreadLocal<RowRecord>>();
	
	
	protected RowRecord getValue(Class<?> clazz){
		ThreadLocal<RowRecord> local = null;
		if(factory.containsKey(clazz)){
			local = factory.get(clazz);
			RowRecord info = local.get();
			if(info == null){
				//当前线程中没有这个 info
				return null;
			}else{
				return info;
			}
		}else{
			return null;
		}
		
	}
	
	//这个方法被调用是当前线程因为没有找到对应的实例
	protected  void setValue(Class<?> clazz,RowRecord record){
		ThreadLocal<RowRecord> local = null;
		if(factory.containsKey(clazz)){
			local = factory.get(clazz);
			local.set(record);
		}
		else{
			local = new ThreadLocal<>();
			local.set(record);
			factory.put(clazz, local);
		}
	}
	
	public RowRecord execute(Class clazz) throws AnnotationException{
		return null;
	}
	
	
	
	/**
	 * 负责解析class的注解，包括{@link PrimaryKey } ,{@link Table}
	 * ,{@link NotInTable}
	 * 由于clazz.getDeclaredFields方法较为费时，实体类class在运行时一般不会<br>
	 * 改变，所以采用 缓存技术，将Field缓存起来，这样每一次对class的解析，相当于仅仅<br>
	 * 是通过Field对象获取相应的Value，这样在速度上会有不小的提升
	 */
	public RowRecord execute(Object object)
							throws AnnotationException
	{
		try {
			Class<?> clazz = object.getClass();
			ThreadLocal<RowRecord> local = null;
			//获取时锁定map
			if(factory.containsKey(clazz)){
				local = factory.get(clazz);
			}
			RowRecord info =null;
			//如果可以获取到，那么则获取info，对象，同时获取到Field的map
			if(local!=null){
				info = local.get();
				Map<Field, Object> map = info.getMap();
				Map<Field, Object> newmap = new LinkedHashMap<Field, Object>(map.size());
				for(Entry<Field,Object> e:map.entrySet()){
					Object value = e.getKey().get(object);
					newmap.put(e.getKey(), value);
				}
				info.setMap(newmap);
				return info;
			}
			
			//如果没有获取到Info，说明是第一次 解析
			info = new RowRecord();
			
			/**
			 * 存储Field域和value,这个键值对的插入顺序应该是有序的，
			 * 这样在构造sql语句，以及预编译sql语句时，构造参数时都需要
			 * 有序的map
			 */
			Map<Field, Object> map = 
					new LinkedHashMap<Field, Object>();
			Annotation[] annotation = clazz.getDeclaredAnnotations();
			
			/*
			 * 是否有主键，分两种情况，一种情况是，Class中有且仅有一个
			 * {@link PrimaryKey} 标记，这是这个域将被保存在 PrimaryKeyField这个
			 * 域中，或者没有PrimaryKey标记，但是有一个域的 name是 'id'在这种情况下，会默认
			 * 这个域作为主键
			 */
			boolean is_have_primarykey = false;
			if(annotation==null){
				throw new LackAnnotationException("至少应该包括一种注解，例如Table注解");
			}
			
			
			for (int i = 0; i < annotation.length; i++) {
				Class<? extends Annotation> type = annotation[i].annotationType();
				
				if(type.equals(Table.class)){
					Table table = clazz.getAnnotation(Table.class);
					String tableName = table.tablename();
					if(tableName==null){
						tableName = clazz.getSimpleName();
					}
					info.setTableName(tableName);
					
					Field[] fields = clazz.getDeclaredFields();
					Field default_primary_key_field = null;
					
					for(Field field:fields){
						NotInTable notInTable = field.getAnnotation(NotInTable.class);
						//也就是说在在数据库表里
						if(notInTable==null){
							field.setAccessible(true);
							
							//System.out.println(field.getName()+" "+field.get(object));
							Object value = field.get(object);
							
							map.put(field,value);
						}
						if(field.getName().equals(Constant.PRIMARY_KEY)){
							default_primary_key_field = field;
						}
						
						PrimaryKey primaryKey =  field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null){
							//说明此时 已经出现过一个主键了，应该报错
							if(is_have_primarykey==true){
								throw new MutiPrimaryKeyException("请保证一个实体类一个PrimaryKey注解定义");
							}else{
								info.setPrimarykey(field);
								is_have_primarykey = true;
							}
							
						}
					}
					if(is_have_primarykey==false&&default_primary_key_field==null){
						throw new LackPrimaryKeyException();
					}
					if(is_have_primarykey==false&&default_primary_key_field!=null){
						info.setPrimarykey(default_primary_key_field);
						//info.setPrimaryKeyName(primaryKeyName);
					}
					
					
					info.setMap(map);
				}
			}
			ThreadLocal<RowRecord> record = new ThreadLocal<>();
			record.set(info);
			factory.put(clazz, record);
			return info;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// 
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// 
			e.printStackTrace();
		}
		return null;
		
		
	}
	public static void main(String[] args) {

	}

}
