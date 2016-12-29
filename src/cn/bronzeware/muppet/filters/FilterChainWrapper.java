package cn.bronzeware.muppet.filters;

import cn.bronzeware.muppet.context.SqlContext;

public class FilterChainWrapper implements FilterChain{

	
	private FilterChain filterChain;
	
	public FilterChainWrapper(FilterChain filterChain){
		this.filterChain = filterChain;
	}
	

	
	
	@Override
	public void doChain(SqlContext context) {
		
			filterChain.doChain(context);
		
	}
	
	

}
