package cn.bronzeware.test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;


public class TestHashMap implements Runnable{

	
	//private static final String  = null;
	
	private A a;
	static int numB = 0;
	public void run() {
		for(int i = 0;i<1000;i++){
			Object object = map.put(""+i, new Object());
			if(object!=null){
				numB++;
			}
		}
	}
	static Map<String, Object> map = new HashMap<String,Object>();
	public static void main(String[] args) throws InterruptedException{

		Map<String, Object> result = new HashMap<String,Object>();
		Thread thread = new Thread(new TestHashMap());
		thread.start();
		int numA = 0;
		for(int i = 0;i<1000;i++){
			Object object = map.put(""+i, new Object());
			if(object!=null){
				numA++;
			}
		}
		Thread.currentThread().sleep(3000);
		System.out.println(map.size()+"   "+map.size());
		numA+=numB;
		System.out.println("计算"+(2000-numA));
		int i = 0;
		
		for(Map.Entry<String, Object> a:map.entrySet())
		{
			i++;
			//Systeem.out.println(a.getKey()+" " +a.getValue());
			if(!result.containsKey(a.getKey())){
				result.put(a.getKey(), a.getKey());
			}else{
					System.out.println("存在"+a.getKey());
				
			}
			
		}
		System.out.println("i"+i);
		  System.out.println(result.size());
		
		
	}
}
