package cn.bronzeware.muppet.context;

import com.sun.swing.internal.plaf.basic.resources.basic;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class SqlExecuteLog {

	public enum SqlContextLogMode{
		SELECT,
		INSERT,
		DELETE,
		UPDATE
	};
	
	private boolean print = false;
	
	private SqlContextLogMode mode;
	
	private ApplicationContext applicationContext;
	
	public SqlExecuteLog(ApplicationContext applicationContext, SqlContextLogMode mode){
		this.mode = mode;
		this.applicationContext = applicationContext;
		printOnMode(mode);
	}
	
	private void printOnMode(SqlContextLogMode mode){
		switch(mode){
		case SELECT:
			print = true;
			break;
		case INSERT:
			print = true;
			break;
		case UPDATE:
			print = true;
			break;
		case DELETE:
			print = true;
			break;
		default:
			print = false;
		}
	}
	
	public void log(String msg, Sql sql){
		log(msg, sql.getSql(), sql.getWhereValues());
	}
	
	public void log(String msg, String sqlString, Object[] values ){
		if(print){
			if(msg != null){
				Logger.println(msg);
			}
			Logger.println("\toperate start");
			Logger.println(String.format("\t\t SQL: %s", sqlString == null ? "":sqlString));
			Logger.println(String.format("\t\t 相关值：%s", values == null ? "" : ArrayUtil.getValues(values, ",")));
			Logger.println("\toperation end");
		}
	}
	
	
}
