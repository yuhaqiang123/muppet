package cn.bronzeware.muppet.core;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.util.XMLUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class StandardXMLConfig extends AbstractConfig implements XMLConfig{

	private StandardDataSourceXMLConfig config;
	
	
	public StandardXMLConfig(String xmlPath) {
		super(xmlPath);
		
		config = new StandardDataSourceXMLConfig(getXMLConfigResource());
		DataSourceResource dataSourceResource = config.getDataSourceInfo();
		//Logger.println(dataSourceResource.getProp().getProperty("url"));
		new DataSourceUtil(dataSourceResource.getProp());
	}
	
	
	
	
}
