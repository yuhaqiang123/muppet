package cn.bronzeware.muppet.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.DataSourceEvent;
import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class DataSourceManager{
	private ApplicationContext applicationContext = null;
	public static final String DEFAULT_DATASOURCE_NAME = "main";
	
	private DataSourceResource[] resources = null;
	
	private DataSourceListener datasourceListener;
	
	private DataSourceUtil defaultDataSource;
	
	public DataSourceListener getDatasourceListener() {
		return datasourceListener;
	}

	private Map<String, DataSourceUtil> dataSourceUtilMap = new HashMap<>();
	
	public DataSourceManager(DataSourceResource[] resources ,ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		this.resources = resources;
		setDatasourceListener(applicationContext.getBean(DataSourceListener.class));
		applicationContext.registerBean(DataSourceManager.class, this);
		initialize();
	}
	
	public void setDatasourceListener(DataSourceListener datasourceListener) {
		this.datasourceListener = datasourceListener;
	}

	public void datasourceCheck(){
		for(Map.Entry<String, DataSourceUtil> entry:dataSourceUtilMap.entrySet()){
			String key = entry.getKey();
			DataSourceUtil dataSourceUtil = entry.getValue();
			try{
				dataSourceUtil.isConnected();
			}catch(Exception e){
				if(datasourceListener != null){
					DataSourceEvent event = new DataSourceEvent();
					event.setError(e);
					event.setKey(key);
					event.setType(DataSourceListener.Type.CONNECTED_ERROR);
					datasourceListener.event(event);
				}
			}
		}
	}
	
	protected void initialize(){
		if(resources==null || resources.length==0){
			throw new DataSourceException(String.format("没有配置数据源"));
		}
		
		defaultDataSource = new DataSourceUtil(resources[0].getProp(), datasourceListener);
		
		for(int i = 0;i< resources.length;i++){
			DataSourceUtil dataSourceUtil = new DataSourceUtil(resources[i].getProp(), datasourceListener);
			if(null != dataSourceUtilMap.put(dataSourceUtil.getDataSourceKey(), dataSourceUtil)){
				throw new DataSourceException(String.format("数据源Name配置重复:%s", dataSourceUtil.getDataSourceKey()));
			}
		}
		Logger.println(String.format("默认数据源：%s", defaultDataSource.getDataSourceKey()));
		/*
		if(!dataSourceUtilMap.containsKey(DEFAULT_DATASOURCE_NAME)){
			throw new DataSourceException(String.format("数据原配置错误，没有默认数据源:%s", DEFAULT_DATASOURCE_NAME));
		}
		*/
	}
	
	public DataSourceUtil getDataSourceUtil(String key){
		if(dataSourceUtilMap.containsKey(key)){
			return dataSourceUtilMap.get(key);
		}else{
			throw new DataSourceException(String.format("没有找到相关:%s数据源", key));
		}
	}
	
	public DataSourceUtil getDefaultDataSource(){
		if(defaultDataSource != null){
			return defaultDataSource;
		}else{
			throw new DataSourceException(String.format("没有找到默认数据源"));
		}
	}
}
