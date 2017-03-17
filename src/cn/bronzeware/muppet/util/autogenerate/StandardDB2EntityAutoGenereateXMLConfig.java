package cn.bronzeware.muppet.util.autogenerate;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.bronzeware.muppet.core.AbstractConfig;
import cn.bronzeware.muppet.core.ResourceConfigException;
import cn.bronzeware.muppet.core.XMLConfigResource;
import cn.bronzeware.muppet.exceptions.ExcpMsg;

/**
 * 解析xml 获取自动代码生成的所需数据
 * 
 * @author 于海强
 *
 */
public class StandardDB2EntityAutoGenereateXMLConfig extends AbstractConfig implements AutoGenerateConfig {

	public static final String XML_TATBLE_ROOT = "#" + XML_ROOT_KEY + "#" + "table";

	public static final String XML_GENERATE_ROOT = "#" + XML_ROOT_KEY + "#" + "generate";
	/*
	 * public StandardAutoGenereateTableXMLConfig(String xmlpath){
	 * super(xmlpath); config(getXMLConfigResource().getDocument(),xmlpath); }
	 */

	public StandardDB2EntityAutoGenereateXMLConfig(XMLConfigResource xmlResource) {
		super(xmlResource);
		config(xmlResource);
	}

	private String xmlPath;
	private AutoInfo info = new AutoInfo();;

	private void config(XMLConfigResource resource) {

		configTables(resource);
		configGeneratePath(resource);

	}

	private void configTables(XMLConfigResource resource) {
		try {
			Map<String, List<Node>> map = (Map) resource.getProp(XML_MAP);
			List<Node> packetList = map.get(XML_TATBLE_ROOT);

			if (packetList == null || packetList.size() < 1) {
				throw new ResourceConfigException(xmlPath + ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
			for (Node node : packetList) {
				NamedNodeMap nodeMap = node.getAttributes();
				String tableName = nodeMap.getNamedItem("tableName").getNodeValue();
				String domainObjectName = nodeMap.getNamedItem("domainObjectName").getNodeValue();
				info.set(tableName, domainObjectName);
			}

		} catch (Exception e) {
			if (e instanceof ResourceConfigException) {
				throw e;
			}
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES + ":" + xmlPath);
		}
	}

	private void configGeneratePath(XMLConfigResource resource) {
		try {
			Map<String, List<Node>> map = (Map)resource.getProp(XML_MAP);
			
			List<Node> packetList = map.get(XML_GENERATE_ROOT);
			if (packetList == null || packetList.size() < 1) {
				throw new ResourceConfigException(xmlPath + ExcpMsg.CANNOT_FOUND_RESOURCE_PACKAGE_TAGS);
			}
			NamedNodeMap nodeMap = packetList.get(0).getAttributes();
			String packageName = nodeMap.getNamedItem("packageName").getNodeValue();
			info.setGeneratePath(packageName);
		} catch (Exception e) {
			if (e instanceof ResourceConfigException) {
				throw e;
			}
			throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_RESOURCE_PACKAGENAMES + ":" + xmlPath);
		}
	}

	@Override
	public AutoInfo getAutoInfo() {

		return info;
	}

}
