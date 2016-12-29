package cn.bronzeware.muppet.filters;

import cn.bronzeware.muppet.context.SqlContext;

public interface Filter {

	public void doFilter(FilterChain chain,SqlContext context);
}
