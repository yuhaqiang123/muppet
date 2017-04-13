package cn.bronzeware.muppet.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.View;
import javax.swing.text.StyledEditorKit.BoldAction;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.resource.ColumnInfo;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.TableInfo;
import cn.bronzeware.muppet.sql.SqlMetaData;
import cn.bronzeware.muppet.sql.SqlType;
import cn.bronzeware.muppet.sqlgenerate.AlterAddColumnGenerate;
import cn.bronzeware.muppet.sqlgenerate.AlterColumnGenerate;
import cn.bronzeware.muppet.sqlgenerate.AlterColumnPrimaryKeyGenerate;
import cn.bronzeware.muppet.sqlgenerate.Generate;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateException;
import cn.bronzeware.muppet.sqlgenerate.TableGenerate;
import cn.bronzeware.muppet.util.CloseUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.muppet.util.log.Msg;

/**
 * 根据用户输入构建sql语句，需要说明的是构造一个StandardResourceBuilder的时候
 * 需要指定一个检查器.这个检查器代理了和数据库的所有检查细节,ResourceBuilder只需要
 * 访问检查器提供的接口既可以获取指定的表，列的情况。然后根据实际的情况，Builder进行相应的语句
 * 构建
 * 
 * 2016年8月18日 下午2:29:00
 * @author 于海强
 *
 */
public class StandardResourceBuilder {
	
	
	protected ApplicationContext applicationContext;
	
	/**
	 * 
	 * @param check {@link DataBaseCheck} 类实例，负责查询数据库
	 */
	public StandardResourceBuilder(ApplicationContext context) {
		this.applicationContext = context;
		tableGenerate = new TableGenerate(applicationContext);
		this.dataBaseCheck = context.getBean(DataBaseCheck.class);
		this.dataSourceUtil = context.getBean(DataSourceManager.class).getDefaultDataSource();
	}
	
	public StandardResourceBuilder(ApplicationContext context
			,DataBaseCheck dataBaseCheck
			,DataSourceUtil dataSourceUtil) {
		this.applicationContext = context;
		tableGenerate = new TableGenerate(applicationContext);
		this.dataBaseCheck = dataBaseCheck;
		this.dataSourceUtil = dataSourceUtil;
	}
	
	
	private DataBaseCheck dataBaseCheck ;
	private TableGenerate tableGenerate = null;
	
	private DataSourceUtil dataSourceUtil;
	/**
	 * 根据用户的输入资源结构进行构建，首先检查是否是TableInfo的实例
	 * 目前仅支持table的构建
	 * @param info
	 * @return  成功构建返回true，否则返回false
	 * @throws BuildException
	 */
	public boolean build(ResourceInfo info) throws BuildException{
		
		if(info!=null && info instanceof TableInfo){
			return buildTable((TableInfo)info);
		}
		
		return false;
		
	}
	
	/**
	 * 构造表
	 * 1.首先检查表是否存在，如果存在则将控制权转发到buildTableExist 方法
	 * 2.如果表不存在，那么则将直接生成一个全新的表 将控制权转移到 buildTableSql方法
	 * @param info
	 * @return
	 * @throws BuildException
	 */
	private boolean buildTable(TableInfo info) throws BuildException{
		DataBaseCheck.TableCheck tableCheck = 
				dataBaseCheck.createTableCheck(info.getTableName());
		if(tableCheck!=null){
			if(tableCheck.isExist()){
				Logger.println(info.getTableName()+Msg.TABLE_EXIST);
				return buildTableExist(info);
			}
			else{
				return buildTableSql(info);
			}
		}
		return false;
	}
	
	/*
	 * 如果表 存在那么 需要检查表中的每一列，是否相同
	 * 如果 相同那么则 转入检查下一列
	 * 如果不同那么则 直接生成修改该列的 dml sql语句
	 * 
	 * 
	 */
	private boolean buildTableExist(TableInfo info) throws BuildException{
		ColumnInfo[] columnInfos = info.getColumns();
		boolean flag = true;
		if(columnInfos!=null){
			for(ColumnInfo columnInfo:columnInfos){
				DataBaseCheck.ColumnCheck columnCheck = 
						dataBaseCheck.createColumnCheck(columnInfo.getTableName(),
								columnInfo.getName());

				if (columnCheck.isExist()) {
					Logger.println(columnInfo.getTableName() + "表中的" + columnInfo.getName() + "列" + Msg.COLUMN_EXIST);
					if (!columnCheck.isEquals(columnInfo)) {
						try {

							boolean success = buildColumnSql(columnCheck, columnInfo);
							if (success) {
								Logger.println("修改" + columnInfo.getTableName() + "表中的" + columnInfo.getName() + "成功");
							} else {
								Logger.println("修改" + columnInfo.getTableName() + "表中的" + columnInfo.getName() + "失败");
								flag = false;
							}

						} catch (InitException e) {
							e.printStackTrace();
							flag = false;
							Logger.println("修改" + columnInfo.getTableName() + "表中的" + columnInfo.getName() + "失败");
						}
					}
					else{
						/**
						 * 如果相等，打印什么信息
						 */
						Logger.println(columnInfo.getTableName()+" 表中的"+columnInfo.getName()
										+Msg.COLUMN_EQUALS);
					}
					
				}
				else{
					/**
					 * 如果不存在怎么办
					 */
					Logger.println(columnInfo.getTableName() + "表中的" + columnInfo.getName() + "列" + Msg.COLUMN_UNEXIST);
					try{
						boolean success = buildUnExistColumn(columnCheck, columnInfo);
						if (success) {
							Logger.println("添加" + columnInfo.getTableName() + "表中的" + columnInfo.getName() + "成功");
						} else {
							Logger.println("添加" + columnInfo.getTableName() + "表中的" + columnInfo.getName() + "失败");
							flag = false;
						}
					}catch(InitException e){
						e.printStackTrace();
						flag = false;
						Logger.println("添加" + columnInfo.getTableName() + "表中的" + columnInfo.getName() + "失败");
					}
				}
			}
		}
		return flag;
	}
	
