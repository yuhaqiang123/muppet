package cn.bronzeware.muppet.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.bronzeware.muppet.core.IllealInvokeException;
import cn.bronzeware.muppet.entities.Note;

public class ArrayUtil {

	
	public static <T> void println(T[] objects){
		if(objects==null){
			throw new IllegalArgumentException("参数Object[] 为空");
		}
		System.out.println(ArrayUtil.class.getName()+"数组"+":Size="+objects.length);
		
		for(Object object:objects){
			System.out.println(object);
		}
	}

	public static String getValues(Collection<?> collection, String split){
		StringBuffer buffer = new StringBuffer();
		for(Object object:collection){
			buffer.append(object.toString());
			buffer.append(split);
		}
		buffer.delete(buffer.length() - split.length(), buffer.length());
		return buffer.toString();
	}
	
	public static String getValues(Object[] collections, String split){
		StringBuffer buffer = new StringBuffer();
		for(Object object:collections){
			buffer.append(object.toString());
			buffer.append(split + "\r\n");
		}
		buffer.delete(buffer.length() - split.length(), buffer.length());
		return buffer.toString();
	}
	

	
	public static void println(Collection<?> collection){
		if(collection==null){
			throw new IllegalArgumentException("参数Collection为空");
		}
		System.out.println(ArrayUtil.class.getName()+"打印Collection:Size="+collection.size());
		for(Object object:collection){
			System.out.println(object);
		}
	}
	
	public static void println(Map<?,?> map){
		if(map==null){
			throw new IllegalArgumentException("参数Map为空");
		}
		System.out.println(ArrayUtil.class.getName()+"打印Map:size="+map.size());
		for(Map.Entry<?, ?> e: map.entrySet()){
			System.out.println("Key:["+e.getKey().toString()+"] \t Value:["+e.getValue().toString()+"]");
		}
	}
	
	public static String getValues(Map<?, ?> map){
		StringBuffer buffer = new StringBuffer();
		for(Map.Entry<?, ?> e: map.entrySet()){
			buffer.append("Key:["+e.getKey().toString()+"] \t Value:["+e.getValue().toString()+"]");
			buffer.append("\r\n");
		}
		return buffer.toString();
	}
	
/*	public static void main(String[] args){
		
	}*/
	
	
	public static <T> void circulate(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		ListCirculateUtil.execute(list, circulate, args);
	}
	
	public static void main(String[] args){
		List<String> list= new Stack<>();
		list.add("fewa");
		list.add("fewa");
		list.add("fewa");
		list.add("fewa");
		
		List<Note> notes = new ArrayList<>();
		
		ArrayUtil.circulate(list, new ListCirculate<String>() {
			@Override
			public void execute(List<String> list, String t,int index
					, Object... args) {
				//list.get(0);
				List<Note> notes = (List<Note>) args[0];
				Note note = new Note();
				note.setId(index);
				note.setPassword(t);
				notes.add(note);
			}
		}
		,notes);
		
		ArrayUtil.println(notes);
	}
	
	
	
	
	
	
}
