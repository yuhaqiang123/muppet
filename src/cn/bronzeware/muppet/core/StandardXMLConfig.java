package cn.bronzeware.muppet.core;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.util.XMLUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class StandardXMLConfig extends AbstractConfig implements XMLConfig, DataSourceXMLConfig{

	private StandardDataSourceXMLConfig config;
	
	private DataSourceManager dataSourceManager;
	
	private DataSourceResource[] dataSourceResources;
	
	private DataSourceListener dataSourceListener;
	
	public StandardXMLConfig(String xmlPath,ApplicationContext applicationContext) {
		super(xmlPath);
		
		config = new StandardDataSourceXMLConfig(getXMLConfigResource());
		dataSourceResources = config.getDataSourceInfo();
		dataSourceListener = config.getDataSourceListener();
	}

	@Override
	public DataSourceResource[] getDataSourceInfo() {
		return dataSourceResources;
	}

	@Override
	public DataSourceListener getDataSourceListener() {
		return dataSourceListener;
	}
	
	
	
	
}
