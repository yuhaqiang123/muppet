package cn.bronzeware.muppet.core;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.text.AbstractDocument.BranchElement;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.core.ioc.StandardApplicationConfig;
import cn.bronzeware.muppet.context.ContextFactory;
import cn.bronzeware.muppet.context.DeleteContext;
import cn.bronzeware.muppet.context.InsertContext;
import cn.bronzeware.muppet.context.SelectContext;
import cn.bronzeware.muppet.context.UpdateContext;
import cn.bronzeware.muppet.context.Context.TYPE;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.transaction.BaseTransactionFactory;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.muppet.transaction.TransactionFactory;
import cn.bronzeware.util.reflect.ReflectUtil;

public class SessionFactory {

	private ApplicationContext applicationContext = null;
	// private StandardSession session;
	private ResourceContext context;
	private ContextFactory contextFactory;

	private DataSourceManager dataSourceManager;

	private InsertContext insertContext;
	private SelectContext selectContext;
	private UpdateContext updateContext;
	private DeleteContext deleteContext;

	private ClosedInvocationHandler closedHandler;

	/*
	 * private TransactionFactory baseTransactionFactory = new
	 * BaseTransactionFactory();
	 */

	public SessionFactory(String config) {
		/**
		 * 完成配置文件读取 完成实体类加载解析 创建Container容器
		 */
		StandardApplicationConfig applicationConfig = new StandardApplicationConfig();
		applicationConfig.setProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY, new String[] { "a" });
		applicationContext = new AutowiredApplicationContext(applicationConfig);
		context = new ResourceContext(config, applicationContext);
		contextFactory = context.getContextFactory();
		insertContext = (InsertContext) contextFactory.getContext(TYPE.INSERT_CONTEXT);
		selectContext = (SelectContext) contextFactory.getContext(TYPE.SELECT_CONTEXT);
		updateContext = (UpdateContext) contextFactory.getContext(TYPE.UPDATE_CONTEXT);
		deleteContext = (DeleteContext) contextFactory.getContext(TYPE.DELETE_CONTEXT);

		applicationContext.registerBean(contextFactory);
		applicationContext.registerBean(selectContext);
		applicationContext.registerBean(insertContext);
		applicationContext.registerBean(updateContext);
		applicationContext.registerBean(deleteContext);
		dataSourceManager = applicationContext.getBean(DataSourceManager.class);

		closedHandler = new ClosedInvocationHandler();
	}

	public Session getSession() {
		return getSession(true);
	}

	private DataSourceUtil getDataSourceUtil(String dataSourceKey) {
		try {
			DataSourceUtil dataSourceUtil = dataSourceManager.getDataSourceUtil(dataSourceKey);
			return dataSourceUtil;
		} catch (Exception e) {
			throw new DataSourceException(e);
		}
	}

	private DataSourceUtil getDataSourceUtil() {
		try {
			DataSourceUtil dataSourceUtil = dataSourceManager.getDefaultDataSource();
			return dataSourceUtil;
		} catch (Exception e) {
			throw new DataSourceException(e);
		}
	}

	private Session initializeSessioin(boolean autoCommit, DataSourceUtil dataSourceUtil) {
		Connection conn = null;
		try {
			conn = dataSourceUtil.getConnection();
		} catch (Exception e) {
			throw new DataSourceException(e);
		}
		Transaction transaction = BaseTransactionFactory.newBaseTransactionFactory(dataSourceUtil).newTransaction(conn,
				autoCommit);
		StandardSession session = new StandardSession(transaction, applicationContext);
		session.setDeleteContext(deleteContext);
		session.setInsertContext(insertContext);
		session.setSelectContext(selectContext);
		session.setUpdateContext(updateContext);
		session.setContainer(context.getContainer());
		ThreadLocalTransaction.set(transaction);

		/*
		 * return ReflectUtil.getClassProxy(session ,closedHandler , new
		 * Class[]{Transaction.class, ApplicationContext.class} , new
		 * Object[]{transaction, applicationContext});
		 */
		return session;
	}

	public Session getSession(boolean autoCommit, String dataSourceKey) {
		DataSourceUtil dataSourceUtil = getDataSourceUtil(dataSourceKey);
		Session session = initializeSessioin(autoCommit, dataSourceUtil);
		return session;
	}

	public Session getSession(boolean autoCommit) {
		DataSourceUtil dataSourceUtil = getDataSourceUtil();
		Session session = initializeSessioin(autoCommit, dataSourceUtil);
		return session;
	}
	

	private void transactionOperationCallback0(String dataSourceKey, TransactionExecute transactionExecute, int mode){
		Session session = null;
		if(dataSourceKey != null){
			session = getSession(false, dataSourceKey);
		}else{
			session = getSession(false);
		}
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			transactionExecute.execute(session, transaction);
			
			switch(mode){
				case TRANSACTION_OPERATION_CALLBACK:
					transaction.commit();
					break;
				case TRANSACTION_OPERATION_CALLBACK_TEST:
					transaction.rollback();
					break;
				default:
					transaction.commit();
					
			}
		} catch (SQLException e) {
			try {
				transaction.rollback();
			} catch (SQLException e1) {
				
			}
		}
		finally {
			session.close();
		}
	}

	private static final int TRANSACTION_OPERATION_CALLBACK_TEST = 1;
	private static final int TRANSACTION_OPERATION_CALLBACK = 2;

	public void transactionOperationCallback(String dataSourceKey, TransactionExecute transactionExecute) {
		transactionOperationCallback0(dataSourceKey, transactionExecute, TRANSACTION_OPERATION_CALLBACK);
	}
	public void transactionOperationCallback(TransactionExecute transactionExecute){
		transactionOperationCallback0(null, transactionExecute, TRANSACTION_OPERATION_CALLBACK);
	}
	
	public void transactionOperationCallTest(String dataSourceKey, TransactionExecute transactionExecute){
		transactionOperationCallback0(dataSourceKey, transactionExecute, TRANSACTION_OPERATION_CALLBACK_TEST);
	}
	public void transactionOperationCallbackTest(TransactionExecute transactionExecute){
		transactionOperationCallback0(null, transactionExecute, TRANSACTION_OPERATION_CALLBACK_TEST);
	}
	
}
