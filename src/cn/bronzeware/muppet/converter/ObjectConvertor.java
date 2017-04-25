package cn.bronzeware.muppet.converter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;


public class ObjectConvertor {

	public static Object getValue(Object object,Field field){
		if(field==null||object==null){
			return null;
		}
		else{
			field.setAccessible(true);
			try {
				return field.get(object);
			} catch (IllegalArgumentException e) {
				// 
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// 
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public static boolean loadField(Field field ,Object object,Object value){
		if(field==null||object==null||value==null){
			return false;
		}else{
			field.setAccessible(true);
			try {
				field.set(object, value);
				return true;
			} catch (IllegalArgumentException e) {
				// 
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			}
			return false;
		}
	}
	
	
	public static void load(Map<Field, Object> map,Object object) throws ParamCanNotBeNullException{
		if(object != null){
			try {
				
				Class clazz = object.getClass();
				
				for(Entry<Field, Object> e:map.entrySet()){
					Field field = e.getKey();
					field.setAccessible(true);
					
					field.set(object, e.getValue());
				}
			} catch (IllegalArgumentException e1) {
				// 
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// 
				e1.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			throw new ParamCanNotBeNullException("object");
		}
	}
	
	public static Object load(Map<String, Object> map,Class clazz){
		
		//map,和clazz不能为空
		if((map!=null&&map.size()!=0)&&clazz!=null){
			try {
				Object object = clazz.newInstance();
				for(Entry<String, Object> e:map.entrySet()){
				      Field field = clazz.getDeclaredField(e.getKey());
					  field.setAccessible(true);
					  field.set(object, e.getValue());
				}
				return object;
			} catch (InstantiationException e1) {
				// 
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// 
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// 
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// 
				e1.printStackTrace();
			}
			
		}
		return null;
	}
	

}
