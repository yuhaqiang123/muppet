package cn.bronzeware.muppet.core;

import cn.bronzeware.muppet.datasource.DataSourceListener;

public interface DataSourceXMLConfig extends XMLConfig{

	public DataSourceResource[] getDataSourceInfo();
	
	public DataSourceListener getDataSourceListener();
}
