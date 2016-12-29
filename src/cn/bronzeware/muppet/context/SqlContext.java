package cn.bronzeware.muppet.context;

import cn.bronzeware.muppet.filters.SqlRequest;
import cn.bronzeware.muppet.filters.SqlResponse;

public interface SqlContext {

	public SqlRequest getSqlRequest();
	
	
	public SqlResponse getSqlResponse();
	
}
