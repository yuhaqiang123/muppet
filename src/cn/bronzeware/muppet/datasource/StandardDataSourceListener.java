package cn.bronzeware.muppet.datasource;

import javax.management.RuntimeErrorException;

import cn.bronzeware.muppet.util.ExceptionUtil;
import cn.bronzeware.muppet.util.log.Logger;

public class StandardDataSourceListener implements DataSourceListener{

	@Override
	public void event(DataSourceEvent event) {
		Logger.println(event);
		//throw ExceptionUtil.getRuntimeException(event.getError());
	}
}
