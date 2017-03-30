package cn.bronzeware.muppet.test;

public class TestAssert {

	public static void main(String[] args){
		Object object = null;
		if(args.length > -1){
			object = null;
		}
		
		assert object != null : "错了";
		System.out.println(object);
	}
}
