package cn.bronzeware.muppet.context;

import java.lang.reflect.InvocationHandler;

import javax.swing.text.AbstractDocument.BranchElement;

import cn.bronzeware.muppet.core.ResourceContext;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;

public class ContextFactory {

	
	private ResourceContext context;
	private  Container<String, ResourceInfo> container;
	private SelectContext selectContext;
	private UpdateContext updateContext;
	private InsertContext insertContext;
	private DeleteContext deleteContext;
	
	private InvocationHandler handler = new ContextInvocationHandler();
	public ContextFactory(ResourceContext context){
		this.context = context;
		this.container = context.getContainer();
		
		 
		
		
		selectContext = new SelectContext(container);
		updateContext = new UpdateContext(container);
		insertContext = new InsertContext(container);
		deleteContext = new DeleteContext(container);
		
		/*
		selectContext = proxy(selectContext);
		updateContext = proxy(updateContext);
		insertContext = proxy(insertContext);
		deleteContext = proxy(deleteContext);*/
		
	}
	
	private <T> T proxy(T t){
		return ReflectUtil.getClassProxy(t, handler,new Class[]{Container.class},new Object[]{container});
	}
	
	
	
	public Context getContext(Context.TYPE type){
		switch(type){
		case DELETE_CONTEXT:
			return deleteContext;
		case SELECT_CONTEXT:
			return selectContext;
		case UPDATE_CONTEXT:
			return updateContext;
		case INSERT_CONTEXT:
			return insertContext;
		}
		return null;
	}
	
}
