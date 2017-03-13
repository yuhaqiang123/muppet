package cn.bronzeware.muppet.core;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import cn.bronzeware.muppet.exceptions.ExcpMsg;
import cn.bronzeware.muppet.util.log.Logger;


public class StandardDataSourceXMLConfig extends AbstractConfig implements DataSourceXMLConfig{

	public StandardDataSourceXMLConfig(XMLConfigResource resource) {
		super(resource);
		this.xmlConfigResource = resource;
		config(resource);
	}
	private XMLConfigResource xmlConfigResource;
	private DataSourceResource resource;
	
	public static final String XML_DATA_SOURCE_ROOT = "#" + XMLConfig.XML_ROOT_KEY + "#" +"datasource";
	
	private void config(XMLConfigResource xmlConfigResource){
		try
		{	
			Map<String,List<Node>> map = (Map)xmlConfigResource.getProp(XML_MAP);
			List<Node> dataSources  = map.get(XML_DATA_SOURCE_ROOT);
			
			if(dataSources==null||dataSources.size()<1){
				throw new ResourceConfigException(xmlConfigResource.getXmlpath()
						+ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
		
			for (Node node:dataSources) {
				 NodeList nodeList = node.getChildNodes();
				 Properties prop = new Properties();
				 for(int j = 0;j<nodeList.getLength();j++){
					 	Node node1 = nodeList.item(j);
					 	NamedNodeMap map1 = node1.getAttributes();
					 	if(map1==null){
					 		continue;
					 	}
					 	Node value = map1.getNamedItem("value");
					 	if(value==null){
					 		continue;
					 	}
						set(prop,node1.getNodeName(),value.getNodeValue());
				 }
				 	
				DataSourceResource resource = new DataSourceResource();
				resource.setProp(prop);
				this.resource = resource;
			}
		}catch(Exception e){
			if(e instanceof ResourceConfigException){
					throw e;
			}
			e.printStackTrace();
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES+":"+this.xmlConfigResource.getXmlpath()
					+e.getMessage());
		}
	}
	
	public void set(Properties prop,String key,String value){
		if(value==null){
			return;
		}
		prop.setProperty(key, value);
	}
	
	public String getValue(NamedNodeMap nodeMap,String key){
		Node node = nodeMap.getNamedItem(key);
		if(node!=null){
			return node.getNodeValue();
		}
		return null;
	}
	
	
	@Override
	public DataSourceResource getDataSourceInfo() {
		
		return this.resource;
	}
}
