package cn.bronzeware.muppet.sqlgenerate;

import java.lang.reflect.Field;
import java.util.Map;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.annotations.AnnotationException;
import cn.bronzeware.muppet.annotations.RowRecord;
import cn.bronzeware.muppet.core.RowsRecordKeeper;
import cn.bronzeware.muppet.core.StandardRowRecordKeeper;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;

/**
 * sqlutil是向context提供的除了sql，另外一个接口，在这个接口中
 * 提供了一个execute方法 ，这个方法用于生成{@link Sql}对象
 * 目前{@link SqlGenerate}提供了四种模式 
 * <br>
 * <h1>1. INSERT</h1>  单表的增加操作
 * <h1>2. UPDATE</h1>  单表的更新操作
 * <h1>3. DELETE</h1>  删除操作
 * <h1>4.SELECT</h1>   单表的查询
 * 
 */
public class SqlGenerateHelper {

	private Container<String, ResourceInfo> container;
	
	private ApplicationContext applicationContext;
	
	public SqlGenerateHelper(Container<String, ResourceInfo> container,ApplicationContext applicationContext){
		this.container = container;
		this.applicationContext = applicationContext;
		this.rowsRecordKeeper = new StandardRowRecordKeeper(container, applicationContext);
	}
	
	private static SqlGenerate getSqlGenerate(int mode){
		SqlGenerate sqlGenerate = null;
		switch (mode) {
		case SqlGenerate.INSERT:
			sqlGenerate = new InsertSQlGenerate();
			break;
		case SqlGenerate.UPDATE:
			sqlGenerate = new UpdateSqlGenerate();
			break;
		case SqlGenerate.DELETE:
			sqlGenerate = new DeleteSqlGenerate();
			break;
		case SqlGenerate.SELECT:
			sqlGenerate = new SelectSqlGenerate();
	
			break;

		default:
			break;
		}
		
		return sqlGenerate;
	}
	private  RowsRecordKeeper rowsRecordKeeper ;
	public  Sql execute(Object object,Sql sql,int mode) 
			throws ParamCanNotBeNullException
			,SqlGenerateException{
		if(sql==null){
			sql = new Sql();
		}
		
		/**
		 * 获取sql生成器
		 */
		SqlGenerate sqlGenerate = getSqlGenerate(mode);
		
		if(sqlGenerate==null){
			throw new IllegalArgumentException("mode非法");
		}
		
		/**
		 * RecordInfo 是 实体注解处理器与 SqlGenerate直接交互的桥梁
		 */
		RowRecord info = null;
		try {
			info = rowsRecordKeeper.execute(object);
		} catch (AnnotationException e1) {
			// 
		   throw new SqlGenerateException(e1.getMessage());
		}
		sql.setTableName(info.getTableName());
		sql.setPrimarykey(info.getPrimarykey());
		sql.setPrimaryKeyName(info.getPrimaryKeyName());
		Map<Field, Object> map = info.getMap();
		sql.setObjectkeys(map.keySet().toArray(new Field[map.size()]));
		sql.setValues(map);
		sql.setColumnNames(info.getColumnNames());
		
		//sql.setValues(info.getMap());
		String sqlString = null;
		try {
			sqlString = sqlGenerate.getSql(sql);
			sql.setSql(sqlString);
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
		return sql;
	}

}
