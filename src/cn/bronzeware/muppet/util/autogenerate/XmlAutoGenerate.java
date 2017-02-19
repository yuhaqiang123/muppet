package cn.bronzeware.muppet.util.autogenerate;

import cn.bronzeware.muppet.core.DataBaseCheck.TableCheck;
import cn.bronzeware.muppet.resource.ResourceNotFoundException;
import cn.bronzeware.muppet.util.log.Logger;

public class XmlAutoGenerate extends DefaultAutoGenerate{

	private AutoInfo info;
	
	public XmlAutoGenerate(AutoInfo info){
		super(info);
		this.info = info;
	}
	
	@Override
	protected String getClassName(String tableName){
		return info.getDomainObjectName(tableName);
	}
	
	
	@Override
	public void generate() {
		for(String tableName:info.KeySets()){
			
				TableCheck tableCheck = check.createTableCheck(tableName);
				if(!tableCheck.isExist()){
					new GenerateException(
							String.format("Unable to generate entity class :%s, related table with name %s does not exist"
									,getClassName(tableName),tableName)).printStackTrace();
					continue;
				}
				createClass(tableCheck);
				Logger.println(tableCheck.getTableName()+"表对应实体类"+info.getDomainObjectName(tableName)+"已经生成。");
			
			
		}
	}

	
	
}
