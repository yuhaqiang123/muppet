package cn.bronzeware.muppet.datasource;

import cn.bronzeware.muppet.listener.Event;

public interface DataSourceListener {
	public void event(DataSourceEvent event);
	
	public enum Type{
		CONNECTED_SUCCESS,/*
		PRE_CHECK,
		AFTER_CHECK,*/
		BOOT_CONNECTED_SUCCESS,
		BOOT_CONNECTED_ERROR,
		BOOT_CONNECTION_CLOSED_SUCCESS,
		BOOT_CONNECTION_CLOSED_ERROR,
		CONNECTED_ERROR,
		TABLE_NOT_EXISTS,
		CONNECTION_CLOSED,
		CONNECTION_CLOSED_ERROR,
	}
}
