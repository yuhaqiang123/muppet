package cn.bronzeware.muppet.core;

import java.util.List;

import cn.bronzeware.muppet.context.SelectContext;
import cn.bronzeware.muppet.resource.Container;

class CriteriaBuilder<T> extends AbstractCriteria<T>{

	


	public CriteriaBuilder(Container container
			, SelectContext selectContext
			,Class<T> clazz)
			throws RuntimeException {
		super(container, selectContext,clazz);
		
	}
	
	
	
}
