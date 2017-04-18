package cn.bronzeware.muppet.test;

import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.bronzeware.muppet.util.log.Log;

public class TestLog4j {
	{
		PropertyConfigurator.configure((
						"bin"+File.separator +Log.class.getPackage().getName().replace(".", File.separator) 
						+ File.separator + "log4j.properties"));
	}
	public Logger logger = Logger.getLogger("error");
	
	public void test1(){
		boolean is = true;
		logger.assertLog(is, "is true");
		logger.debug("debug is call");
		logger.error("error is call");
		logger.fatal("fatal is call");
		logger.debug("level:" + logger.getLevel());
		logger.trace("trace is call");
		logger.info("info is call");
	}
	
	public static void main(String[] args){
		TestLog4j log4j = new TestLog4j();
		log4j.test1();
	}

}
