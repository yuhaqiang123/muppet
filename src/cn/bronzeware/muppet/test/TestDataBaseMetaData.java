package cn.bronzeware.muppet.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.util.CloseUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class TestDataBaseMetaData {

	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException{
		Connection connection = new DataSourceUtil().getConnection();
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = metaData.getTypeInfo();
		while (rs.next()) {
			System.out.print(rs.getString("TYPE_NAME")+"  ");
			System.out.print(rs.getShort("DATA_TYPE")+"  ");
			System.out.println(rs.getString("PRECISION"));
		}
		CloseUtil.close(connection);
		CloseUtil.close(rs);
	}
}
