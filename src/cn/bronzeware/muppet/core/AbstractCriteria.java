package cn.bronzeware.muppet.core;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import cn.bronzeware.muppet.context.ContextException;
import cn.bronzeware.muppet.context.SelectContext;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;

abstract class AbstractCriteria<T> implements Criteria<T>{

	
	//private Class<T> clazz;

	private Class<T> clazz;
	private Container<String,ResourceInfo> container;
	
	private String name;
	
	private ColumnInfo[] columnInfos;
	
	private ResourceInfo info;

	StringBuffer selectBuffer = new StringBuffer();
	
	StringBuffer buffer = new StringBuffer();
	List<Object> values = new ArrayList<>();
	
	private boolean isAnd = true;
	
	private boolean isOr = true;
	
	
	
	private boolean isOrderBy = true;
	private StringBuffer orderByBuffer = new StringBuffer();
	private List<Object> orderByValues = new ArrayList<>();
	
	private final String PLACEHOLDER = " ? ";
	
	private boolean isWheres = true;
	private List<Object> whereValues = new ArrayList<>();
	private StringBuffer whereBuffer = new StringBuffer();
	
	


	private SelectContext context ;
	
	
	/*public String getCriterias(){
		
		
		return whereBuffer.toString();
	}*/
	
	public String getCriterias(){
		
		return whereBuffer.toString();
		
	}
	
	
	public AbstractCriteria(Container container,
			SelectContext selectContext
			,Class<T> clazz) throws RuntimeException{
		this.clazz = clazz;
		/*String name = ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0].toString();
		
		System.out.println(name);*/
		this.container = container;
		if(container.contains(clazz.getName())){
			info = this.container.get(clazz.getName());
			columnInfos = info.getColumns();
			name = info.getName();
		}else{
			throw new IllegalArgumentException("请指定正确的实体类型");
		}
		
		selectBuffer.append(" select * from "+name);
		selectBuffer.append(" where ");
		
		this.context = selectContext;
	}

	

	private void addAnd(){
		
		if(isWheres==true){
			//buffer.append(" where ");
			isWheres = false;
		}
		
			if(isAnd==true){
				whereBuffer.append(" ");
				isAnd = false;
			}else{
				whereBuffer.append(" and ");
			}
		
	}
	
	
	
	
	private void addOr(){
		if(isWheres==true){
			//buffer.append(" where ");
			isWheres = false;
		}

		whereBuffer.append(" or ");
		isOr = false;
			/*if(isOr==true){
				buffer.append(" ");
				isOr = false;
			}else{
				buffer.append(" or ");
			}*/
	
	}
	
	
	@Override
	public Criteria andPropEqual(String prop, Object value) {
		
		addAnd();
		whereBuffer.append(" "+prop+" = "+PLACEHOLDER);
		whereValues.add(value);
		
		return this;
	}

	@Override
	public Criteria andPropLike(String prop, Object value) {
		
		addAnd();
		whereBuffer.append(" "+ prop +" like "+PLACEHOLDER);
		whereValues.add(value);
		return this;
	}

	@Override
	public Criteria andPropNotEqual(String prop, Object value) {
		
		addAnd();
		whereBuffer.append(" "+ prop+" <> "+PLACEHOLDER);
		whereValues.add(value);
		return this;
	}

	/*public static void main(String[] args){
		StringBuffer buffer = new StringBuffer();
		buffer.append("fhiewhf");
		buffer.insert(0, "(");
		buffer.insert(buffer.length(), ")");
		System.out.println(buffer.toString());
	}*/
	private void addParenthesis(StringBuffer buffer){
		buffer.insert(0, "(");
		buffer.insert(buffer.length(),")");
	}
	
	
	@Override
	public Criteria or(Criteria criteria) {
		
		
		AbstractCriteria abstractCriteria = (AbstractCriteria)criteria;
		String criteriaStrings = abstractCriteria.getCriterias();
		addParenthesis(whereBuffer);
		addOr();
		StringBuffer appendBuffer = new StringBuffer(criteriaStrings);
		addParenthesis(appendBuffer);
		
		whereBuffer.append(appendBuffer);
		
		whereValues.addAll(abstractCriteria.getWhereValues());
		return this;
	}
	
	
	

	@Override
	public Criteria order(String prop, boolean isAsc) {
		if(isOrderBy==true){
			orderByBuffer.append(" order by ");
			isOrderBy = false;
		}else{
			orderByBuffer.append(" ,");
		}
		
		orderByBuffer.append(" "+prop+" "+ (isAsc==true?"ASC":"DESC"));
		
		return this;
	}

	public boolean isAnd() {
		return isAnd;
	}


	public void setAnd(boolean isAnd) {
		this.isAnd = isAnd;
	}


	public boolean isOr() {
		return isOr;
	}


	public void setOr(boolean isOr) {
		this.isOr = isOr;
	}


	public boolean isWheres() {
		return isWheres;
	}


	public void setWheres(boolean isWheres) {
		this.isWheres = isWheres;
	}


	public boolean isOrderBy() {
		return isOrderBy;
	}


	public void setOrderBy(boolean isOrderBy) {
		this.isOrderBy = isOrderBy;
	}


	@Override
	public Criteria andPropLess(String prop, Object value) {
		
		addAnd();
		whereBuffer.append(" "+prop+" < " +PLACEHOLDER);
		whereValues.add(value);
		return this;
	}

	@Override
	public Criteria andPropGreater(String prop, Object value) {
		addAnd();
		whereBuffer.append(" " +prop+" > "+PLACEHOLDER);
		whereValues.add(value);
		return this;
	}

	@Override
	public Criteria andPropLessEq(String prop, Object value) {
		addAnd();
		whereBuffer.append(" "+ prop+" <= "+PLACEHOLDER);
		whereValues.add(value);
		return this;
	}

	@Override
	public Criteria andPropGreaterEq(String prop, Object value) {
		addAnd();
		whereBuffer.append(" "+prop+" >= "+PLACEHOLDER);
		whereValues.add(value);
		return this;
	}
	
	public List<T> list(){
		
		try {
			if(isWheres==false){
				buffer.insert(0, " where ");
				buffer.append(whereBuffer);
				values.addAll(whereValues);
				//values.add
			}
			if(isOrderBy==false){
				buffer.append(orderByValues);
				values.addAll(orderByValues);
			}
			
			//System.out.println(values.toArray()[1]);
			return this.context.execute(clazz, buffer.toString(), values.toArray());
		} catch (ContextException e) {
			
		}
		throw new RuntimeException("未捕获的异常");
	}



	public StringBuffer getOrderByBuffer() {
		return orderByBuffer;
	}



	public void setOrderByBuffer(StringBuffer orderByBuffer) {
		this.orderByBuffer = orderByBuffer;
	}



	public List<Object> getOrderByValues() {
		return orderByValues;
	}



	public void setOrderByValues(List<Object> orderByValues) {
		this.orderByValues = orderByValues;
	}



	public List<Object> getWhereValues() {
		return whereValues;
	}



	public void setWhereValues(List<Object> whereValues) {
		this.whereValues = whereValues;
	}



	public StringBuffer getWhereBuffer() {
		return whereBuffer;
	}



	public void setWhereBuffer(StringBuffer whereBuffer) {
		this.whereBuffer = whereBuffer;
	}

}
