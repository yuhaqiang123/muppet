package cn.bronzeware.muppet.test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bronzeware.muppet.util.log.Logger;

public class TestJMX {

	public static void main(String[] args){
		
		System.out.println(String.class.getClassLoader());
		Object object = new Object();
		TestJMX jmx = new TestJMX();
		System.out.println(jmx.getClass().getClassLoader().getClass().getName());
		///Integer i = (Integer)new Object();
		/*ClassLoader ccs;
		Object object;
		ccs.getClass().isi*/
		
		/*System.out.println(Object.class.isInstance(new LinkedList<>()));
		System.out.println(Object.class.isAssignableFrom(ArrayList.class));*/
		List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
		int poolFound = 0;
		int poolWithStatus = 0;
		int [] a = new int[100000];
		for(MemoryPoolMXBean bean:list){
			try{
				Logger.println("found pool:"+bean.getName());
				long usage = bean.getCollectionUsage().getUsed();
				long init = bean.getCollectionUsage().getInit();
				System.out.println(bean.getName()+"init:"+init/(1024*1024)+":usage after gc "+usage);
				if(usage>0){
					poolWithStatus++;
				}
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			
		}
		
		
	}
}
