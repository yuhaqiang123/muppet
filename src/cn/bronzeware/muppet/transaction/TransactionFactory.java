package cn.bronzeware.muppet.transaction;

import java.sql.Connection;
import java.util.Properties;

public interface TransactionFactory {

  /*void setProperties(Properties props);*/

  Transaction newTransaction(Connection conn, boolean autoCommit);

}
