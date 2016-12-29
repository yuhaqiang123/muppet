package cn.bronzeware.muppet.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.TabExpander;

public  class StandardContainer implements Container<String,ResourceInfo>{

	

	private static Map<String, ResourceInfo> tables = 
			new ConcurrentHashMap<>(50);
	
	

	
	@Override
	public ResourceInfo get(String name) {
		
		if(tables.containsKey(name)){
			return tables.get(name);
		}else{
			return null;
		}
	}

	@Override
	public void set(String name, ResourceInfo info) {
		tables.put(name, info);
	}

	@Override
	public boolean contains(String name){
		return tables.containsKey(name);
	}

}
