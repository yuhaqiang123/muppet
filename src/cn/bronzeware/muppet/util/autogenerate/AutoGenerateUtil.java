package cn.bronzeware.muppet.util.autogenerate;

import cn.bronzeware.muppet.core.EntityMappingDBXMLConfig;
import cn.bronzeware.muppet.core.StandardEntityMappingDBXMLConfig;
import cn.bronzeware.muppet.core.StandardXMLConfig;
import cn.bronzeware.muppet.core.XMLConfig;


/**
 * 用户使用工具类，只需要向这个类传一个配置文件，即可自动生成实体类
 * @author 于海强
 *
 */
public class AutoGenerateUtil {

	public static void generate(String xmlPath)
	{
		XMLConfig config = new StandardXMLConfig(xmlPath);
		EntityMappingDBXMLConfig resourceConfig = new StandardEntityMappingDBXMLConfig(config.getXMLConfigResource());
		AutoGenerateConfig autoGenerateConfig = new StandardDB2EntityAutoGenereateXMLConfig(resourceConfig.getXMLConfigResource());
		AutoGenerate autoGenerate = new XmlAutoGenerate(autoGenerateConfig.getAutoInfo());
		autoGenerate.generate();
	}
	
	public static void main(String[] args){
		AutoGenerateUtil.generate("muppet.xml");
	}
	
}
