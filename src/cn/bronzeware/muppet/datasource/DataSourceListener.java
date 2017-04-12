package cn.bronzeware.muppet.datasource;

import cn.bronzeware.muppet.listener.Event;

public interface DataSourceListener {
	public void event(DataSourceEvent event);
	
	public enum Type{
		CONNECTED,/*
		PRE_CHECK,
		AFTER_CHECK,*/
		CONNECTED_ERROR,
		CLOSED,
	}
}
