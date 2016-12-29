package cn.bronzeware.muppet.filters;

import cn.bronzeware.muppet.context.SqlContext;

public interface FilterChain {

	void doChain(SqlContext context);
}
