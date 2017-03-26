package cn.bronzeware.muppet.core;

public class SimpleDataSourceXMLConfig extends AbstractConfig implements DataSourceXMLConfig{

	public SimpleDataSourceXMLConfig(String xmlPath) {
		super(xmlPath);
		
	}

	@Override
	public DataSourceResource[] getDataSourceInfo() {
		
		return null;
	}

}