	private boolean executeSql(Map<String, String> map) throws BuildException{
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = this.dataSourceUtil.getConnection();
			for(Map.Entry<String, String> entry:map.entrySet()){
				if(!entry.getValue().equals("")){
					ps = connection.prepareStatement(entry.getValue());
					ps.execute();
					CloseUtil.close(ps);
					Logger.println(entry.getKey());
				}
				
			}
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.println("修改表文件出现异常");
			throw new BuildException("修改表文件出现异常\n"+e.getMessage());
		}
		finally {
			CloseUtil.close(connection);
			CloseUtil.close(ps);
		}
	}
	/**
	 * 像一个已经存在的表中新建列sql
	 * @param columnCheck
	 * @param info
	 * @return
	 * @throws BuildException
	 */
	private boolean buildUnExistColumn(
			DataBaseCheck.ColumnCheck columnCheck
			,ColumnInfo info) throws BuildException{
	
		
		Generate generate = new AlterAddColumnGenerate();
		String alterColumnDefault = "";
		try {
			alterColumnDefault = generate.generate(info);
			Logger.println(Msg.ADD_COLUMN+"添加的Sql语句:"+alterColumnDefault);
		} catch (SqlGenerateException e) {
			throw new BuildException(e.getMessage());
		}
	
	
		Map<String,String> map = new HashMap<String,String>(2);
		map.put(Msg.ADD_COLUMN+"成功！", alterColumnDefault );
		boolean result = executeSql(map);
		return result;
	}
	
	/**
	 * 构建  存在的列  修改后的sql语句，并执行
	 * @param columnCheck 列检查器
	 * @param info 列数据
	 * @return 
	 * @throws BuildException
	 */
	private boolean buildColumnSql(
			DataBaseCheck.ColumnCheck columnCheck
			,ColumnInfo info) throws BuildException{
	
			boolean isNullable = columnCheck.isNullable();
			boolean isPrimaryKey = columnCheck.isPrimaryKey();
			boolean isUnique = columnCheck.isUnique();
			boolean isIndex = columnCheck.isIndex();
			//columnCheck.isForeignKey();
			String defaultValue = columnCheck.getDefaultValue();
			int length = columnCheck.getLength();
			SqlType sqlType = columnCheck.getSqlType();
			
			
			
			Generate generate = new AlterColumnGenerate();
			String alterColumnDefault = "";
			try {
				alterColumnDefault = generate.generate(info);
				Logger.println(Msg.ALTER_COLUMN+"修改的Sql语句:"+alterColumnDefault);
			} catch (SqlGenerateException e) {
				e.printStackTrace();
			}
		
			String primaryKeySql = "";
			if(isPrimaryKey!=info.isIsprivarykey()){
				Generate primaryKyGenerate = new AlterColumnPrimaryKeyGenerate();
				try {
					primaryKeySql = primaryKyGenerate.generate(info);
					if(info.isIsprivarykey()==true){
						Logger.println(info.getName()+Msg.ADD_PRIMARYKEY);
					}else{
						Logger.println(info.getName()+Msg.DELETE_PRIMARYKEY);
					}
					Logger.println(Msg.ALTER_PRIMARYKEY+"修改的Sql语句:"+primaryKeySql);
				} catch (SqlGenerateException e) {
				
				}
			}
			Map<String,String> map = new HashMap<String,String>(2);
			map.put(Msg.ALTER_PRIMARYKEY+"成功", primaryKeySql);
			map.put(Msg.ALTER_COLUMN+"成功！", alterColumnDefault );
			boolean result = executeSql(map);
		return result;
		
	}
	
	/**
	 * 新建表sql语句，并执行
	 * @param info
	 * @return
	 * @throws InitException
	 */
	private boolean buildTableSql(TableInfo info) throws InitException{
		try {
			tableGenerate.generate(info);
		} catch (SqlGenerateException e) {
			throw new BuildException("构建Sql语句时出错->\n"+e.getMessage());
		}
			Connection connection = null;
			PreparedStatement ps = null;
			try {
				connection = dataSourceUtil.getConnection();
				Logger.println(Msg.CREATE_TABLE+":"+info.getTableName()
				+"\n"+
						info.getSql()
							.replace(",", "\n,"));
				ps = connection.prepareStatement(info.getSql());
				return ps.execute();
				
			} catch (SQLException e) {
				throw new BuildException(e.getMessage());
			}finally {
				CloseUtil.close(ps);
				CloseUtil.close(connection);
			}
			
		
	}
	
	
	/**
	 * @deprecated
	 * @param info
	 * @return
	 * @throws InitException
	 */
	public boolean resourceBuild(ResourceInfo info) throws InitException{
		
		Connection connection;
		TableInfo tableInfo = null;
		if(info!=null && info instanceof TableInfo){
			tableInfo = (TableInfo) info;
		}
		try {
			connection = dataSourceUtil.getConnection();
			DatabaseMetaData database = connection.getMetaData();
			
			//PreparedStatement ps = connection.prepareStatement(info.getSql());
			//boolean success = ps.execute();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new ResourceBuildException(e.getMessage());
		}
	}
	
	public static void main(String[] args) {

	}

}
