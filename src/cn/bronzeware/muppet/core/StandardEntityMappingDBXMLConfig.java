package cn.bronzeware.muppet.core;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.bronzeware.muppet.exceptions.ExcpMsg;
import cn.bronzeware.muppet.util.autogenerate.EntityMappingDBResource;


/**
 * 实现XML读取用户配置文件
 * 目前主要需要读取实体类位置
 * @since 1.4
 * @version 1.4
 * 2016年8月8日 下午5:55:45
 * @author 董浩
 *
 */
public class StandardEntityMappingDBXMLConfig extends AbstractConfig implements EntityMappingDBXMLConfig{

	
	
	private String[] packetNames = null;
	private String configPaths;
	
	private EntityMappingDBResource entityMappingDBXMLResource = new EntityMappingDBResource(); 
	
	
	public StandardEntityMappingDBXMLConfig(XMLConfigResource xmlResourceConfig){
		super(xmlResourceConfig);
		config(getXMLConfigResource().getDocument());
	}
	
	
	/**
	 * 获取实体类的位置
	 */
	@Override
	public String[] getResourcePackageNames(){
		return packetNames;
	}
	
	/**
	 * 根据用户输入的配置文件进行解析
	 * @param configpaths
	 * @throws ResourceConfigException
	 */
	private void config(Document document) throws ResourceConfigException{
		
	
		configPackage(document);

	}
	
	
	private void configPackage(Document document) throws ResourceConfigException{
		try
		{	
			NodeList packetList = document.getElementsByTagName("package");
			
			
			if(packetList==null||packetList.getLength()<1){
				throw new ResourceConfigException(configPaths+ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
			packetNames = new String[packetList.getLength()];
			for (int i = 0;i<packetList.getLength();i++) {
				NamedNodeMap nodeMap = packetList.item(i).getAttributes();
				String packetname = nodeMap.getNamedItem("name").getNodeValue();
				packetNames[i] = packetname;
			}
			entityMappingDBXMLResource.setPackages(packetNames);
		}catch(Exception e){
			if(e instanceof ResourceConfigException){
					throw e;
			}
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES+":"+configPaths);
		}
		
	}


	@Override
	public boolean isBuilded() {
		return false;
	}

}
