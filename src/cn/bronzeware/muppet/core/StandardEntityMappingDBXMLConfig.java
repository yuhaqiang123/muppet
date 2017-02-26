package cn.bronzeware.muppet.core;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.event.DocumentEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.bronzeware.muppet.exceptions.ExcpMsg;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.muppet.util.autogenerate.EntityMappingDBResource;
import cn.bronzeware.muppet.util.log.Logger;


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
	private boolean buildTable = false;
	
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
		configBuildTable(document);
	}
	
	
	private void configPackage(Document document) throws ResourceConfigException{
		try{
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
	
	/**
	 * 解析xml,解析
	 * <buildtable value="true|false"></buildtable>
	 * @param document
	 * @throws ResourceConfigException
	 */
	private void configBuildTable(Document document) throws ResourceConfigException{
		NodeList nodes = document.getElementsByTagName("buildtable");
		if(Utils.notEmpty(nodes) || nodes.getLength() < 1){
			try{
				NamedNodeMap map = nodes.item(0).getAttributes();
				String buildTableString = map.getNamedItem("value").getNodeValue();
				this.buildTable = Boolean.valueOf(buildTableString);
			}catch (Exception e) {
				throw new ResourceConfigException(String.format("解析buildtable出错，value只能为true或false"));
			}
		}else{
			this.buildTable = false;
		}
	}


	@Override
	public boolean isBuilded() {
		return this.buildTable;
	}

}
