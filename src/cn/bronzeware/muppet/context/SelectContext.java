package cn.bronzeware.muppet.context;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;


import cn.bronzeware.muppet.converter.ObjectConvertor;
import cn.bronzeware.muppet.core.ThreadLocalTransaction;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.filters.FilterChain;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerate;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateException;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateHelper;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;


/**
 * 查询服务类，提供各种查询接口
 * 
 */
/**
 *
 */
public class SelectContext  extends AbstractContext implements DefaultFilter{
	
	
	public SelectContext(Container<String, ResourceInfo> container){
		this.container = container;
		sqlGenerateHelper = new SqlGenerateHelper(container);
	}
	private Container<String, ResourceInfo> container;
	private SqlGenerateHelper sqlGenerateHelper ;
	
	/**
	 * 这个类可以处理单表的查询功能，调用方需要提供待查询表的实体类
	 * 通过实体类上的{@link @Table}注解，我们可以知道与之匹配的
	 * 表，进而生成sql语句中select中的部分，通过指定{@link @NotInTable}
	 * 注解我们可以将这个字段排除，这样这个字段就不会出现再select中了，同时，可以给我么提供了
	 * 这样一种方便，即实体类可以 拥有比单表更多的字段。可以不用受实际表的限制，但是相反
	 * 表中如果有比实体类多余的字段，实体类同样是无法存储的
	 * 
	 * @param object 对应实体类的对象 本应该是Clazz对象，但是为了和其他update，insert,delete
	 * 实现相同接口，使用object
	 * 
	 * @param wheres select查询语句需要一个where字句，这个子句毫无疑问，需要用户来指定 例如:
	 * <br/>
	 *  username = ? and id > ?
	 *  <br/>用户需要提供类似的语句，这个语句相关位置需要用？代替，我们不会对语句进行校验
	 *  当数据库引擎执行时会返回异常结果
	 *  <br/>
	 *  @param wherevalues where相关子句的位置 需要填充值的数组例如上面的例子
	 *  我们需要像这样传入 new Object[]{"于海强",7}
	 *  
	 * @version 1.3.1 修改了wheresvalues查询为空的情况
	 * 
	 * 
	 *@author 于海强
	 *2016-6-25  下午4:40:31
	 *
	 */
	public <T> List<T> execute(Class<T> clazz
			,String wheres 
			,Object[] wherevalues)
	throws ContextException
	{
		Object object = ReflectUtil.getObject(clazz);
	
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Transaction transaction = ThreadLocalTransaction.get();
			connection = transaction.getConnection();
			Sql sql = new Sql();
			sql.setWheres(wheres);
			
			/**
			 * 构造sql语句，需要select模式，
			 */
			sql = sqlGenerateHelper.execute(object,sql, SqlGenerate.SELECT);
			String sqlString = sql.getSql();
			
			/**
			 * 获取相关实体类的属性数组，
			 */
			Field[] fields = sql.getObjectkeys();
			
			/**
			 * 相关属性域与其值得map
			 */
			Map<Field, Object> map = sql.getValues();
			
			ps = connection.prepareStatement(sqlString);
		/*	System.out.println("Time:"+
					new Date(System.currentTimeMillis()).toLocaleString()
					+"  "+sqlString);*/
			Logger.println(sqlString);
			if(wherevalues!=null){
				
				
				int i = 1; 
				
				/**
				 * 预编译sql语句
				 */
				
				for(Object object2:wherevalues){
					ps.setObject(i, object2);
					i++;
					//System.out.println(object2);
				}
				
			}
			
			 rs = ps.executeQuery();
		
			/*List<Map<Field,Object>> resultList = new LinkedList<Map<Field,Object>>();*/
			List list = new LinkedList();
			while (rs!=null&&rs.next()) {
				Map<Field,Object> newmap = new LinkedHashMap<Field, Object>(fields.length);
				for(Field field:fields){
					newmap.put(field,rs.getObject(field.getName()));
				}
				
				Object newObject = object.getClass().newInstance();
				ObjectConvertor.load(newmap,newObject);
				list.add(newObject);
			}
			//System.out.println(list.size());
			return list;
		} catch (SQLException e) {
			// 
			throw new SqlGenerateContextException(e.getMessage());
		} catch (ParamCanNotBeNullException e) {
			// 
			throw new SqlGenerateContextException(e.getMessage());
		} catch (InstantiationException e) {
			// 
			throw new SqlGenerateContextException(e.getMessage());
		} catch (IllegalAccessException e) {
			// 
			throw new SqlGenerateContextException(e.getMessage());
		} catch (SqlGenerateException e) {
			// 
			throw new SqlGenerateContextException(e.getMessage());
		}
		finally{
			try {
				if(rs!=null){
					rs.close();
				}
				
				if(ps!=null){
					ps.close();
				}
				
				/*if(connection!=null){
					connection.close();
				}*/
				
			} catch (SQLException e) {
				// 
				e.printStackTrace();
			}
			
			
			
		}
	}
	public static void main(String[] args) throws ContextException {

		/*SelectContext selectContext = new SelectContext();
		User testEntity = new User();
		
		String wheres = "id = ?";
		List<User> list = 
				selectContext.execute(testEntity, wheres, new Object[]{4});
		
		String  queryString = "select u.id as user_id" +
				",u.username as username,u.password as password ,n.value,u.id" +
				" from t_user u,t_note n where u.id != n.user_id";
		
		
		
		//List<Note> list = selectContext.execute(queryString, new Object[]{}, Note.class);
		List<Note> list2 = selectContext.execute(new Note(), "id > ?", new Object[]{1});
		
		
		System.out.println(list2.toString());*/
	}
	
	/**
	 * 
	 * 支持多表查询
	 */
	public <T> List<T> execute(String queryString,Object[] values,Class<T> clazz){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<T> list = null;
		try {
			Transaction transaction = ThreadLocalTransaction.get();
			Connection connection = transaction.getConnection();
			System.out.println(queryString);
			////////
			ps = connection.prepareStatement(queryString);
			
			if(values!=null){
				int j =1;
				for(Object object2:values){
					ps.setObject(j, object2);
					j++;
				}
			}
			
			
			rs = ps.executeQuery();
			
			
			//ResultSetMetaData metaData = rs.getMetaData();
			String[] querys = null;
			try{
				querys = mutiQuery(queryString);
			}catch(Exception e){
				querys = getColumnNames(rs);
			}
			
			list = new ArrayList(rs.getRow());
			while(rs.next()){
				Map<String,Object> map  = new HashMap<String, Object>(querys.length);
				for(int i = 0;i< querys.length;i++){
					Object value = rs.getObject(querys[i]);
					map.put(querys[i], value);
				}
				T object = (T) ObjectConvertor.load(map, clazz);
				list.add(object);
			}
			
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		}
		finally{
			try {
				
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
				
			} catch (SQLException e) {
				// 
				e.printStackTrace();
			}
		}
		return list;
		
	}
	
	
	private String[] mutiQuery(String queryString){
		
		int select_index = new StringBuffer(queryString.toUpperCase()).indexOf("SELECT");
		int from_index = new StringBuffer(queryString.toUpperCase()).indexOf("FROM");
		queryString = new StringBuffer(queryString).substring(select_index+7, from_index);
		String[] reString = queryString.split(",");
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		for(int i = 0;i<reString.length;i++){
			String querykey = reString[i].trim();
			int as_index = new StringBuffer(querykey.toUpperCase()).indexOf(" AS ");
			if(as_index>0){
				querykey = new String(querykey.substring(as_index + 4));
				
			}
			querykey = querykey.trim();
			int dot_index = querykey.indexOf('.');
			if(dot_index>0){
				int space_index = querykey.indexOf(' ');
				
				if(space_index>-1){
					querykey = new String(querykey.substring(dot_index + 1,space_index));
				}else{
					querykey = new String(querykey.substring(dot_index + 1));
				}
			}
			reString[i] = querykey;
			System.out.println(querykey);
		}
		return reString;
	}
	@Override
	public void doFilter(FilterChain chain, SqlContext context) {
		
		
	}

	
	
	
}
