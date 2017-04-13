package cn.bronzeware.muppet.core;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.DocumentEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.bronzeware.muppet.datasource.EntityPackage;
import cn.bronzeware.muppet.datasource.EntityPkgOnDataSourceConfig;
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
public class StandardEntityMappingDBXMLConfig extends AbstractConfig 
								implements EntityMappingDBXMLConfig, EntityPkgOnDataSourceConfig{

	
	
	private String[] packetNames = null;
	
	private String configPaths;
	
	private boolean buildTable = false;
	
	private List<EntityPackage> entityPackages;
	
	public static final String XML_PACKAGE_ROOT = "#" + XML_ROOT_KEY + "#" +"entity" + "#" +"package";
	
	public static final String XML_BUILD_TABLE = "#" + XML_ROOT_KEY + "#" + "buildtable";
	
	
	private EntityMappingDBResource entityMappingDBXMLResource = new EntityMappingDBResource(); 
	
	
	public StandardEntityMappingDBXMLConfig(XMLConfigResource xmlResourceConfig){
		super(xmlResourceConfig);
		config(getXMLConfigResource());
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
	private void config(XMLConfigResource resource) throws ResourceConfigException{
		configPackage(resource);
		configBuildTable(resource);
	}
	
	
	private void configPackage(XMLConfigResource resource) throws ResourceConfigException{
		try{
			Map<String, List<Node>> xmlMap = (Map)resource.getProp(XML_MAP);
			List<Node> list = xmlMap.get(XML_PACKAGE_ROOT);
			if(list ==null||list.size()<1){
				throw new ResourceConfigException(configPaths+ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
			packetNames = new String[list.size()];
			List<EntityPackage> entityPackages = new ArrayList<>(list.size());
			int i = 0;
			for (Node node:list) {
				NamedNodeMap nodeMap = node.getAttributes();
				String packetname = nodeMap.getNamedItem("name").getNodeValue();
				String dataSource = null;
				if(nodeMap.getNamedItem("datasource") != null){
					dataSource = nodeMap.getNamedItem("datasource").getNodeValue();
				}
				EntityPackage entityPackage = new EntityPackage();
				entityPackage.setDataSourceKey(dataSource);
				entityPackage.setPkgName(packetname);
				entityPackages.add(entityPackage);
				packetNames[i] = packetname;
				i++;
			}
			this.entityPackages = entityPackages;
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
	private void configBuildTable(XMLConfigResource resource) throws ResourceConfigException{
		Map<String, List<Node>> map = (Map)resource.getProp(XML_MAP);
		List<Node> nodes = map.get(XML_BUILD_TABLE);
		if(Utils.notEmpty(nodes) || nodes.size() < 1){
			try{
				NamedNodeMap namedMap = nodes.get(0).getAttributes();
				String buildTableString = namedMap.getNamedItem("value").getNodeValue();
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


	@Override
	public List<EntityPackage> getEntityPackage() {
		return this.entityPackages;
	}

}
