package cn.bronzeware.muppet.filters;

import cn.bronzeware.muppet.core.AbstractConfig;
import cn.bronzeware.muppet.core.XMLConfigResource;

public class StandardFilterXMLConfig extends AbstractConfig implements FilterXMLConfig{

	public StandardFilterXMLConfig(XMLConfigResource resource) {
		super(resource);
		
	}

	@Override
	public FilterWrapper[] getFilterWrapper() {
		
		return null;
	}
	
	
	
	
	

}
