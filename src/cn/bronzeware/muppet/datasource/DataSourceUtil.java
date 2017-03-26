package cn.bronzeware.muppet.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.transform.Source;

import org.apache.commons.dbcp.BasicDataSource;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.NameMap;

import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.util.log.Logger;



public class DataSourceUtil {

	private String name;
	
	/**
	 * &generateSimpleParameterMetadata=true
	 */
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String DRIVERNAME = "drivername";
	private static  String username = "root";
	private static  String USERNAME = "username";
	
	private static String password = "root";
	private static String PASSWORD = "password";
	
	private static String url = "jdbc:mysql://localhost:3306/muppet?Unicode=true&characterEncoding=utf-8";
	private static String URL = "url";
	
	private static int  initialPoolSize = 5;
	private static String INITIALPOOLSIZE = "initialpoolsize";
	
	private static boolean autocommit = true;
	private static String AUTOCOMMIT = "aotocommit";
	
	private static int maxactive = 20;
	private static String MAXACTIVE = "maxactive";
	
	private static int maxidle = 5;
	private static String MAXIDLE = "maxidle";
	
	private static String MINIDLE = "minidle";
	private static int minidle = 1;
	
	private static String MAXWAIT = "maxwait";
	private static int maxwait = 10000;
	
	private static BasicDataSource source = new BasicDataSource();
	private static boolean isinit = false;
	
	
	
	
	public DataSourceUtil(){
		
	}
	public String getName(){
		return name;
	}
	
	public DataSourceUtil(Properties properties){
		if(properties == null){
			new ParamCanNotBeNullException("properties").printStackTrace();
			return ;
		}else {
			if(properties.containsKey("datasource_name"))
			{
				name = properties.getProperty("datasource_name");
			}
			
			if(properties.containsKey(DRIVERNAME)){
				driverName = properties.getProperty(DRIVERNAME);
			}
			
			if(properties.containsKey(AUTOCOMMIT)){
				if(properties.getProperty(AUTOCOMMIT).equals("true")){
					autocommit = true;
				}else{
					autocommit = false;
				}
			}
			
			if(properties.containsKey(USERNAME))
			{
				username = properties.getProperty(USERNAME);
			}
			
			if(properties.containsKey(PASSWORD)){
				password = properties.getProperty(PASSWORD);
			}
			
			if(properties.containsKey(INITIALPOOLSIZE)){
				initialPoolSize = Integer.parseInt(
						properties.getProperty(INITIALPOOLSIZE));
			}
			
			if(properties.containsKey(URL)){
				
				url = properties.getProperty(URL);
			}
			
			if(properties.containsKey(MAXWAIT)){
				maxwait = Integer.parseInt(properties.getProperty(MAXWAIT));
			}
			
			if(properties.containsKey(MAXACTIVE)){
				maxactive = Integer.parseInt(properties.getProperty(MAXACTIVE));
			}
			
			if(properties.containsKey(MAXIDLE)){
				maxidle = Integer.parseInt(properties.getProperty(MAXIDLE));
			}
			
			
		}
		
		initial();
	}
	
	@SuppressWarnings("deprecation")
	private static void initial(){
		/**
		 * 初始化数据源
		 */
		{
			// 2. 设置连接池对象
			//source.
			if(isinit == true){
				return ;
			}
			
			isinit = true;
			
			source.setDriverClassName(driverName);

			source.setUrl(url);

			source.setUsername(username);

			source.setPassword(password);

			source.setDefaultAutoCommit(autocommit);

			/**
			 * ?
			 */
			source.setDefaultTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

			source.setInitialSize(initialPoolSize);

			/**
			 * 数据库最大连接数
			 */
			source.setMaxActive(maxactive);

			/**
			 * 最大空闲时间
			 */
			source.setMaxIdle(maxidle);

			
			/**
			 * 最小空闲时间
			 */
			source.setMinIdle(minidle);

			/**
			 * 最大等待时间
			 */
			source.setMaxWait(maxwait);
			
			
			
		}
		
	}

	public  Connection getConnection()
			throws SQLException {
		initial();
		return source.getConnection();
	}
	
	
	
	/*@Override
	public PrintWriter getLogWriter() throws SQLException {
		
		
		return source.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		source.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		source.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		
		return source.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return source.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return true;
	}

	@Override
	public Connection getConnection() throws SQLException {
		
		return source.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		
		return source.getConnection(username, password);
	}
	*/
}
