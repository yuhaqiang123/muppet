package cn.bronzeware.muppet.filters;

import cn.bronzeware.muppet.context.DefaultFilter;
import cn.bronzeware.muppet.context.SqlContext;
import cn.bronzeware.muppet.tabledivision.TableDivisionStrategy;

public class StandardTableDivisionDefaultFilter implements DefaultFilter{

	private TableDivisionStrategy strategy;
	
	public StandardTableDivisionDefaultFilter(TableDivisionStrategy strategy)
	{
		this.strategy = strategy;
	}
	
	@Override
	public void doFilter(FilterChain chain, SqlContext context) {
		
		
	}

	
}
