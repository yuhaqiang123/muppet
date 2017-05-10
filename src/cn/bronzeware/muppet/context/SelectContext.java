package cn.bronzeware.muppet.context;

import java.lang.reflect.Executable;
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

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.context.SqlExecuteLog.SqlContextLogMode;
import cn.bronzeware.muppet.converter.ObjectConvertor;
import cn.bronzeware.muppet.core.ThreadLocalTransaction;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.filters.FilterChain;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.TableInfo;
import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerate;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateException;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateHelper;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.util.ExceptionUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;

/**
 * 查询服务类，提供各种查询接口
 * 
 */
/**
 *
 */
public class SelectContext extends AbstractContext implements DefaultFilter {

	public SelectContext(Container<String, ResourceInfo> container, ApplicationContext applicationContext) {
		this.container = container;
		this.applicationContext = applicationContext;
		sqlGenerateHelper = new SqlGenerateHelper(container, applicationContext);
		log = new SqlExecuteLog(applicationContext, SqlContextLogMode.SELECT);
	}

	private Container<String, ResourceInfo> container;
	private SqlGenerateHelper sqlGenerateHelper;

	private ApplicationContext applicationContext;
	
	private SqlExecuteLog log ;	
	public <T> T executeOnPrimaryKey(Class<T> clazz, Object primaryKeyValue){
		TableInfo tableInfo = (TableInfo)container.get(clazz.getName());
		if(tableInfo == null){
			throw new ContextException("%s无法映射到数据库，请检查相关配置");
		}
		ColumnInfo primaryKeyColumnInfo = tableInfo.getPrimaryKey();
		if(primaryKeyColumnInfo == null){
			throw new ContextException(String.format("没找到匹配的主键，请检查%s 类的配置", clazz.getName()));
		}
		String primaryKeyName = primaryKeyColumnInfo.getName();
		List<T> list = this.execute(clazz, String.format(" %s = ?",primaryKeyName), new Object[]{primaryKeyValue});
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * <p>这个类可以处理单表的查询功能，调用方需要提供待查询表的实体类 通过实体类上的{@link @Table}注解，我们可以知道与之匹配的
	 * 表，进而生成sql语句中select中的部分，通过指定{@link @NotInTable}
	 * 注解我们可以将这个字段排除，这样这个字段就不会出现再select中了，同时，可以给我么提供了 这样一种方便，即实体类可以
	 * 拥有比单表更多的字段。可以不用受实际表的限制，但是相反 表中如果有比实体类多余的字段，实体类同样是无法存储的
	 * 
	 * @param object
	 *            对应实体类的对象 本应该是Clazz对象，但是为了和其他update，insert,delete
	 *            实现相同接口，使用object
	 * 
	 * @param wheres
	 *            select查询语句需要一个where字句，这个子句毫无疑问，需要用户来指定 例如: <br/>
	 *            username = ? and id > ? <br/>
	 *            用户需要提供类似的语句，这个语句相关位置需要用？代替，我们不会对语句进行校验 当数据库引擎执行时会返回异常结果 <br/>
	 * @param wherevalues
	 *            where相关子句的位置 需要填充值的数组例如上面的例子 我们需要像这样传入 new Object[]{"于海强",7}
	 * 
	 * @version 1.3.1 修改了wheresvalues查询为空的情况
	 * 
	 * 
	 * @author 于海强 2016-6-25 下午4:40:31
	 *
	 */
	public <T> List<T> execute(Class<T> clazz, String wheres, Object[] wherevalues) throws ContextException {
		return this.execute(clazz, wheres, null,wherevalues);
	}


	public <T> List<T> execute(Class<T> clazz, String wheres, String others,Object[] placeHolderValues) throws ContextException {
		Object object = ReflectUtil.getObject(clazz);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Transaction transaction = ThreadLocalTransaction.get();
			connection = transaction.getConnection();
			Sql sql = new Sql();
			sql.setWheres(wheres);
			sql.setWhereValues(placeHolderValues);
			sql.setOthers(others);
			/**
			 * 构造sql语句，需要select模式，
			 */
			sql = sqlGenerateHelper.execute(object, sql, SqlGenerate.SELECT);
			String sqlString = sql.getSql();

			/**
			 * 获取相关实体类的属性数组，
			 */
			//Field[] fields = sql.getObjectkeys();
			
			Map<Field , String> columnFields = sql.getColumnNames();

			/**
			 * 相关属性域与其值得map
			 */
			//Map<Field, Object> map = sql.getValues();

			ps = connection.prepareStatement(sqlString);
			/*
			 * System.out.println("Time:"+ new
			 * Date(System.currentTimeMillis()).toLocaleString()
			 * +"  "+sqlString);
			 */
			
			log.log(null, sql);
			
			if (placeHolderValues != null) {
				int i = 1;
				/**
				 * 预编译sql语句
				 */
				for (Object object2 : placeHolderValues) {
					ps.setObject(i, object2);
					i++;
				}
			}
			
			rs = ps.executeQuery();
			
			List list = new LinkedList();
			while (rs != null && rs.next()) {
				Map<Field, Object> newmap = new LinkedHashMap<Field, Object>(columnFields.size());
				for (Map.Entry<Field, String> fieldEntry:columnFields.entrySet()) {
					newmap.put(fieldEntry.getKey(), rs.getObject(fieldEntry.getValue()));
				}

				Object newObject = object.getClass().newInstance();
				ObjectConvertor.load(newmap, newObject);
				list.add(newObject);
			}
			// System.out.println(list.size());
			return list;
		} catch (SQLException e) {
			//
			throw new SqlGenerateContextException(e);
		} catch (ParamCanNotBeNullException e) {
			//
			throw new SqlGenerateContextException(e);
		} catch (InstantiationException e) {
			//
			throw new SqlGenerateContextException(e);
		} catch (IllegalAccessException e) {
			//
			throw new SqlGenerateContextException(e);
		} catch (SqlGenerateException e) {
			//
			throw new SqlGenerateContextException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				//
				throw new ContextException(e);
			}
		}
	}
	
	

	private void mappingResult(ResultSet rs, String[] keys, List<Map<String, Object>> result) {
		try {
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>(keys.length);
				for (String key : keys) {
					Object value = rs.getObject(key);
					map.put(key, value);
				}
				result.add(map);
			}
		} catch (SQLException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}

	private void mappingResult(ResultSet rs, String[] keys, Map<String, Object> result) {
		try {
			if (rs.next()) {
				for (int i = 0; i < keys.length; i++) {
					Object value = rs.getObject(keys[i]);
					result.put(keys[i], value);
				}
			}
		} catch (SQLException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}

	private <T> List<T> mappingResult(ResultSet rs, Map<String, Field> keys, Class<T> clazz) {
		try {
			List<T> list = new ArrayList<>(rs.getRow());
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>(keys.size());
				for (Map.Entry<String, Field> key : keys.entrySet()) {
					Object value = rs.getObject(key.getKey());
					map.put(key.getValue().getName(), value);//设置为对应field的值
				}
				T object = (T) ObjectConvertor.load(map, clazz);
				list.add(object);
			}
			return list;
		} catch (SQLException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}

	public static void main(String[] args) throws ContextException {

		/*
		 * SelectContext selectContext = new SelectContext(); User testEntity =
		 * new User();
		 * 
		 * String wheres = "id = ?"; List<User> list =
		 * selectContext.execute(testEntity, wheres, new Object[]{4});
		 * 
		 * String queryString = "select u.id as user_id" +
		 * ",u.username as username,u.password as password ,n.value,u.id" +
		 * " from t_user u,t_note n where u.id != n.user_id";
		 * 
		 * 
		 * 
		 * //List<Note> list = selectContext.execute(queryString, new
		 * Object[]{}, Note.class); List<Note> list2 = selectContext.execute(new
		 * Note(), "id > ?", new Object[]{1});
		 * 
		 * 
		 * System.out.println(list2.toString());
		 */
	}

	public <T> List<T> execute(String sql, Object[] values, Class<T> clazz){
		List<T> list = (List<T>)this.executeForObject(sql, values, clazz);
		return list;
	}
	
	public Map<String, Object> executeToMap(String sql, Object[] values)
	{
		return this.executeForObject(sql, values, Map.class);
	}
	
	public  List<Map<String, Object>> executeToList(String sql, Object[] values){
		return this.executeForObject(sql, values, List.class);
	}
	
	/**
	 * 
	 * 支持多表查询
	 */
	public <T> T executeForObject(String queryString, Object[] values, Class<T> clazz) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Transaction transaction = ThreadLocalTransaction.get();
			Connection connection = transaction.getConnection();
			
			log.log(null, queryString, values);
			ps = connection.prepareStatement(queryString);

			if (values != null) {
				int j = 1;
				for (Object object2 : values) {
					ps.setObject(j, object2);
					j++;
				}
			}

			rs = ps.executeQuery();

			// ResultSetMetaData metaData = rs.getMetaData();
			String[] querys = null;
			/*
			try {
				querys = mutiQuery(queryString);
			} catch (Exception e) {
				querys = getColumnNames(rs);
			}
			*/
			querys = getColumnNames(rs);
			
			
			
			if (List.class.isAssignableFrom(clazz)) {
				List<Map<String, Object>> list = new ArrayList<>(rs.getRow());
				mappingResult(rs, querys, list);
				return (T) list;
			} else if (Map.class.isAssignableFrom(clazz)) {
				Map<String, Object> map = new HashMap<>(querys.length);
				mappingResult(rs, querys, map);
				return (T) map;
			} else {
				Map<String, Field> queryKey2Field = new HashMap<>(querys.length);
				for(String query : querys){
					ColumnInfo columnInfo = (ColumnInfo) container.get(clazz.getName(), query);
					//Logger.debug("打印测试信息:" + clazz.getName() + query);
					if(columnInfo != null){
						queryKey2Field.put(query, columnInfo.getField());
					}else{
						//不做处理,说明此时查询出的结果集中有 实体类中无法映射的列
					}
				}
				
				List<T> list = (List<T>) mappingResult(rs, queryKey2Field, clazz);
				return (T) list;
			}

		} catch (SQLException e) {
			throw ExceptionUtil.getRuntimeException(e);
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				throw ExceptionUtil.getRuntimeException(e);
			}
		}

	}

	private String[] mutiQuery(String queryString) {

		int select_index = new StringBuffer(queryString.toUpperCase()).indexOf("SELECT");
		int from_index = new StringBuffer(queryString.toUpperCase()).indexOf("FROM");
		queryString = new StringBuffer(queryString).substring(select_index + 7, from_index);
		String[] reString = queryString.split(",");
		for (int i = 0; i < reString.length; i++) {
			String querykey = reString[i].trim();
			int as_index = new StringBuffer(querykey.toUpperCase()).indexOf(" AS ");
			if (as_index > 0) {
				querykey = new String(querykey.substring(as_index + 4));

			}
			querykey = querykey.trim();
			int dot_index = querykey.indexOf('.');
			if (dot_index > 0) {
				int space_index = querykey.indexOf(' ');

				if (space_index > -1) {
					querykey = new String(querykey.substring(dot_index + 1, space_index));
				} else {
					querykey = new String(querykey.substring(dot_index + 1));
				}
			}
			reString[i] = querykey;
		}
		return reString;
	}

	@Override
	public void doFilter(FilterChain chain, SqlContext context) {

	}

}
