package cn.bronzeware.muppet.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.TabExpander;

public  class StandardContainer implements Container<String,ResourceInfo>{

	public static int BEAN_NUMBER = 20;
	
	private  Map<String, ResourceInfo> tables = 
			new ConcurrentHashMap<>(BEAN_NUMBER);
	
	private Map<String, ColumnInfo> columns = 
			new ConcurrentHashMap<>(BEAN_NUMBER * 8);
	
	@Override
	public ResourceInfo get(String className) {
		
		if(tables.containsKey(className)){
			return tables.get(className);
		}else{
			return null;
		}
	}
	
	private String getColumnIdentification(String className, String columnName){
		return String.format("%s.%s", className, columnName);
	}
	
	public ColumnInfo get(String className, String columnName){
		return columns.get(getColumnIdentification(className, columnName));
	}

	@Override
	public void set(String className, ResourceInfo info) {
		tables.put(className, info);
		
		for(ColumnInfo columnInfo : info.getColumns()){
			columns.put(getColumnIdentification(className, columnInfo.getName()), columnInfo);
		}
		
	}

	@Override
	public boolean contains(String name){
		return tables.containsKey(name);
	}

}
