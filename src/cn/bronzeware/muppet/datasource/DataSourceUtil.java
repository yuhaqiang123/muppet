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

import cn.bronzeware.muppet.core.DataSourceException;
import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.util.log.Logger;

public class DataSourceUtil {

	private String name;

	/**
	 * &generateSimpleParameterMetadata=true
	 */
	private String driverName = "com.mysql.jdbc.Driver";
	private String DRIVERNAME = "drivername";
	private String username = "root";
	private String USERNAME = "username";

	private String password = "root";
	private String PASSWORD = "password";

	private String url = "jdbc:mysql://localhost:3306/muppet?Unicode=true&characterEncoding=utf-8";
	private String URL = "url";

	private int initialPoolSize = 5;
	private String INITIALPOOLSIZE = "initialpoolsize";

	private boolean autocommit = true;
	private String AUTOCOMMIT = "aotocommit";

	private int maxactive = 20;
	private String MAXACTIVE = "maxactive";

	private int maxidle = 5;
	private String MAXIDLE = "maxidle";

	private String MINIDLE = "minidle";
	private int minidle = 1;

	private String MAXWAIT = "maxwait";
	private int maxwait = 10000;

	private final String CAN_NOT_CONNECTED = "无法获取数据库连接，检查请检查数据库配置是否出现问题";
	
	private BasicDataSource source = new BasicDataSource();
	private boolean isinit = false;

	public DataSourceUtil() {

	}

	public String getName() {
		return name;
	}
	
	
	public void isConnected(){
		Connection connection = null;
		try{
			connection = source.getConnection();
		}catch(Throwable throwable){
			throw new DataSourceException(throwable, CAN_NOT_CONNECTED);
		}
		finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					
				}
			}
		}
	}

	public DataSourceUtil(Properties properties) {
		if (properties == null) {
			new ParamCanNotBeNullException("properties").printStackTrace();
			return;
		} else {
			if (properties.containsKey("datasource_name")) {
				name = properties.getProperty("datasource_name");
			}

			if (properties.containsKey(DRIVERNAME)) {
				driverName = properties.getProperty(DRIVERNAME);
			}

			if (properties.containsKey(AUTOCOMMIT)) {
				if (properties.getProperty(AUTOCOMMIT).equals("true")) {
					autocommit = true;
				} else {
					autocommit = false;
				}
			}

			if (properties.containsKey(USERNAME)) {
				username = properties.getProperty(USERNAME);
			}

			if (properties.containsKey(PASSWORD)) {
				password = properties.getProperty(PASSWORD);
			}

			if (properties.containsKey(INITIALPOOLSIZE)) {
				initialPoolSize = Integer.parseInt(properties.getProperty(INITIALPOOLSIZE));
			}

			if (properties.containsKey(URL)) {

				url = properties.getProperty(URL);
			}

			if (properties.containsKey(MAXWAIT)) {
				maxwait = Integer.parseInt(properties.getProperty(MAXWAIT));
			}

			if (properties.containsKey(MAXACTIVE)) {
				maxactive = Integer.parseInt(properties.getProperty(MAXACTIVE));
			}

			if (properties.containsKey(MAXIDLE)) {
				maxidle = Integer.parseInt(properties.getProperty(MAXIDLE));
			}

		}

		initial();
	}

	@SuppressWarnings("deprecation")
	private void initial() {
		/**
		 * 初始化数据源
		 */
		{
			// 2. 设置连接池对象
			// source.
			if (isinit == true) {
				return;
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

	public Connection getConnection() throws SQLException {
		initial();
		Connection connection = null;
		try {
			connection = source.getConnection();
		} catch (Throwable throwable) {
			DataSourceException e = new DataSourceException(throwable, CAN_NOT_CONNECTED);
			throw e;
		}
		return connection;
	}

	
}
