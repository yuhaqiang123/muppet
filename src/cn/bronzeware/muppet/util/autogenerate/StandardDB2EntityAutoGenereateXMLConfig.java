package cn.bronzeware.muppet.util.autogenerate;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import cn.bronzeware.muppet.core.AbstractConfig;
import cn.bronzeware.muppet.core.ResourceConfigException;
import cn.bronzeware.muppet.core.XMLConfigResource;
import cn.bronzeware.muppet.exceptions.ExcpMsg;

/**
 * 解析xml 获取自动代码生成的所需数据
 * @author 于海强
 *
 */
public class StandardDB2EntityAutoGenereateXMLConfig extends AbstractConfig implements AutoGenerateConfig{

	/*
	public StandardAutoGenereateTableXMLConfig(String xmlpath){
		super(xmlpath);
		config(getXMLConfigResource().getDocument(),xmlpath);
	}*/
	
	
	public StandardDB2EntityAutoGenereateXMLConfig(XMLConfigResource xmlResource){
		super(xmlResource);
		config(xmlResource.getDocument(),xmlResource.getXmlpath());
	}
	
	
	
	private String xmlPath;
	private AutoInfo info = new AutoInfo();; 
	private void config(Document document,String xmlPath){
		
		configTables(document, xmlPath);
		configGeneratePath(document, xmlPath);
				
	}
	private void configTables(Document document,String xmlPath){
		try
		{	
			NodeList packetList = document.getElementsByTagName("table");
			
			
			if(packetList==null||packetList.getLength()<1){
				throw new ResourceConfigException(xmlPath + ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
			for (int i = 0;i<packetList.getLength();i++) {
				NamedNodeMap nodeMap = packetList.item(i).getAttributes();
				String tableName = nodeMap.getNamedItem("tableName").getNodeValue();
				String domainObjectName = nodeMap.getNamedItem("domainObjectName").getNodeValue();
				info.set(tableName, domainObjectName);
			}
			
		}catch(Exception e){
			if(e instanceof ResourceConfigException){
					throw e;
			}
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES+":"+xmlPath);
		}
	}
	
	private void configGeneratePath(Document document,String xmlPath){
		try
		{	
			NodeList packetList = document.getElementsByTagName("generate");
			
			
			if(packetList==null||packetList.getLength()<1){
				throw new ResourceConfigException(xmlPath + ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
			
			for (int i = 0;i<packetList.getLength();i++) {
				
				NamedNodeMap nodeMap = packetList.item(i).getAttributes();
				String packageName = nodeMap.getNamedItem("packageName").getNodeValue();
				info.setGeneratePath(packageName);
			}
			
		}catch(Exception e){
			if(e instanceof ResourceConfigException){
					throw e;
			}
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES+":"+xmlPath);
		}
	}
	
	
	@Override
	public AutoInfo getAutoInfo() {
		
		
		return info;
	}


}
