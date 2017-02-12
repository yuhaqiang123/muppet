package cn.bronzeware.muppet.core;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.regexp.internal.recompile;

import cn.bronzeware.muppet.exceptions.ExcpMsg;
import cn.bronzeware.muppet.util.log.Logger;

//import sun.jdbc.odbc.OdbcDef;

public class StandardDataSourceXMLConfig extends AbstractConfig implements DataSourceXMLConfig{

	public StandardDataSourceXMLConfig(XMLConfigResource resource) {
		super(resource);
		this.xmlConfigResource = resource;
		config(resource);
	}
	private XMLConfigResource xmlConfigResource;

	private DataSourceResource resource;
	
	private void config(XMLConfigResource xmlConfigResource){
		Document document = xmlConfigResource.getDocument();
		try
		{	
			
			NodeList packetList = document.getElementsByTagName("datasource");
			
			if(packetList==null||packetList.getLength()<1){
				throw new ResourceConfigException(xmlConfigResource.getXmlpath()
						+ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
		
			for (int i = 0;i<1;i++) {
				 NodeList nodeList = packetList.item(i).getChildNodes();
				 Properties prop = new Properties();
				 for(int j = 0;j<nodeList.getLength();j++){
					 	Node node = nodeList.item(j);
					 	NamedNodeMap map = node.getAttributes();
					 	if(map==null){
					 		continue;
					 	}
					 	Node value = map.getNamedItem("value");
					 	if(value==null){
					 		continue;
					 	}
						set(prop,node.getNodeName(),value.getNodeValue());
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
