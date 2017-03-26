package cn.bronzeware.muppet.core;

import java.sql.Connection;
import java.sql.SQLException;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.muppet.context.ContextFactory;
import cn.bronzeware.muppet.context.DeleteContext;
import cn.bronzeware.muppet.context.InsertContext;
import cn.bronzeware.muppet.context.SelectContext;
import cn.bronzeware.muppet.context.UpdateContext;
import cn.bronzeware.muppet.context.Context.TYPE;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.transaction.BaseTransactionFactory;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionFactory;
import cn.bronzeware.util.reflect.ReflectUtil;

public class SessionFactory {

	private ApplicationContext applicationContext = new AutowiredApplicationContext();
	private StandardSession session;
	private ResourceContext context;
	private ContextFactory contextFactory;
	
	private  InsertContext insertContext ;
	private  SelectContext selectContext ;
	private  UpdateContext updateContext ;
	private  DeleteContext deleteContext ;
	
	private ClosedInvocationHandler closedHandler;
	
	private TransactionFactory baseTransactionFactory = 
			new BaseTransactionFactory();
	
	
	public SessionFactory(String config){
		/**
		 * 完成配置文件读取
		 * 完成实体类加载解析
		 * 创建Container容器
		 */
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
		closedHandler = new ClosedInvocationHandler();
	}
	
	
	public Session getSession(){
		return getSession(true);
	}
	
	
	public Session getSession(boolean autoCommit){
		Connection conn = null;
		try {
			conn = new DataSourceUtil().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Transaction transaction = baseTransactionFactory.newTransaction(conn, autoCommit);
		StandardSession session = new StandardSession(transaction, applicationContext);
		session.setDeleteContext(deleteContext);
		session.setInsertContext(insertContext);
		session.setSelectContext(selectContext);
		session.setUpdateContext(updateContext);
		session.setContainer(context.getContainer());
		ThreadLocalTransaction.set(transaction);
		
		return ReflectUtil.getClassProxy(session
				,closedHandler
				, new Class[]{Transaction.class, ApplicationContext.class}
		, new  Object[]{transaction, applicationContext});
	}
	
}
