package cn.bronzeware.muppet.core;

import java.util.List;

public interface Criteria<T> {

	
	
	public List<T> list();
	
	/*Criteria createCriteria(Class<T> clazz);*/
	
	Criteria andPropEqual(String prop,Object value);
	
	Criteria andPropLike(String prop,Object value);
	
	Criteria andPropNotEqual(String prop,Object value);
	
	Criteria or(Criteria criteria);
	
	Criteria order(String prop,boolean isAsc);
	
	Criteria andPropLess(String prop,Object value);
	
	Criteria andPropGreater(String prop,Object value);
	
	Criteria andPropLessEq(String prop,Object value);
	
	Criteria andPropGreaterEq(String prop,Object value);
	
	
	
	
	
	//Criteria page();
	
	public static final boolean ASC = true;
	
	public static final boolean DESC = false;
	
}
