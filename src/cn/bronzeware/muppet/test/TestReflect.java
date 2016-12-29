package cn.bronzeware.muppet.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



import cn.bronzeware.muppet.Test;
import cn.bronzeware.muppet.entities.Note;


/**
 * 调用10,000,000次 反射平均时间70  正常调用 9毫秒 
 * 
 * 
 * 
 * @author 于海强
 *
 */
public class TestReflect {

	private static int circle = 10000000;
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {

		long otime = TestReflect.getObject();
		long rtime = TestReflect.reflectObject();
		System.out.println("正常访问"+otime);
		System.out.println("反射访问"+rtime);
	}
	
	public static long getObject(){
		Note note = new Note();
		long start = System.currentTimeMillis();
		for(int i  =0 ;i<circle;i++){
			note.getId();
		}
		long end = System.currentTimeMillis();
		return end - start;
		
	}
	
	public static long reflectObject() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Class note = Note.class;
		Note note1 = new Note();
		Field field = note.getDeclaredField("id");
		field.setAccessible(true);
		long  start = System.currentTimeMillis();
		for(int i = 0;i<circle;i++){
			field.get(note1);
		}
		long end = System.currentTimeMillis();
		return end  - start;
	}
	public static long object(){
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < circle; i++) {
			Note note = new Note();
			note.setUsername("yuhaiqiang");
			
		}
		long end = System.currentTimeMillis();
		return end - start;
	}
	
	/**
	 * 
	 * Class clazzClass  = note.getClass();
			if(resultMetod==null){
				Method[] methods = clazzClass.getDeclaredMethods();
				for(Method method:methods){
					if(resultMetod==null&&method.getName().equals("setUsername")){
						resultMetod = method;
						System.out.println("fbiu");
						break;
					}
				}
			}
			resultMetod.invoke(note, new Object[]{"username"});
	 * @throws NoSuchMethodException 
	 */
	
	public  static long Reflect() throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException{
		long start = System.currentTimeMillis();
		Field resultField = null;
		Method resultMetod = null;
		for (int i = 0; i < circle; i++) {
			Note note = new Note();
			/*Class clazzClass  = note.getClass();
			if(resultField==null){
				Field[] fields = clazzClass.getDeclaredFields();
				for(Field field:fields){
					if(field.getName().equals("username")){
						resultField = field;
					}
				}
			}
			resultField.setAccessible(true);
			resultField.set(note, "username");*/
			//resultField = null;
			
			Class clazzClass  = note.getClass();
			if(resultMetod==null){
				Method methods = clazzClass.getDeclaredMethod("setUsername");
				/*for(Method method:methods){
					if(method.getName().equals("setUsername")){
						resultMetod = method;
						break;
					}
				}*/
				resultMetod = methods;
			}
			resultMetod.invoke(note, new Object[]{"username"});
			
		}
		long end = System.currentTimeMillis();
		return end - start;
	}

}
