package cn.bronzeware.muppet.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.DataSourceUtil;

public class DataSourceManager{
	private ApplicationContext applicationContext = null;
	public static final String DEFAULT_DATASOURCE_NAME = "main";
	
	private DataSourceResource[] resources = null;
	
	private Map<String, DataSourceUtil> dataSourceUtilMap = new HashMap<>();
	
	public DataSourceManager(DataSourceResource[] resources ,ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		this.resources = resources;
		applicationContext.registerBean(DataSourceManager.class, this);
		initialize();
	}
	
	protected void initialize(){
		for(int i = 0;i< resources.length;i++){
			DataSourceUtil dataSourceUtil = new DataSourceUtil(resources[i].getProp());
			if(null != dataSourceUtilMap.put(dataSourceUtil.getName(), dataSourceUtil)){
				throw new DataSourceException(String.format("数据源Name配置重复:%s", dataSourceUtil.getName()));
			}
		}
		if(!dataSourceUtilMap.containsKey(DEFAULT_DATASOURCE_NAME)){
			throw new DataSourceException(String.format("数据原配置错误，没有默认数据源:%s", DEFAULT_DATASOURCE_NAME));
		}
	}
	
	public DataSourceUtil getDataSourceUtil(String key){
		if(dataSourceUtilMap.containsKey(key)){
			return dataSourceUtilMap.get(key);
		}else{
			throw new DataSourceException(String.format("没有找到相关:%s数据源", key));
		}
	}
	
	public DataSourceUtil getDefaultDataSource(){
		if(dataSourceUtilMap.containsKey(DEFAULT_DATASOURCE_NAME)){
			return dataSourceUtilMap.get(DEFAULT_DATASOURCE_NAME);
		}else{
			throw new DataSourceException(String.format("没有找到默认数据源"));
		}
	}
}
