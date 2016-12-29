package cn.bronzeware.muppet.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;




/**
 * @deprecated
 */
public class SimpleConectionPool implements DataSource{

	
	
	
	

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		
		return null;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		
		return null;
	}
	
	
	
	

}
