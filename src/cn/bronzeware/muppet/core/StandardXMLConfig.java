package cn.bronzeware.muppet.core;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.util.XMLUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class StandardXMLConfig extends AbstractConfig implements XMLConfig{

	private StandardDataSourceXMLConfig config;
	
	private DataSourceManager dataSourceManager;
	
	public StandardXMLConfig(String xmlPath,ApplicationContext applicationContext) {
		super(xmlPath);
		
		config = new StandardDataSourceXMLConfig(getXMLConfigResource());
		DataSourceResource[] dataSourceResources = config.getDataSourceInfo();
		dataSourceManager = new DataSourceManager(dataSourceResources, applicationContext);
		//Logger.println(dataSourceResource.getProp().getProperty("url"));
		//new DataSourceUtil(dataSourceResource.getProp());
	}
	
	
	
	
}
