package cn.bronzeware.muppet.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListCirculateUtil {

	
	/**
	 * 根据不同的List类型提供最快速的迭代方案
	 * @param list 待迭代的list
	 * @param circulate 指定循环处理
	 * @param args 客户端传入参数
	 */
	public static <T> void execute(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		if(list instanceof ArrayList){
			executeArrayList(list, circulate, args);
		}
		else if(list instanceof LinkedList){
			executeLinkedList(list, circulate, args);
		}
		else if(list instanceof Vector){
			executeVector(list, circulate, args);
		}
		else if(list instanceof CopyOnWriteArrayList){
			executeCopyOnWriteArrayList(list, circulate, args);
		}
		else {
			executeDefault(list, circulate, args);
		}
	}
	
	private  static <T> void executeDefault(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		int i = 0;
		for(T t:list){
			circulate.execute(list, t
					, i, args);
			i++;
		}
	}
	
	
	private static <T> void executeCopyOnWriteArrayList(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		for(int i = 0;i<list.size();i++){
			circulate.execute(list, list.get(i)
					, i, args);
		}
	}
	
	
	private static <T> void executeVector(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		for(int i = 0;i<list.size();i++){
			circulate.execute(list, list.get(i), i, args);
		}
	}
	
	
	
	private static <T> void executeLinkedList(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		int i = 0;
		for(Iterator<T> iterator = list.iterator();iterator.hasNext();)
		{
			T t = iterator.next();
			circulate.execute(list, t, i, args);
			i++;
		}
	}
	
	
	
	private static <T> void executeArrayList(List<T> list
			,ListCirculate<T> circulate
			,Object... args){
		for(int i = 0;i<list.size();i++){
			circulate.execute(list, list.get(i), i,args);
		}
	
	}
	
	
	
}
