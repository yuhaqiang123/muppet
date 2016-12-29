package cn.bronzeware.muppet.util.log;

import java.util.Date;

import javax.xml.crypto.Data;

import cn.bronzeware.util.reflect.ReflectUtil;

public class Logger {

	public static boolean isStart = true;
	public static boolean isDebug = true;
	public static boolean isError = true;
	public static boolean isInfo = true;
	public static boolean isPrintTime = false;
	
	private static StandardLogger logger = ReflectUtil.getClassProxy(StandardLogger.class
			,new LoggerInvocationHandler());
	
	
	
	
	public static void  println(Object object){
		logger.println(object);
	}
	
	
	public static void print(Object message){
		logger.print(message);
	}
	
	public static void error(Object message){
		logger.error(message);
	}
	
	public static void errorln(Object message){
		logger.errorln(message);
	}
	
	public static void info(Object message){
		logger.info(message);
	}
	
	public static void infoln(Object message){
		logger.infoln(message);
	}
	
	public static void debug(Object message){
		logger.debug(message);
	}
	
	public static void debugln(Object message){
		logger.debugln(message);
	}
	
	
	
	
	/*public static void main(String[] args){
		Logger.println("println");
		Logger.debug("debug");
		System.out.println();
		Logger.debugln("debuglb");
		Logger.print("print");
		System.out.println();
		Logger.info("info");
		System.out.println();
		Logger.infoln("infoln");
		Logger.error("error");
		System.out.println();
		Logger.errorln("errorln");
	}*/
	

	
}
