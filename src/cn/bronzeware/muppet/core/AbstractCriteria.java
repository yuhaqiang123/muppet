package cn.bronzeware.muppet.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bronzeware.muppet.context.ContextException;
import cn.bronzeware.muppet.context.SelectContext;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.util.log.Logger;

abstract class AbstractCriteria<T> implements Criteria<T>{
	private Class<T> clazz;
	private Container<String,ResourceInfo> container;
	
	private String name;
	
	private ColumnInfo[] columnInfos;
	
	private ResourceInfo info;

	StringBuffer selectBuffer = new StringBuffer();
	
	StringBuffer buffer = new StringBuffer();
	List<Object> values = new ArrayList<>();
	
	private boolean isAnd = false;
	
	private boolean isOr = false;
	
	private boolean isCount = false;
	
	private boolean isOrderBy = false;
	private StringBuffer orderByBuffer = new StringBuffer();
	private List<Object> orderByValues = new ArrayList<>();
	
	private final String PLACEHOLDER = " ? ";
	
	private boolean isWheres = false;
	private List<Object> whereValues = new ArrayList<>();
	private StringBuffer whereBuffer = new StringBuffer();


	private boolean isLimit = false;

	private boolean isSelect = false;
	private long start = -1;
	private int offset = -1;
	private StringBuffer limit = null;
	private SelectContext context ;
	private StringBuffer otherSql;
	
	private void clearCriteria(){
		selectBuffer = new StringBuffer();
		buffer = new StringBuffer();
		values = new ArrayList<>();
		isAnd = false;
		isOr = false;
		isCount = false;
		isOrderBy = false;
		orderByBuffer = new StringBuffer();
		orderByValues = new ArrayList<>();
		
		otherSql = new StringBuffer();
		isWheres = false;
		whereValues = new ArrayList<>();
		whereBuffer = new StringBuffer();
		isLimit = false;
		isSelect = false;
		start = -1;
		offset = -1;
		limit = null;
	}
	
	public String getCriterias(){
		return whereBuffer.toString();
	}

	public AbstractCriteria(Container container,
	                        SelectContext selectContext
			, Class<T> clazz) throws RuntimeException{
		this.clazz = clazz;
		this.container = container;
		if(container.contains(clazz.getName())){
			info = this.container.get(clazz.getName());
			columnInfos = info.getColumns();
			name = info.getName();
		}else{
			throw new IllegalArgumentException("请指定正确的实体类型");
		}

		this.context = selectContext;
	}

	@Override
	public Criteria select(String string) {
		isSelect = true;
		selectBuffer = new StringBuffer(String.format(" select %s from %s", string, info.getName()));
		return this;
	}

	private void addAnd(){
		isWheres = true;
		if(isAnd == false){
			whereBuffer.append(" ");
			isAnd = true;
		}else{
			whereBuffer.append(" and ");
		}
	}
	
	private void addOr(){
		isWheres = true;
		whereBuffer.append(" or ");
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
		if(!(criteria instanceof AbstractCriteria)){
			throw new IllegalArgumentException(String.format("不支持的Criteria类型:%s", criteria.getClass().getName()));
		}
		
		AbstractCriteria abstractCriteria = (AbstractCriteria)criteria;
		String criteriaStrings = abstractCriteria.getCriterias();
		StringBuffer appendBuffer = new StringBuffer(criteriaStrings);
		addParenthesis(whereBuffer);
		addOr();
		addParenthesis(appendBuffer);
		whereBuffer.append(appendBuffer);
		
		whereValues.addAll(abstractCriteria.getWhereValues());
		return this;
	}
	@Override
	public Criteria order(String prop, boolean isAsc) {
		isOrderBy = true;
		orderByBuffer = new StringBuffer(" order by "+prop+" "+ (isAsc==true?"ASC":"DESC"));
		return this;
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
	
	private SqlAndParam buildSql(boolean select){
		if(isWheres==true){
			if(select){
				buffer.insert(0, " where ");
			}
			buffer.append(whereBuffer);
			values.addAll(whereValues);
		}
		otherSql = new StringBuffer();
		if(isOrderBy==true){
			otherSql.append(orderByBuffer);
			values.addAll(orderByValues);
		}
		
		if(isLimit == true){
			otherSql.append(limit);
		}
		String resultBuffer =  null;
		if(isSelect == false){
			resultBuffer = buffer.toString();
		}else{
			buffer.insert(0, selectBuffer.toString());
			resultBuffer = buffer.toString();
		}
		Object[] paramVlues = values.toArray();
		String otherSqlString = otherSql.toString();
		clearSql();
		return new SqlAndParam(resultBuffer.toString(), otherSqlString, paramVlues);
	}
	
	class SqlAndParam{
		public SqlAndParam(String sql, String otherSql, Object[] paramValues){
			this.whereSql = sql;
			this.otherSql = otherSql;
			this.paramValues = paramValues;
		}
		public String whereSql;
		public String otherSql;
		public Object[] paramValues;
	}
	
	public List<T> list(boolean clear){
		try {
			SqlAndParam sqlAndParam = buildSql(isSelect);
			String sql = sqlAndParam.whereSql;
			String otherSql = sqlAndParam.otherSql;

			Object[] paramsValues = sqlAndParam.paramValues;
			//System.err.println(otherSql);
			List<T> list = null;
			if(isSelect == true){
				sql += otherSql;
				list = this.context.execute(sql, paramsValues, clazz);
			}else{
				list = this.context.execute(clazz, sql, otherSql, paramsValues);
			}
			if(clear){
				clearCriteria();
			}
			return list;
		} catch (ContextException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public List<T> list(){
		return list(true);
	}
	
	private void clearSql(){
		buffer = new StringBuffer();
		otherSql = new StringBuffer();
		values.clear();
	}
	
	@Override
	public T one(){
		List<T> list = list(true);
		if(list != null && list.size() > 0 ){
			return list.get(0);
		}else{
			return null ;
		}
	}
	
	
	@Override
	public int count(){
		isCount = true;
		StringBuffer preSelectBuffer = selectBuffer;
		boolean preIsSelect = isSelect;
		selectBuffer = new StringBuffer(String.format("select count(*) from %s", info.getName()));
		isSelect = false;
		SqlAndParam sqlAndParam = buildSql(isSelect);
		String sql = sqlAndParam.whereSql;
		
		sql = selectBuffer.append(sql).append(otherSql).toString();
		Object[] paramValues = sqlAndParam.paramValues;
		Map<String, Object> map = this.context.executeToMap(sql, paramValues);
		selectBuffer = preSelectBuffer;
		isSelect = preIsSelect;
		return (int)(long)map.get("count(*)");
	}
	
	@Override
	public List<T> each(){
		EachArrayList<T> eachList = new EachArrayList<T>(this, count());
		return eachList;
	}
	
	public Criteria<T> limit(long start, int offset){
		limit = new StringBuffer(String.format(" limit %d, %d", start, offset));
		isLimit = true;
		return this;
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
	
}
