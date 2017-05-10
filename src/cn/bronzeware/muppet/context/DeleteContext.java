package cn.bronzeware.muppet.context;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.context.SqlExecuteLog.SqlContextLogMode;
import cn.bronzeware.muppet.converter.ObjectConvertor;
import cn.bronzeware.muppet.core.ThreadLocalTransaction;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.entities.User;
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
import cn.bronzeware.muppet.util.log.Logger;

/**
 * 删除操作支持<br/>
 * 目前提供单表普通删除.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;按照主键删除 <br/>
 * 
 * 
 * @author yuhaiqiang  yuhaiqiangvip@sina.com
 * @time 2017年5月3日 下午5:24:25
 */
public class DeleteContext extends AbstractContext {

	public DeleteContext(Container<String, ResourceInfo> container, ApplicationContext applicationContext) {
		this.container = container;
		this.sqlGenerateHelper = new SqlGenerateHelper(container, applicationContext);
		log = new SqlExecuteLog(applicationContext, SqlExecuteLog.SqlContextLogMode.DELETE);
	}

	/**
	 * sql 执行记录器,要执行的sql都将托给SqlExecuteLog记录
	 */
	private SqlExecuteLog log ;
	
	/**
	 * Container管理.Container通过ClassName为key,查找对应实体bean
	 */
	private Container<String, ResourceInfo> container;
	
	/**
	 * SqlGenerateHelper 负责生成sql语句.
	 */
	private SqlGenerateHelper sqlGenerateHelper;
	
	/**
	 * 应用上下文,类库所有组件都托管给ApplicationContext管理
	 */
	private ApplicationContext applicationContext;

	/**
	 * 
	 * @param clazz  删除该参数类型 对应table的记录
	 * @param primaryKey 
	 * @return
	 * @throws ContextException
	 */
	public Object executeByPrimaryKey(Class clazz, Object primaryKey) throws ContextException {
		TableInfo tableInfo = (TableInfo) container.get(clazz.getName());
		ColumnInfo columnInfo = tableInfo.getPrimaryKey();
		String primaryKeyName = columnInfo.getName();
		String wheres = String.format(" %s = ? ", primaryKeyName);

		return this.execute(clazz, wheres.toString(), new Object[] { primaryKey });

	}

	private Sql getSql(Object object, Sql sql, int sqlGenerate)
			throws ParamCanNotBeNullException, SqlGenerateException {
		if (object instanceof Class) {
			sql = sqlGenerateHelper.executeClass((Class) object, sql, sqlGenerate);
		} else {
			sql = sqlGenerateHelper.execute(object, sql, SqlGenerate.DELETE);
		}
		return sql;
	}

	public Object execute(Class clazz, String wheres, Object[] whereValues) {
		return this.execute((Object)clazz, wheres, whereValues);
	}

	public Object execute(Object object, String wheres, Object[] wherevalues) throws ContextException {

		Connection connection = null;
		PreparedStatement ps = null;
		try {
			Transaction transaction = ThreadLocalTransaction.get();
			connection = transaction.getConnection();
			Sql sql = new Sql();
			sql.setWheres(wheres);
			sql.setWhereValues(wherevalues);
			sql = getSql(object, sql, SqlGenerate.DELETE);

			String sqlString = sql.getSql();
			Map<Field, Object> map = sql.getValues();

			ps = connection.prepareStatement(sqlString);
			
			log.log(null, sql);
			
			int i = 1;

			for (Object object2 : wherevalues) {
				ps.setObject(i, object2);
				i++;
			}

			int success = ps.executeUpdate();
			return success > 0 ? true : false;
		} catch (SQLException e) {
			throw new ContextException(e);
		} catch (ParamCanNotBeNullException e) {
			throw new ContextException(e);
		} catch (SqlGenerateException e) {
			throw new ContextException(e);
		} finally {
			try {

				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				throw new ContextException(e);
			}
		}

	}
}
