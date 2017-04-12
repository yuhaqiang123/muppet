package cn.bronzeware.muppet.core;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.datasource.StandardDataSourceListener;
import cn.bronzeware.muppet.exceptions.ExcpMsg;
import cn.bronzeware.muppet.util.log.Logger;

/**
 * 
 * @author Administrator
 *
 */
public class StandardDataSourceXMLConfig extends AbstractConfig implements DataSourceXMLConfig{

	public StandardDataSourceXMLConfig(XMLConfigResource resource) {
		super(resource);
		this.xmlConfigResource = resource;
		dataSourceListener = new StandardDataSourceListener();
		config(resource);
	}
	private XMLConfigResource xmlConfigResource;
	
	private DataSourceResource[] resources;
	
	private DataSourceListener dataSourceListener;
	
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
			resources = new DataSourceResource[dataSources.size()];
			int i = 0;
			for (Node node:dataSources) {
				Properties prop = new Properties();
				try{
					String dataSourceName = node.getAttributes().getNamedItem("name").getNodeValue();
					set(prop, "datasource_name", dataSourceName);
				}catch (NullPointerException e) {
					throw new DataSourceException(String.format("数据源配置错误，datasource没有name标识"));
				}
				 
				NodeList nodeList = node.getChildNodes();
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
				this.resources[i++] = resource;
			}
		}catch(Exception e){
			if(e instanceof ResourceConfigException){
					throw e;
			}
			//e.printStackTrace();
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES+":"+this.xmlConfigResource.getXmlpath()
					+e.getMessage());
		}
	}
	
	protected void set(Properties prop,String key,String value){
		if(value==null){
			return;
		}
		prop.setProperty(key, value);
	}
	
	protected String getValue(NamedNodeMap nodeMap,String key){
		Node node = nodeMap.getNamedItem(key);
		if(node!=null){
			return node.getNodeValue();
		}
		return null;
	}
	
	
	@Override
	public DataSourceResource[] getDataSourceInfo() {
		
		return this.resources;
	}

	
	
	@Override
	public DataSourceListener getDataSourceListener() {
		
		return dataSourceListener;
	}
}
