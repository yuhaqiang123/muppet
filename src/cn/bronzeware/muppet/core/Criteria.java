package cn.bronzeware.muppet.core;

import java.util.List;

public interface Criteria<T> {

	public T one();
	
	public int count();
	
	public List<T> list();
	
	public List<T> each();
	
	public Criteria<T> limit(long start,int offset);
	
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

	Criteria select(String string);

	//Criteria each();

	//Criteria each(int size);

	static final int EACH_SIZE = 100;
	
	//Criteria page();
	
	public static final boolean ASC = true;
	
	public static final boolean DESC = false;
	
}
