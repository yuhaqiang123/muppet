package cn.bronzeware.muppet.datasource;

import javax.management.RuntimeErrorException;

import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.ExceptionUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class StandardDataSourceListener implements DataSourceListener{

	@Override
	public void event(DataSourceEvent event) {
		Logger.println("------------------------------------------------");
		Logger.println(event);
		ArrayUtil.println(Thread.currentThread().getStackTrace());
		Logger.println("------------------------------------------------");
		//throw ExceptionUtil.getRuntimeException(event.getError());
	}
}
