package cn.bronzeware.muppet.util.log;

import java.util.Date;

public class StandardLogger {

	private boolean isStart = true;
	private boolean isPrintTime = false;
	
	private  void print(String args){
		StringBuffer buffer = new StringBuffer();
		/*if(isPrintTime){
			buffer.append(new Date(System.currentTimeMillis()).toLocaleString());
		}*/
		if(args==null){
			buffer.append("");
		}else{
			buffer.append(args+": ");
			if(args.startsWith("error")){
				System.err.print(buffer.toString());
				return;
			}
		}
		
		System.out.print(buffer.toString());
	}
	
	private  void print(){
		System.out.print("");
	}
	
	
	
	public  void  println(Object object){
		
			print();
			System.out.println(object);
		
	}
	
	
	public  void print(Object message){
		
			print();
			System.out.print(message);
		
	}
	
	public  void error(Object message){
		print("error");
		System.err.print(message);
	}
	
	public void info(Object message){
		print("info");
		System.out.print(message);
	}
	
	public void debug(Object object){
		print("");
		System.out.print(object);
	}
	
	public  void errorln(Object message){
		print("error");
		System.err.println(message);
	}
	
	public void infoln(Object message){
		print("info");
		System.out.println(message);
	}
	
	public void debugln(Object object){
		print("");
		System.out.println(object);
	}
	
}
	
